package com.github.kevinrob

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;

/*
input - a
{
    "clientId": "sdk-java",
    "timestamp": 1694226699911,
    "eventType": "connected",
    "sessionIdentifier": "d8690138-4ddb-4410-bd5c-3c896aa45502",
    "principalIdentifier": "95cfe6feba625b99b0c992f64b05ddfb1aba728a0e97361b2a40597c65c80ac2",
    "ipAddress": "172.112.167.56",
    "versionNumber": 40
}

{
    "clientId": "iotconsole-856f172a-7919-4a4e-ad8e-29ab900009ce",
    "timestamp": 1694226763013,
    "eventType": "subscribed",
    "sessionIdentifier": "e74c97f2-0f31-43ca-a4df-3e77b409e079",
    "principalIdentifier": "AIDATYAHN6IGWGZK24MAR",
    "topics": [
        "$aws/events/presence/connected/#"
    ]
}
{
    "clientId": "iotconsole-856f172a-7919-4a4e-ad8e-29ab900009ce",
    "timestamp": 1694226763013,
    "eventType": "subscribed",
    "sessionIdentifier": "e74c97f2-0f31-43ca-a4df-3e77b409e079",
    "principalIdentifier": "AIDATYAHN6IGWGZK24MAR",
    "topics": [
        "$aws/events/subscriptions/#"
    ]
}
{
    "clientId": "iotconsole-856f172a-7919-4a4e-ad8e-29ab900009ce",
    "timestamp": 1694226763005,
    "eventType": "subscribed",
    "sessionIdentifier": "e74c97f2-0f31-43ca-a4df-3e77b409e079",
    "principalIdentifier": "AIDATYAHN6IGWGZK24MAR",
    "topics": [
        "$aws/events/subscriptions/subscribed/#"
    ]
}

{
    "clientId": "iotconsole-856f172a-7919-4a4e-ad8e-29ab900009ce",
    "timestamp": 1694226763004,
    "eventType": "subscribed",
    "sessionIdentifier": "e74c97f2-0f31-43ca-a4df-3e77b409e079",
    "principalIdentifier": "AIDATYAHN6IGWGZK24MAR",
    "topics": [
        "$aws/events/presence/disconnected/#"
    ]
}

{
    "data": "CgMxLjISInNlbGY6OnVzcHRlc3QtY29udHJvbC5hcnJpc2VlYS5jb20aF29zOjowMDI0NTYtMDk0NTI3MEI1N0YwUiUIARIhdXNwL2FnZW50L29zOjowMDI0NTYtMDk0NTI3MEI1N0Yw",
    "ts": 1694228063276
}
{
    "data": "CgMxLjISInNlbGY6OnVzcHRlc3QtY29udHJvbC5hcnJpc2VlYS5jb20aF29zOjowMDI0NTYtMDk0NTI3MEI1N0YwOrIBEq8BCiAKHEV2ZW50LTIwMjMtMDktMDlUMDI6NTQ6MjNaLTEQAxKKAQqHAUKEAQoWZGVmYXVsdC1ib290LWV2ZW50LUFDUxABGmgKB0RldmljZS4SBUJvb3QhGgwKCkNvbW1hbmRLZXkaGgoFQ2F1c2USEUxvY2FsRmFjdG9yeVJlc2V0GhgKD0Zpcm13YXJlVXBkYXRlZBIFZmFsc2UaEgoMUGFyYW1ldGVyTWFwEgJ7fQ==",
    "ts": 1694228063292
}

 */

class LambdaRequestStreamHandler : RequestStreamHandler {
    override fun handleRequest(
        inputStream: InputStream?,
        outputStream: OutputStream, context: Context?
    ) {
        val input: String = IOUtils.toString(inputStream, "UTF-8")
        println("Hello World - $input")

        outputStream.write("OK".toByteArray())
    }
}