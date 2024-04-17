#!/bin/zsh

ORIGINAL_FILE_PATH="$1"
TARGET_LANGUAGE="$2"

if [ -z "$ORIGINAL_FILE_PATH" ]; then
    echo "Missing ORIGINAL_FILE_PATH (arg1)"
    exit 1
fi

if [ -z "$TARGET_LANGUAGE" ]; then
    echo "Missing TARGET_LANGUAGE (arg2, e.g. German, French, Spanish, etc.)"
    exit 1
fi

java \
-DrunModes=SYNC \
-DoriginalFilePath="$ORIGINAL_FILE_PATH" \
-DtargetLanguage="$TARGET_LANGUAGE" \
-jar ../../target/sv-i18n-translator.jar