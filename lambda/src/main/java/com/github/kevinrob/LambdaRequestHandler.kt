package com.github.kevinrob

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler


class LambdaRequestHandler : RequestHandler<String, String> {


    override fun handleRequest(input: String, context: Context): String {
        context.getLogger().log("Input: $input")
        return "Hello World - $input"
    }
}