import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "1.5.32"
    id("com.github.johnrengelman.shadow")
}

group = "com.github.kevinrob"
version = "0.1"

repositories {
    mavenCentral()
}

val awsCdkVersion = "2.95.0"
val constructsVersion = "10.2.70"

dependencies {
    implementation(kotlin("stdlib-jdk8"))


    implementation("software.amazon.awscdk:aws-cdk-lib:${awsCdkVersion}")
    implementation("software.constructs:constructs:${constructsVersion}")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

tasks.test {
    useJUnitPlatform()
}


application {
    mainClassName = "com.lambda.TmpApp"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("app.jar")
    archiveClassifier.set("")
    archiveVersion.set("")
    doLast {
        copy {
            from(archiveFile)
            into(layout.buildDirectory.dir("libs"))
            rename { fileName ->
                fileName.replace(".jar", ".zip")
            }
        }
    }
}