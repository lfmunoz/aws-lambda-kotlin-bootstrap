//________________________________________________________________________________
// BUILD SCRIPT
//________________________________________________________________________________
buildscript {
    repositories {
        mavenCentral()
        jcenter()

    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.7.10"))
    }
}

//________________________________________________________________________________
// VERSIONS
//________________________________________________________________________________
val guavaVersion by extra { "28.0-jre" }
val kotlinVersion by extra { "1.7.10"}
val kotlinCoroutinesVersion by extra { "1.6.4"}
val jacksonVersion by extra { "2.13.2"}
val vertxVersion by extra {"4.4.1"}

val juniperVersion by extra { "5.4.0" }
val assertjVersion by extra {"3.16.1"}
val mockkVersion by extra {"1.9"}
val slf4jVersion by extra {"2.0.1"}
val slf4jFluentVersion by extra {"0.14.0"}
val logbackCoreVersion by extra {"1.4.3"}
val awaitilityVersion by extra {"4.1.1"}

//________________________________________________________________________________
// PLUGINS
//________________________________________________________________________________
plugins {
    java
    kotlin("jvm") version "1.5.32"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}


allprojects {
    apply {
        plugin("kotlin")
        plugin("maven-publish")
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.jvmTarget = "11"
        }

    }
    java {
        withJavadocJar()
        withSourcesJar()
    }
}