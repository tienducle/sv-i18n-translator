@echo off
set ORIGINAL_FILE_PATH=%1
set TARGET_LANGUAGE=%2

if %ORIGINAL_FILE_PATH% == "" (
    echo "Missing ORIGINAL_FILE_PATH (arg1)"
    exit /b 1
)

if %TARGET_LANGUAGE% == "" (
    echo "Missing TARGET_LANGUAGE (arg2, e.g. German, French, Spanish, etc.)"
    exit /b 1
)

java ^
-DrunModes=REFORMAT ^
-DoriginalFilePath="%ORIGINAL_FILE_PATH%" ^
-DtargetLanguage="%TARGET_LANGUAGE%" ^
-jar ../../target/sv-i18n-translator.jar
