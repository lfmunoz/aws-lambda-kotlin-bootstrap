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