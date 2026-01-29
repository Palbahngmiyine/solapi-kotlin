rootProject.name = "solapi.sdk"

// Example modules auto-discovery
rootDir.listFiles()
    ?.filter { it.isDirectory && it.name.startsWith("solapi-kotlin-example") }
    ?.filter { File(it, "build.gradle.kts").exists() }
    ?.forEach { include(":${it.name}") }
