plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    val example = project.findProperty("example") as String? ?: "Main"
    mainClass.set("com.solapi.example.${example}Example")
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
