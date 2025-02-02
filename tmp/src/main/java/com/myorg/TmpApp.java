package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;

import java.util.Arrays;

public class TmpApp {
    public static void main(final String[] args) {
        App app = new App();
        String appName = "aws-lambda-cdk-plain";
        Tags.of(app).add("project", "airhacks.live");
        Tags.of(app).add("environment","workshops");
        Tags.of(app).add("application", appName);


        StackProps stackProps = StackProps.builder()
                .build();

        new LambdaStack(app, appName, stackProps);
        app.synth();
    }
}

