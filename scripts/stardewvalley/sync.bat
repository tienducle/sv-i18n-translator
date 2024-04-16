@echo off
set ORIGINAL_FILE_PATH=%1
set TRANSLATED_FILE_PATH=%2

if %ORIGINAL_FILE_PATH% == "" (
    echo "Missing ORIGINAL_FILE_PATH (arg1)"
    exit /b 1
)

if %TRANSLATED_FILE_PATH% == "" (
    echo "Missing TRANSLATED_FILE_PATH (arg2)"
    exit /b 1
)

java ^
-DrunModes=SYNC ^
-DoriginalFilePath="%ORIGINAL_FILE_PATH%" ^
-DtranslatedFilePath="%TRANSLATED_FILE_PATH%" ^
-jar ../../target/sv-i18n-translator.jar
