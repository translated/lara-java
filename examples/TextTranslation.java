import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.translator.*;

import java.util.Arrays;
import java.util.List;

/**
 * Complete text translation examples for the Lara Java SDK
 * 
 * This example demonstrates:
 * - Single string translation
 * - Multiple strings translation
 * - Translation with instructions
 * - TextBlocks translation (mixed translatable/non-translatable content)
 * - Auto-detect source language
 * - Advanced options
 * - Get available languages
 */
public class TextTranslation {
    
    public static void main(String[] args) {
        // Set your credentials here
        String accessKeyId = "your-access-key-id";
        String accessKeySecret = "your-access-key-secret";

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);
        
        // Example 1: Basic single string translation
        System.out.println("=== Basic Single String Translation ===");
        try {
            TextResult result = lara.translate("Hello, world!", "en-US", "fr-FR");
            System.out.println("Original: Hello, world!");
            System.out.println("French: " + result.getTranslation() + "\n");
        } catch (LaraException e) {
            System.out.println("Error translating text: " + e.getMessage() + "\n");
            return;
        }

        // Example 2: Multiple strings translation
        System.out.println("=== Multiple Strings Translation ===");
        try {
            String[] texts = {"Hello", "How are you?", "Goodbye"};
            TextResult result = lara.translate(texts, "en-US", "es-ES");
            System.out.println("Original: " + Arrays.toString(texts));
            System.out.println("Spanish: " + result.getTranslations() + "\n");
        } catch (LaraException e) {
            System.out.println("Error translating multiple texts: " + e.getMessage() + "\n");
            return;
        }

        // Example 3: TextBlocks translation (mixed translatable/non-translatable content)
        System.out.println("=== TextBlocks Translation ===");
        try {
            TextBlock[] textBlocks = {
                new TextBlock("Adventure novels, mysteries, cookbooks—wait, who packed those?", true),
                new TextBlock("<br>", false),  // Non-translatable HTML
                new TextBlock("Suddenly, it doesn't feel so deserted after all.", true),
                new TextBlock("<div class=\"separator\"></div>", false),  // Non-translatable HTML
                new TextBlock("Every page you turn is a new journey, and the best part?", true)
            };
            
            TextResult result = lara.translateBlocks(textBlocks, "en-US", "it-IT");
            List<TextBlock> translations = result.getTranslationBlocks();
            
            System.out.println("Original TextBlocks: " + textBlocks.length + " blocks");
            System.out.println("Translated blocks: " + translations.size());
            for (int i = 0; i < translations.size(); i++) {
                System.out.println("Block " + (i + 1) + ": " + translations.get(i).getText());
            }
            System.out.println();
        } catch (LaraException e) {
            System.out.println("Error with TextBlocks translation: " + e.getMessage() + "\n");
            return;
        }

        // Example 4: Translation with instructions
        System.out.println("=== Translation with Instructions ===");
        try {
            TranslateOptions options = new TranslateOptions()
                .setInstructions("Be formal", "Use technical terminology");
            
            TextResult result = lara.translate("Could you send me the report by tomorrow morning?", "en-US", "de-DE", options);
            System.out.println("Original: Could you send me the report by tomorrow morning?");
            System.out.println("German (formal): " + result.getTranslation() + "\n");
        } catch (LaraException e) {
            System.out.println("Error with instructed translation: " + e.getMessage() + "\n");
            return;
        }

        // Example 5: Auto-detecting source language
        System.out.println("=== Auto-detect Source Language ===");
        try {
            TextResult result = lara.translate("Bonjour le monde!", null, "en-US");
            System.out.println("Original: Bonjour le monde!");
            System.out.println("Detected source: " + result.getSourceLanguage());
            System.out.println("English: " + result.getTranslation() + "\n");
        } catch (LaraException e) {
            System.out.println("Error with auto-detection: " + e.getMessage() + "\n");
            return;
        }

        // Example 6: Advanced options with comprehensive settings
        System.out.println("=== Translation with Advanced Options ===");
        try {
            TranslateOptions options = new TranslateOptions()
                .setAdaptTo("mem_1A2b3C4d5E6f7G8h9I0jKl", "mem_2XyZ9AbC8dEf7GhI6jKlMn") // Replace with actual memory IDs
                .setGlossaries("gls_1A2b3C4d5E6f7G8h9I0jKl", "gls_2XyZ9AbC8dEf7GhI6jKlMn") // Replace with actual glossary IDs
                .setInstructions("Be professional")
                .setStyle(TranslationStyle.FLUID)
                .setContentType("text/plain")
                .setTimeoutMs(10000L)

            TextResult result = lara.translate("This is a comprehensive translation example", "en-US", "it-IT", options);
            System.out.println("Original: This is a comprehensive translation example");
            System.out.println("Italian (with all options): " + result.getTranslation() + "\n");
        } catch (LaraException e) {
            System.out.println("Error with advanced translation: " + e.getMessage() + "\n");
            return;
        }

        // Example 7: Get available languages
        System.out.println("=== Available Languages ===");
        try {
            List<String> languages = lara.getLanguages();
            System.out.print("Supported languages: [" + languages + "]");
        } catch (LaraException e) {
            System.out.println("Error getting languages: " + e.getMessage());
            return;
        }
    }
} 