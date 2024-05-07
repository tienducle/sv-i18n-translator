# Quick Manual for Windows users

## Requirements

### OpenAI API key

The bundled scripts are configured to translate the input text to german using OpenAI API (gpt-4-turbo-preview).  Therefore you must obtain an API key first. Since OpenAI switch to a prepaid model, you may need to charge your account first.

### Java Development Kit (JDK)

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

### Download sv-i18n-translator

- Download the repository as a zip file
  - Release artifacts may be provided in the future
- Extract the zip file, e.g. to your Downloads folder

## Usage 

- Open PowerShell
- Execute the following commands

Switch to the directory where the scripts are located
```powershell
cd .\Downloads\sv-i18n-translator-main\scripts\stardewvalley
```

Replace *yourusername* and *your-openai-key* with your actual username and OpenAI API key.
The command below will just run the translation on a bundled test file.
```powershell
.\translate.bat `
"C:\Users\yourusername\Downloads\sv-i18n-translator-main\src\test\resources\data\text\default.json" `
"German" `
"your-openai-key"
```

If you want to translate your own files, replace the paths accordingly.
The GPT instruction is in the [translate.bat](../scripts/stardewvalley/translate.bat) script.
Feel free to experiment with it.

# Experimental

## Ollama

To use the Ollama script, you need to install Ollama, run it and pull your desired model. The example below uses the [llama3:instruct](https://ollama.com/library/llama3:instruct) model.

```powershell
ollama pull llama3:instruct
```

### Usage

⚠️ Please note, that the script references a context messages file, which contains examples for German. You can read more about it in the [General Manual](./general-manual.md), section [4.2 Context Message files](./general-manual.md#42-context-message-files).

Switch to the directory where the scripts are located

```powershell
cd .\Downloads\sv-i18n-translator-main\scripts\stardewvalley\ollama
```

Replace yourusername with your actual username. The command below will just run the translation on a bundled test file.

```powershell
.\translate.bat `
"C:\Users\yourusername\Downloads\sv-i18n-translator-main\src\test\resources\data\text\default.json" `
"German" `
"llama3:instruct"
```