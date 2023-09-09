package com.lambda;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.constructs.Construct;

import java.util.Map;

public class LambdaStack extends Stack {
    static Map<String, String> configuration = Map.of("message", "hello,duke");
    static String eventsFunctionName  = "lfm-mqtt-handler-events";
    static String uspFunctionName  = "lfm-mqtt-handler-usp";

    static String lambdaHandlerName = "com.github.kevinrob.LambdaRequestStreamHandler::handleRequest";
    static String uspHandlerName = "com.github.kevinrob.LambdaUSPStreamHandler::handleRequest";
    static int memory = 128;
    static int timeout = 10;

    public LambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function eventsHandler = createFunction(eventsFunctionName, lambdaHandlerName, configuration, memory, timeout);
        Function uspHandler = createFunction(uspFunctionName, uspHandlerName, configuration, memory, timeout);

        CfnOutput.Builder.create(this, "FunctionEvents").value(eventsHandler.getFunctionArn()).build();
        CfnOutput.Builder.create(this, "FunctionUSP").value(uspHandler.getFunctionArn()).build();
    }


    Function createFunction(String functionName,String functionHandler, Map<String,String> configuration, int memory, int timeout) {
        return Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_17)
                .architecture(Architecture.ARM_64)
                .code(Code.fromAsset("/home/luis/git_public/aws-lambda-kotlin-bootstrap/lambda/build/libs/lambda-0.1-all.jar"))
//                .code(Code.fromAsset("../lambda/binary/function.jar"))
                .handler(functionHandler)
                .memorySize(memory)
                .functionName(functionName)
                .environment(configuration)
                .timeout(Duration.seconds(timeout))
                .tracing(Tracing.ACTIVE)
                .build();
    }

}