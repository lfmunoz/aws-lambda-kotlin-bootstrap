package com.lambda;

import com.lambda.LambdaStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

public class TmpApp {

    static Environment makeEnv(String region) {
        return Environment.builder()
//                .account(accountId)
                .region(region)
                .build();
    }

    public static void main(final String[] args) {
        App app = new App();
        String appName = "aws-lambda-cdk-plain";
        Tags.of(app).add("application", appName);

        Environment awsEnvironment = makeEnv("us-east-2");

        StackProps stackProps = StackProps.builder()
                .env(awsEnvironment)
                .build();

        new LambdaStack(app, appName, stackProps);
        app.synth();
    }
}

