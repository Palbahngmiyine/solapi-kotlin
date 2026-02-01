package com.solapi.sdk.message.model.kakao.bms

import com.solapi.sdk.message.lib.JsonSupport
import com.solapi.sdk.message.model.kakao.KakaoBmsOption
import com.solapi.sdk.message.model.kakao.KakaoBmsTargeting
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.serialization.encodeToString

class BmsSerializationTest {

    @Test
    fun `BmsChatBubbleType serializes to uppercase string`() {
        // Given
        val chatBubbleType = BmsChatBubbleType.PREMIUM_VIDEO

        // When
        val json = JsonSupport.json.encodeToString(chatBubbleType)

        // Then
        assertTrue(json.contains("\"PREMIUM_VIDEO\""))
    }

    @Test
    fun `BmsButtonType serializes correctly`() {
        // Given
        val buttonTypeWL = BmsButtonType.WL
        val buttonTypeBF = BmsButtonType.BF

        // When
        val jsonWL = JsonSupport.json.encodeToString(buttonTypeWL)
        val jsonBF = JsonSupport.json.encodeToString(buttonTypeBF)

        // Then
        assertTrue(jsonWL.contains("\"WL\""))
        assertTrue(jsonBF.contains("\"BF\""))
    }

    @Test
    fun `BmsButton serializes all fields correctly`() {
        // Given
        val button = BmsButton(
            linkType = BmsButtonType.WL,
            name = "버튼",
            linkMobile = "https://example.com",
            targetOut = true
        )

        // When
        val json = JsonSupport.json.encodeToString(button)

        // Then
        assertTrue(json.contains("\"linkType\":\"WL\""))
        assertTrue(json.contains("\"name\":\"버튼\""))
        assertTrue(json.contains("\"linkMobile\":\"https://example.com\""))
        assertTrue(json.contains("\"targetOut\":true"))
    }

    @Test
    fun `BmsCommerce serializes prices as numbers without quotes`() {
        // Given
        val commerce = BmsCommerce(
            title = "상품",
            regularPrice = 129000,
            discountPrice = 99000,
            discountRate = 23
        )

        // When
        val json = JsonSupport.json.encodeToString(commerce)

        // Then
        assertTrue(json.contains("\"regularPrice\":129000"))
        assertTrue(json.contains("\"discountPrice\":99000"))
        assertTrue(json.contains("\"discountRate\":23"))
        assertFalse(json.contains("\"regularPrice\":\"129000\""))
    }

    @Test
    fun `BmsCoupon serializes with required fields`() {
        // Given
        val coupon = BmsCoupon(
            title = "5000원 할인 쿠폰",
            description = "설명",
            linkMobile = "https://example.com"
        )

        // When
        val json = JsonSupport.json.encodeToString(coupon)

        // Then
        assertTrue(json.contains("\"title\":\"5000원 할인 쿠폰\""))
        assertTrue(json.contains("\"description\":\"설명\""))
        assertTrue(json.contains("\"linkMobile\":\"https://example.com\""))
    }

    @Test
    fun `BmsVideo serializes all fields`() {
        // Given
        val video = BmsVideo(
            videoUrl = "https://tv.kakao.com/v/123456",
            imageId = "IMG001",
            imageLink = "https://example.com"
        )

        // When
        val json = JsonSupport.json.encodeToString(video)

        // Then
        assertTrue(json.contains("\"videoUrl\":\"https://tv.kakao.com/v/123456\""))
        assertTrue(json.contains("\"imageId\":\"IMG001\""))
        assertTrue(json.contains("\"imageLink\":\"https://example.com\""))
    }

    @Test
    fun `BmsMainWideItem serializes without header and content fields`() {
        // Given
        val wideItem = BmsMainWideItem(
            title = "타이틀",
            imageId = "IMG123",
            linkMobile = "https://example.com"
        )

        // When
        val json = JsonSupport.json.encodeToString(wideItem)

        // Then
        assertTrue(json.contains("\"title\":\"타이틀\""))
        assertTrue(json.contains("\"imageId\":\"IMG123\""))
        assertTrue(json.contains("\"linkMobile\":\"https://example.com\""))
        assertFalse(json.contains("\"header\""))
        assertFalse(json.contains("\"content\""))
        assertFalse(json.contains("\"buttons\""))
    }

    @Test
    fun `BmsCarouselItem with Feed fields serializes correctly`() {
        // Given
        val item = BmsCarouselItem(
            header = "제목",
            content = "내용",
            imageId = "IMG123"
        )

        // When
        val json = JsonSupport.json.encodeToString(item)

        // Then
        assertTrue(json.contains("\"header\":\"제목\""))
        assertTrue(json.contains("\"content\":\"내용\""))
        assertTrue(json.contains("\"imageId\":\"IMG123\""))
    }

    @Test
    fun `BmsCarouselItem with Commerce fields serializes correctly`() {
        // Given
        val commerce = BmsCommerce(title = "상품", regularPrice = 10000)
        val item = BmsCarouselItem(
            commerce = commerce,
            additionalContent = "추가정보",
            imageId = "IMG123"
        )

        // When
        val json = JsonSupport.json.encodeToString(item)

        // Then
        assertTrue(json.contains("\"commerce\""))
        assertTrue(json.contains("\"additionalContent\":\"추가정보\""))
        assertTrue(json.contains("\"imageId\":\"IMG123\""))
    }

    @Test
    fun `KakaoBmsOption maintains backward compatibility with targeting only`() {
        // Given
        val option = KakaoBmsOption(targeting = KakaoBmsTargeting.I)

        // When
        val json = JsonSupport.json.encodeToString(option)

        // Then
        assertTrue(json.contains("\"targeting\":\"I\""))
    }

    @Test
    fun `KakaoBmsOption with CAROUSEL_FEED serializes without head field`() {
        // Given
        val option = KakaoBmsOption(
            targeting = KakaoBmsTargeting.I,
            chatBubbleType = BmsChatBubbleType.CAROUSEL_FEED,
            carousel = BmsCarousel(
                list = listOf(
                    BmsCarouselItem(header = "제목", content = "내용", imageId = "IMG123")
                ),
                tail = BmsCarouselTail(linkMobile = "https://example.com")
            )
        )

        // When
        val json = JsonSupport.json.encodeToString(option)

        // Then
        assertTrue(json.contains("\"chatBubbleType\":\"CAROUSEL_FEED\""))
        assertTrue(json.contains("\"carousel\""))
        assertTrue(json.contains("\"list\""))
        assertTrue(json.contains("\"header\":\"제목\""))
        assertFalse(json.contains("\"head\""))
    }

    @Test
    fun `KakaoBmsOption with CAROUSEL_COMMERCE serializes with head field`() {
        // Given
        val option = KakaoBmsOption(
            targeting = KakaoBmsTargeting.I,
            chatBubbleType = BmsChatBubbleType.CAROUSEL_COMMERCE,
            carousel = BmsCarousel(
                head = BmsCarouselHead(header = "인트로", content = "설명", imageId = "IMG000"),
                list = listOf(
                    BmsCarouselItem(
                        commerce = BmsCommerce(title = "상품", regularPrice = 129000),
                        imageId = "IMG123"
                    )
                ),
                tail = BmsCarouselTail(linkMobile = "https://example.com")
            )
        )

        // When
        val json = JsonSupport.json.encodeToString(option)

        // Then
        assertTrue(json.contains("\"chatBubbleType\":\"CAROUSEL_COMMERCE\""))
        assertTrue(json.contains("\"carousel\""))
        assertTrue(json.contains("\"head\""))
        assertTrue(json.contains("\"header\":\"인트로\""))
        assertTrue(json.contains("\"commerce\""))
    }

    @Test
    fun `KakaoBmsOption serializes all field types correctly`() {
        // Given
        val option = KakaoBmsOption(
            targeting = KakaoBmsTargeting.I,
            chatBubbleType = BmsChatBubbleType.COMMERCE,
            adult = false,
            header = "헤더",
            imageId = "IMG001",
            imageLink = "https://example.com",
            additionalContent = "추가내용",
            content = "본문",
            buttons = listOf(
                BmsButton(linkType = BmsButtonType.WL, name = "버튼", linkMobile = "https://example.com")
            ),
            commerce = BmsCommerce(title = "상품", regularPrice = 50000),
            video = BmsVideo(videoUrl = "https://tv.kakao.com/v/123456")
        )

        // When
        val json = JsonSupport.json.encodeToString(option)

        // Then
        assertTrue(json.contains("\"chatBubbleType\":\"COMMERCE\""))
        assertTrue(json.contains("\"targeting\":\"I\""))
        assertTrue(json.contains("\"adult\":false"))
        assertTrue(json.contains("\"buttons\""))
        assertTrue(json.contains("\"commerce\""))
        assertTrue(json.contains("\"video\""))
    }
}
