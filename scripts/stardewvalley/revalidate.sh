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
-DrunModes=REVALIDATE \
-DoriginalFilePath="$ORIGINAL_FILE_PATH" \
-DtranslatedFilePath="$TRANSLATED_FILE_PATH" \
-Dvalidation.adapter.configurationFile=stardewvalley.json \
-Dvalidation.adapter=StardewValley \
-jar ../../target/sv-i18n-translator-0.0.1.jar