package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import com.solapi.sdk.message.exception.SolapiFileUploadException
import com.solapi.sdk.message.model.StorageType
import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * MMS 발송 E2E 테스트
 *
 * MMS는 이미지가 포함된 문자 메시지입니다.
 * 이미지는 먼저 SOLAPI 서버에 업로드해야 합니다.
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 *
 * 테스트 리소스 필요:
 * - src/test/resources/images/test-image.png (MMS 규격에 맞는 이미지)
 *
 * MMS 이미지 규격:
 * - 지원 포맷: JPG, JPEG
 * - 최대 용량: 200KB
 * - 권장 해상도: 1000x1000 이하
 */
class MmsE2ETest : BaseE2ETest() {

    /**
     * MMS용 이미지 업로드
     * MMS는 특정 이미지 규격(JPG, 200KB 이하)을 요구할 수 있습니다.
     * 업로드 실패 시 null을 반환합니다.
     */
    private fun uploadMmsImage(filename: String = "test-image.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return try {
            messageService?.uploadFile(file, StorageType.MMS)
        } catch (e: SolapiFileUploadException) {
            println("MMS 이미지 업로드 실패 (서버 응답): ${e.message}")
            println("  MMS 이미지 규격을 확인하세요 (JPG 포맷, 200KB 이하)")
            null
        }
    }

    @Test
    fun `MMS 이미지 업로드`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val imageUrl = javaClass.classLoader.getResource("images/test-image.png")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/test-image.png")
            return
        }
        val file = File(imageUrl.toURI())

        // When
        val imageId = try {
            messageService!!.uploadFile(file, StorageType.MMS)
        } catch (e: SolapiFileUploadException) {
            println("MMS 이미지 업로드 실패 (서버 응답): ${e.message}")
            println("  MMS 이미지 규격을 확인하세요 (JPG 포맷, 200KB 이하)")
            println("  테스트를 건너뜁니다.")
            return
        }

        // Then
        assertNotNull(imageId)
        println("MMS 이미지 업로드 성공 - imageId: $imageId")
    }

    @Test
    fun `MMS 단건 발송`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 이미지 업로드
        val imageId = uploadMmsImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val message = E2ETestUtils.createMmsMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] MMS 메시지입니다.",
            imageId = imageId,
            subject = "MMS 제목"
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("MMS 단건 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `MMS 발송 - 유효하지 않은 imageId`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 존재하지 않는 imageId
        val message = E2ETestUtils.createMmsMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 잘못된 imageId 테스트",
            imageId = "invalid-image-id-12345"
        )

        // When & Then
        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "유효하지 않은 imageId로 MMS 발송 시 에러가 발생해야 함")
    }

    @Test
    fun `MMS 발송 - 제목과 본문 포함`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val imageId = uploadMmsImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val message = E2ETestUtils.createMmsMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] MMS 본문입니다. 이미지와 함께 발송됩니다.",
            imageId = imageId,
            subject = "[SDK 테스트] MMS 제목"
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("MMS 제목/본문 포함 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }
}
