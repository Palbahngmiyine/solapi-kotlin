package com.solapi.example

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.StorageType
import com.solapi.sdk.message.model.kakao.KakaoBmsOption
import com.solapi.sdk.message.model.kakao.KakaoOption
import com.solapi.sdk.message.model.kakao.bms.BmsButton
import com.solapi.sdk.message.model.kakao.bms.BmsButtonType
import com.solapi.sdk.message.model.kakao.bms.BmsChatBubbleType
import com.solapi.sdk.message.model.kakao.bms.BmsCoupon
import com.solapi.sdk.message.service.DefaultMessageService
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * 카카오 브랜드 메시지 (BMS_FREE) 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 * - SOLAPI_KAKAO_PF_ID: 카카오 비즈니스 채널 ID
 *
 * 브랜드 메시지 특징:
 * - 다양한 템플릿 형태 지원 (TEXT, IMAGE, WIDE, COMMERCE 등)
 * - 쿠폰, 버튼 등 다양한 구성요소 포함 가능
 * - 캐러셀 형태의 메시지 지원
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
    val pfId = System.getenv("SOLAPI_KAKAO_PF_ID")
        ?: error("SOLAPI_KAKAO_PF_ID must be set for Kakao Brand Message")

    // SDK 클라이언트 생성
    val messageService = SolapiClient.createInstance(apiKey, apiSecret)

    // 예제 1: TEXT 타입 브랜드 메시지
    sendTextBrandMessage(messageService, sender, recipient, pfId)

    // 예제 2: IMAGE 타입 브랜드 메시지 (이미지 파일 필요)
    // sendImageBrandMessage(messageService, sender, recipient, pfId)
}

/**
 * TEXT 타입 브랜드 메시지 발송
 */
private fun sendTextBrandMessage(
    messageService: DefaultMessageService,
    sender: String,
    recipient: String,
    pfId: String
) {
    println("\n=== TEXT 타입 브랜드 메시지 발송 ===")

    // BMS 버튼 생성
    val buttons = listOf(
        BmsButton(
            linkType = BmsButtonType.WL,
            name = "바로가기",
            linkMobile = "https://example.com",
            linkPc = "https://example.com"
        ),
        BmsButton(
            linkType = BmsButtonType.AC,    // Add Channel
            name = "채널 추가"
        )
    )

    // 쿠폰 생성 (선택사항)
    val coupon = BmsCoupon(
        title = "10% 할인쿠폰",
        description = "첫 구매 고객 전용"
    )

    // BMS 옵션 설정
    val bmsOption = KakaoBmsOption(
        chatBubbleType = BmsChatBubbleType.TEXT,
        content = "브랜드 메시지 TEXT 타입 테스트입니다.",
        buttons = buttons,
        coupon = coupon,
        adult = false
    )

    // 브랜드 메시지 생성
    val message = Message(
        type = MessageType.BMS_FREE,
        from = sender,
        to = recipient,
        kakaoOptions = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )
    )

    try {
        val response = messageService.send(message)

        println("TEXT 브랜드 메시지 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")

    } catch (e: Exception) {
        System.err.println("TEXT 브랜드 메시지 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * IMAGE 타입 브랜드 메시지 발송 (이미지 파일 필요)
 */
private fun sendImageBrandMessage(
    messageService: DefaultMessageService,
    sender: String,
    recipient: String,
    pfId: String
) {
    println("\n=== IMAGE 타입 브랜드 메시지 발송 ===")

    try {
        // 이미지 파일 로드
        val imageUrl = object {}.javaClass.classLoader.getResource("images/sample.jpg")
        if (imageUrl == null) {
            println("Sample image not found. Skipping IMAGE type example.")
            return
        }

        val tempFile = File.createTempFile("bms-image", ".jpg").apply {
            deleteOnExit()
        }
        imageUrl.openStream().use { input ->
            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }

        // 이미지 업로드 (BMS 스토리지 타입)
        val imageId = messageService.uploadFile(tempFile, StorageType.BMS)
        println("이미지 업로드 완료 - imageId: $imageId")

        // BMS 버튼 생성
        val buttons = listOf(
            BmsButton(
                linkType = BmsButtonType.WL,
                name = "자세히 보기",
                linkMobile = "https://example.com",
                linkPc = "https://example.com"
            )
        )

        // BMS 옵션 설정
        val bmsOption = KakaoBmsOption(
            chatBubbleType = BmsChatBubbleType.IMAGE,
            imageId = imageId,
            imageLink = "https://example.com/image",
            content = "IMAGE 타입 브랜드 메시지입니다.",
            buttons = buttons,
            adult = false
        )

        val message = Message(
            type = MessageType.BMS_FREE,
            from = sender,
            to = recipient,
            kakaoOptions = KakaoOption(
                pfId = pfId,
                bms = bmsOption
            )
        )

        val response = messageService.send(message)

        println("IMAGE 브랜드 메시지 발송 성공!")
        println("Group ID: ${response.groupInfo?.groupId}")

    } catch (e: Exception) {
        System.err.println("IMAGE 브랜드 메시지 발송 실패: ${e.message}")
        e.printStackTrace()
    }
}
