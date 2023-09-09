# How to use this?

- Fork/Clone/Download this project
- In `settings.gradle`, change the `rootProject.name`
- In `build.gradle.kts`, change `group = "..."`
- In `src/main/java/com/example/main.kt`, change `package com.example`

# How to build JAR?

- Run Gradle task `shadowJar` with `gradlew shadowJar`
- The JAR will be in `build/libs/...-all.jar`

# ... and after?

You can upload the JAR to AWS Lambda and starting playing with it! Enjoy! 😉



# Development 

## Install CDK

```bash
nvm use v18.16.0      
# Now using node v18.16.0 (npm v9.5.1)
npm install -g aws-cdk
cdk --version 
#2.95.0 (build cfa7e88)
```


```
aws sts get-caller-identity
```

## CDK Usage

The `cdk.json` file tells the CDK Toolkit how to execute your app.

* `mvn package`     compile and run tests
* `cdk ls`          list all stacks in the app
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk docs`        open CDK documentation



## References

* https://aws.amazon.com/getting-started/guides/setup-cdk/module-two/
