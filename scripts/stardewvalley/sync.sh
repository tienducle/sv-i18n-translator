#!/bin/zsh

ORIGINAL_FILE_PATH="$1"
TRANSLATED_FILE_PATH="$2"

if [ -z "$ORIGINAL_FILE_PATH" ]; then
    echo "Missing ORIGINAL_FILE_PATH (arg1)"
    exit 1
fi

if [ -z "$TRANSLATED_FILE_PATH" ]; then
    echo "Missing TRANSLATED_FILE_PATH (arg2)"
    exit 1
fi

java \
-DrunModes=SYNC \
-DoriginalFilePath="$ORIGINAL_FILE_PATH" \
-DtranslatedFilePath="$TRANSLATED_FILE_PATH" \
-jar ../../target/sv-i18n-translator-0.0.1.jar