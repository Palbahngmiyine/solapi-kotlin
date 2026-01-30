rootProject.name = "solapi.sdk"

// Example modules auto-discovery
// - publish/release 태스크 실행 시 자동으로 제외
// - -PskipExamples=true 로 명시적 제외 가능
val isPublishTask = gradle.startParameter.taskNames.any {
    it.contains("publish", ignoreCase = true) ||
        it.contains("Release", ignoreCase = true)
}
val skipExamples = providers.gradleProperty("skipExamples").orNull?.toBoolean() ?: isPublishTask

if (!skipExamples) {
    rootDir.listFiles()
        ?.filter { it.isDirectory && it.name.startsWith("solapi-kotlin-example") }
        ?.filter { File(it, "build.gradle.kts").exists() }
        ?.forEach { include(":${it.name}") }
}
