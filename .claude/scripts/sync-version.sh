#!/bin/bash
# 버전 동기화 스크립트
# build.gradle.kts의 버전이 변경되면 README.md와 LLM_GUIDE.md에 반영

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

GRADLE_FILE="$PROJECT_ROOT/build.gradle.kts"
README_FILE="$PROJECT_ROOT/README.md"
LLM_GUIDE_FILE="$PROJECT_ROOT/LLM_GUIDE.md"

# build.gradle.kts가 수정된 경우에만 실행
if [[ -n "$CLAUDE_FILE_PATHS" ]]; then
    if ! echo "$CLAUDE_FILE_PATHS" | grep -q "build.gradle.kts"; then
        exit 0
    fi
fi

# build.gradle.kts에서 버전 추출 (예: version = "1.1.0" -> 1.1.0)
VERSION=$(grep -E '^version\s*=' "$GRADLE_FILE" | head -1 | sed -E 's/.*"([0-9]+\.[0-9]+\.[0-9]+)".*/\1/')

if [[ -z "$VERSION" ]] || [[ ! "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "유효한 버전을 찾을 수 없습니다: $VERSION"
    exit 1
fi

# README.md 버전 업데이트
if [[ -f "$README_FILE" ]]; then
    # com.solapi:sdk:X.X.X 패턴 업데이트
    sed -i '' -E "s/(com\.solapi:sdk:)[0-9]+\.[0-9]+\.[0-9]+/\1$VERSION/g" "$README_FILE"
    # <version>X.X.X</version> 패턴
    sed -i '' -E "s|(<version>)[0-9]+\.[0-9]+\.[0-9]+(</version>)|\1$VERSION\2|g" "$README_FILE"
fi

# LLM_GUIDE.md 버전 업데이트
if [[ -f "$LLM_GUIDE_FILE" ]]; then
    sed -i '' -E "s/(com\.solapi:sdk:)[0-9]+\.[0-9]+\.[0-9]+/\1$VERSION/g" "$LLM_GUIDE_FILE"
    sed -i '' -E "s|(<version>)[0-9]+\.[0-9]+\.[0-9]+(</version>)|\1$VERSION\2|g" "$LLM_GUIDE_FILE"
fi

echo "버전 $VERSION 동기화 완료"
