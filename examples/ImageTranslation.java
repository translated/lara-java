import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.translator.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

/**
 * Complete image translation examples for the Lara Java SDK
 *
 * This example demonstrates:
 * - Basic image translation (returns translated image)
 * - Image translation with advanced options (memories, glossaries, style)
 * - Text extraction from images with translation
 * - Text extraction with verbose output for paragraph details
 * - Editing translations and rendering with classic and generative models
 */
public class ImageTranslation {

    public static void main(String[] args) {
        // All examples can use environment variables for credentials:
        // export LARA_ACCESS_KEY_ID="your-access-key-id"
        // export LARA_ACCESS_KEY_SECRET="your-access-key-secret"

        // Set your credentials here
        String accessKeyId = System.getenv("LARA_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LARA_ACCESS_KEY_SECRET");

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);

        // Replace with your actual image file path
        String sampleImagePath = "sample_image.png";  // Create this file with your content
        File sampleImage = new File(sampleImagePath);

        if (!sampleImage.exists()) {
            System.out.println("Please create a sample image file at: " + sampleImagePath);
            System.out.println("Use an image containing text to translate.\n");
            return;
        }

        // Example 1: Basic image translation
        System.out.println("=== Basic Image Translation ===");
        String targetLang = "de-DE";

        System.out.println("Translating image: " + sampleImage.getName() + " to " + targetLang);

        try {
            ImageTranslateOptions translateOptions = new ImageTranslateOptions()
                .setModel(ImageTranslationModel.GENERATIVE_FAST);

            // Save translated image - replace with your desired output path
            String outputPath = "sample_image_translated.png";
            try (InputStream imageStream = lara.images.translate(sampleImage, null, targetLang, translateOptions)) {
                Files.copy(imageStream, Paths.get(outputPath));
            }

            System.out.println("✅ Image translation completed");
            System.out.println("📄 Translated image saved to: " + outputPath + "\n");
        } catch (LaraException | IOException e) {
            System.out.println("Error translating image: " + e.getMessage() + "\n");
            return;
        }

        // Example 2: Image translation with source language and advanced options
        System.out.println("=== Image Translation with Advanced Options ===");
        String sourceLang = "en-US";

        try {
            ImageTranslateOptions options = new ImageTranslateOptions()
                .setAdaptTo("mem_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual memory IDs
                .setGlossaries("gls_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual glossary IDs
                .setStyle(TranslationStyle.FLUID)
                .setModel(ImageTranslationModel.INPAINTING)
                .setNoTrace(false);

            InputStream imageStream = lara.images.translate(sampleImage, sourceLang, "fr-FR", options);

            String outputPath = "advanced_image_translated.png";
            Files.copy(imageStream, Paths.get(outputPath));
            imageStream.close();

            System.out.println("✅ Advanced image translation completed");
            System.out.println("📄 Translated image saved to: " + outputPath + "\n");
        } catch (LaraException | IOException e) {
            System.out.println("Error in advanced translation: " + e.getMessage() + "\n");
            return;
        }

        // Example 3: Extract and translate text from image (basic)
        System.out.println("=== Text Extraction and Translation from Image ===");

        try {
            ImageTextResult result = lara.images.translateText(sampleImage, "es-ES");

            System.out.println("✅ Text extraction and translation completed");
            System.out.println("🌍 Detected source language: " + result.getSourceLanguage());
            System.out.println("📝 Extracted paragraphs:");

            List<ImageParagraph> paragraphs = result.getParagraphs();
            for (int i = 0; i < paragraphs.size(); i++) {
                ImageParagraph paragraph = paragraphs.get(i);
                System.out.println("\n  Paragraph " + (i + 1) + ":");
                System.out.println("    Original: " + paragraph.getText());
                System.out.println("    Translated: " + paragraph.getTranslation());
            }
            System.out.println();
        } catch (LaraException e) {
            System.out.println("Error extracting text: " + e.getMessage() + "\n");
            return;
        }

        // Example 4: Extract and translate text with advanced options
        System.out.println("=== Text Extraction with Advanced Options ===");

        try {
            ImageTextTranslateOptions options = new ImageTextTranslateOptions()
                .setAdaptTo("mem_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual memory IDs
                .setGlossaries("gls_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual glossary IDs
                .setStyle(TranslationStyle.CREATIVE)
                .setVerbose(true)  // Include detailed matching information
                .setNoTrace(false);

            ImageTextResult result = lara.images.translateText(sampleImage, sourceLang, "it-IT", options);

            System.out.println("✅ Advanced text extraction completed");
            System.out.println("🌍 Source language: " + result.getSourceLanguage());

            // Show adapted-to resources used
            List<String> adaptedTo = result.getAdaptedTo();
            if (adaptedTo != null && !adaptedTo.isEmpty()) {
                System.out.println("📚 Adapted to memories: " + String.join(", ", adaptedTo));
            }

            // Show glossaries used
            List<String> glossaries = result.getGlossaries();
            if (glossaries != null && !glossaries.isEmpty()) {
                System.out.println("📖 Glossaries applied: " + String.join(", ", glossaries));
            }

            System.out.println("\n📝 Paragraphs with detailed matches:");
            List<ImageParagraph> paragraphs = result.getParagraphs();
            for (int i = 0; i < paragraphs.size(); i++) {
                ImageParagraph paragraph = paragraphs.get(i);
                System.out.println("\n  Paragraph " + (i + 1) + ":");
                System.out.println("    Original: " + paragraph.getText());
                System.out.println("    Translated: " + paragraph.getTranslation());

                // Show memory matches if available
                if (paragraph.getAdaptedToMatches() != null && !paragraph.getAdaptedToMatches().isEmpty()) {
                    System.out.println("    Memory matches found: " + paragraph.getAdaptedToMatches().size());
                }

                // Show glossary matches if available
                if (paragraph.getGlossariesMatches() != null && !paragraph.getGlossariesMatches().isEmpty()) {
                    System.out.println("    Glossary matches found: " + paragraph.getGlossariesMatches().size());
                }
            }
            System.out.println();
        } catch (LaraException e) {
            System.out.println("Error in advanced text extraction: " + e.getMessage() + "\n");
        }

        // Example 5: Request layout, edit a translation, and render it without translating again
        System.out.println("=== Extract, Edit, and Render Translations ===");
        try {
            ImageTextResult result = lara.images.translateText(sampleImage, sourceLang, targetLang,
                new ImageTextTranslateOptions().setIncludeLayout(true));

            if (result.getParagraphs().isEmpty()) {
                System.out.println("No text found to render.");
                return;
            }

            // includeLayout=true guarantees complete layout metadata on every paragraph.
            List<ImageLayoutParagraph> editedParagraphs = new ArrayList<>();
            for (ImageParagraph paragraph : result.getParagraphs()) {
                editedParagraphs.add((ImageLayoutParagraph) paragraph);
            }
            ImageLayoutParagraph first = editedParagraphs.get(0);
            editedParagraphs.set(0, new ImageLayoutParagraph(first.getText(), "Hallo Welt!",
                first.getBbox(), first.getLinesBboxes(), first.getTextInfo(), first.getAlignment()));

            try (InputStream rendered = lara.images.renderTranslated(sampleImage, result.getSourceLanguage(),
                    targetLang, editedParagraphs, ImageTranslationModel.OVERLAY)) {
                Files.copy(rendered, Paths.get("edited_image_overlay.png"));
            }
            System.out.println("Edited image saved to: edited_image_overlay.png");

            // Generative models accept text-only paragraphs. Omit model for generative_fast.
            // To choose explicitly, pass ImageGenerativeModel.GENERATIVE or GENERATIVE_FAST.
            List<ImageParagraph> textOnlyParagraphs = new ArrayList<>();
            for (ImageParagraph paragraph : editedParagraphs) {
                textOnlyParagraphs.add(new ImageParagraph(paragraph.getText(), paragraph.getTranslation()));
            }
            // Pass null to omit the source language. The same File can be reused for each request.
            try (InputStream rendered = lara.images.renderTranslated(sampleImage, null, targetLang, textOnlyParagraphs)) {
                Files.copy(rendered, Paths.get("edited_image_generative.png"));
            }
            System.out.println("Generative image saved to: edited_image_generative.png");
        } catch (LaraException | IOException e) {
            System.out.println("Error extracting, editing, or rendering translations: " + e.getMessage());
        }

        System.out.println("=== All examples completed ===");
    }
}
