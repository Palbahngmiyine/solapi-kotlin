package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import com.solapi.sdk.message.model.StorageType
import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * 카카오 친구톡 (CTA/CTI) E2E 테스트
 *
 * 친구톡은 카카오톡 채널 친구에게 발송하는 광고성 메시지입니다.
 * CTA: 텍스트 친구톡
 * CTI: 이미지 친구톡
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 * - SOLAPI_KAKAO_PF_ID: 카카오 비즈니스 채널 ID
 */
class KakaoFriendTalkE2ETest : BaseE2ETest() {

    /**
     * 카카오용 이미지 업로드
     */
    private fun uploadKakaoImage(filename: String = "test-image.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.KAKAO)
    }

    // ==================== CTA (텍스트 친구톡) 테스트 ====================

    @Test
    fun `친구톡 발송 - 텍스트만`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val message = E2ETestUtils.createFriendTalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 친구톡 텍스트 메시지입니다.",
            pfId = pfId!!
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 텍스트 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 발송 - 웹링크 버튼`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val buttons = listOf(
            E2ETestUtils.createWebLinkButton(
                buttonName = "바로가기",
                linkMo = "https://example.com"
            )
        )

        val message = E2ETestUtils.createFriendTalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 웹링크 버튼 포함 친구톡입니다.",
            pfId = pfId!!,
            buttons = buttons
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 웹링크 버튼 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 발송 - 앱링크 버튼`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val buttons = listOf(
            E2ETestUtils.createAppLinkButton(
                buttonName = "앱 열기",
                linkAnd = "intent://main#Intent;scheme=example;package=com.example;end",
                linkIos = "exampleapp://main"
            )
        )

        val message = E2ETestUtils.createFriendTalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 앱링크 버튼 포함 친구톡입니다.",
            pfId = pfId!!,
            buttons = buttons
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 앱링크 버튼 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 발송 - 봇키워드 버튼`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val buttons = listOf(
            E2ETestUtils.createBotKeywordButton(buttonName = "문의하기")
        )

        val message = E2ETestUtils.createFriendTalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 봇키워드 버튼 포함 친구톡입니다.",
            pfId = pfId!!,
            buttons = buttons
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 봇키워드 버튼 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 발송 - 메시지전달 버튼`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val buttons = listOf(
            E2ETestUtils.createMessageDeliveryButton(buttonName = "전달하기")
        )

        val message = E2ETestUtils.createFriendTalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 메시지전달 버튼 포함 친구톡입니다.",
            pfId = pfId!!,
            buttons = buttons
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 메시지전달 버튼 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 발송 - 광고 플래그`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given - adFlag = true
        val message = E2ETestUtils.createFriendTalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 광고성 친구톡입니다.",
            pfId = pfId!!,
            adFlag = true
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 광고 플래그 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 발송 - 다중 버튼`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given - 여러 버튼 조합
        val buttons = listOf(
            E2ETestUtils.createWebLinkButton("홈페이지", "https://example.com"),
            E2ETestUtils.createBotKeywordButton("상담 요청")
        )

        val message = E2ETestUtils.createFriendTalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 다중 버튼 친구톡입니다.",
            pfId = pfId!!,
            buttons = buttons
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 다중 버튼 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== CTI (이미지 친구톡) 테스트 ====================

    @Test
    fun `친구톡 이미지 발송`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val imageId = uploadKakaoImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val message = E2ETestUtils.createFriendTalkImageMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 이미지 친구톡입니다.",
            pfId = pfId!!,
            imageId = imageId
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 이미지 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 이미지 발송 - 버튼 포함`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val imageId = uploadKakaoImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val buttons = listOf(
            E2ETestUtils.createWebLinkButton("자세히 보기", "https://example.com")
        )

        val message = E2ETestUtils.createFriendTalkImageMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 버튼 포함 이미지 친구톡입니다.",
            pfId = pfId!!,
            imageId = imageId,
            buttons = buttons
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 이미지+버튼 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `친구톡 이미지 발송 - 광고 플래그`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given
        val imageId = uploadKakaoImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val message = E2ETestUtils.createFriendTalkImageMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] 광고성 이미지 친구톡입니다.",
            pfId = pfId!!,
            imageId = imageId,
            adFlag = true
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("친구톡 이미지 광고 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }
}
