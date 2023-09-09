//________________________________________________________________________________
// PLUGINS
//________________________________________________________________________________
plugins {
    id("com.github.johnrengelman.shadow")
}



group = "com.github.kevinrob"
version = "0.1"

val kotlinVersion: String by rootProject.extra

dependencies {
//    implementation(kotlin("stdlib-jdk8", kotlin_version))
    implementation("com.amazonaws:aws-lambda-java-core:1.1.0")
    implementation("com.amazonaws:aws-lambda-java-events:2.1.0")
    implementation("org.json:json:20180130")
// https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.0")
}
