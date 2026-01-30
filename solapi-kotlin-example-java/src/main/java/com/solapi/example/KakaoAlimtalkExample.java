package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.MessageType;
import com.solapi.sdk.message.model.kakao.KakaoOption;
import com.solapi.sdk.message.service.DefaultMessageService;

import java.util.HashMap;
import java.util.Map;

/**
 * 카카오 알림톡 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 * - SOLAPI_KAKAO_PF_ID: 카카오 비즈니스 채널 ID
 * - SOLAPI_KAKAO_TEMPLATE_ID: 카카오 알림톡 템플릿 ID
 *
 * 알림톡 특징:
 * - 사전에 검수 승인된 템플릿만 사용 가능
 * - 정보성 메시지 전용 (광고 불가)
 * - 변수 치환을 통해 동적 내용 전달 가능
 * 번외:
 * 브랜드 메시지 템플릿과 동일한 형태의 코드로 발송하실 수 있습니다!
 */
public class KakaoAlimtalkExample {

    public static void main(String[] args) {
        // 환경변수에서 설정 로드
        String apiKey = System.getenv("SOLAPI_API_KEY");
        String apiSecret = System.getenv("SOLAPI_API_SECRET");
        String sender = System.getenv("SOLAPI_SENDER");
        String recipient = System.getenv("SOLAPI_RECIPIENT");
        String pfId = System.getenv("SOLAPI_KAKAO_PF_ID");
        String templateId = System.getenv("SOLAPI_KAKAO_TEMPLATE_ID");

        if (apiKey == null || apiSecret == null) {
            System.err.println("SOLAPI_API_KEY and SOLAPI_API_SECRET must be set");
            System.exit(1);
        }
        if (sender == null || recipient == null) {
            System.err.println("SOLAPI_SENDER and SOLAPI_RECIPIENT must be set");
            System.exit(1);
        }
        if (pfId == null || templateId == null) {
            System.err.println("SOLAPI_KAKAO_PF_ID and SOLAPI_KAKAO_TEMPLATE_ID must be set for Kakao Alimtalk");
            System.exit(1);
        }

        // SDK 클라이언트 생성
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);

        // 템플릿 변수 설정 (템플릿에 맞게 조정 필요)
        Map<String, String> variables = new HashMap<>();
        variables.put("name", "홍길동");
        variables.put("code", "123456");

        // 카카오 옵션 설정
        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setPfId(pfId);
        kakaoOption.setTemplateId(templateId);
        kakaoOption.setVariables(variables);

        // 알림톡 메시지 생성
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(recipient);
        message.setKakaoOptions(kakaoOption);

        try {
            // 알림톡 발송
            MultipleDetailMessageSentResponse response = messageService.send(message, null);

            System.out.println("알림톡 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());

        } catch (Exception e) {
            System.err.println("알림톡 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
