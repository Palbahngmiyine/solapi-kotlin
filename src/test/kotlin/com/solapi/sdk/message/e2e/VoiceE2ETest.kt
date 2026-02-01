package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.voice.VoiceOption
import com.solapi.sdk.message.model.voice.VoiceType
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * 음성 메시지 (Voice) E2E 테스트
 *
 * 음성 메시지는 TTS(Text-to-Speech)를 통해 텍스트를 음성으로 변환하여 발송합니다.
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 */
class VoiceE2ETest : BaseE2ETest() {

    @Test
    fun `음성 메시지 발송 - 여성 음성`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val message = E2ETestUtils.createVoiceMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "안녕하세요. 테스트 음성 메시지입니다. 여성 음성으로 발송됩니다.",
            voiceType = VoiceType.FEMALE
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("음성 메시지 여성 음성 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `음성 메시지 발송 - 남성 음성`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val message = E2ETestUtils.createVoiceMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "안녕하세요. 테스트 음성 메시지입니다. 남성 음성으로 발송됩니다.",
            voiceType = VoiceType.MALE
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("음성 메시지 남성 음성 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `음성 메시지 발송 - 헤더 메시지`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 헤더 메시지 포함
        val message = E2ETestUtils.createVoiceMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "본문 메시지입니다.",
            voiceType = VoiceType.FEMALE,
            headerMessage = "안녕하세요. 솔라피 테스트입니다."
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("음성 메시지 헤더 포함 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `음성 메시지 발송 - 테일 메시지`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 테일 메시지 포함
        // Note: tailMessage는 headerMessage와 함께 사용해야 할 수 있음
        val message = Message(
            type = MessageType.VOICE,
            from = senderNumber,
            to = testPhoneNumber,
            text = "본문 메시지입니다.",
            voiceOptions = VoiceOption(
                voiceType = VoiceType.FEMALE,
                headerMessage = "안녕하세요.",
                tailMessage = "감사합니다. 좋은 하루 되세요."
            )
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("음성 메시지 테일 포함 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `음성 메시지 발송 - 전체 옵션`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 모든 옵션 조합
        val message = Message(
            type = MessageType.VOICE,
            from = senderNumber,
            to = testPhoneNumber,
            text = "중요한 공지사항을 안내드립니다.",
            voiceOptions = VoiceOption(
                voiceType = VoiceType.FEMALE,
                headerMessage = "안녕하세요. 솔라피 테스트입니다.",
                tailMessage = "다시 한번 안내드립니다."
            )
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("음성 메시지 전체 옵션 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `음성 메시지 발송 - 기본값 (여성 음성)`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - VoiceOption 기본값 사용 (FEMALE)
        val message = Message(
            type = MessageType.VOICE,
            from = senderNumber,
            to = testPhoneNumber,
            text = "기본값 테스트 음성 메시지입니다.",
            voiceOptions = VoiceOption()
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("음성 메시지 기본값 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }
}
