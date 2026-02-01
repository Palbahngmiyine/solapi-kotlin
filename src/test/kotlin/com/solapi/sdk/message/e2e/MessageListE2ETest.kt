package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.dto.request.MessageListRequest
import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.model.MessageStatusType
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * 메시지 목록 조회 E2E 테스트
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 */
class MessageListE2ETest : BaseE2ETest() {

    @Test
    fun `메시지 목록 조회 - 기본`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // When - 파라미터 없이 조회
        val response = messageService!!.getMessageList()

        // Then
        assertNotNull(response)
        println("메시지 목록 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }

    @Test
    fun `메시지 목록 조회 - 발신번호 필터`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val request = MessageListRequest(
            from = senderNumber
        )

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        println("발신번호 필터 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }

    @Test
    fun `메시지 목록 조회 - 수신번호 필터`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val request = MessageListRequest(
            to = testPhoneNumber
        )

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        println("수신번호 필터 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }

    @Test
    fun `메시지 목록 조회 - 메시지 타입 필터`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - SMS 타입만 조회
        val request = MessageListRequest(
            type = "SMS"
        )

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        println("메시지 타입 필터 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }

    @Test
    fun `메시지 목록 조회 - 상태 필터 (완료)`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 발송 완료 메시지만 조회
        val request = MessageListRequest(
            status = MessageStatusType.COMPLETE
        )

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        println("상태 필터 (완료) 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }

    @Test
    fun `메시지 목록 조회 - 상태 필터 (대기)`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 발송 대기 메시지만 조회
        val request = MessageListRequest(
            status = MessageStatusType.PENDING
        )

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        println("상태 필터 (대기) 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }

    @Test
    fun `메시지 목록 조회 - 날짜 범위 필터`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 최근 7일간 메시지 조회
        val request = MessageListRequest()
        request.setStartDateFromLocalDateTime(LocalDateTime.now().minusDays(7))
        request.setEndDateFromLocalDateTime(LocalDateTime.now())

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        println("날짜 범위 필터 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }

    @Test
    fun `메시지 목록 조회 - 페이지네이션`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 첫 페이지 (limit = 5)
        val firstPageRequest = MessageListRequest(
            limit = 5
        )

        // When - 첫 페이지 조회
        val firstPageResponse = messageService!!.getMessageList(firstPageRequest)

        // Then
        assertNotNull(firstPageResponse)
        println("첫 페이지 조회 성공 - 조회 건수: ${firstPageResponse.messageList?.size ?: 0}")

        // Given - 다음 페이지 (startKey 사용)
        // messageList는 Map<String, Message>이므로 마지막 키를 가져옴
        val lastMessageId = firstPageResponse.messageList?.keys?.lastOrNull()
        if (lastMessageId != null) {
            val secondPageRequest = MessageListRequest(
                limit = 5,
                startKey = lastMessageId
            )

            // When - 두 번째 페이지 조회
            val secondPageResponse = messageService!!.getMessageList(secondPageRequest)

            // Then
            assertNotNull(secondPageResponse)
            println("두 번째 페이지 조회 성공 - 조회 건수: ${secondPageResponse.messageList?.size ?: 0}")
        }
    }

    @Test
    fun `메시지 목록 조회 - 특정 messageIds`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 먼저 메시지 목록을 조회하여 messageId 획득
        // messageList는 Map<String, Message>이므로 keys가 messageId
        val initialResponse = messageService!!.getMessageList(MessageListRequest(limit = 3))
        val messageIds = initialResponse?.messageList?.keys?.toList()

        if (messageIds.isNullOrEmpty()) {
            println("조회할 메시지가 없어 테스트를 건너뜁니다.")
            return
        }

        // Given - 특정 messageIds로 조회
        val request = MessageListRequest(
            messageIds = messageIds
        )

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        assertTrue(
            (response.messageList?.size ?: 0) <= messageIds.size,
            "조회된 메시지 수는 요청한 messageIds 수 이하여야 함"
        )
        println("특정 messageIds 조회 성공 - 요청: ${messageIds.size}건, 조회: ${response.messageList?.size ?: 0}건")
    }

    @Test
    fun `메시지 목록 조회 - 복합 필터`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 발신번호 + 타입 + 날짜 범위 조합
        val request = MessageListRequest(
            from = senderNumber,
            type = "SMS",
            limit = 10
        )
        request.setStartDateFromLocalDateTime(LocalDateTime.now().minusDays(30))

        // When
        val response = messageService!!.getMessageList(request)

        // Then
        assertNotNull(response)
        println("복합 필터 조회 성공 - 조회 건수: ${response.messageList?.size ?: 0}")
    }
}
