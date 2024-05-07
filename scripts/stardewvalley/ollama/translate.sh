#!/bin/zsh

ORIGINAL_FILE_PATH="$1"
TARGET_LANGUAGE="$2"
OLLAMA_MODEL_NAME="$OLLAMA_MODEL_NAME"
if [ -z "$OLLAMA_MODEL_NAME" ]; then
    OLLAMA_MODEL_NAME="$3"
fi

SYSTEM_MESSAGE='
Translate the input text to '$TARGET_LANGUAGE'.
The input is a text from the video game Stardew Valley.
Preserve all special characters.
Preserve all single quotes.
Preserve all double quotes.
Do not change anything that is surrounded by curly brackets.
Do not change words that start with %.
Do not change anything of this list: $neutral, $a, $b, $c, $d, $e, $h, $k, $l, $p, $q, $r, $s, $u, $y.
Use informal language instead of formal language.
Respond only with the translated text.
If you can not translate the input, or you are unsure, respond with <ERROR>.
'

if [ -z "$ORIGINAL_FILE_PATH" ]; then
    echo "Missing ORIGINAL_FILE_PATH (arg1)"
    exit 1
fi

if [ -z "$TARGET_LANGUAGE" ]; then
    echo "Missing TARGET_LANGUAGE (arg2, e.g. German, French, Spanish, etc.)"
    exit 1
fi

if [ -z "$OLLAMA_MODEL_NAME" ]; then
    echo "Missing OLLAMA_MODEL_NAME (arg3)"
    exit 1
fi

java \
-DrunModes=TRANSLATE \
-DoriginalFilePath="$ORIGINAL_FILE_PATH" \
-DtargetLanguage="$TARGET_LANGUAGE" \
-Dtranslation.adapter=Ollama \
-Dtranslation.adapter.contextMessagesFilePath=../../../config/context/context-messages-de.json \
-Dtranslation.adapter.maxHistorySize=10 \
-Dtranslation.adapter.ollama.model="$OLLAMA_MODEL_NAME" \
-Dtranslation.adapter.ollama.chat.systemMessage="$SYSTEM_MESSAGE" \
-Dvalidation.adapter.configurationFile=stardewvalley.json \
-Dvalidation.adapter=StardewValley \
-jar ../../../target/sv-i18n-translator.jar
