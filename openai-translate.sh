#!/bin/zsh

if [ -z "$OPENAI_API_KEY" ]; then
    echo "OPENAI_API_KEY is required to be set as an environment variable."
    echo "e.g. run 'export OPENAI_API_KEY=sk-...'"
    exit 1
fi

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
-DrunModes=TRANSLATE \
-DoriginalFilePath="$ORIGINAL_FILE_PATH" \
-DtranslatedFilePath="$TRANSLATED_FILE_PATH" \
-Dtranslation.adapter.openai.apiKey="$OPENAI_API_KEY" \
-Dtranslation.adapter.translation.openai.chat.model=gpt-4-turbo-preview \
-Dtranslation.adapter.openai.chat.maxTokens=4000 \
-Dtranslation.adapter.openai.chat.initTemperature=0.2 \
-Dtranslation.adapter.openai.chat.temperatureIncrement=0.6 \
-Dtranslation.adapter.openai.chat.systemMessage="Translate the input text to German.\nRespond only with the translated text.\nIf you can not translate the input, or you are unsure, just respond with <ERROR>." \
-Dvalidation.adapter.configurationFile=stardewvalley.json \
-Dvalidation.adapter=StardewValley \
-jar out/artifacts/sdv_i18n_translator_jar/sdv-i18n-translator.jar