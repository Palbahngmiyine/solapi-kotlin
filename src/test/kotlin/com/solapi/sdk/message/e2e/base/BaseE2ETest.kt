package com.solapi.sdk.message.e2e.base

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException
import com.solapi.sdk.message.model.StorageType
import com.solapi.sdk.message.service.DefaultMessageService
import java.io.File

/**
 * E2E 테스트를 위한 공통 베이스 클래스
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 *
 * 카카오 테스트 추가 환경변수:
 * - SOLAPI_KAKAO_PF_ID: 카카오 비즈니스 채널 ID
 * - SOLAPI_KAKAO_TEMPLATE_ID: 카카오 알림톡 템플릿 ID (선택)
 */
abstract class BaseE2ETest {

    protected val apiKey: String? = System.getenv("SOLAPI_API_KEY")
    protected val apiSecret: String? = System.getenv("SOLAPI_API_SECRET")
    protected val senderNumber: String = System.getenv("SOLAPI_SENDER") ?: "01000000000"
    protected val testPhoneNumber: String = System.getenv("SOLAPI_RECIPIENT") ?: "01000000000"
    protected val pfId: String? = System.getenv("SOLAPI_KAKAO_PF_ID")
    protected val templateId: String? = System.getenv("SOLAPI_KAKAO_TEMPLATE_ID")

    protected val messageService: DefaultMessageService? by lazy {
        if (apiKey != null && apiSecret != null) {
            SolapiClient.createInstance(apiKey, apiSecret)
        } else {
            null
        }
    }

    /**
     * 기본 환경변수 설정 여부 확인 (API Key, Secret)
     * @return 환경변수가 설정되었으면 true, 아니면 false
     */
    protected fun assumeBasicEnvironmentConfigured(): Boolean {
        if (apiKey.isNullOrBlank() || apiSecret.isNullOrBlank()) {
            println("환경변수가 설정되지 않아 테스트를 건너뜁니다. (SOLAPI_API_KEY, SOLAPI_API_SECRET 필요)")
            return false
        }
        return true
    }

    /**
     * 카카오 환경변수 설정 여부 확인 (pfId 포함)
     * @return 환경변수가 설정되었으면 true, 아니면 false
     */
    protected fun assumeKakaoEnvironmentConfigured(): Boolean {
        if (!assumeBasicEnvironmentConfigured()) return false
        if (pfId.isNullOrBlank()) {
            println("카카오 환경변수가 설정되지 않아 테스트를 건너뜁니다. (SOLAPI_KAKAO_PF_ID 필요)")
            return false
        }
        return true
    }

    /**
     * 카카오 알림톡 템플릿 환경변수 설정 여부 확인
     * @return 환경변수가 설정되었으면 true, 아니면 false
     */
    protected fun assumeKakaoTemplateConfigured(): Boolean {
        if (!assumeKakaoEnvironmentConfigured()) return false
        if (templateId.isNullOrBlank()) {
            println("카카오 템플릿 환경변수가 설정되지 않아 테스트를 건너뜁니다. (SOLAPI_KAKAO_TEMPLATE_ID 필요)")
            return false
        }
        return true
    }

    /**
     * 예외 상세 정보 출력
     */
    protected fun printExceptionDetails(e: Exception) {
        println("예상된 에러 발생: ${e.message}")
        if (e is SolapiMessageNotReceivedException) {
            println("  실패한 메시지 목록 (${e.failedMessageList.size}건):")
            e.failedMessageList.forEachIndexed { index, failed ->
                println("    [${index + 1}] to: ${failed.to}, statusCode: ${failed.statusCode}, statusMessage: ${failed.statusMessage}")
            }
        }
    }

    /**
     * 이미지 파일 업로드
     * @param storageType 스토리지 타입
     * @param filename 리소스 파일명 (images/ 디렉토리 내)
     * @return 업로드된 이미지 ID, 파일이 없으면 null
     */
    protected fun uploadImage(storageType: StorageType, filename: String): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, storageType)
    }
}
