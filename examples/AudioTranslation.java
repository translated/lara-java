import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.errors.S3Exception;
import com.translated.lara.translator.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Complete audio translation examples for the Lara Java SDK
 *
 * This example demonstrates:
 * - Basic audio translation
 * - Advanced options with memories and glossaries
 * - Step-by-step audio translation with status monitoring
 */
public class AudioTranslation {

    public static void main(String[] args) {
        // All examples can use environment variables for credentials:
        // export LARA_ACCESS_KEY_ID="your-access-key-id"
        // export LARA_ACCESS_KEY_SECRET="your-access-key-secret"

        String accessKeyId = System.getenv("LARA_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LARA_ACCESS_KEY_SECRET");

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);

        // Replace with your actual audio file path
        String sampleFilePath = "sample_audio.mp3";  // Create this file with your content
        File sampleFile = new File(sampleFilePath);

        if (!sampleFile.exists()) {
            System.out.println("Please create a sample audio file at: " + sampleFilePath);
            System.out.println("Supported formats: .wav, .mp3, .opus, .ogg, .webm\n");
            return;
        }

        String sourceLang = "en-US";
        String targetLang = "de-DE";

        // Example 1: Basic audio translation
        System.out.println("=== Basic Audio Translation ===");
        System.out.println("Translating audio: " + sampleFile.getName() + " from " + sourceLang + " to " + targetLang);

        try {
            InputStream audioStream = lara.audio.translate(sampleFile, sourceLang, targetLang);

            String outputPath = "sample_audio_translated.mp3";
            Files.copy(audioStream, Paths.get(outputPath));
            audioStream.close();

            System.out.println("✅ Audio translation completed");
            System.out.println("📄 Translated file saved to: " + outputPath + "\n");
        } catch (LaraException | S3Exception | InterruptedException | IOException e) {
            System.out.println("Error translating audio: " + e.getMessage() + "\n");
            return;
        }

        // Example 2: Audio translation with advanced options
        System.out.println("=== Audio Translation with Advanced Options ===");
        try {
            AudioUploadOptions options = new AudioUploadOptions();
            options.setAdaptTo("mem_1A2b3C4d5E6f7G8h9I0jKl");  // Replace with actual memory IDs
            options.setGlossaries("gls_1A2b3C4d5E6f7G8h9I0jKl");  // Replace with actual glossary IDs
            options.setStyle(TranslationStyle.FLUID);
            options.setNoTrace(false);

            InputStream audioStream = lara.audio.translate(sampleFile, sourceLang, targetLang, options);

            String outputPath = "advanced_audio_translated.mp3";
            Files.copy(audioStream, Paths.get(outputPath));
            audioStream.close();

            System.out.println("✅ Advanced audio translation completed");
            System.out.println("📄 Translated file saved to: " + outputPath + "\n");
        } catch (LaraException | S3Exception | InterruptedException | IOException e) {
            System.out.println("Error in advanced translation: " + e.getMessage());
            return;
        }

        // Example 3: Step-by-step audio translation
        System.out.println("=== Step-by-Step Audio Translation ===");
        try {
            AudioUploadOptions uploadOptions = new AudioUploadOptions();
            uploadOptions.setAdaptTo("mem_1A2b3C4d5E6f7G8h9I0jKl");  // Replace with actual memory ID
            uploadOptions.setGlossaries("gls_1A2b3C4d5E6f7G8h9I0jKl");  // Replace with actual glossary ID
            uploadOptions.setStyle(TranslationStyle.CREATIVE);

            System.out.println("Step 1: Uploading audio...");
            Audio audio = lara.audio.upload(sampleFile, sourceLang, "es-ES", uploadOptions);
            System.out.println("Audio uploaded with ID: " + audio.getId());
            System.out.println("Initial status: " + audio.getStatus());

            System.out.println("\nStep 2: Checking status...");
            Audio status = lara.audio.status(audio.getId());
            System.out.println("Current status: " + status.getStatus());

            System.out.println("\nStep 3: Waiting for translation to complete...");
            while (status.getStatus() != Audio.Status.TRANSLATED && status.getStatus() != Audio.Status.ERROR) {
                Thread.sleep(2000);
                status = lara.audio.status(audio.getId());
                System.out.println("Current status: " + status.getStatus());
            }

            if (status.getStatus() == Audio.Status.ERROR) {
                System.out.println("Translation failed: " + status.getErrorReason());
                return;
            }

            InputStream audioStream = lara.audio.download(audio.getId());

            String outputPath = "step_audio_translated.mp3";
            Files.copy(audioStream, Paths.get(outputPath));
            audioStream.close();

            System.out.println("✅ Step-by-step translation completed");
            System.out.println("📄 Translated file saved to: " + outputPath);

        } catch (LaraException | S3Exception | IOException | InterruptedException e) {
            System.out.println("Error in step-by-step process: " + e.getMessage() + "\n");
        }
    }
}
