# SV i18n Translator

This project was created to translate large i18n files of Stardew Valley mods to german.

I've used it to create translations for Stardew Valley Expanded and Ridgeside Village.

It still has some rough edges and may or may not work for you.

## Features
- Automatically synchronizes added/removed keys of source file to target file
- Retains formatting of source file
- Configurable validations
  - Checks for dialogue control sequences
- Uses OpenAI API for translations
  - If a validation fails, it will retry with higher temperature
- Preliminary support for Ollama (via Rest API) and DeepL API 
  - Initial results were not satisfying, this would need more work

## Usage

#### Requirements

- JDK >11
- Maven 3.x
- OpenAI API key

#### Run using scripts

```bash
cd scripts/stardewvalley
```

###### Step 1

This will synchronize the keys of the source file to the target file.

New keys will be added to the target file and keys that are not present in the source file will be removed from the target file.

```bash
./step1-sync.sh "/path/to/source/file" "/path/to/target/file"
```

###### Step 2

This will revalidate the target file with the current validation configuration.

Use this, if the code or the validation configuration has changed. If a translation is not accepted by the current validation anymore, it will be tagged with <ERROR> and will be picked up during translation again.

```bash
./step2-revalidate.sh "/path/to/source/file" "/path/to/target/file"
```

###### Step 3

This will translate the target file using the OpenAI API.

The OpenAI API key must be additionally provided here.

```bash
./step3-translate.sh "/path/to/source/file" "/path/to/target/file" "sk-..."
```

###### Step 4

This will reformat the target file to match the source file.

```bash
./step4-reformat.sh "/path/to/source/file" "/path/to/target/file"
```