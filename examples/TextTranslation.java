import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.translator.DetectResult;
import com.translated.lara.translator.ProfanitiesDetect;
import com.translated.lara.translator.ProfanitiesHandling;
import com.translated.lara.translator.QualityEstimationResult;
import com.translated.lara.translator.Styleguide;
import com.translated.lara.translator.StyleguideChange;
import com.translated.lara.translator.StyleguideResults;
import com.translated.lara.translator.TextBlock;
import com.translated.lara.translator.TextResult;
import com.translated.lara.translator.TranslateOptions;
import com.translated.lara.translator.TranslationStyle;
import com.translated.lara.translator.Translator;

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
 * - Translation with styleguides
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
                .setTimeoutMs(10000L);

            TextResult result = lara.translate("This is a comprehensive translation example", "en-US", "it-IT", options);
            System.out.println("Original: This is a comprehensive translation example");
            System.out.println("Italian (with all options): " + result.getTranslation() + "\n");
        } catch (LaraException e) {
            System.out.println("Error with advanced translation: " + e.getMessage() + "\n");
            return;
        }

        // Example 7: Translation with profanity detection and handling
        System.out.println("=== Translation with Profanity Detection and Handling ===");
        try {
            String profanityText = "Don't be such a tool.";

            // Detect profanities in both source and target, report without modifying the translation
            TranslateOptions detectOptions = new TranslateOptions()
                .setProfanitiesDetect(ProfanitiesDetect.SOURCE_TARGET)
                .setProfanitiesHandling(ProfanitiesHandling.DETECT);
            TextResult detectResult = lara.translate(profanityText, "en-US", "it-IT", detectOptions);
            System.out.println("Original: " + profanityText);
            System.out.println("Detect mode translation: " + detectResult.getTranslation());
            TextResult.ProfanitiesResult detectProfanities = detectResult.getProfanitiesResult();
            if (detectProfanities != null && detectProfanities.getTarget() != null) {
                System.out.println("Target masked text: " + detectProfanities.getTarget().getSingle().getMaskedText());
                System.out.println("Target profanities found: " + detectProfanities.getTarget().getSingle().getProfanities().size());
            }
            if (detectProfanities != null && detectProfanities.getSource() != null) {
                System.out.println("Source masked text: " + detectProfanities.getSource().getSingle().getMaskedText());
            }

            // Detect profanities in target only and hide them (replace them with asterisks)
            TranslateOptions hideOptions = new TranslateOptions()
                .setProfanitiesDetect(ProfanitiesDetect.TARGET)
                .setProfanitiesHandling(ProfanitiesHandling.HIDE);
            TextResult hideResult = lara.translate(profanityText, "en-US", "it-IT", hideOptions);
            System.out.println("Hide mode translation: " + hideResult.getTranslation());

        } catch (LaraException e) {
            System.out.println("Error with profanity detection: " + e.getMessage() + "\n");
            return;
        }

        // Example 8: Translation with reasoning
        System.out.println("=== Translation with Reasoning ===");
        try {
            TranslateOptions reasoningOptions = new TranslateOptions()
                .setReasoning(true);

            System.out.println("Original: Wonderful cavernous interior in a central but quiet and private area!");
            System.out.println("Streaming partial results:");

            Consumer<TextResult> streamingCallback = partialResult -> {
                System.out.println("Partial result: " + partialResult.getTranslation());
            };

            TextResult finalResult = lara.translate("Wonderful cavernous interior in a central but quiet and private area!", "en-US", "it-IT", reasoningOptions, streamingCallback);

            System.out.println("Final result: " + finalResult.getTranslation() + "\n");
        } catch (LaraException e) {
            System.out.println("Error with reasoning translation: " + e.getMessage() + "\n");
            return;
        }

        // Example 9: List available styleguides
        System.out.println("=== List Available Styleguides ===");
        String styleguideId = null;
        try {
            List<Styleguide> styleguides = lara.styleguides.list();
            System.out.println("Total styleguides: " + styleguides.size());
            for (Styleguide sg : styleguides) {
                System.out.println("  - " + sg.getName() + " (ID: " + sg.getId() + ")");
            }

            if (!styleguides.isEmpty()) {
                styleguideId = styleguides.get(0).getId();
            }
            System.out.println();
        } catch (LaraException e) {
            System.out.println("Error listing styleguides: " + e.getMessage() + "\n");
        }

        // Example 10: Get a specific styleguide by ID
        if (styleguideId != null) {
            System.out.println("=== Get Styleguide Details ===");
            try {
                Styleguide styleguide = lara.styleguides.get(styleguideId);
                if (styleguide != null) {
                    System.out.println("Name: " + styleguide.getName());
                    System.out.println("ID: " + styleguide.getId());
                    System.out.println("Owner: " + styleguide.getOwnerId());
                    System.out.println("Created: " + styleguide.getCreatedAt());
                    System.out.println("Updated: " + styleguide.getUpdatedAt());
                }
                System.out.println();
            } catch (LaraException e) {
                System.out.println("Error getting styleguide: " + e.getMessage() + "\n");
            }
        }

        // Example 11: Translate with a styleguide
        if (styleguideId != null) {
            System.out.println("=== Translate with Styleguide ===");
            try {
                TranslateOptions options = new TranslateOptions()
                        .setStyleguideId(styleguideId);

                TextResult result = lara.translate(
                        "Our team is excited to announce that the new feature is now available for all users.",
                        "en-US", "it-IT", options
                );
                System.out.println("Original: Our team is excited to announce that the new feature is now available for all users.");
                System.out.println("Italian (with styleguide): " + result.getTranslation() + "\n");
            } catch (LaraException e) {
                System.out.println("Error translating with styleguide: " + e.getMessage() + "\n");
            }
        }

        // Example 12: Translate with styleguide reasoning
        if (styleguideId != null) {
            System.out.println("=== Translate with Styleguide Reasoning ===");
            try {
                TranslateOptions options = new TranslateOptions()
                        .setStyleguideId(styleguideId)
                        .setStyleguideReasoning(true)
                        .setStyleguideExplanationLanguage("en-US");

                TextResult result = lara.translate(
                        "Please submit the required documentation before the deadline.",
                        "en-US", "it-IT", options
                );
                System.out.println("Original: Please submit the required documentation before the deadline.");
                System.out.println("Italian (with styleguide): " + result.getTranslation());

                StyleguideResults sgResults = result.getStyleguideResults();
                if (sgResults != null) {
                    System.out.println("Original translation (before styleguide): " + sgResults.getOriginalTranslation());

                    List<StyleguideChange> changes = sgResults.getChanges();
                    if (changes != null && !changes.isEmpty()) {
                        System.out.println("Changes applied: " + changes.size());
                        for (StyleguideChange change : changes) {
                            System.out.println("  Change ID: " + change.getId());
                            System.out.println("  Before: " + change.getOriginalTranslation());
                            System.out.println("  After:  " + change.getRefinedTranslation());
                            System.out.println("  Why:    " + change.getExplanation());
                        }
                    } else {
                        System.out.println("No changes were needed — translation already matches the styleguide.");
                    }
                }
                System.out.println();
            } catch (LaraException e) {
                System.out.println("Error with styleguide reasoning: " + e.getMessage() + "\n");
            }
        }

        // Example 13: Get available languages
        System.out.println("=== Available Languages ===");
        try {
            List<String> languages = lara.getLanguages();
            System.out.print("Supported languages: [" + languages + "]");
        } catch (LaraException e) {
            System.out.println("Error getting languages: " + e.getMessage());
            return;
        }

        // Example 14: Detect language of a given text
        System.out.println("=== Language Detection ===");
        try {
            DetectResult detectResult = lara.detect("Hola, ¿cómo estás?");
            System.out.println("Text: Hola, ¿cómo estás?");
            System.out.println("Detected Language: " + detectResult.getLanguage());
        } catch (LaraException e) {
            System.out.println("Error detecting language: " + e.getMessage());
            return;
        }

        // Example 15: Detect languages with hint and passlist
        System.out.println("=== Language Detection with Hint and Passlist ===");
        try {
            DetectResult detectResult = lara.detect("Hola, ¿cómo estás?", "es", new String[]{"es", "pt", "it"});
            System.out.println("Text: Hola, ¿cómo estás?");
            System.out.println("Detected Language: " + detectResult.getLanguage());
        } catch (LaraException e) {
            System.out.println("Error detecting language with hint/passlist: " + e.getMessage());
            return;
        }

        // Example 16: Quality estimation for a single sentence pair
        System.out.println("=== Quality Estimation: single sentence ===");
        try {
            QualityEstimationResult qeSingle = lara.qualityEstimation(
                    "en-US", "it-IT",
                    "Hello, how are you today?",
                    "Ciao, come stai oggi?"
            );
            System.out.println("Score: " + qeSingle.getScore() + "\n");
        } catch (LaraException e) {
            System.out.println("Error in quality estimation (single): " + e.getMessage() + "\n");
            return;
        }

        // Example 17: Quality estimation for a batch of sentence pairs
        System.out.println("=== Quality Estimation: batch ===");
        try {
            List<QualityEstimationResult> qeBatch = lara.qualityEstimation(
                    "en-US", "it-IT",
                    Arrays.asList("Good morning.", "The weather is nice."),
                    Arrays.asList("Buongiorno.", "Il tempo è bello.")
            );
            System.out.println("Scores: " + qeBatch.stream().map(r -> Double.toString(r.getScore())).reduce((a, b) -> a + ", " + b).orElse("") + "\n");
        } catch (LaraException e) {
            System.out.println("Error in quality estimation (batch): " + e.getMessage() + "\n");
            return;
        }

        // Example 18: Translation with reasoning (streaming)
        System.out.println("=== Translation with Reasoning ===");
        try {
            TranslateOptions reasoningOptions = new TranslateOptions()
                    .setReasoning(true);

            System.out.println("Original: Wonderful cavernous interior in a central but quiet and private area!");
            System.out.println("Streaming partial results:");

            Consumer<TextResult> streamingCallback = partialResult -> {
                System.out.println("Partial result: " + partialResult.getTranslation());
            };

            TextResult finalResult = lara.translate("Wonderful cavernous interior in a central but quiet and private area!", "en-US", "it-IT", reasoningOptions, streamingCallback);

            System.out.println("Final result: " + finalResult.getTranslation() + "\n");
        } catch (LaraException e) {
            System.out.println("Error with reasoning translation: " + e.getMessage() + "\n");
            return;
        }
    }
} 