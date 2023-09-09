package com.github.kevinrob

import com.google.protobuf.ByteString
import eco.usp.Constants
import usp.UspMsg
import usp_record.UspRecord
import java.lang.Exception

//________________________________________________________________________________
// PERIODIC NOTIFY   (CLIENT TO SERVER)
//________________________________________________________________________________
val PERIODIC_NOTIFY_EVENT_MSG: UspMsg.Msg = UspMsg.Msg.newBuilder()
    .setHeader(UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.NOTIFY)
        .build())
    .setBody(UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder()
            .setNotify(UspMsg.Notify.newBuilder()
                .setEvent(UspMsg.Notify.Event.newBuilder()
                    .setEventName("Periodic!")
                    .setObjPath("Device.LocalAgent.Periodic")
                    .build())
                .build())
            .build())
        .build())
    .build()

//________________________________________________________________________________
// BOOT NOTIFY  (CLIENT TO SERVER)
//________________________________________________________________________________
fun bootEvent(command: String, commandKey: String, firmwareUpdated: Boolean = false): UspMsg.Msg {
    return UspMsg.Msg.newBuilder()
        .setHeader(UspMsg.Header.newBuilder()
            .setMsgType(UspMsg.Header.MsgType.NOTIFY)
            .build())
        .setBody(UspMsg.Body.newBuilder()
            .setRequest(UspMsg.Request.newBuilder()
                .setNotify(UspMsg.Notify.newBuilder()
                    .setEvent(UspMsg.Notify.Event.newBuilder()
                        .setObjPath("Device.")
                        .setEventName("Boot!")
                        .putParams("Command", command)
                        .putParams("CommandKey", commandKey)
                        .putParams("Cause", "LocalReboot")
                        .putParams("FirmwareUpdated", firmwareUpdated.toString())
                        .build()))
                .build())
            .build())
        .build()
}

//________________________________________________________________________________
// NOTIFY - TransferComplete! (CLIENT TO SERVER)
//________________________________________________________________________________
/*
Test:-
DOWNLOAD-19b: Get a TransferComplete! event

Request:-

Response:-
header {
  msg_id: "Event-1"
  msg_type: NOTIFY
}
body {
  request {
    notify {
      subscription_id: "DOWNLOAD-18"
      send_resp: false
      event {
        obj_path: "Device.LocalAgent."
        event_name: "TransferComplete!"
        params {
          key: "Affected"
          value: "Device.DeviceInfo.FirmwareImage.2"
        }
        params {
          key: "Command"
          value: "Device.DeviceInfo.FirmwareImage.2.Download()"
        }
        params {
          key: "CommandKey"
          value: "DOWNLOAD-19a"
        }
        params {
          key: "CompleteTime"
          value: "XXX"
        }
        params {
          key: "FaultCode"
          value: "0"
        }
        params {
          key: "FaultString"
        }
        params {
          key: "Requestor"
          value: "CONTROLLER"
        }
        params {
          key: "StartTime"
          value: "XXX"
        }
        params {
          key: "TransferType"
          value: "Download"
        }
        params {
          key: "TransferURL"
          value: "ftp://localhost/imageC.txt"
        }
      }
    }
  }
}
 */
fun buildTransferComplete(
    msgId: String = "Event-1",
    start: String = "XXX",
    end: String = "XXX",
    url: String = "ftp://localhost/imageC.txt"
): UspMsg.Msg {

    val notifyReq= UspMsg.Notify.newBuilder()
        .setSendResp(false)
        .setSubscriptionId("DOWNLOAD-18")
        .setEvent(UspMsg.Notify.Event.newBuilder()
            .setObjPath("Device.LocalAgent.")
            .setEventName("TransferComplete!")
            .putParams("Affected", "Device.DeviceInfo.FirmwareImage.2")
            .putParams("Command", "Device.DeviceInfo.FirmwareImage.2.Download()")
            .putParams("CommandKey", "DOWNLOAD-19a")
            .putParams("CompleteTime", start)
            .putParams("StartTime", end)
            .putParams("FaultCode", "0")
            .putParams("Requestor", "CONTROLLER")
            .putParams("TransferType", "Download")
            .putParams("TransferURL", url)
            .build())

    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.NOTIFY)
        .setMsgId(msgId)
        .build()

    val body = UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder().setNotify(notifyReq))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()
}


//________________________________________________________________________________
// NOTIFY - OperationComplete - Download  (CLIENT TO SERVER)
//________________________________________________________________________________
/*
Operation Complete is always generated at the end of an async data model command, but it is only sent to USP Controllers that have subscribed to receive that notification

Test:-
DOWNLOAD-19c: Get an OperationComplete event

Request:-

Response:-
header {
  msg_id: "OperationComplete-1"
  msg_type: NOTIFY
}
body {
  request {
    notify {
      subscription_id: "DOWNLOAD-17"
      send_resp: false
      oper_complete {
        obj_path: "Device.DeviceInfo.FirmwareImage.2."
        command_name: "Download()"
        command_key: "DOWNLOAD-19a"
        req_output_args {
        }
      }
    }
  }
}
 */
fun operationCompleteDownload(msgId: String = "OperationComplete-1") : UspMsg.Msg {
    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.NOTIFY)
        .setMsgId(msgId)
        .build()

    val notifyReq= UspMsg.Notify.newBuilder()
        .setSendResp(false)
        .setSubscriptionId("DOWNLOAD-17")
        .setOperComplete(UspMsg.Notify.OperationComplete.newBuilder()
            .setObjPath("Device.DeviceInfo.FirmwareImage.2.")
            .setCommandKey(msgId)
            .setCommandName("Download()")
        ).build()

    val body = UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder().setNotify(notifyReq))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()

}

//________________________________________________________________________________
// NOTIFY - OperationComplete - Download FAILURE (CLIENT TO SERVER)
//________________________________________________________________________________
/*
Operation Complete is always generated at the end of an async data model command, but it is only sent to USP Controllers that have subscribed to receive that notification

{
  "endpoint_id": "os::002456-0800270B57FF",
  "node_id": "usp-stomper:4308587722",
  "notification": {
    "oper_complete": {
      "cmd_failure": {
        "err_code": 7022,
        "err_msg": "DownloadFile: file download failed: curl_res=6, Couldn't resolve host name"
      },
      "command_key": "FIRMWARE_UPDATE;;0.1_C#dev_endpoint#24:a148d4df-2ff5-4e85-82ed-fab562d7e2b6:usp_ctl-rpc-response-operate-127.0.1.1#1625601098990",
      "command_name": "Download()",
      "obj_path": "Device.DeviceInfo.FirmwareImage.2."
    },
    "send_resp": true,
    "subscription_id": "dev_endpoint_OperationComplete"
  },
  "rpc_address": "usp_proxy-rpc-request-exchange/usp_stomp-rpc-request-127.0.1.1#1932863188",
  "ts": 1625605221695
}
 */
fun operationCompleteDownloadFailure(msgId: String = "OperationComplete-1") : UspMsg.Msg {
    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.NOTIFY)
        .setMsgId(msgId)
        .build()

    val notifyReq= UspMsg.Notify.newBuilder()

        .setSendResp(false)
        .setSubscriptionId("DOWNLOAD-17")
        .setOperComplete(UspMsg.Notify.OperationComplete.newBuilder()
            .setCmdFailure(
                UspMsg.Notify.OperationComplete.CommandFailure.newBuilder()
                    .setErrCode(7022)
                    .setErrMsg("DownloadFile: file download failed: curl_res=6, Couldn't resolve host name")
            )
           .setObjPath("Device.DeviceInfo.FirmwareImage.2.")
           .setCommandKey(msgId)
           .setCommandName("Download()")
        ).build()

    val body = UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder().setNotify(notifyReq))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()

}

//________________________________________________________________________________
// NOTIFY - OperationComplete - ScheduleTimer (CLIENT TO SERVER)
//________________________________________________________________________________
/*
Test:-
SHEDT-3: Get an OperationComplete event

Request:-

Response:-
header {
  msg_id: "OperationComplete-1"
  msg_type: NOTIFY
}
body {
  request {
    notify {
      subscription_id: "SCHEDT-1"
      send_resp: false
      oper_complete {
        obj_path: "Device."
        command_name: "ScheduleTimer()"
        command_key: "SHEDT-2"
        req_output_args {
        }
      }
    }
  }
}
 */
fun operationCompleteScheduleTimer(msgId: String = "OperationComplete-1") : UspMsg.Msg {
    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.NOTIFY)
        .setMsgId(msgId)
        .build()

    val notifyReq= UspMsg.Notify.newBuilder()
        .setSendResp(false)
        .setSubscriptionId("SCHEDT-1")
        .setOperComplete(UspMsg.Notify.OperationComplete.newBuilder()
            .setObjPath("Device.")
            .setCommandKey(msgId)
            .setCommandName("ScheduleTimer()")
        ).build()

    val body = UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder().setNotify(notifyReq))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()

}
//________________________________________________________________________________
// NOTIFY - OperationComplete - ScheduleTimer FAILURE (CLIENT TO SERVER)
//________________________________________________________________________________
/*
{
    "body": {
    "response": {
    "operate_resp": {
    "operation_results": [
    {
        "cmd_failure": {
        "err_code": 7022,
        "err_msg": "Device.ScheduleTimer() is not supported by the data model schema"
    },
        "executed_command": "Device.ScheduleTimer()"
    }
    ]
}
}
},
    "header": {
    "msg_id": "dev_endpoint#22:3aa65d2f-929a-421a-aa22-6b8a068f3045:usp_ctl-rpc-response-operate-127.0.1.1#1625601098990",
    "msg_type": "OPERATE_RESP"
    }
}

 */
fun operationCompleteScheduleTimerFailure(msgId: String = "OperationComplete-1") : UspMsg.Msg {
    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.NOTIFY)
        .setMsgId(msgId)
        .build()

    val notifyReq= UspMsg.Notify.newBuilder()
        .setSendResp(false)
        .setSubscriptionId("SCHEDT-1")
        .setOperComplete(UspMsg.Notify.OperationComplete.newBuilder()
            .setObjPath("Device.")
            .setCommandKey(msgId)
            .setCommandName("ScheduleTimer()")
        ).build()

    val body = UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder().setNotify(notifyReq))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()

}

//________________________________________________________________________________
// USP MESSAGE TO RECORD
//________________________________________________________________________________
/**
 * Build a USP Record for use with ECO Controller
 *
 * @param fromIdentifier The identifier for the source of the message
 * @param toIdentifier   The identifier for the recipient of the message
 * @param payload        The ByteString payload, which should correspond to a USP Message
 * @return The USP Record using a no session context and plain text payload security
 */
fun buildRecord2(fromId: String, toId: String, message: UspMsg.Msg): UspRecord.Record {
    val noSessionContext = UspRecord.NoSessionContextRecord.newBuilder()
        .setPayload(message.toByteString())
        .build()

    return UspRecord.Record.newBuilder()
        .setPayloadSecurity(UspRecord.Record.PayloadSecurity.PLAINTEXT)
        .setToId(toId)
        .setFromId(fromId)
        .setNoSessionContext(noSessionContext)
        .build()
}


//________________________________________________________________________________
// GET  (SERVER TO CLIENT)
//________________________________________________________________________________
/*
header {
    msg_id: "dev_endpoint#115:caafa18b-4e55-40ec-8e48-6fff9c7e2d0a:usp_ctl-rpc-response-127.0.1.1#1599771280218"
    msg_type: GET
}
body {
    request {
        get {
            param_paths: "Device.LocalAgent.Controller.*.EndpointID"
        }
    }
}


header {
    msg_id: "dev_endpoint#115:caafa18b-4e55-40ec-8e48-6fff9c7e2d0a:usp_ctl-rpc-response-127.0.1.1#1599771280218"
    msg_type: GET_RESP
}
body {
    response {
        get_resp {
            req_path_results {
                requested_path: "Device.LocalAgent.Controller.*.EndpointID"
                resolved_path_results {
                    resolved_path: "Device.LocalAgent.Controller."
                    result_params {
                        key: "1.EndpointID"
                        value: "dev_endpoint"
                    }
                }
            }
        }
    }
}
 */
fun uspGetRequest(paths: List<String>) : UspMsg.Msg {

    val getRequest = UspMsg.Get.newBuilder()
        .addAllParamPaths(paths)
        .build()

    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.GET)
        .setMsgId("Event-1")
        .build()

    val body = UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder().setGet(getRequest))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()

}

fun uspGetResp() : UspMsg.GetResp {

    val resolv = UspMsg.GetResp.ResolvedPathResult.newBuilder().putResultParams("test", "test")
    val req = UspMsg.GetResp.RequestedPathResult.newBuilder().addResolvedPathResults(resolv)
    return UspMsg.GetResp.newBuilder().addReqPathResults(req).build()
}

//________________________________________________________________________________
// OPERATE REQUEST USP MESSAGE BUILDER
//________________________________________________________________________________
fun buildOperateRequest(
    command: String,
    args: Map<String, String>,
    sendResp: Boolean = true,
    commandKey: String = getRandomString(15)
) : UspMsg.Msg {
    val operate = UspMsg.Operate.getDefaultInstance().toBuilder()
        .setCommand(command)
        .setCommandKey(commandKey)
        .setSendResp(sendResp)
        .putAllInputArgs(args)
        .build()

    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.OPERATE)
        .setMsgId(commandKey)
        .build()

    val body = UspMsg.Body.newBuilder()
        .setRequest(UspMsg.Request.newBuilder().setOperate(operate))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()
}


//________________________________________________________________________________
// OPERATE RESPONSE USP MESSAGE BUILDER
//________________________________________________________________________________
fun buildOperateResp(command: String, requestNumber: Int) : UspMsg.OperateResp {
    val operationResults = UspMsg.OperateResp.OperationResult.newBuilder()
        .setExecutedCommand(command)
        .setReqObjPath("Device.LocalAgent.Request.$requestNumber")
        .build()

    return UspMsg.OperateResp.newBuilder()
        .addOperationResults(operationResults)
        .build()
}

fun buildOperateResponseMsg(msgId: String, command: String, requestNumber: Int) : UspMsg.Msg {
    val operateResp = buildOperateResp(command, requestNumber)
    val header = UspMsg.Header.newBuilder()
        .setMsgType(UspMsg.Header.MsgType.OPERATE_RESP)
        .setMsgId(msgId)
        .build()

    val body = UspMsg.Body.newBuilder()
        .setResponse(UspMsg.Response.newBuilder().setOperateResp(operateResp))
        .build()

    return UspMsg.Msg.newBuilder()
        .setHeader(header)
        .setBody(body)
        .build()
}

//________________________________________________________________________________
// OPERATE DOWNLOAD REQUEST (SERVER TO CLIENT)
//________________________________________________________________________________
/*
Operate Example: Download Request

Request:-
header {
  msg_id: "DOWNLOAD-19a"
  msg_type: OPERATE
}
body {
  request {
    operate {
      command: "Device.DeviceInfo.FirmwareImage.2.Download()"
      command_key: "DOWNLOAD-19a"
      send_resp: true
      input_args {
        key: "URL"
        value: "ftp://localhost/imageC.txt"
      }
      input_args {
        key: "AutoActivate"
        value: "false"
      }
      input_args {
        key: "Username"
        value: "ftpuser"
      }
      input_args {
        key: "Password"
        value: "ftpuser"
      }
    }
  }
}

Response:-
header {
  msg_id: "DOWNLOAD-19a"
  msg_type: OPERATE_RESP
}
body {
  response {
    operate_resp {
      operation_results {
        executed_command: "Device.DeviceInfo.FirmwareImage.2.Download()"
        req_obj_path: "Device.LocalAgent.Request.1"
      }
    }
  }
}
 */

fun buildDownloadOperateRequest(sendResp: Boolean = true) : UspMsg.Msg {
    val command = "Device.DeviceInfo.FirmwareImage.1.Download()"
    return buildOperateRequest(
        command,
        mapOf(
            "URL"  to "ftp://localhost/imageC.txt",
            "AutoActivate" to "false",
            "Username" to "ftpuser",
            "Password" to "ftpuser",
            "CheckSumAlgorithm" to "SHA-1",
            "CheckSum" to "c5c80fc6ddea4c3012c07dd7e676610cde5e4659"
        ),
        sendResp
    )
}



//________________________________________________________________________________
// OPERATE ScheduleTimer REQUEST (SERVER TO CLIENT)
//________________________________________________________________________________

/*
Test:-
SHEDT-2: Schedule a timer

Request:-
header {
  msg_id: "SHEDT-2"
  msg_type: OPERATE
}
body {
  request {
    operate {
      command: "Device.ScheduleTimer()"
      command_key: "SHEDT-2"
      send_resp: true
      input_args {
        key: "DelaySeconds"
        value: "5"
      }
    }
  }
}

Response:-
header {
  msg_id: "SHEDT-2"
  msg_type: OPERATE_RESP
}
body {
  response {
    operate_resp {
      operation_results {
        executed_command: "Device.ScheduleTimer()"
        req_obj_path: "Device.LocalAgent.Request.1"
      }
    }
  }
}

 */
fun buildScheduleTimerOperateRequest(delayInSeconds: Int, sendResp: Boolean = true) : UspMsg.Msg {
    return buildOperateRequest(
        "Device.ScheduleTimer()",
        mapOf("DelaySeconds" to delayInSeconds.toString()),
        sendResp
    )
}


fun buildOperateCompleteMessage(msgId: String, requestNumber: Int): UspMsg.Msg {
    return UspMsg.Msg.newBuilder()
        .setHeader(UspMsg.Header.newBuilder()
            .setMsgType(UspMsg.Header.MsgType.OPERATE_RESP)
            .setMsgId(msgId)
            .build())
        .setBody(UspMsg.Body.newBuilder()
            .setRequest(UspMsg.Request.newBuilder()
                .setNotify(UspMsg.Notify.newBuilder()
                    .setEvent(UspMsg.Notify.Event.newBuilder()
                        .setEventName("Device.LocalAgent.Request.$requestNumber")
                        .build())
                    .build())
                .build())
            .build())
        .build()
}

/**
 * Build a USP Record for use with ECO Controller
 *
 * @param fromIdentifier The identifier for the source of the message
 * @param toIdentifier   The identifier for the recipient of the message
 * @param payload        The ByteString payload, which should correspond to a USP Message
 * @return The USP Record using a no session context and plain text payload security
 */
fun buildRecord(fromIdentifier: String?, toIdentifier: String?, payload: ByteString?): UspRecord.Record {
    val noSessionContextRecord = UspRecord.NoSessionContextRecord.newBuilder().setPayload(payload).build()
    return UspRecord.Record.newBuilder()
        .setPayloadSecurity(UspRecord.Record.PayloadSecurity.PLAINTEXT)
        .setNoSessionContext(noSessionContextRecord)
        .setVersion(Constants.USP_VERSION)
        .setFromId(fromIdentifier)
        .setToId(toIdentifier)
        .build()

}
/**
 * Build a USP Record for the given USP Response for use in ECO Control
 *
 * @param messageId      The message identifier to use
 * @param fromIdentifier The identifier for the source of the message
 * @param toIdentifier   The identifier for the recipient of the message
 * @param response       The USP Response to build into a Record
 * @return The USP Record using a no session context and plain text payload security
 */
fun buildResponseRecord(
    messageId: String,
    fromIdentifier: String,
    toIdentifier: String,
    response: UspMsg.Response
): UspRecord.Record {
    return buildRecord(
        fromIdentifier,
        toIdentifier,
        buildMessageFromResponse(messageId, response).toByteString())
}

/**
 * Build a USP Msg from a generic response.
 *
 * @param messageId The message ID to use
 * @param response  The USP Response
 * @return The USP Msg object
 */
fun buildMessageFromResponse(messageId: String, response: UspMsg.Response): UspMsg.Msg {
    return UspMsg.Msg.newBuilder()
        .setHeader(
            UspMsg.Header.newBuilder()
                .setMsgType(getMessageTypeForResponse(response))
                .setMsgId(messageId)
        )
        .setBody(
            UspMsg.Body.newBuilder()
                .setResponse(response)
        )
        .build()
}

fun toUspMsg(uspRecordBytes: ByteArray?): UspMsg.Msg? {
    var result: UspMsg.Msg? = null
    return try {
        val record = UspRecord.Record.parseFrom(uspRecordBytes)
        UspMsg.Msg.parseFrom(record.noSessionContext.payload)
    } catch (e: Exception) {
        null
    }
}

/**
 * Simple utility to Map USP Response type to the appropriate USP Message header
 *
 * @param response The USP Response
 * @return The appropriate MsgType header to include in a USP Msg built for the response
 */
fun getMessageTypeForResponse(response: UspMsg.Response): UspMsg.Header.MsgType {
    return when (response.respTypeCase) {
        UspMsg.Response.RespTypeCase.GET_RESP -> UspMsg.Header.MsgType.GET_RESP
        UspMsg.Response.RespTypeCase.GET_SUPPORTED_DM_RESP -> UspMsg.Header.MsgType.GET_SUPPORTED_DM_RESP
        UspMsg.Response.RespTypeCase.GET_INSTANCES_RESP -> UspMsg.Header.MsgType.GET_INSTANCES_RESP
        UspMsg.Response.RespTypeCase.SET_RESP -> UspMsg.Header.MsgType.SET_RESP
        UspMsg.Response.RespTypeCase.ADD_RESP -> UspMsg.Header.MsgType.ADD_RESP
        UspMsg.Response.RespTypeCase.DELETE_RESP -> UspMsg.Header.MsgType.DELETE_RESP
        UspMsg.Response.RespTypeCase.OPERATE_RESP -> UspMsg.Header.MsgType.OPERATE_RESP
        UspMsg.Response.RespTypeCase.NOTIFY_RESP -> UspMsg.Header.MsgType.NOTIFY_RESP
        UspMsg.Response.RespTypeCase.RESPTYPE_NOT_SET -> UspMsg.Header.MsgType.UNRECOGNIZED
        else -> UspMsg.Header.MsgType.UNRECOGNIZED
    }
}


/**
 * Build a USP Operate Response from an OperationComplete notification
 *
 * @param completeNotification The OperationComplete notification
 * @return A corresponding USP Response containing the OperateResponse with the same results as the notification.
 */
fun buildResponseFromNotification(completeNotification: UspMsg.Notify.OperationComplete): UspMsg.Response {
    val respBuilder = UspMsg.OperateResp.newBuilder()
    val resultBuilder = UspMsg.OperateResp.OperationResult.newBuilder()
    resultBuilder.executedCommand = completeNotification.commandName
    if (completeNotification.hasCmdFailure()) {
        resultBuilder.cmdFailure = UspMsg.OperateResp.OperationResult.CommandFailure.newBuilder()
            .setErrCode(completeNotification.cmdFailure.errCode)
            .setErrMsg(completeNotification.cmdFailure.errMsg)
            .build()
    } else if (completeNotification.hasReqOutputArgs()) {
        resultBuilder.reqOutputArgs = UspMsg.OperateResp.OperationResult.OutputArgs.newBuilder()
            .putAllOutputArgs(completeNotification.reqOutputArgs.outputArgsMap)
            .build()
    }
    respBuilder.addOperationResults(resultBuilder.build())
    return UspMsg.Response.newBuilder().setOperateResp(respBuilder).build()
}