package com.github.kevinrob

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import org.joda.time.DateTime

@Suppress("unused")
class Handler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        context.logger.log("Start invocation of snsFailureEvents! ${DateTime.now()}")

        println("--------------------------------------")
        println("Body")
        println("--------------------------------------")
        println(input.body)
        return APIGatewayProxyResponseEvent().apply {
            statusCode = 200
            body = "It's working!"
        }
    }
}