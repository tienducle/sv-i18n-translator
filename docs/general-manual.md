# General Manual

<!-- TOC -->
* [1. Requirements](#1-requirements)
  * [1.1 Java Development Kit (JDK)](#11-java-development-kit-jdk)
  * [1.2 Download sv-i18n-translator](#12-download-sv-i18n-translator)
* [2. Configuration](#2-configuration)
  * [2.1 General properties](#21-general-properties)
      * [2.1.1 originalFilePath](#211-originalfilepath)
      * [2.1.2 targetLanguage](#212-targetlanguage)
      * [2.1.3 runModes](#213-runmodes)
  * [2.2 Translation Adapter properties](#22-translation-adapter-properties)
    * [2.2.1 Common properties](#221-common-properties)
    * [2.2.2 OpenAI properties](#222-openai-properties)
    * [2.2.3 Ollama properties](#223-ollama-properties)
    * [2.2.4 DeepL properties](#224-deepl-properties)
  * [2.3 Validation Adapter properties](#23-validation-adapter-properties)
* [3. Usage](#3-usage)
  * [3.1 Running the application](#31-running-the-application)
* [4. Appendix](#4-appendix)
  * [4.1 System Message](#41-system-message)
  * [4.2 Context Message files](#42-context-message-files)
  * [4.3 Validation Adapter configuration files](#43-validation-adapter-configuration-files)
<!-- TOC -->


---
# 1. Requirements

## 1.1 Java Development Kit (JDK)

Obtain a JDK version greater than 11. In the example below, Zulu OpenJDK 11 is used.

- Go to https://www.azul.com/downloads/#zulu
- Scroll **down** to the table view
- Select the following:
    - Java Version: Java 11 (LTS)
    - Operating System: Windows
    - Architecture: x86 64-bit
    - Java Package: JDK
- Download the .msi file, if you don't know how to set PATH variables
- Run the installer

## 1.2 Download sv-i18n-translator

- Download the repository as a zip file
    - Release artifacts may be provided in the future
- Extract the zip file, e.g. to your Downloads folder


---
# 2. Configuration

Start with the general configuration and then jump to the section of the adapter you want to use. 

## 2.1 General properties

Navigate to src/main/resources and make a copy of the application.properties file. Name the copy local.properties.

Go to each of the properties below and set them according to your needs.

#### 2.1.1 originalFilePath

Specify the full (absolute) path to the original JSON file here.

```
originalFilePath=C:/Users/yourusername/Downloads/sv-i18n-translator-main/src/test/resources/data/text/default.json
```

#### 2.1.2 targetLanguage

Specify the target language to which you want to translate the text to.

```properties
targetLanguage=German
```

#### 2.1.3 runModes

The runModes property specifies the run modes of the application. In general, you want to first SYNC the files, then TRANSLATE them and finally REFORMAT them, so you can compare the translated file with the original file.

Detailed descriptions can be found in the application.properties file.

```properties
runModes=SYNC,TRANSLATE,REFORMAT
```

## 2.2 Translation Adapter properties

### 2.2.1 Common properties

#### 2.2.1.1 translation.adapter

Specify which translation adapter to use. The following adapters are available:
- OpenAI
- Ollama
- DeepL

```properties
translation.adapter=OpenAI
```

#### 2.2.1.2 translation.adapter.maxAttempts

Specify the maximum number of attempts, including the first attempt, to try to translate a text. If the maximum number of attempts is reached, the text will be skipped.

```properties
translation.adapter.maxAttempts=4
```

#### 2.2.1.3 translation.adapter.contextMessagesFilePath (optional)

Specify the relative path (from where you execute the application) to the context messages file. This file contains the context messages that are used to translate the text. For details, see the [4.2 Context Message files](#42-context-message-files) section.

```properties
translation.adapter.contextMessagesFilePath=config/context/context-messages.json
```

#### 2.2.1.4 translation.adapter.maxHistorySize

Specify the maximum number of past messages, that should be included with each API request. This is used to provide the LLMs with additional context information that can help to improve the translation quality. The number should be a multiple of 2, because the messages are grouped in pairs (user request, assistant response).

⚠️ **Important**: Default is 0. Using many history messages can increase the cost with services like OpenAI.

```properties
translation.adapter.maxHistorySize=0
```

### 2.2.2 OpenAI properties

#### 2.2.2.1 translation.adapter.openai.apiKey

To use OpenAI, you must create an API key. Since OpenAI switched to a prepaid model, you may need to charge your account first.
    
```properties
translation.adapter.openai.apiKey=sk-...
```

#### 2.2.2.2 translation.adapter.translation.openai.chat.model

Specify the model to use for the translation requests.

```properties
translation.adapter.translation.openai.chat.model=gpt-4-turbo-preview
```

#### 2.2.2.3 translation.adapter.openai.chat.maxTokens

Specify the upper limit of tokens. The requested tokens are calculated for each request and can be smaller than this number, but never exceed it.

```properties
translation.adapter.openai.chat.maxTokens=4000
```

#### 2.2.2.4 translation.adapter.openai.chat.initTemperature

Specify the initial temperature for the translation requests. The temperature is used to control the randomness of the translation.

```properties
translation.adapter.openai.chat.initTemperature=0.2
```

#### 2.2.2.5 translation.adapter.openai.chat.temperatureIncrement

Specify the temperature increment for the translation request after a failed attempt.

```properties
translation.adapter.openai.chat.temperatureIncrement=0.6
```

#### 2.2.2.6 translation.adapter.openai.chat.systemMessage

Specify the system message that is used to start the conversation with the LLMs. For more information, see the [4.1 System Message](#41-system-message) section.

```properties
translation.adapter.openai.chat.systemMessage=Translate the input text to ${targetLanguage}. If you can not translate the input, or you are unsure, respond with <ERROR>.
```

### 2.2.3 Ollama properties

⚠️ Note: Ollama is supported but the translation quality (at least with the models I have tried) is nowhere near OpenAI's GPT-4.

#### 2.2.3.1 translation.adapter.ollama.scheme

(Optional) Specify the http scheme of the Ollama API.

```properties
translation.adapter.ollama.scheme=http
```

#### 2.2.3.2 translation.adapter.ollama.host

(Optional) Specify the host of the Ollama API.

```properties
translation.adapter.ollama.host=localhost
```

#### 2.2.3.3 translation.adapter.ollama.port

(Optional) Specify the http port of the Ollama API.

```properties
translation.adapter.ollama.port=11434
```

#### 2.2.3.4 translation.adapter.ollama.model

Specify the model to use for the translation requests.

```properties
translation.adapter.ollama.model=llama3:instruct
```

#### 2.2.3.5 translation.adapter.ollama.chat.systemMessage

Specify the system message that is used to start the conversation with the LLMs. For more information, see the [4.1 System Message](#41-system-message) section.

```properties
translation.adapter.openai.chat.systemMessage=Translate the input text to ${targetLanguage}. If you can not translate the input, or you are unsure, respond with <ERROR>.
```

### 2.2.4 DeepL properties

WIP

## 2.3 Validation Adapter properties

#### 2.3.1 validation.adapter

Specify the validation adapter to use. The following adapters are available:
- Default
  - Default implementation which only applies checks defined in the `validation.adapter.configurationFile`
- StardewValley
  - Custom implementation for Stardew Valley which applies additional checks for the game's text files

For more information, see the [4.3 Validation Adapter configuration](#43-validation-adapter-configuration) section.

```properties
validation.adapter=StardewValley
```

#### 2.3.2 validation.adapter.configurationFile

Specify a configuration file for the validation adapter. The file contains the rules that are used to validate the translations.

For more information, see the [4.3 Validation Adapter configuration](#43-validation-adapter-configuration) section.

```properties
validation.adapter.configurationFile=stardewvalley.json
```

---
# 3. Usage

⚠️ Before proceeding, make sure that you have followed the sections:
* [1. Requirements](#1-requirements)
  * [1.1 Java Development Kit (JDK)](#11-java-development-kit-jdk)
  * [1.2 Download sv-i18n-translator](#12-download-sv-i18n-translator)
* [2. Configuration](#2-configuration)
  * [2.1 General properties](#21-general-properties)
  * [2.2 Translation Adapter properties](#22-translation-adapter-properties)
    * [2.2.1 Common properties](#221-common-properties)
    * **And at least one of** 
    * [2.2.2 OpenAI properties](#222-openai-properties)
    * [2.2.3 Ollama properties](#223-ollama-properties)
    * [2.2.4 DeepL properties](#224-deepl-properties)

The examples below will assume that you have a local-openai.properties file under `src/main/resources/local-openai.properties` with the following content (comments omitted for brevity):

```properties
originalFilePath=src/test/resources/data/text/default.json
targetLanguage=German

runModes=SYNC,REVALIDATE,TRANSLATE,REFORMAT

translation.adapter=OpenAI
translation.adapter.maxAttempts=4

# Path to context messages file.
# Note: When using OpenAI, this can increase the cost by a lot.
#translation.adapter.contextMessagesFilePath=config/context/context-messages-de.json

# Maximum number of past messages to be included in the API request.
# Should be a multiple of 2 (to include always the translation request and translated response).
# Note: When using OpenAI, this can increase the cost by a lot.
#translation.adapter.maxHistorySize=0

translation.adapter.openai.apiKey=sk-...
translation.adapter.translation.openai.chat.model=gpt-4-turbo-preview
translation.adapter.openai.chat.maxTokens=4000
translation.adapter.openai.chat.initTemperature=0.2
translation.adapter.openai.chat.temperatureIncrement=0.6
translation.adapter.openai.chat.systemMessage=Translate the input text to ${targetLanguage}.\nThe input is a text from the video game Stardew Valley.\nPreserve all special characters.\nPreserve all single quotes.\nPreserve all double quotes.\nDo not change anything that is surrounded by curly brackets.\nDo not change words that start with %.\nDo not change anything of this list: $neutral, $a, $b, $c, $d, $e, $h, $k, $l, $p, $q, $r, $s, $u, $y.\nRespond only with the translated text.\nUse the informal 'Du' form of address instead of formal 'Sie'.\nIf you can not translate the input, or you are unsure, respond with <ERROR>.

validation.adapter=StardewValley
validation.adapter.configurationFile=stardewvalley.json
```

If you want to use Ollama, here is an example for `src/main/resources/local-ollama.properties`:

```properties
originalFilePath=src/test/resources/data/text/default.json
targetLanguage=German

runModes=SYNC,REVALIDATE,TRANSLATE,REFORMAT

translation.adapter=Ollama
translation.adapter.maxAttempts=4
translation.adapter.contextMessagesFilePath=config/context/context-messages-de.json
translation.adapter.maxHistorySize=6

translation.adapter.ollama.scheme=http
translation.adapter.ollama.host=localhost
translation.adapter.ollama.port=11434
translation.adapter.ollama.model=llama3:instruct
translation.adapter.ollama.chat.systemMessage=Translate the input text to ${targetLanguage}.\nThe input is a text from the video game Stardew Valley.\nPreserve all special characters.\nPreserve all single quotes.\nPreserve all double quotes.\nDo not change anything that is surrounded by curly brackets.\nDo not change words that start with %.\nDo not change anything of this list: $neutral, $a, $b, $c, $d, $e, $h, $k, $l, $p, $q, $r, $s, $u, $y.\nRespond only with the translated text.\nUse the informal 'Du' form of address instead of formal 'Sie'.\nIf you can not translate the input, or you are unsure, respond with <ERROR>.

validation.adapter=StardewValley
validation.adapter.configurationFile=stardewvalley.json
```

## 3.1 Running the application

Open a terminal and navigate to the root directory of the project. Then execute the following command:

```bash
java -jar target/sv-i18n-translator.jar --spring.config.location=src/main/resources/local-openai.properties
```

---
# 4. Appendix

## 4.1 System Message

Improve the translation quality by providing the LLMs with additional context information. The system message is used to start the conversation with the LLMs. The system message should contain information about the input text, the target language, and any special instructions that the LLMs should follow.

Mostly tested with OpenAI.

```properties
translation.adapter.openai.chat.systemMessage=Translate the input text to ${targetLanguage}.\nThe input is a text from the video game Stardew Valley.\nPreserve all special characters.\nPreserve all single quotes.\nPreserve all double quotes.\nDo not change anything that is surrounded by curly brackets.\nDo not change words that start with %.\nDo not change anything of this list: $neutral, $a, $b, $c, $d, $e, $h, $k, $l, $p, $q, $r, $s, $u, $y.\nRespond only with the translated text.\nUse the informal 'Du' form of address instead of formal 'Sie'.\nIf you can not translate the input, or you are unsure, respond with <ERROR>.
```

## 4.2 Context Message files

Location: `config/context`

Context message files are JSON files that contain example messages. These context messages are included in the API requests to translate the text. The context messages are used to provide the LLMs with additional context information that can help to improve the translation quality.

⚠️ **Important**: The context messages must be written in the language that you want to translate the text to.

⚠️ **Important**: Using many context messages can increase the cost with services like OpenAI.

```json
{
  "contextMessages":
  [
    {
      "role": "user",
      "content": "Hey there, @.#$b#...#$b#Wow it's pretty hot today, huh?.$1"
    },
    {
      "role": "assistant",
      "content": "Hey, @.#$b#...#$b#Wow, es ist echt heiß heute, hm?.$1"
    },
    {
      "role": "user",
      "content": "Oh hey, you must be the new farmer that moved to Stardew Valley?$0#$e#I haven't seen you around before.$0#$e#Nice to meet you!$0#$e#I got some work to do, will catch ya later."
    },
    {
      "role": "assistant",
      "content": "Oh hey, du musst der Neuzugang hier in Stardew Valley sein?$0#$e#Ich habe dich hier in der Gegend noch nie gesehen.$0#$e#Schön dich kennen zu lernen$0#$e#Ich habe etwas Arbeit zu erledigen, wir sehen uns später."
    }
  ]
}
```

## 4.3 Validation Adapter configuration files

Location: `src/main/resources/validation`\
Will be moved to `config/validation` in the future.

The validation adapter configuration files contain rules that will be checked by the validation adapter to decide if a translation should be accepted or rejected. A rejection will lead to the translation being retried until `translation.adapter.maxAttempts` is reached. Translation adapters can decide to handle retries differently, e.g. increase randomness or temperature. 

#### 4.3.1 rejectionList

Specify a list of strings that will lead to rejection of the translation if it is found anywhere in the translated text.

Type: Array of Strings

```json
{
  "rejectionList": [
    "hallo dort",
    "hallo, dort",
    "hallo herr.",
    "hallo, herr."
  ],
  "countSpecialCharacters": "",
  "regexValidationList": []
}
```

#### 4.3.2 countSpecialCharacters

Specify characters that should be counted in both the original and translated text. The count of these characters must be the same in both texts, otherwise the translation will be rejected.

Type: String

```json
{
  "rejectionList": [],
  "countSpecialCharacters": "@$#%^€<>()[]",
  "regexValidationList": []
}
```

Reject example:

- Original: "Hey there, @.`#$b#`...#$b#Wow it's pretty hot today, huh?.$1"
- Translated: "Hey, @.`#$b`...#$b#Wow, es ist echt heiß heute, hm?.$1"

Accept example:
- Original: "Hey there, @.#$b#...#$b#Wow it's pretty hot today, huh?.$1"
- Translated: "Hey, @.#$b#...#$b#Wow, es ist echt heiß heute, hm?.$1"

#### 4.3.3 regexValidationList

Specify regex patterns that should be validated in both the original and translated text. The translation will be rejected if the pattern matches differ between the original and translated text.

- Type: Array of regexValidation objects
  - regexValidation object:
    - id: String
    - pattern: String
    - description: String

```json
{
  "rejectionList": [],
  "countSpecialCharacters": "",
  "regexValidationList": [
    {
      "id": "PORTRAIT_SWITCH_COMMAND_PATTERN_1",
      "pattern": "#*\\$\\w+#\\$\\w+#",
      "description": "Check for portrait switch command ($0#$e#, $a#$1#, etc.)"
    },
    {
      "id": "PORTRAIT_SWITCH_COMMAND_PATTERN_2",
      "pattern": "#\\$\\w+#",
      "description": "Check for portrait switch command (#$0#, #$a#, etc.)"
    }
  ]
}
```