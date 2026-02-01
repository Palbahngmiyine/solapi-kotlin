package com.solapi.sdk.message.model.kakao.bms

import kotlinx.serialization.Serializable

/**
 * BMS Free 쿠폰 정보
 *
 * @property title 쿠폰 제목 (서버에서 필수).
 *   허용 형식 (5가지만 가능):
 *   - "${숫자}원 할인 쿠폰" (1~99,999,999)
 *   - "${숫자}% 할인 쿠폰" (1~100)
 *   - "배송비 할인 쿠폰"
 *   - "${7자 이내} 무료 쿠폰"
 *   - "${7자 이내} UP 쿠폰"
 * @property description 쿠폰 설명 (서버에서 필수).
 *   길이 제한: WIDE/WIDE_ITEM_LIST는 최대 18자, 그 외는 최대 12자
 */
@Serializable
data class BmsCoupon(
    var title: String? = null,
    var description: String? = null,
    var linkMobile: String? = null,
    var linkPc: String? = null,
    var linkAndroid: String? = null,
    var linkIos: String? = null
)
