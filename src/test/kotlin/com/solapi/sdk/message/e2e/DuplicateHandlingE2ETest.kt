package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.dto.request.SendRequestConfig
import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * 중복 수신번호 처리 E2E 테스트
 *
 * SendRequestConfig의 allowDuplicates 옵션을 통해 동일 수신번호로
 * 여러 메시지 발송 시 중복 허용 여부를 제어할 수 있습니다.
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 */
class DuplicateHandlingE2ETest : BaseE2ETest() {

    @Test
    fun `중복 수신번호 허용`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 동일 수신번호로 여러 메시지 생성
        val messages = listOf(
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 중복 허용 테스트 1/3"
            ),
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 중복 허용 테스트 2/3"
            ),
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 중복 허용 테스트 3/3"
            )
        )

        val config = SendRequestConfig(
            allowDuplicates = true
        )

        // When
        val response = messageService!!.send(messages, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("중복 수신번호 허용 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  발송 요청: ${messages.size}건, 접수: ${response.groupInfo?.count?.total ?: 0}건")
    }

    @Test
    fun `중복 수신번호 비허용`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 동일 수신번호로 여러 메시지 생성 (중복 비허용)
        val messages = listOf(
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 중복 비허용 테스트 1/3"
            ),
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 중복 비허용 테스트 2/3"
            ),
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 중복 비허용 테스트 3/3"
            )
        )

        val config = SendRequestConfig(
            allowDuplicates = false
        )

        // When
        val response = messageService!!.send(messages, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("중복 수신번호 비허용 발송 - groupId: ${response.groupInfo?.groupId}")
        println("  발송 요청: ${messages.size}건, 접수: ${response.groupInfo?.count?.total ?: 0}건")
        // 중복 비허용 시 동일 수신번호는 1건만 접수됨
    }

    @Test
    fun `showMessageList 옵션`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - showMessageList = true로 메시지 목록 포함
        val message = E2ETestUtils.createSmsMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] showMessageList 옵션 테스트"
        )

        val config = SendRequestConfig(
            showMessageList = true
        )

        // When
        val response = messageService!!.send(message, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("showMessageList 옵션 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  messageList 포함 여부: ${response.messageList.isNotEmpty()}")
        response.messageList.forEach { msg ->
            println("    - messageId: ${msg.messageId}")
        }
    }

    @Test
    fun `showMessageList false 옵션`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - showMessageList = false (기본값)
        val message = E2ETestUtils.createSmsMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] showMessageList false 테스트"
        )

        val config = SendRequestConfig(
            showMessageList = false
        )

        // When
        val response = messageService!!.send(message, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("showMessageList false 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `중복 허용과 showMessageList 조합`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 두 옵션 모두 활성화
        val messages = listOf(
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 조합 테스트 1/2"
            ),
            E2ETestUtils.createSmsMessage(
                from = senderNumber,
                to = testPhoneNumber,
                text = "[SDK 테스트] 조합 테스트 2/2"
            )
        )

        val config = SendRequestConfig(
            allowDuplicates = true,
            showMessageList = true
        )

        // When
        val response = messageService!!.send(messages, config)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("중복 허용 + showMessageList 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  발송 요청: ${messages.size}건")
        println("  messageList 건수: ${response.messageList.size}")

        // 중복 허용 시 요청한 메시지 수만큼 messageList에 포함되어야 함
        if (response.messageList.isNotEmpty()) {
            assertTrue(
                response.messageList.size == messages.size,
                "중복 허용 시 요청 메시지 수와 응답 메시지 수가 동일해야 함"
            )
        }
    }
}
