package com.solapi.example;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.MessageType;
import com.solapi.sdk.message.model.voice.VoiceOption;
import com.solapi.sdk.message.model.voice.VoiceType;
import com.solapi.sdk.message.service.DefaultMessageService;

/**
 * 음성 메시지 발송 예제
 *
 * 환경변수 설정:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 수신번호
 *
 * 음성 메시지는 TTS(Text-to-Speech)를 통해 텍스트를 음성으로 변환하여 발송합니다.
 * VoiceType: FEMALE(여성), MALE(남성)
 */
public class SendVoiceExample {

    public static void main(String[] args) {
        // 환경변수에서 설정 로드
        String apiKey = System.getenv("SOLAPI_API_KEY");
        String apiSecret = System.getenv("SOLAPI_API_SECRET");
        String sender = System.getenv("SOLAPI_SENDER");
        String recipient = System.getenv("SOLAPI_RECIPIENT");

        if (apiKey == null || apiSecret == null) {
            System.err.println("SOLAPI_API_KEY and SOLAPI_API_SECRET must be set");
            System.exit(1);
        }
        if (sender == null || recipient == null) {
            System.err.println("SOLAPI_SENDER and SOLAPI_RECIPIENT must be set");
            System.exit(1);
        }

        // SDK 클라이언트 생성
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);

        // 음성 옵션 설정
        VoiceOption voiceOption = new VoiceOption();
        voiceOption.setVoiceType(VoiceType.FEMALE);       // 여성 음성
        voiceOption.setHeaderMessage("안녕하세요.");       // 헤더 메시지
        voiceOption.setTailMessage("감사합니다.");         // 테일 메시지

        // 음성 메시지 생성
        Message message = new Message();
        message.setType(MessageType.VOICE);
        message.setFrom(sender);
        message.setTo(recipient);
        message.setText("음성 메시지 본문입니다. 이 메시지는 TTS로 변환되어 발송됩니다.");
        message.setVoiceOptions(voiceOption);

        try {
            // 음성 메시지 발송
            MultipleDetailMessageSentResponse response = messageService.send(message, null);

            System.out.println("음성 메시지 발송 성공!");
            System.out.println("Group ID: " + response.getGroupInfo().getGroupId());

        } catch (Exception e) {
            System.err.println("음성 메시지 발송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
