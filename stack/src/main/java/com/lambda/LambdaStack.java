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
    static String functionName  = "handleRequest";
    static String lambdaHandler = "com.example.Handler::handleRequest";
    static int memory = 128;
    static int timeout = 10;

    public LambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function function = createFunction(functionName, lambdaHandler, configuration, memory, timeout);

        CfnOutput.Builder.create(this, "FunctionARN").value(function.getFunctionArn()).build();
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