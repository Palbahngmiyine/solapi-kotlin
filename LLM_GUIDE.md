# SOLAPI Kotlin SDK - LLM Guide

SDK를 사용하여 코드를 작성하는 LLM 에이전트용 기술 가이드.

## Quick Reference

| 항목 | 값 |
|------|-----|
| SDK | `com.solapi:sdk:1.1.0` |
| Docs | https://developers.solapi.com/llms.txt |
| API Ref | https://solapi.github.io/solapi-kotlin/ |
| Java | 8+ |
| Kotlin | 1.8+ |

## Setup

**Gradle (Kotlin DSL):**
```kotlin
implementation("com.solapi:sdk:1.1.0")
```

**Maven:**
```xml
<dependency>
    <groupId>com.solapi</groupId>
    <artifactId>sdk</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Core Pattern

**Kotlin:**
```kotlin
val service = SolapiClient.createInstance("API_KEY", "API_SECRET")
val message = Message(from = "발신번호", to = "수신번호", text = "내용")
val response = service.send(message)
```

**Java:**
```java
DefaultMessageService service = SolapiClient.INSTANCE.createInstance("API_KEY", "API_SECRET");
Message message = new Message();
message.setFrom("발신번호");
message.setTo("수신번호");
message.setText("내용");
service.send(message, null);
```

## Message Types

| Type | Field | Notes |
|------|-------|-------|
| SMS | text | < 80 bytes |
| LMS | text | 80-2000 bytes |
| MMS | imageId | uploadFile() 후 사용, JPG/JPEG, max 200KB |
| ATA | kakaoOptions | 카카오 알림톡, 템플릿 필수 |
| BMS_* | kakaoOptions | 카카오 브랜드 메시지 |
| RCS_* | rcsOptions | RCS 문자 |
| NSA | naverOptions | 네이버 스마트 알림 |
| FAX | fileId | uploadFile() 후 사용 |
| VOICE | voiceOptions | 음성 메시지 |

## KakaoOption

```kotlin
KakaoOption(
    pfId = "카카오채널ID",
    templateId = "템플릿ID",
    variables = mapOf("name" to "홍길동", "code" to "123456")
)
```

```java
KakaoOption kakaoOption = new KakaoOption();
kakaoOption.setPfId("카카오채널ID");
kakaoOption.setTemplateId("템플릿ID");
kakaoOption.setVariables(Map.of("name", "홍길동", "code", "123456"));
```

## File Upload (MMS)

```kotlin
val imageId = service.uploadFile(file, StorageType.MMS)
val message = Message(
    type = MessageType.MMS,
    from = "발신번호",
    to = "수신번호",
    text = "MMS 내용",
    subject = "제목",
    imageId = imageId
)
```

## Batch Send

```kotlin
val messages = recipients.map { recipient ->
    Message(from = sender, to = recipient, text = "메시지")
}
service.send(messages)  // max 10,000
```

중복 번호 허용:
```kotlin
val config = SendRequestConfig().apply { allowDuplicates = true }
service.send(messages, config)
```

## Scheduled Send

```kotlin
val config = SendRequestConfig().apply {
    setScheduledDateFromLocalDateTime(
        LocalDateTime.now().plusMinutes(10),
        ZoneId.of("Asia/Seoul")
    )
}
service.send(message, config)
```

제약: 최소 10분 후 ~ 최대 6개월 이내

## API Methods

**발송:**
| Method | Description |
|--------|-------------|
| `send(message)` | 단건 발송 |
| `send(messages)` | 다건 발송 (max 10,000) |
| `send(message, config)` | 설정과 함께 발송 |
| `uploadFile(file, type)` | 파일 업로드 |

**조회:**
| Method | Description |
|--------|-------------|
| `getBalance()` | 잔액 조회 |
| `getQuota()` | 일일 한도 조회 |
| `getMessageList(request)` | 발송 내역 조회 |

**카카오 템플릿:**
| Method | Description |
|--------|-------------|
| `getKakaoAlimtalkTemplates()` | 알림톡 템플릿 목록 |
| `getKakaoAlimtalkTemplate(id)` | 템플릿 상세 |
| `getSendableKakaoAlimtalkTemplates()` | 발송 가능 템플릿 |

## Exceptions

| Exception | Cause |
|-----------|-------|
| `SolapiBadRequestException` | 잘못된 파라미터 |
| `SolapiInvalidApiKeyException` | API 키 오류 |
| `SolapiApiKeyException` | API 키 관련 오류 |
| `SolapiFileUploadException` | 파일 업로드 실패 |
| `SolapiMessageNotReceivedException` | 발송 실패 |
| `SolapiEmptyResponseException` | 빈 응답 |
| `SolapiUnknownException` | 알 수 없는 오류 |

## Error Handling Pattern

```kotlin
try {
    service.send(message)
} catch (e: SolapiBadRequestException) {
    // 잘못된 요청
} catch (e: SolapiInvalidApiKeyException) {
    // API 키 오류
} catch (e: SolapiMessageNotReceivedException) {
    // 발송 실패
} catch (e: SolapiException) {
    // 기타 오류
}
```

## Environment Variables

| Variable | Description |
|----------|-------------|
| `SOLAPI_API_KEY` | API 키 |
| `SOLAPI_API_SECRET` | API 시크릿 |
| `SOLAPI_SENDER` | 발신번호 |
| `SOLAPI_KAKAO_PF_ID` | 카카오 채널 ID |
| `SOLAPI_KAKAO_TEMPLATE_ID` | 알림톡 템플릿 ID |

## Imports

**Kotlin:**
```kotlin
import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.StorageType
import com.solapi.sdk.message.model.SendRequestConfig
import com.solapi.sdk.message.model.kakao.KakaoOption
```

**Java:**
```java
import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.MessageType;
import com.solapi.sdk.message.model.StorageType;
import com.solapi.sdk.message.model.SendRequestConfig;
import com.solapi.sdk.message.model.kakao.KakaoOption;
import com.solapi.sdk.message.service.DefaultMessageService;
```
