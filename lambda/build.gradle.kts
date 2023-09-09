//________________________________________________________________________________
// PLUGINS
//________________________________________________________________________________
plugins {
    id("com.github.johnrengelman.shadow")
}



group = "com.github.kevinrob"
version = "0.1"

val kotlinVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val vertxVersion: String by rootProject.extra
val mockkVersion: String by rootProject.extra
val juniperVersion: String by rootProject.extra
val assertjVersion: String by rootProject.extra

dependencies {
    implementation("org.fissore:slf4j-fluent:0.12.0")
    implementation( "ch.qos.logback:logback-classic:1.2.3")
    implementation( "ch.qos.logback:logback-core:1.2.3")

//    implementation(kotlin("stdlib-jdk8", kotlin_version))
    implementation("com.amazonaws:aws-lambda-java-core:1.1.0")
    implementation("com.amazonaws:aws-lambda-java-events:2.1.0")
    implementation("eco.usp:usp-schema:1.0.7")
    implementation("org.apache.kafka:kafka-clients:2.6.2")
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    // VERTX
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("io.vertx:vertx-web-client:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin:$vertxVersion") {
        exclude("org.jetbrains.kotlin")
    }
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion") {
        exclude("org.jetbrains.kotlin")
    }


    implementation("org.json:json:20180130")
// https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.13.0")
    // TESTING
    testImplementation("io.vertx:vertx-junit5:$vertxVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$juniperVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")


    testImplementation("net.mguenther.kafka:kafka-junit:2.7.0")
}


tasks {
    test {
        useJUnitPlatform()
    }
}
