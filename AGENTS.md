# SOLAPI Kotlin SDK - Knowledge Base

**Generated:** 2026-01-27 | **Commit:** 618f129 | **Branch:** main

## CRITICAL: Development Principles

**MUST follow `CLAUDE.md` development principles:**

| Principle | Rule |
|-----------|------|
| **Tidy First** | NEVER mix structural and behavioral changes in a single commit |
| **Commit Separation** | `refactor:` (structural) vs `feat:`/`fix:` (behavioral) in separate commits |
| **TDD** | Write tests first (Red → Green → Refactor) |
| **Single Responsibility** | Classes/methods have single responsibility only |
| **Tidy Code First** | Clean up target area code before adding features |

```bash
# Correct commit order
git commit -m "refactor: extract validation logic to separate method"
git commit -m "feat: add phone number format validation"

# Forbidden (mixed commit)
git commit -m "feat: add validation and refactor code"  # ❌ FORBIDDEN
```

---

## OVERVIEW

Kotlin/Java SDK for SOLAPI messaging platform. Supports SMS, LMS, MMS, Kakao Alimtalk/Brand Message, Naver Smart Notification, RCS, Fax, and Voice messaging.

## STRUCTURE

```
src/main/java/com/solapi/sdk/
├── SolapiClient.kt          # Entry point (use this)
├── NurigoApp.kt             # DEPRECATED - do not use
└── message/
    ├── service/             # API operations (send, query, templates)
    ├── model/               # Domain models (Message, options)
    │   └── kakao/           # 19 files - Kakao templates, buttons, options
    ├── dto/                 # Request/Response DTOs
    ├── exception/           # Exception hierarchy (8 types)
    └── lib/                 # Internal utilities (auth, helpers)
```

## WHERE TO LOOK

| Task | Location | Notes |
|------|----------|-------|
| **Initialize SDK** | `SolapiClient.kt` | `createInstance(apiKey, secretKey)` |
| **Send messages** | `service/DefaultMessageService.kt` | `send(message)` or `send(messages)` |
| **Query messages** | `service/DefaultMessageService.kt` | `getMessageList(params)` |
| **Upload files** | `service/DefaultMessageService.kt` | `uploadFile(file, type)` for MMS/Fax |
| **Kakao Alimtalk** | `service/DefaultMessageService.kt` | 11 template methods |
| **Create Message** | `model/Message.kt` | Data class with all message options |
| **Kakao options** | `model/kakao/KakaoOption.kt` | Alimtalk/FriendTalk config |
| **Handle errors** | `exception/` | Catch specific `Solapi*Exception` types |
| **HTTP layer** | `service/MessageHttpService.kt` | Retrofit interface (internal) |
| **Auth** | `lib/Authenticator.kt` | HMAC-SHA256 (internal, auto-injected) |

## CODE PATTERNS

### Serialization
```kotlin
@Serializable
data class Message(
    var to: String? = null,
    var from: String? = null,
    // All fields nullable with defaults for flexibility
)
```
- **ALWAYS** use `@Serializable` annotation
- **ALWAYS** use `kotlinx.serialization` (not Jackson/Gson)
- **ALWAYS** provide nullable fields with defaults

### Service Methods
```kotlin
@JvmOverloads  // Java interop
@Throws(SolapiMessageNotReceivedException::class, ...)
fun send(messages: List<Message>, config: SendRequestConfig? = null): MultipleDetailMessageSentResponse
```
- **ALWAYS** annotate with `@JvmOverloads` for optional params
- **ALWAYS** declare `@Throws` for checked exceptions

### Exception Handling
```kotlin
// Internal: Map error codes to exceptions
when (errorResponse.errorCode) {
    "ValidationError" -> throw SolapiBadRequestException(msg)
    "InvalidApiKey" -> throw SolapiInvalidApiKeyException(msg)
    else -> throw SolapiUnknownException(msg)
}
```
- Exceptions are `sealed interface` based (SolapiException)
- 8 specific exception types

### Phone Number Normalization
```kotlin
init {
    from = from?.replace("-", "")
    to = to?.replace("-", "")
}
```
- Dashes auto-stripped from phone numbers in `Message.init`

### Test Conventions
```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class AuthenticatorTest {
    @Test
    fun `generateAuthInfo returns HMAC-SHA256 format`() {
        // Given
        val authenticator = Authenticator("api-key", "secret")
        // When
        val result = authenticator.generateAuthInfo()
        // Then
        assertTrue(result.startsWith("HMAC-SHA256 "))
    }
}
```
- **ALWAYS** use `kotlin.test` (NOT JUnit directly)
- **ALWAYS** use Given-When-Then comment structure
- **ALWAYS** use backtick method names for readability

## ANTI-PATTERNS

| Forbidden | Required |
|-----------|----------|
| `NurigoApp.initialize()` | `SolapiClient.createInstance()` |
| Direct `DefaultMessageService()` | Use factory via `SolapiClient` |
| Catch generic `Exception` | Catch specific `Solapi*Exception` |
| `net.nurigo.sdk` imports | `com.solapi.sdk` package only |
| Jackson/Gson serialization | `kotlinx.serialization` only |
| Mixed structural+behavioral commits | Separate commits per Tidy First |

## INTERNAL CLASSES (Do Not Use Directly)

- `Authenticator` - HMAC auth (auto-injected via interceptor)
- `ErrorResponse` - Internal error DTO
- `MessageHttpService` - Retrofit interface
- `JsonSupport` - Serialization config
- `MapHelper`, `Criterion` - Internal utilities

## EXCEPTION TYPES

| Exception | When Thrown |
|-----------|-------------|
| `SolapiApiKeyException` | Empty/missing API key |
| `SolapiInvalidApiKeyException` | Invalid credentials |
| `SolapiBadRequestException` | Validation error, bad input |
| `SolapiEmptyResponseException` | Server returned empty body |
| `SolapiFileUploadException` | File upload failed |
| `SolapiMessageNotReceivedException` | All messages failed (has `failedMessageList`) |
| `SolapiUnknownException` | Unclassified server error |

## KAKAO INTEGRATION

19 model files in `model/kakao/`:
- `KakaoOption` - Main config (pfId, templateId, variables)
- `KakaoAlimtalkTemplate*` - Template CRUD models
- `KakaoBrandMessageTemplate` - Brand message with carousels
- `KakaoButton`, `KakaoButtonType` - Button configurations

Template workflow: `getKakaoAlimtalkTemplateCategories()` → `createKakaoAlimtalkTemplate()` → `requestKakaoAlimtalkTemplateInspection()`

## BUILD & TEST

```bash
./gradlew clean build test    # Full build
./gradlew test                # Tests only
./gradlew shadowJar           # Fat JAR with relocated deps
```

**Shadow JAR**: Dependencies relocated to `com.solapi.shadow.*` to prevent conflicts.

## NOTES

- **Java 8 target**: Code must work on JVM 1.8
- **Source location**: Kotlin in `src/main/java/` (unconventional but intentional)
- **Tests**: `src/test/kotlin/`, `kotlin.test` (Kotlin native), Given-When-Then style
- **Version**: Auto-generated at `build/generated/source/kotlin/com/solapi/sdk/Version.kt`
- **Docs**: Dokka output to `./docs/`, run `./gradlew dokkaGeneratePublicationHtml`
