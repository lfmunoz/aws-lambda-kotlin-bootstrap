package com.github.kevinrob

import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.fissore.slf4j.FluentLoggerFactory
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//________________________________________________________________________________
// KAFKA CONFIG
//________________________________________________________________________________
data class KafkaConfig(
    var bootstrapServer: String = "localhost:9092",
    var topic: String = "default-topic",
    var groupId: String = "default-groupId",
    var compression: String = "none", // none, lz4
    var offset: String = "none" // latest, earliest, none(use zookeper)
) : java.io.Serializable {
    companion object {
        fun fromJson(json: String) = mapper.readValue<KafkaConfig>(json)
    }

    fun toJson(): String = mapper.writeValueAsString(this)
}

//________________________________________________________________________________
// KAFKA MESSAGE
//________________________________________________________________________________
data class KafkaMessage(
    val key: ByteArray,
    val value: ByteArray
) : java.io.Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KafkaMessage
        if (!key.contentEquals(other.key)) return false
        return true
    }

    override fun hashCode(): Int {
        return key.contentHashCode()
    }
}

class KafkaPublisherStateful(val aKafkaConfig: KafkaConfig) {
    companion object {
        private val log = FluentLoggerFactory.getLogger(KafkaPublisherStateful::class.java)
    }

    val aKafkaProducer = KafkaProducer<ByteArray, ByteArray>(producerProps(aKafkaConfig))

    suspend fun send(kafkaMessage: KafkaMessage) = suspendCoroutine<RecordMetadata> { cont ->
        aKafkaProducer.send(
            ProducerRecord(
                aKafkaConfig.topic,
                kafkaMessage.key,
                kafkaMessage.value
            )
        ) { metadata: RecordMetadata, e: Exception? ->
            e?.let {
                cont.resumeWithException(it)
            } ?: cont.resume(metadata)
        }
    }

    fun sendAndForget(kafkaMessage: KafkaMessage): Unit {
        aKafkaProducer.send(ProducerRecord(aKafkaConfig.topic, kafkaMessage.key, kafkaMessage.value))
    }

    fun shutdown() {
        aKafkaProducer.close()
    }

    private fun producerProps(aKafkaConfig: KafkaConfig): Properties {
        val serializer = ByteArraySerializer::class.java.canonicalName
        val props = Properties()
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, aKafkaConfig.bootstrapServer)
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializer)
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer)
        props.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, aKafkaConfig.compression)
        return props
    }
}

/*
class KafkaPublisherBare {
    companion object {
        private val log = FluentLoggerFactory.getLogger(KafkaPublisherBare::class.java)

        fun connect(aKafkaConfig: KafkaConfig, aFlow: Flow<KafkaMessage>) = flow<KafkaPublisherResult> {
            // The producer is thread safe and sharing a single producer instance across threads will generally be faster than
            // having multiple instances.
            log.info().log("[kafka producer connecting] - {}", aKafkaConfig)
            val aKafkaProducer = KafkaProducer<ByteArray, ByteArray>(producerProps(aKafkaConfig))
            try {
                aFlow.collect { item ->
                    aKafkaProducer.send(
                        ProducerRecord(
                            aKafkaConfig.topic,
                            item.key,
                            item.value
                        )
                    ) { metadata: RecordMetadata, e: Exception? ->
                        e?.let {
                            e.printStackTrace()
                        } ?: log.trace().log("The offset of the record we just sent is: " + metadata.offset())
                    }
                    emit(KafkaPublisherResult.Published(item))
                }
                emit(KafkaPublisherResult.Success)
            } catch (e: Exception) {
                log.error().withCause(e).log("[kafka producer error] - captured exception")
                emit(KafkaPublisherResult.Failure(e.toString()))
            } finally {
                aKafkaProducer.close()
            }
        }

        private fun producerProps(aKafkaConfig: KafkaConfig): Properties {
            val serializer = ByteArraySerializer::class.java.canonicalName
            val props = Properties()
            props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, aKafkaConfig.bootstrapServer)
            props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializer)
            props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer)
            props.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, aKafkaConfig.compression)
            return props
        }

    }

}// end of KafkaPublisherBare

 */

sealed class KafkaPublisherResult {
    object Success : KafkaPublisherResult()
    data class Failure(val error: String) : KafkaPublisherResult()
    data class Published(val kafkaMessage: KafkaMessage) : KafkaPublisherResult()
}