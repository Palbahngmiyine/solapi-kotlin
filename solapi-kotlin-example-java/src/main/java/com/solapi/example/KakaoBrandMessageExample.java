package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.MessageType;
import com.solapi.sdk.message.model.StorageType;
import com.solapi.sdk.message.model.kakao.KakaoBmsOption;
import com.solapi.sdk.message.model.kakao.KakaoOption;
import com.solapi.sdk.message.model.kakao.bms.BmsButton;
import com.solapi.sdk.message.model.kakao.bms.BmsButtonType;
import com.solapi.sdk.message.model.kakao.bms.BmsChatBubbleType;
import com.solapi.sdk.message.model.kakao.bms.BmsCoupon;
import com.solapi.sdk.message.service.DefaultMessageService;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

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
public class KakaoBrandMessageExample {

    public static void main(String[] args) {
        // 환경변수에서 설정 로드
        String apiKey = System.getenv("SOLAPI_API_KEY");
        String apiSecret = System.getenv("SOLAPI_API_SECRET");
        String sender = System.getenv("SOLAPI_SENDER");
        String recipient = System.getenv("SOLAPI_RECIPIENT");
        String pfId = System.getenv("SOLAPI_KAKAO_PF_ID");

        if (apiKey == null || apiSecret == null) {
            System.err.println("SOLAPI_API_KEY and SOLAPI_API_SECRET must be set");
            System.exit(1);
        }
        if (sender == null || recipient == null) {
            System.err.println("SOLAPI_SENDER and SOLAPI_RECIPIENT must be set");
            System.exit(1);
        }
        if (pfId == null) {
            System.err.println("SOLAPI_KAKAO_PF_ID must be set for Kakao Brand Message");
            System.exit(1);
        }

        // SDK 클라이언트 생성
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);

        // 예제 1: TEXT 타입 브랜드 메시지
        sendTextBrandMessage(messageService, sender, recipient, pfId);

        // 예제 2: IMAGE 타입 브랜드 메시지 (이미지 파일 필요)
        // sendImageBrandMessage(messageService, sender, recipient, pfId);
    }

    /**
     * TEXT 타입 브랜드 메시지 발송
     */
    private static void sendTextBrandMessage(
            DefaultMessageService messageService,
            String sender,
            String recipient,
            String pfId
    ) {
        System.out.println("\n=== TEXT 타입 브랜드 메시지 발송 ===");

        // BMS 버튼 생성
        BmsButton webLinkButton = new BmsButton();
        webLinkButton.setLinkType(BmsButtonType.WL);
        webLinkButton.setName("바로가기");
        webLinkButton.setLinkMobile("https://example.com");
        webLinkButton.setLinkPc("https://example.com");

        BmsButton channelAddButton = new BmsButton();
        channelAddButton.setLinkType(BmsButtonType.AC);
        channelAddButton.setName("채널 추가");

        List<BmsButton> buttons = Arrays.asList(webLinkButton, channelAddButton);

        // 쿠폰 생성 (선택사항)
        BmsCoupon coupon = new BmsCoupon();
        coupon.setTitle("10% 할인쿠폰");
        coupon.setDescription("첫 구매 고객 전용");

        // BMS 옵션 설정
        KakaoBmsOption bmsOption = new KakaoBmsOption();
        bmsOption.setChatBubbleType(BmsChatBubbleType.TEXT);
        bmsOption.setContent("브랜드 메시지 TEXT 타입 테스트입니다.");
        bmsOption.setButtons(buttons);
        bmsOption.setCoupon(coupon);
        bmsOption.setAdult(false);

        // 카카오 옵션 설정
        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setPfId(pfId);
        kakaoOption.setBms(bmsOption);

        // 브랜드 메시지 생성
        Message message = new Message();
        message.setType(MessageType.BMS_FREE);
        message.setFrom(sender);
        message.setTo(recipient);
        message.setKakaoOptions(kakaoOption);

        try {
            MultipleDetailMessageSentResponse response = messageService.send(message, null);

            System.out.println("TEXT 브랜드 메시지 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());

        } catch (Exception e) {
            System.err.println("TEXT 브랜드 메시지 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * IMAGE 타입 브랜드 메시지 발송 (이미지 파일 필요)
     */
    private static void sendImageBrandMessage(
            DefaultMessageService messageService,
            String sender,
            String recipient,
            String pfId
    ) {
        System.out.println("\n=== IMAGE 타입 브랜드 메시지 발송 ===");

        try {
            // 이미지 파일 로드
            URL imageUrl = KakaoBrandMessageExample.class.getClassLoader().getResource("images/sample.jpg");
            if (imageUrl == null) {
                System.err.println("Sample image not found. Skipping IMAGE type example.");
                return;
            }

            File tempFile = File.createTempFile("bms-image", ".jpg");
            tempFile.deleteOnExit();
            try (InputStream is = imageUrl.openStream()) {
                Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // 이미지 업로드 (BMS 스토리지 타입)
            String imageId = messageService.uploadFile(tempFile, StorageType.BMS, null);
            System.out.println("이미지 업로드 완료 - imageId: " + imageId);

            // BMS 버튼 생성
            BmsButton webLinkButton = new BmsButton();
            webLinkButton.setLinkType(BmsButtonType.WL);
            webLinkButton.setName("자세히 보기");
            webLinkButton.setLinkMobile("https://example.com");
            webLinkButton.setLinkPc("https://example.com");

            // BMS 옵션 설정
            KakaoBmsOption bmsOption = new KakaoBmsOption();
            bmsOption.setChatBubbleType(BmsChatBubbleType.IMAGE);
            bmsOption.setImageId(imageId);
            bmsOption.setImageLink("https://example.com/image");
            bmsOption.setContent("IMAGE 타입 브랜드 메시지입니다.");
            bmsOption.setButtons(Arrays.asList(webLinkButton));
            bmsOption.setAdult(false);

            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(pfId);
            kakaoOption.setBms(bmsOption);

            Message message = new Message();
            message.setType(MessageType.BMS_FREE);
            message.setFrom(sender);
            message.setTo(recipient);
            message.setKakaoOptions(kakaoOption);

            MultipleDetailMessageSentResponse response = messageService.send(message, null);

            System.out.println("IMAGE 브랜드 메시지 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());

        } catch (Exception e) {
            System.err.println("IMAGE 브랜드 메시지 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
