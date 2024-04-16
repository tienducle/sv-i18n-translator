@echo off
set ORIGINAL_FILE_PATH=%1
set TRANSLATED_FILE_PATH=%2
set OPEN_AI_API_KEY=%3

set SYSTEM_MESSAGE=^
Translate the input text to German.^
The input is a text from the video game Stardew Valley.^
Preserve all special characters.^
Preserve all single quotes.^
Preserve all double quotes.^
Do not change anything that is surrounded by curly brackets.^
Do not change words that start with %%.^
Do not change anything of this list: $neutral, $a, $b, $c, $d, $e, $h, $k, $l, $p, $q, $r, $s, $u, $y.^
Use informal language instead of formal language.^
Respond only with the translated text.^
If you can not translate the input, or you are unsure, respond with ^<ERROR^>.

if %ORIGINAL_FILE_PATH% == "" (
    echo "Missing ORIGINAL_FILE_PATH (arg1)"
    exit /b 1
)

if %TRANSLATED_FILE_PATH% == "" (
    echo "Missing TRANSLATED_FILE_PATH (arg2)"
    exit /b 1
)

if %OPEN_AI_API_KEY% == "" (
    echo "Missing OPEN_AI_API_KEY (arg3)"
    exit /b 1
)

java ^
-DrunModes=TRANSLATE ^
-DoriginalFilePath="%ORIGINAL_FILE_PATH%" ^
-DtranslatedFilePath="%TRANSLATED_FILE_PATH%" ^
-Dtranslation.adapter=OpenAI ^
-Dtranslation.adapter.openai.apiKey="%OPEN_AI_API_KEY%" ^
-Dtranslation.adapter.translation.openai.chat.model=gpt-4-turbo-preview ^
-Dtranslation.adapter.openai.chat.maxTokens=4000 ^
-Dtranslation.adapter.openai.chat.initTemperature=0.2 ^
-Dtranslation.adapter.openai.chat.temperatureIncrement=0.6 ^
-Dtranslation.adapter.openai.chat.systemMessage="%SYSTEM_MESSAGE%" ^
-Dvalidation.adapter.configurationFile=stardewvalley.json ^
-Dvalidation.adapter=StardewValley ^
-jar ../../target/sv-i18n-translator.jar
