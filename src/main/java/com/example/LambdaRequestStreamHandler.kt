package com.example

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class LambdaRequestStreamHandler : RequestStreamHandler {
    override fun handleRequest(
        inputStream: InputStream?,
        outputStream: OutputStream, context: Context?
    ) {
        val input: String = IOUtils.toString(inputStream, "UTF-8")
        outputStream.write("Hello World - $input".toByteArray())
    }
}