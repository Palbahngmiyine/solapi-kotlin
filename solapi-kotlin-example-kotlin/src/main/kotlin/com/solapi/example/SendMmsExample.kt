package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.StorageType
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * MMS 이미지 첨부 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 *
 * 참고: MMS 이미지 규격
 * - 지원 포맷: JPG, JPEG
 * - 최대 용량: 200KB
 * - 권장 해상도: 1000x1000 이하
 */
fun main() {
    // 환경변수에서 설정 로드
    val apiKey = System.getenv("SOLAPI_API_KEY")
        ?: error("SOLAPI_API_KEY must be set")
    val apiSecret = System.getenv("SOLAPI_API_SECRET")
        ?: error("SOLAPI_API_SECRET must be set")
    val sender = System.getenv("SOLAPI_SENDER")
        ?: error("SOLAPI_SENDER must be set")
    val recipient = System.getenv("SOLAPI_RECIPIENT")
        ?: error("SOLAPI_RECIPIENT must be set")

    // SDK 클라이언트 생성
    val messageService = SolapiClient.createInstance(apiKey, apiSecret)

    try {
        // 이미지 파일 로드 (리소스에서)
        val imageUrl = object {}.javaClass.classLoader.getResource("images/sample.jpg")
            ?: error("Sample image not found in resources/images/sample.jpg. Please add a JPG image file (max 200KB).")

        // 임시 파일로 복사
        val tempFile = File.createTempFile("mms-image", ".jpg").apply {
            deleteOnExit()
        }
        imageUrl.openStream().use { input ->
            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }

        // 이미지 업로드
        println("이미지 업로드 중...")
        val imageId = messageService.uploadFile(tempFile, StorageType.MMS)
        println("이미지 업로드 완료 - imageId: $imageId")

        // MMS 메시지 생성
        val message = Message(
            type = MessageType.MMS,
            from = sender,
            to = recipient,
            text = "안녕하세요. MMS 이미지 첨부 메시지입니다.",
            subject = "MMS 제목",
            imageId = imageId
        )

        // 메시지 발송
        val response = messageService.send(message)

        println("MMS 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")

    } catch (e: Exception) {
        System.err.println("MMS 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}
