# Lara Java SDK

[![Java Version](https://img.shields.io/badge/java-8+-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

This SDK empowers you to build your own branded translation AI leveraging our translation fine-tuned language model. 

All major translation features are accessible, making it easy to integrate and customize for your needs. 

## 🌍 **Features:**
- **Text Translation**: Single strings, multiple strings, and complex text blocks
- **Document Translation**: Word, PDF, and other document formats with status monitoring
- **Translation Memory**: Store and reuse translations for consistency
- **Glossaries**: Enforce terminology standards across translations
- **Language Detection**: Automatic source language identification
- **Advanced Options**: Translation instructions and more

## 📚 Documentation

Lara's SDK full documentation is available at [https://developers.laratranslate.com/](https://developers.laratranslate.com/)

## 🚀 Quick Start

### Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.translated.lara</groupId>
    <artifactId>lara-sdk</artifactId>
    <version>1.4.2</version>
</dependency>
```

Or for Gradle, add to your `build.gradle`:

```groovy
implementation 'com.translated.lara:lara-sdk:1.4.2'
```

### Basic Usage

```java
import com.translated.lara.Credentials;
import com.translated.lara.translator.Translator;
import com.translated.lara.translator.TextResult;
import com.translated.lara.errors.LaraException;
import java.util.Arrays;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        // Set your credentials using environment variables (recommended)
        Credentials credentials = new Credentials(
            System.getenv("LARA_ACCESS_KEY_ID"),
            System.getenv("LARA_ACCESS_KEY_SECRET")
        );

        // Create translator instance
        Translator lara = new Translator(credentials);

        try {
            // Simple text translation
            TextResult result = lara.translate("Hello, world!", "en-US", "fr-FR");
            
            System.out.println("Translation: " + result.getTranslation());
            // Output: Translation: Bonjour, le monde !
        } catch (LaraException e) {
            System.err.println("Translation error: " + e.getMessage());
        }
    }
}
```

## 📖 Examples

The `examples/` directory contains comprehensive examples for all SDK features.

**All examples use environment variables for credentials, so set them first:**
```bash
export LARA_ACCESS_KEY_ID="your-access-key-id"
export LARA_ACCESS_KEY_SECRET="your-access-key-secret"
```

### Text Translation
- **[TextTranslation.java](examples/TextTranslation.java)** - Complete text translation examples
  - Single string translation
  - Multiple strings translation  
  - Translation with instructions
  - TextBlocks translation (mixed translatable/non-translatable content)
  - Auto-detect source language
  - Advanced translation options
  - Get available languages
  - Detect language

```bash
cd examples
javac -cp ../target/classes:../target/dependency/* TextTranslation.java
java -cp .:../target/classes:../target/dependency/* TextTranslation
```

### Document Translation
- **[DocumentTranslation.java](examples/DocumentTranslation.java)** - Document translation examples
  - Basic document translation
  - Advanced options with memories and glossaries
  - Step-by-step translation with status monitoring

```bash
cd examples
javac -cp ../target/classes:../target/dependency/* DocumentTranslation.java
java -cp .:../target/classes:../target/dependency/* DocumentTranslation
```

### Translation Memory Management
- **[MemoriesManagement.java](examples/MemoriesManagement.java)** - Memory management examples
  - Create, list, update, delete memories
  - Add individual translations
  - Multiple memory operations
  - TMX file import with progress monitoring
  - Translation deletion
  - Translation with TUID and context

```bash
cd examples
javac -cp ../target/classes:../target/dependency/* MemoriesManagement.java
java -cp .:../target/classes:../target/dependency/* MemoriesManagement
```

### Glossary Management
- **[GlossariesManagement.java](examples/GlossariesManagement.java)** - Glossary management examples
  - Create, list, update, delete glossaries
  - CSV import with status monitoring
  - Glossary export
  - Glossary terms count
  - Import status checking

```bash
cd examples
javac -cp ../target/classes:../target/dependency/* GlossariesManagement.java
java -cp .:../target/classes:../target/dependency/* GlossariesManagement
```

## 🔧 API Reference

### Core Components

### 🔐 Authentication

The SDK supports authentication via access key and secret:

```java
Credentials credentials = new Credentials("your-access-key-id", "your-access-key-secret");
Translator lara = new Translator(credentials);
```

**Environment Variables (Recommended):**
```bash
export LARA_ACCESS_KEY_ID="your-access-key-id"
export LARA_ACCESS_KEY_SECRET="your-access-key-secret"
```

```java
Credentials credentials = new Credentials(
    System.getenv("LARA_ACCESS_KEY_ID"),
    System.getenv("LARA_ACCESS_KEY_SECRET")
);
```


### 🌍 Translator

```java
// Create translator with credentials
Translator lara = new Translator(credentials);
```

#### Text Translation

```java
// Basic translation
TextResult result = lara.translate("Hello", "en-US", "fr-FR");

// Multiple strings
String[] texts = {"Hello", "World"};
TextResult result = lara.translate(texts, "en-US", "fr-FR");

// TextBlocks (mixed translatable/non-translatable content)
TextBlock[] textBlocks = {
    new TextBlock("Translatable text", true),
    new TextBlock("<br>", false),  // Non-translatable HTML
    new TextBlock("More translatable text", true),
};
TextResult result = lara.translateBlocks(textBlocks, "en-US", "fr-FR");

// With advanced options
TranslateOptions options = new TranslateOptions()
    .setInstructions("Formal tone")
    .setAdaptTo("memory-id")  // Replace with actual memory IDs
    .setGlossaries("glossary-id")  // Replace with actual glossary IDs
    .setStyle(TranslationStyle.FLUID)
    .setTimeoutMs(10000L);

TextResult result = lara.translate("Hello", "en-US", "fr-FR", options);
```

### 📖 Document Translation
#### Simple document translation

```java
File inputFile = new File("/path/to/your/document.txt");  // Replace with actual file path
InputStream fileStream = lara.documents.translate(inputFile, "document.txt", "en-US", "fr-FR");

// With options
DocumentTranslateOptions options = new DocumentTranslateOptions()
    .setAdaptTo("memory-id")  // Replace with actual memory IDs
    .setGlossaries("glossary-id")  // Replace with actual glossary IDs
    .setStyle(TranslationStyle.FLUID)
    .setNoTrace(false);

InputStream fileStream = lara.documents.translate(inputFile, "document.txt", "en-US", "fr-FR", options);
```
### Document translation with status monitoring
#### Document upload
```java
//Optional: upload options
DocumentUploadOptions uploadOptions = new DocumentUploadOptions()
    .setAdaptTo("memory-id")  // Replace with actual memory IDs
    .setGlossaries("glossary-id")  // Replace with actual glossary IDs
    .setNoTrace(false);

Document document = lara.documents.upload(inputFile, "document.txt", "en-US", "fr-FR", uploadOptions);
```
#### Document translation status monitoring
```java
String status = lara.documents.status(document.getId());
```
#### Download translated document
```java
InputStream fileStream = lara.documents.download(document.getId());
```

### 🧠 Memory Management

```java
// Create memory
Memory memory = lara.memories.create("MyMemory");

// Create memory with external ID (MyMemory integration)
Memory memory = lara.memories.create("Memory from MyMemory", "aabb1122");  // Replace with actual external ID

// Important: To update/overwrite a translation unit you must provide a tuid. Calls without a tuid always create a new unit and will not update existing entries.
// Add translation to single memory
MemoryImport memoryImport = lara.memories.addTranslation("mem_1A2b3C4d5E6f7G8h9I0jKl", "en-US", "fr-FR", "Hello", "Bonjour", "greeting_001");

// Add translation to multiple memories
List<String> memoryIds = Arrays.asList("mem_1A2b3C4d5E6f7G8h9I0jKl", "mem_2XyZ9AbC8dEf7GhI6jKlMn");  // Replace with actual memory IDs
MemoryImport memoryImport = lara.memories.addTranslation(memoryIds, "en-US", "fr-FR", "Hello", "Bonjour", "greeting_002");

// Add with context
MemoryImport memoryImport = lara.memories.addTranslation(
        "mem_1A2b3C4d5E6f7G8h9I0jKl", "en-US", "fr-FR", "Hello", "Bonjour", "tuid", 
        "sentenceBefore", "sentenceAfter"
);

// TMX import from file
File tmxFile = new File("/path/to/your/memory.tmx");  // Replace with actual TMX file path
MemoryImport memoryImport = lara.memories.importTmx("mem_1A2b3C4d5E6f7G8h9I0jKl", tmxFile);

// Delete translation
// Important: if you omit tuid, all entries that match the provided fields will be removed
MemoryImport deleteJob = lara.memories.deleteTranslation(
        "mem_1A2b3C4d5E6f7G8h9I0jKl", "en-US", "fr-FR", "Hello", "Bonjour", "greeting_001"
);

// Wait for import completion (timeout in MILLISECONDS)
MemoryImport completedImport = lara.memories.waitForImport(memoryImport, 300000L); // 5 minutes
```

### 📚 Glossary Management

```java
// Create glossary
Glossary glossary = lara.glossaries.create("MyGlossary");

// Import CSV from file
File csvFile = new File("/path/to/your/glossary.csv");  // Replace with actual CSV file path
GlossaryImport glossaryImport = lara.glossaries.importCsv("gls_1A2b3C4d5E6f7G8h9I0jKl", csvFile);

// Check import status
GlossaryImport importStatus = lara.glossaries.getImportStatus("gls_1A2b3C4d5E6f7G8h9I0jKl");

// Wait for import completion
GlossaryImport completedImport = lara.glossaries.waitForImport(glossaryImport, 300000L); // 5 minutes

// Export glossary
String csvData = lara.glossaries.export("gls_1A2b3C4d5E6f7G8h9I0jKl", Glossary.Type.CSV_TABLE_UNI, "en-US");

// Get glossary terms count
GlossaryCounts counts = lara.glossaries.counts("gls_1A2b3C4d5E6f7G8h9I0jKl");
```

### Translation Options

```java
public class TranslateOptions {
    setAdaptTo(String... memoryIds)             // Memory IDs to adapt to
    setGlossaries(String... glossaryIds)        // Glossary IDs to use
    setInstructions(String... instructions)     // Translation instructions
    setStyle(TranslationStyle style)            // Translation style (FLUID, FAITHFUL, CREATIVE)
    setContentType(String contentType)          // Content type (text/plain, text/html, etc.)
    setMultiline(Boolean multiline)             // Enable multiline translation
    setTimeoutMs(Long timeoutMs)                // Request timeout in milliseconds
    setSourceHint(String sourceHint)            // Hint for source language detection
    setNoTrace(Boolean noTrace)                 // Disable request tracing
    setVerbose(Boolean verbose)                 // Enable verbose response
}
```

### Language Codes

The SDK supports full language codes (e.g., `en-US`, `fr-FR`, `es-ES`) as well as simple codes (e.g., `en`, `fr`, `es`):

```java
// Full language codes (recommended)
TextResult result = lara.translate("Hello", "en-US", "fr-FR");

// Simple language codes
TextResult result = lara.translate("Hello", "en", "fr");
```

### 🌐 Supported Languages

The SDK supports all languages available in the Lara API. Use the `getLanguages()` method to get the current list:

```java
List<String> languages = lara.getLanguages();
System.out.println("Supported languages: " + String.join(", ", languages));
```

## ⚙️ Configuration

### Error Handling

The SDK provides detailed error information:

```java
try {
    TextResult result = lara.translate("Hello", "en-US", "fr-FR");
    System.out.println("Translation: " + result.getTranslation());
} catch (LaraApiException e) {
    System.err.println("API Error [" + e.getStatusCode() + "]: " + e.getMessage());
    System.err.println("Error type: " + e.getType());
} catch (LaraException e) {
    System.err.println("SDK Error: " + e.getMessage());
}
```

## 📋 Requirements

- Java 8 or higher
- Maven or Gradle
- Valid Lara API credentials

## 🧪 Testing

Run the examples to test your setup:

```bash
# All examples use environment variables for credentials, so set them first:
export LARA_ACCESS_KEY_ID="your-access-key-id"
export LARA_ACCESS_KEY_SECRET="your-access-key-secret"
```

```bash
# Build the project
mvn clean compile dependency:copy-dependencies

# Run basic text translation example
cd examples
javac -cp ../target/classes:../target/dependency/* TextTranslation.java
java -cp .:../target/classes:../target/dependency/* TextTranslation
```

## 🏗️ Building from Source

```bash
# Clone the repository
git clone https://github.com/translated/lara-java.git
cd lara-java

# Build with Maven
mvn clean install
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Happy translating! 🌍✨
