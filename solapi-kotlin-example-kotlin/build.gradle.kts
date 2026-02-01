plugins {
    kotlin("jvm") version "2.3.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
}

kotlin {
    jvmToolchain(8)
}

application {
    val example = project.findProperty("example") as String? ?: "Main"
    mainClass.set("com.solapi.example.${example}ExampleKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

// Disable distTar/distZip/installDist/startScripts from regular builds
// These are only needed when explicitly building distributions
tasks.named("distTar") { enabled = false }
tasks.named("distZip") { enabled = false }
tasks.named("installDist") { enabled = false }
tasks.named("startScripts") { enabled = false }
