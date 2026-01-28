package com.solapi.sdk.message.e2e

import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException
import com.solapi.sdk.message.lib.BmsTestUtils
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.StorageType
import com.solapi.sdk.message.model.kakao.KakaoOption
import com.solapi.sdk.message.model.kakao.bms.BmsCoupon
import com.solapi.sdk.message.service.DefaultMessageService
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import java.io.File

/**
 * BMS Free 발송 E2E 테스트
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 * - SOLAPI_KAKAO_PF_ID: 카카오 비즈니스 채널 ID
 * - SOLAPI_KAKAO_TEMPLATE_ID: 카카오 알림톡 템플릿 ID (선택)
 */
class BmsFreeE2ETest {

    private val apiKey: String? = System.getenv("SOLAPI_API_KEY")
    private val apiSecret: String? = System.getenv("SOLAPI_API_SECRET")
    private val pfId: String? = System.getenv("SOLAPI_KAKAO_PF_ID")
    private val senderNumber: String = System.getenv("SOLAPI_SENDER") ?: "01000000000"
    private val testPhoneNumber: String = System.getenv("SOLAPI_RECIPIENT") ?: "01000000000"

    private val messageService: DefaultMessageService? by lazy {
        if (apiKey != null && apiSecret != null) {
            SolapiClient.createInstance(apiKey, apiSecret)
        } else {
            null
        }
    }

    /**
     * 환경변수 설정 여부 확인
     * @return 환경변수가 설정되었으면 true, 아니면 false
     */
    private fun assumeEnvironmentConfigured(): Boolean {
        if (apiKey.isNullOrBlank() || apiSecret.isNullOrBlank() || pfId.isNullOrBlank()) {
            println("환경변수가 설정되지 않아 테스트를 건너뜁니다. (SOLAPI_API_KEY, SOLAPI_API_SECRET, SOLAPI_KAKAO_PF_ID 필요)")
            return false
        }
        return true
    }

    /**
     * 테스트 이미지 업로드 (일반 - KAKAO 타입, PREMIUM_VIDEO 썸네일용)
     * @param filename 리소스 파일명
     * @return 업로드된 이미지 ID
     */
    private fun uploadTestImage(filename: String = "test-image.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.KAKAO)
    }

    /**
     * BMS 타입 이미지 업로드 (IMAGE, COMMERCE용)
     * @param filename 리소스 파일명
     * @return 업로드된 이미지 ID
     */
    private fun uploadBmsImage(filename: String = "test-image.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.BMS)
    }

    /**
     * BMS WIDE 타입 이미지 업로드 (WIDE용 - 2:1 비율)
     * @param filename 리소스 파일명
     * @return 업로드된 이미지 ID
     */
    private fun uploadBmsWideImage(filename: String = "test-image-2to1.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.BMS_WIDE)
    }

    /**
     * BMS WIDE_ITEM_LIST 메인 이미지 업로드 (2:1 비율)
     * @param filename 리소스 파일명
     * @return 업로드된 이미지 ID
     */
    private fun uploadBmsWideMainItemImage(filename: String = "test-image-2to1.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.BMS_WIDE_MAIN_ITEM_LIST)
    }

    /**
     * BMS WIDE_ITEM_LIST 서브 이미지 업로드 (1:1 비율)
     * @param filename 리소스 파일명
     * @return 업로드된 이미지 ID
     */
    private fun uploadBmsWideSubItemImage(filename: String = "test-image.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.BMS_WIDE_SUB_ITEM_LIST)
    }

    /**
     * BMS CAROUSEL_FEED 이미지 업로드 (2:1 비율)
     * @param filename 리소스 파일명
     * @return 업로드된 이미지 ID
     */
    private fun uploadBmsCarouselFeedImage(filename: String = "test-image-2to1.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.BMS_CAROUSEL_FEED_LIST)
    }

    /**
     * BMS CAROUSEL_COMMERCE 이미지 업로드 (2:1 비율)
     * @param filename 리소스 파일명
     * @return 업로드된 이미지 ID
     */
    private fun uploadBmsCarouselCommerceImage(filename: String = "test-image-2to1.png"): String? {
        val imageUrl = javaClass.classLoader.getResource("images/$filename")
        if (imageUrl == null) {
            println("테스트 이미지가 없어 건너뜁니다: images/$filename")
            return null
        }
        val file = File(imageUrl.toURI())
        return messageService?.uploadFile(file, StorageType.BMS_CAROUSEL_COMMERCE_LIST)
    }

    private fun createBmsFreeMessage(kakaoOption: KakaoOption, text: String? = null): Message = Message(
        type = MessageType.BMS_FREE,
        from = senderNumber,
        to = testPhoneNumber,
        text = text,
        kakaoOptions = kakaoOption
    )

    private fun printExceptionDetails(e: Exception) {
        println("예상된 에러 발생: ${e.message}")
        if (e is SolapiMessageNotReceivedException) {
            println("  실패한 메시지 목록 (${e.failedMessageList.size}건):")
            e.failedMessageList.forEachIndexed { index, failed ->
                println("    [${index + 1}] to: ${failed.to}, statusCode: ${failed.statusCode}, statusMessage: ${failed.statusMessage}")
            }
        }
    }

    // ==================== TEXT 타입 테스트 ====================

    @Test
    fun `TEXT 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val bmsOption = BmsTestUtils.createTextBmsOption(
            content = "BMS Free TEXT 최소 구조 테스트"
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("TEXT 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `TEXT 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val buttons = listOf(
            BmsTestUtils.createWebLinkButton("바로가기", "https://example.com"),
            BmsTestUtils.createChannelAddButton("채널 추가")
        )

        val coupon = BmsTestUtils.createPercentCoupon(10, "할인쿠폰")

        val bmsOption = BmsTestUtils.createTextBmsOptionFull(
            content = "BMS Free TEXT 전체 필드 테스트",
            buttons = buttons,
            coupon = coupon,
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("TEXT 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== IMAGE 타입 테스트 ====================

    @Test
    fun `IMAGE 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val bmsOption = BmsTestUtils.createImageBmsOption(
            imageId = imageId,
            content = "BMS Free IMAGE 최소 구조 테스트"
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("IMAGE 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `IMAGE 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val buttons = listOf(
            BmsTestUtils.createWebLinkButton("자세히 보기", "https://example.com"),
            BmsTestUtils.createAppLinkButton("앱 열기", "intent://main", "iosapp://main")
        )

        val coupon = BmsTestUtils.createWonCoupon(5000, "5000원 할인")

        val bmsOption = BmsTestUtils.createImageBmsOptionFull(
            imageId = imageId,
            imageLink = "https://example.com/image",
            buttons = buttons,
            coupon = coupon,
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption, text = "BMS Free IMAGE 전체 필드 테스트")
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("IMAGE 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== WIDE 타입 테스트 ====================

    @Test
    fun `WIDE 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsWideImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val bmsOption = BmsTestUtils.createWideBmsOption(
            imageId = imageId,
            content = "BMS Free WIDE 최소 구조 테스트"
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("WIDE 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `WIDE 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsWideImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val buttons = listOf(
            BmsTestUtils.createWebLinkButton("바로가기", "https://example.com"),
            BmsTestUtils.createBotKeywordButton("문의하기")
        )

        val coupon = BmsTestUtils.createShippingCoupon("무료배송")

        val bmsOption = BmsTestUtils.createWideBmsOptionFull(
            imageId = imageId,
            imageLink = "https://example.com/wide",
            buttons = buttons,
            coupon = coupon,
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption, text = "BMS Free WIDE 전체 필드 테스트")
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("WIDE 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== WIDE_ITEM_LIST 타입 테스트 ====================

    @Test
    fun `WIDE_ITEM_LIST 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val mainImageId = uploadBmsWideMainItemImage()
        val subImageId = uploadBmsWideSubItemImage()
        if (mainImageId == null || subImageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val mainWideItem = BmsTestUtils.createMainWideItem(
            imageId = mainImageId,
            title = "메인 아이템"
        )

        // WIDE_ITEM_LIST는 최소 3개의 서브 아이템이 필요합니다
        val subWideItemList = listOf(
            BmsTestUtils.createSubWideItem(subImageId, "서브 아이템 1"),
            BmsTestUtils.createSubWideItem(subImageId, "서브 아이템 2"),
            BmsTestUtils.createSubWideItem(subImageId, "서브 아이템 3")
        )

        val bmsOption = BmsTestUtils.createWideItemListBmsOption(
            mainWideItem = mainWideItem,
            subWideItemList = subWideItemList
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("WIDE_ITEM_LIST 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `WIDE_ITEM_LIST 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val mainImageId = uploadBmsWideMainItemImage()
        val subImageId = uploadBmsWideSubItemImage()
        if (mainImageId == null || subImageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val mainWideItem = BmsTestUtils.createMainWideItem(
            imageId = mainImageId,
            title = "메인 아이템 타이틀",
            linkMobile = "https://example.com/main"
        )

        val subWideItemList = listOf(
            BmsTestUtils.createSubWideItem(subImageId, "서브 아이템 1", "https://example.com/sub1"),
            BmsTestUtils.createSubWideItem(subImageId, "서브 아이템 2", "https://example.com/sub2"),
            BmsTestUtils.createSubWideItem(subImageId, "서브 아이템 3", "https://example.com/sub3")
        )

        val buttons = listOf(
            BmsTestUtils.createWebLinkButton("전체보기", "https://example.com/all")
        )

        val coupon = BmsTestUtils.createFreeCoupon("커피", "무료쿠폰")

        val bmsOption = BmsTestUtils.createWideItemListBmsOptionFull(
            mainWideItem = mainWideItem,
            subWideItemList = subWideItemList,
            header = "WIDE_ITEM_LIST 헤더",
            buttons = buttons,
            coupon = coupon,
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("WIDE_ITEM_LIST 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== COMMERCE 타입 테스트 ====================

    @Test
    fun `COMMERCE 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsImage("test-image-2to1.png")
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val commerce = BmsTestUtils.createCommerce(
            title = "테스트 상품",
            regularPrice = 50000
        )

        val buttons = listOf(
            BmsTestUtils.createWebLinkButton("구매하기", "https://example.com/buy")
        )

        val bmsOption = BmsTestUtils.createCommerceBmsOption(
            imageId = imageId,
            commerce = commerce,
            buttons = buttons
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("COMMERCE 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `COMMERCE 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsImage("test-image-2to1.png")
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val commerce = BmsTestUtils.createCommerce(
            title = "프리미엄 상품",
            regularPrice = 129000,
            discountPrice = 99000,
            discountRate = 23
        )

        val buttons = listOf(
            BmsTestUtils.createWebLinkButton("구매하기", "https://example.com/buy"),
            BmsTestUtils.createWebLinkButton("장바구니", "https://example.com/cart")
        )

        val coupon = BmsTestUtils.createUpCoupon("포인트", "2배 적립")

        val bmsOption = BmsTestUtils.createCommerceBmsOptionFull(
            imageId = imageId,
            commerce = commerce,
            buttons = buttons,
            imageLink = "https://example.com/product",
            additionalContent = "무료배송 | 오늘 출발",
            coupon = coupon,
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("COMMERCE 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== CAROUSEL_FEED 타입 테스트 ====================

    @Test
    fun `CAROUSEL_FEED 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsCarouselFeedImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val itemButtons = listOf(
            BmsTestUtils.createWebLinkButton("바로가기", "https://example.com")
        )

        val carouselItems = listOf(
            BmsTestUtils.createCarouselFeedItem(imageId, "아이템 1", "내용 1", buttons = itemButtons),
            BmsTestUtils.createCarouselFeedItem(imageId, "아이템 2", "내용 2", buttons = itemButtons)
        )

        val bmsOption = BmsTestUtils.createCarouselFeedBmsOption(
            carouselItems = carouselItems
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("CAROUSEL_FEED 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `CAROUSEL_FEED 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsCarouselFeedImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val itemButtons = listOf(
            BmsTestUtils.createWebLinkButton("자세히", "https://example.com/detail")
        )

        val carouselItems = listOf(
            BmsTestUtils.createCarouselFeedItem(
                imageId = imageId,
                header = "피드 아이템 1",
                content = "피드 내용 1",
                imageLink = "https://example.com/1",
                buttons = itemButtons
            ),
            BmsTestUtils.createCarouselFeedItem(
                imageId = imageId,
                header = "피드 아이템 2",
                content = "피드 내용 2",
                imageLink = "https://example.com/2",
                buttons = itemButtons,
                coupon = BmsTestUtils.createPercentCoupon(5, "할인")
            ),
            BmsTestUtils.createCarouselFeedItem(
                imageId = imageId,
                header = "피드 아이템 3",
                content = "피드 내용 3",
                imageLink = "https://example.com/3",
                buttons = itemButtons
            )
        )

        val tail = BmsTestUtils.createCarouselTail(
            linkMobile = "https://example.com/more",
            linkPc = "https://example.com/more"
        )

        val bmsOption = BmsTestUtils.createCarouselFeedBmsOptionFull(
            carouselItems = carouselItems,
            tail = tail,
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("CAROUSEL_FEED 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== CAROUSEL_COMMERCE 타입 테스트 ====================

    @Test
    fun `CAROUSEL_COMMERCE 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsCarouselCommerceImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val itemButtons = listOf(
            BmsTestUtils.createWebLinkButton("구매하기", "https://example.com")
        )

        val carouselItems = listOf(
            BmsTestUtils.createCarouselCommerceItem(
                imageId = imageId,
                commerce = BmsTestUtils.createCommerce("상품 1", 30000),
                buttons = itemButtons
            ),
            BmsTestUtils.createCarouselCommerceItem(
                imageId = imageId,
                commerce = BmsTestUtils.createCommerce("상품 2", 40000),
                buttons = itemButtons
            )
        )

        val bmsOption = BmsTestUtils.createCarouselCommerceBmsOption(
            carouselItems = carouselItems
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("CAROUSEL_COMMERCE 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `CAROUSEL_COMMERCE 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsCarouselCommerceImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val head = BmsTestUtils.createCarouselHead(
            header = "베스트 상품",
            content = "이번 주 인기 상품",
            imageId = imageId,
            linkMobile = "https://example.com/best"
        )

        val itemButtons = listOf(
            BmsTestUtils.createWebLinkButton("구매", "https://example.com/buy")
        )

        val carouselItems = listOf(
            BmsTestUtils.createCarouselCommerceItem(
                imageId = imageId,
                commerce = BmsTestUtils.createCommerce("상품 A", 50000, 40000, 20),
                additionalContent = "무료배송",
                imageLink = "https://example.com/a",
                buttons = itemButtons
            ),
            BmsTestUtils.createCarouselCommerceItem(
                imageId = imageId,
                commerce = BmsTestUtils.createCommerce("상품 B", 80000, 60000, 25),
                additionalContent = "오늘 출발",
                imageLink = "https://example.com/b",
                buttons = itemButtons,
                coupon = BmsTestUtils.createWonCoupon(3000, "할인")
            ),
            BmsTestUtils.createCarouselCommerceItem(
                imageId = imageId,
                commerce = BmsTestUtils.createCommerce("상품 C", 35000),
                additionalContent = "인기상품",
                imageLink = "https://example.com/c",
                buttons = itemButtons
            )
        )

        val tail = BmsTestUtils.createCarouselTail(
            linkMobile = "https://example.com/all",
            linkPc = "https://example.com/all"
        )

        val bmsOption = BmsTestUtils.createCarouselCommerceBmsOptionFull(
            carouselItems = carouselItems,
            tail = tail,
            head = head,
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("CAROUSEL_COMMERCE 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== PREMIUM_VIDEO 타입 테스트 ====================

    @Test
    fun `PREMIUM_VIDEO 타입 - 최소 구조`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadTestImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val video = BmsTestUtils.createVideo(
            videoUrl = "https://tv.kakao.com/v/460734285",
            imageId = imageId
        )

        val bmsOption = BmsTestUtils.createPremiumVideoBmsOption(
            video = video,
            content = "BMS Free PREMIUM_VIDEO 최소 구조 테스트"
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("PREMIUM_VIDEO 최소 구조 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `PREMIUM_VIDEO 타입 - 전체 필드`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadTestImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val video = BmsTestUtils.createVideo(
            videoUrl = "https://tv.kakao.com/v/460734285",
            imageId = imageId,
            imageLink = "https://example.com/video"
        )

        val buttons = listOf(
            BmsTestUtils.createWebLinkButton("영상 보기", "https://tv.kakao.com/v/460734285")
        )

        val bmsOption = BmsTestUtils.createPremiumVideoBmsOptionFull(
            video = video,
            content = "BMS Free PREMIUM_VIDEO 전체 필드 테스트",
            header = "PREMIUM_VIDEO 헤더",
            buttons = buttons,
            coupon = BmsTestUtils.createPercentCoupon(10, "프리미엄 비디오 쿠폰"),
            adult = false
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("PREMIUM_VIDEO 전체 필드 - groupId: ${response.groupInfo?.groupId}")
    }

    // ==================== Error Cases 테스트 ====================

    @Test
    fun `IMAGE without imageId - 필수 필드 누락`() {
        if (!assumeEnvironmentConfigured()) return

        val bmsOption = BmsTestUtils.createImageBmsOption(
            imageId = "", // 빈 이미지 ID
            content = "이미지 없는 IMAGE 타입"
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)

        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "이미지 ID 없이 IMAGE 타입 발송 시 에러가 발생해야 함")
    }

    @Test
    fun `COMMERCE without buttons - 버튼 없이 발송 허용`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadBmsImage("test-image-2to1.png")
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val commerce = BmsTestUtils.createCommerce("상품", 10000)

        val bmsOption = BmsTestUtils.createCommerceBmsOption(
            imageId = imageId,
            commerce = commerce,
            buttons = emptyList()
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)
        val response = messageService!!.send(message)

        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("COMMERCE 버튼 없이 발송 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `PREMIUM_VIDEO with invalid videoUrl`() {
        if (!assumeEnvironmentConfigured()) return

        val imageId = uploadTestImage()
        if (imageId == null) {
            println("이미지 업로드 실패로 테스트 건너뜀")
            return
        }

        val video = BmsTestUtils.createVideo(
            videoUrl = "https://invalid-video-url.com/video", // 잘못된 비디오 URL (카카오 TV가 아님)
            imageId = imageId
        )

        val bmsOption = BmsTestUtils.createPremiumVideoBmsOption(
            video = video,
            content = "잘못된 비디오 URL 테스트"
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)

        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "잘못된 비디오 URL로 PREMIUM_VIDEO 타입 발송 시 에러가 발생해야 함")
    }

    @Test
    fun `Invalid coupon title format`() {
        if (!assumeEnvironmentConfigured()) return

        val invalidCoupon = BmsCoupon(
            title = "잘못된 쿠폰 제목",
            description = "설명"
        )

        // TEXT 타입은 adult, content, buttons, coupon만 지원
        val bmsOption = BmsTestUtils.createTextBmsOption(
            content = "잘못된 쿠폰 테스트"
        ).copy(
            buttons = listOf(BmsTestUtils.createWebLinkButton()),
            coupon = invalidCoupon
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)

        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "잘못된 쿠폰 제목 형식으로 발송 시 에러가 발생해야 함")
    }

    @Test
    fun `CAROUSEL_FEED without carousel`() {
        if (!assumeEnvironmentConfigured()) return

        val bmsOption = BmsTestUtils.createCarouselFeedBmsOption(
            carouselItems = emptyList() // 빈 캐러셀
        )

        val kakaoOption = KakaoOption(
            pfId = pfId,
            bms = bmsOption
        )

        val message = createBmsFreeMessage(kakaoOption)

        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "빈 캐러셀로 CAROUSEL_FEED 타입 발송 시 에러가 발생해야 함")
    }
}
