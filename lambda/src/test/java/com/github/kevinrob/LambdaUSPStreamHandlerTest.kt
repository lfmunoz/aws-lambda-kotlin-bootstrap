package com.github.kevinrob

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

//import org.assertj.core.api.Assertions
//import org.assertj.core.api.Assertions.assertThat

val dataA = "CgMxLjISInNlbGY6OnVzcHRlc3QtY29udHJvbC5hcnJpc2VlYS5jb20aF29zOjowMDI0NTYtMDk0NTI3MEI1N0YwOrIBEq8BCiAKHEV2ZW50LTIwMjMtMDktMDlUMDI6NTQ6MjNaLTEQAxKKAQqHAUKEAQoWZGVmYXVsdC1ib290LWV2ZW50LUFDUxABGmgKB0RldmljZS4SBUJvb3QhGgwKCkNvbW1hbmRLZXkaGgoFQ2F1c2USEUxvY2FsRmFjdG9yeVJlc2V0GhgKD0Zpcm13YXJlVXBkYXRlZBIFZmFsc2UaEgoMUGFyYW1ldGVyTWFwEgJ7fQ=="

class LambdaUSPStreamHandlerTest {
    val lambdaUSPStreamHandler = LambdaUSPStreamHandler()
    @Test
    fun `usp lambda happy path`(): Unit = runBlocking {


        val json = IoTPayload(
            data = dataA,
            ts = System.currentTimeMillis()
        ).toJson()
        val inputStream = ByteArrayInputStream(json.toByteArray(Charsets.UTF_8))
        val outputStream = ByteArrayOutputStream()

        lambdaUSPStreamHandler.handleRequest(inputStream, outputStream, null)
    }
}