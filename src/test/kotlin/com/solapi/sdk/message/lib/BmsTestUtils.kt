package com.solapi.sdk.message.lib

import com.solapi.sdk.message.model.kakao.KakaoBmsOption
import com.solapi.sdk.message.model.kakao.KakaoBmsTargeting
import com.solapi.sdk.message.model.kakao.bms.*

/**
 * BMS Free 테스트 헬퍼 함수들
 */
object BmsTestUtils {

    /**
     * 웹링크 버튼 생성
     */
    fun createWebLinkButton(
        name: String = "버튼",
        linkMobile: String = "https://example.com",
        linkPc: String? = null,
        targetOut: Boolean? = null
    ): BmsButton = BmsButton(
        linkType = BmsButtonType.WL,
        name = name,
        linkMobile = linkMobile,
        linkPc = linkPc,
        targetOut = targetOut
    )

    /**
     * 앱링크 버튼 생성
     */
    fun createAppLinkButton(
        name: String = "앱 열기",
        linkAndroid: String = "intent://...",
        linkIos: String = "iosapp://..."
    ): BmsButton = BmsButton(
        linkType = BmsButtonType.AL,
        name = name,
        linkAndroid = linkAndroid,
        linkIos = linkIos
    )

    /**
     * 채널 추가 버튼 생성
     */
    fun createChannelAddButton(name: String = "채널 추가"): BmsButton = BmsButton(
        linkType = BmsButtonType.AC,
        name = name
    )

    /**
     * 봇 키워드 버튼 생성
     */
    fun createBotKeywordButton(name: String = "키워드"): BmsButton = BmsButton(
        linkType = BmsButtonType.BK,
        name = name
    )

    /**
     * 쿠폰 생성 - 퍼센트 할인
     */
    fun createPercentCoupon(
        percent: Int = 10,
        description: String = "설명",
        linkMobile: String? = "https://example.com"
    ): BmsCoupon = BmsCoupon(
        title = "${percent}% 할인 쿠폰",
        description = description,
        linkMobile = linkMobile
    )

    /**
     * 쿠폰 생성 - 금액 할인
     */
    fun createWonCoupon(
        won: Int = 5000,
        description: String = "설명",
        linkMobile: String? = "https://example.com"
    ): BmsCoupon = BmsCoupon(
        title = "${won}원 할인 쿠폰",
        description = description,
        linkMobile = linkMobile
    )

    /**
     * 쿠폰 생성 - 배송비 할인
     */
    fun createShippingCoupon(
        description: String = "설명",
        linkMobile: String? = "https://example.com"
    ): BmsCoupon = BmsCoupon(
        title = "배송비 할인 쿠폰",
        description = description,
        linkMobile = linkMobile
    )

    /**
     * 쿠폰 생성 - 무료 쿠폰
     */
    fun createFreeCoupon(
        item: String = "커피",
        description: String = "설명",
        linkMobile: String? = "https://example.com"
    ): BmsCoupon = BmsCoupon(
        title = "$item 무료 쿠폰",
        description = description,
        linkMobile = linkMobile
    )

    /**
     * 쿠폰 생성 - UP 쿠폰
     */
    fun createUpCoupon(
        item: String = "포인트",
        description: String = "설명",
        linkMobile: String? = "https://example.com"
    ): BmsCoupon = BmsCoupon(
        title = "$item UP 쿠폰",
        description = description,
        linkMobile = linkMobile
    )

    /**
     * 커머스 정보 생성
     */
    fun createCommerce(
        title: String = "상품명",
        regularPrice: Long = 50000,
        discountPrice: Long? = null,
        discountRate: Int? = null,
        discountFixed: Long? = null
    ): BmsCommerce = BmsCommerce(
        title = title,
        regularPrice = regularPrice,
        discountPrice = discountPrice,
        discountRate = discountRate,
        discountFixed = discountFixed
    )

    /**
     * 비디오 정보 생성
     */
    fun createVideo(
        videoUrl: String,
        imageId: String,
        imageLink: String? = null
    ): BmsVideo = BmsVideo(
        videoUrl = videoUrl,
        imageId = imageId,
        imageLink = imageLink
    )

    /**
     * 캐러셀 피드 아이템 생성
     */
    fun createCarouselFeedItem(
        imageId: String,
        header: String = "제목",
        content: String = "내용",
        imageLink: String? = null,
        buttons: List<BmsButton>? = null,
        coupon: BmsCoupon? = null
    ): BmsCarouselItem = BmsCarouselItem(
        header = header,
        content = content,
        imageId = imageId,
        imageLink = imageLink,
        buttons = buttons,
        coupon = coupon
    )

    /**
     * 캐러셀 커머스 아이템 생성
     */
    fun createCarouselCommerceItem(
        imageId: String,
        commerce: BmsCommerce,
        additionalContent: String? = null,
        imageLink: String? = null,
        buttons: List<BmsButton>? = null,
        coupon: BmsCoupon? = null
    ): BmsCarouselItem = BmsCarouselItem(
        commerce = commerce,
        additionalContent = additionalContent,
        imageId = imageId,
        imageLink = imageLink,
        buttons = buttons,
        coupon = coupon
    )

    /**
     * 캐러셀 헤드 생성
     */
    fun createCarouselHead(
        header: String = "인트로",
        content: String? = null,
        imageId: String? = null,
        linkMobile: String? = null
    ): BmsCarouselHead = BmsCarouselHead(
        header = header,
        content = content,
        imageId = imageId,
        linkMobile = linkMobile
    )

    /**
     * 캐러셀 테일 생성
     */
    fun createCarouselTail(
        linkMobile: String = "https://example.com",
        linkPc: String? = null
    ): BmsCarouselTail = BmsCarouselTail(
        linkMobile = linkMobile,
        linkPc = linkPc
    )

    /**
     * 메인 와이드 아이템 생성
     */
    fun createMainWideItem(
        imageId: String,
        title: String = "메인 타이틀",
        linkMobile: String? = "https://example.com",
        linkPc: String? = null
    ): BmsMainWideItem = BmsMainWideItem(
        title = title,
        imageId = imageId,
        linkMobile = linkMobile,
        linkPc = linkPc
    )

    /**
     * 서브 와이드 아이템 생성
     */
    fun createSubWideItem(
        imageId: String,
        title: String,
        linkMobile: String? = "https://example.com",
        linkPc: String? = null
    ): BmsSubWideItem = BmsSubWideItem(
        title = title,
        imageId = imageId,
        linkMobile = linkMobile,
        linkPc = linkPc
    )

    /**
     * TEXT 타입 BMS 옵션 생성 (최소)
     */
    fun createTextBmsOption(
        content: String = "텍스트 메시지 내용",
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.TEXT,
        content = content
    )

    /**
     * TEXT 타입 BMS 옵션 생성 (전체 필드)
     * TEXT 타입은 adult, content, buttons, coupon만 지원 (header, additionalContent 미지원)
     */
    fun createTextBmsOptionFull(
        content: String = "텍스트 메시지 내용",
        buttons: List<BmsButton>,
        coupon: BmsCoupon? = null,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.TEXT,
        adult = adult,
        content = content,
        buttons = buttons,
        coupon = coupon
    )

    /**
     * IMAGE 타입 BMS 옵션 생성 (최소)
     */
    fun createImageBmsOption(
        imageId: String,
        content: String = "이미지 메시지 내용",
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.IMAGE,
        imageId = imageId,
        content = content
    )

    /**
     * IMAGE 타입 BMS 옵션 생성 (전체 필드)
     * IMAGE 타입은 header, additionalContent 미지원 - Message.text로 content 전달
     */
    fun createImageBmsOptionFull(
        imageId: String,
        imageLink: String = "https://example.com",
        buttons: List<BmsButton>,
        coupon: BmsCoupon? = null,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.IMAGE,
        adult = adult,
        imageId = imageId,
        imageLink = imageLink,
        buttons = buttons,
        coupon = coupon
    )

    /**
     * WIDE 타입 BMS 옵션 생성 (최소)
     */
    fun createWideBmsOption(
        imageId: String,
        content: String = "와이드 메시지 내용",
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.WIDE,
        imageId = imageId,
        content = content
    )

    /**
     * WIDE 타입 BMS 옵션 생성 (전체 필드)
     */
    fun createWideBmsOptionFull(
        imageId: String,
        imageLink: String = "https://example.com",
        buttons: List<BmsButton>,
        coupon: BmsCoupon? = null,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.WIDE,
        adult = adult,
        imageId = imageId,
        imageLink = imageLink,
        buttons = buttons,
        coupon = coupon
    )

    /**
     * WIDE_ITEM_LIST 타입 BMS 옵션 생성 (최소)
     * header는 WIDE_ITEM_LIST 타입의 필수 필드
     */
    fun createWideItemListBmsOption(
        mainWideItem: BmsMainWideItem,
        subWideItemList: List<BmsSubWideItem>,
        header: String = "WIDE_ITEM_LIST",
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.WIDE_ITEM_LIST,
        header = header,
        mainWideItem = mainWideItem,
        subWideItemList = subWideItemList
    )

    /**
     * WIDE_ITEM_LIST 타입 BMS 옵션 생성 (전체 필드)
     */
    fun createWideItemListBmsOptionFull(
        mainWideItem: BmsMainWideItem,
        subWideItemList: List<BmsSubWideItem>,
        header: String = "헤더",
        buttons: List<BmsButton>,
        coupon: BmsCoupon? = null,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.WIDE_ITEM_LIST,
        adult = adult,
        header = header,
        mainWideItem = mainWideItem,
        subWideItemList = subWideItemList,
        buttons = buttons,
        coupon = coupon
    )

    /**
     * COMMERCE 타입 BMS 옵션 생성 (최소)
     */
    fun createCommerceBmsOption(
        imageId: String,
        commerce: BmsCommerce,
        buttons: List<BmsButton>,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.COMMERCE,
        imageId = imageId,
        commerce = commerce,
        buttons = buttons
    )

    /**
     * COMMERCE 타입 BMS 옵션 생성 (전체 필드)
     * COMMERCE 타입은 header 미지원, additionalContent 지원
     */
    fun createCommerceBmsOptionFull(
        imageId: String,
        commerce: BmsCommerce,
        buttons: List<BmsButton>,
        imageLink: String = "https://example.com",
        additionalContent: String = "추가 내용",
        coupon: BmsCoupon? = null,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.COMMERCE,
        adult = adult,
        imageId = imageId,
        imageLink = imageLink,
        additionalContent = additionalContent,
        commerce = commerce,
        buttons = buttons,
        coupon = coupon
    )

    /**
     * CAROUSEL_FEED 타입 BMS 옵션 생성 (최소)
     */
    fun createCarouselFeedBmsOption(
        carouselItems: List<BmsCarouselItem>,
        tail: BmsCarouselTail? = null,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.CAROUSEL_FEED,
        carousel = BmsCarousel(
            list = carouselItems,
            tail = tail
        )
    )

    /**
     * CAROUSEL_FEED 타입 BMS 옵션 생성 (전체 필드)
     */
    fun createCarouselFeedBmsOptionFull(
        carouselItems: List<BmsCarouselItem>,
        tail: BmsCarouselTail,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.CAROUSEL_FEED,
        adult = adult,
        carousel = BmsCarousel(
            list = carouselItems,
            tail = tail
        )
    )

    /**
     * CAROUSEL_COMMERCE 타입 BMS 옵션 생성 (최소)
     */
    fun createCarouselCommerceBmsOption(
        carouselItems: List<BmsCarouselItem>,
        tail: BmsCarouselTail? = null,
        head: BmsCarouselHead? = null,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.CAROUSEL_COMMERCE,
        carousel = BmsCarousel(
            head = head,
            list = carouselItems,
            tail = tail
        )
    )

    /**
     * CAROUSEL_COMMERCE 타입 BMS 옵션 생성 (전체 필드)
     */
    fun createCarouselCommerceBmsOptionFull(
        carouselItems: List<BmsCarouselItem>,
        tail: BmsCarouselTail,
        head: BmsCarouselHead,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.CAROUSEL_COMMERCE,
        adult = adult,
        carousel = BmsCarousel(
            head = head,
            list = carouselItems,
            tail = tail
        )
    )

    /**
     * PREMIUM_VIDEO 타입 BMS 옵션 생성 (최소)
     */
    fun createPremiumVideoBmsOption(
        video: BmsVideo,
        content: String = "비디오 메시지 내용",
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.PREMIUM_VIDEO,
        video = video,
        content = content
    )

    /**
     * PREMIUM_VIDEO 타입 BMS 옵션 생성 (전체 필드)
     * PREMIUM_VIDEO 타입은 adult, header, content, video, buttons, coupon만 지원 (additionalContent 미지원)
     */
    fun createPremiumVideoBmsOptionFull(
        video: BmsVideo,
        content: String = "비디오 메시지 내용",
        header: String = "헤더",
        buttons: List<BmsButton>,
        coupon: BmsCoupon? = null,
        adult: Boolean = false,
        targeting: KakaoBmsTargeting = KakaoBmsTargeting.I
    ): KakaoBmsOption = KakaoBmsOption(
        targeting = targeting,
        chatBubbleType = BmsChatBubbleType.PREMIUM_VIDEO,
        adult = adult,
        header = header,
        video = video,
        content = content,
        buttons = buttons,
        coupon = coupon
    )
}
