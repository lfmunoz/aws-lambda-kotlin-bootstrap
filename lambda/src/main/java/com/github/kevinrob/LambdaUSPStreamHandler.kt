package com.github.kevinrob

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.commons.io.IOUtils
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import usp.UspControl
import usp.UspMsg
import usp_record.UspRecord
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.Map
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator


/*
{
    "data": "CgMxLjISInNlbGY6OnVzcHRlc3QtY29udHJvbC5hcnJpc2VlYS5jb20aF29zOjowMDI0NTYtMDk0NTI3MEI1N0YwUiUIARIhdXNwL2FnZW50L29zOjowMDI0NTYtMDk0NTI3MEI1N0Yw",
    "ts": 1694228063276
}
{
    "data": "CgMxLjISInNlbGY6OnVzcHRlc3QtY29udHJvbC5hcnJpc2VlYS5jb20aF29zOjowMDI0NTYtMDk0NTI3MEI1N0YwOrIBEq8BCiAKHEV2ZW50LTIwMjMtMDktMDlUMDI6NTQ6MjNaLTEQAxKKAQqHAUKEAQoWZGVmYXVsdC1ib290LWV2ZW50LUFDUxABGmgKB0RldmljZS4SBUJvb3QhGgwKCkNvbW1hbmRLZXkaGgoFQ2F1c2USEUxvY2FsRmFjdG9yeVJlc2V0GhgKD0Zpcm13YXJlVXBkYXRlZBIFZmFsc2UaEgoMUGFyYW1ldGVyTWFwEgJ7fQ==",
    "ts": 1694228063292
}

 */

data class IoTPayload(
    val data: String,
    val ts: Long
) {

    companion object {
        fun fromJson(json: String) = mapper.readValue<IoTPayload>(json)
        fun fromJson(bytes: ByteArray) = mapper.readValue<IoTPayload>(bytes)
    }

    fun toJson(): String = mapper.writeValueAsString(this)
}

/*
return {
    messageId: `${record.fromId}-${Date.now()}`,
    timestamp: Date.now(),
    commandKey: msg.header.msgId,
    deviceId: device.id,
    externalIds: device.externalIds,
    keyAttributes: device.keyAttributes,
    tags: device.tags,
    tenantId: device.tenantId,
    protocol: device.connection.protocol,
    transport: device.connection.transport,
    type: getMessageResponsePayloadType(msg),
    payload: buildDeviceMessageResponsePayload(msg)
};

 */
private fun getBootstrapServer(): String? {
    return System.getenv("bootstrap_server")
}

fun getProducerProperties(): Properties? {
//////    val serializer: String = org.apache.kafka.common.serialization.StringSerializer::class.java.getCanonicalName()
////    val callbackHandler: String = software.amazon.msk.auth.iam.IAMClientCallbackHandler::class.java.getCanonicalName()
//    val loginModule: String = software.amazon.msk.auth.iam.IAMLoginModule::class.java.getCanonicalName()
    val configuration = Map.of(
//        "key.serializer", serializer,
//        "value.serializer", serializer,
        "bootstrap.servers", getBootstrapServer(),
        "security.protocol", "SASL_SSL",
        "sasl.mechanism", "AWS_MSK_IAM",
//        "sasl.jaas.config", "$loginModule required;",
//        "sasl.client.callback.handler.class", callbackHandler,
        "connections.max.idle.ms", "60",
        "reconnect.backoff.ms", "1000"
    )
    val kafkaProducerProperties = Properties()
    for ((key, value) in configuration) {
        kafkaProducerProperties.put(key, value)
    }
    return kafkaProducerProperties
}


class LambdaUSPStreamHandler : RequestStreamHandler {
    override fun handleRequest(
        inputStream: InputStream?,
        outputStream: OutputStream,
        context: Context?
    ) {
        val json = IOUtils.toString(inputStream, "UTF-8")
        val ioTPayload = IoTPayload.fromJson(json)
        val binaryData = Base64.getDecoder().decode(ioTPayload.data)

        val record = UspRecord.Record.parseFrom(binaryData)
        val toId = record.toId     // "self::usptest-control.arriseea.com"
        val fromId = record.fromId // "os::002456-0945270B57F0"

        val uspMsg = UspMsg.Msg.parseFrom(record.noSessionContext.payload)
        println("uspMsg - ${uspMsg.toString()}")

//        val recordGet = buildRecord(record.toId, record.fromId, msg)
        processUSPRecord(record)

       //  https://aws.amazon.com/blogs/compute/creating-a-serverless-apache-kafka-publisher-using-aws-lambda/
//        val producer: KafkaProducer<String, String> = createProducer()
//        val record: ProducerRecord<String, String> =
//            ProducerRecord<String, String>(TOPIC_NAME, context!!.awsRequestId, message)
//
//        val send: Future<RecordMetadata> = producer.send(record)
//        producer.flush()


        outputStream.write("OK".toByteArray())
    }

    fun processUSPRecord(record: UspRecord.Record) {
        if (UspRecord.Record.PayloadSecurity.PLAINTEXT != record.payloadSecurity) {
            throw IllegalArgumentException("Invalid payload security only supported is PLAIN_TEXT.")
        }

        val uspMsg = UspMsg.Msg.parseFrom(record.noSessionContext.payload)

        when (uspMsg.header.msgType) {
            UspMsg.Header.MsgType.NOTIFY -> {
                if (uspMsg.hasBody() && uspMsg.body.hasRequest() && uspMsg.body.request.hasNotify()) {
                    val notification = uspMsg.body.request.notify
                    val messageId = uspMsg.header.msgId
                    handleNotify(messageId, notification)
                }
            }
            else -> {
                null
            }
        }

    }



    fun handleNotify(messageId: String, notification: UspMsg.Notify): UspMsg.Msg? {
        // do something with notificaiton

        if (notification.hasOperComplete()) {
            val cmdKey = notification.operComplete.commandKey
            val opResp = buildMessageFromResponse( cmdKey, buildResponseFromNotification(notification.operComplete) )
            forwardToController(
                "", "",
                UspControl.RpcResponse.newBuilder().setMessage(opResp).build().toByteArray()
            )

        }

        if (notification.sendResp) {
            return buildMessageFromResponse(
                messageId, UspMsg.Response.newBuilder()
                    .setNotifyResp(
                        UspMsg.NotifyResp.newBuilder()
                            .setSubscriptionId(notification.subscriptionId)
                    ).build()
            )
        }
        return null
    }

    fun forwardToController( agentEndpointId: String, destination: String, payload: ByteArray ) {
//        buildRecord(agentEndpointId, destination, payload)
    }


}