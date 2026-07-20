import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraApiException;
import com.translated.lara.errors.LaraException;
import com.translated.lara.errors.S3Exception;
import com.translated.lara.translator.*;

import java.io.File;

/**
 * Audio transcript translation examples for the Lara Java SDK.
 *
 * This example demonstrates:
 * - One-shot audio transcript translation
 * - Step-by-step upload, poll status, retrieve transcript flow
 */
public class AudioTranscriptTranslation {

    public static void main(String[] args) {
        String accessKeyId = System.getenv("LARA_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LARA_ACCESS_KEY_SECRET");

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);

        String sampleFilePath = "sample_audio.mp3";
        File sampleFile = new File(sampleFilePath);

        if (!sampleFile.exists()) {
            System.out.println("Please create a sample audio file at: " + sampleFilePath);
            System.out.println("Supported formats: .wav, .mp3, .opus, .ogg, .webm\n");
            return;
        }

        String sourceLang = "en";
        String targetLang = "it";

        // Example 1: One-shot transcript translation
        System.out.println("=== One-shot Audio Transcript Translation ===");
        try {
            AudioTranscriptOptions options = new AudioTranscriptOptions()
                    .setStyle(TranslationStyle.FLUID)
                    .setNoTrace(false);

            AudioTextResult transcript = lara.audio.translateTranscript(sampleFile, sourceLang, targetLang, options);

            System.out.println("✅ Transcript translation completed");
            System.out.println("Translation: " + transcript.getTranslation());
            System.out.println("Segments: " + transcript.getSegments().size() + "\n");
        } catch (LaraException | S3Exception | InterruptedException e) {
            System.out.println("Error translating transcript: " + e.getMessage() + "\n");
            return;
        }

        // Example 2: Step-by-step transcript translation
        System.out.println("=== Step-by-Step Audio Transcript Translation ===");
        try {
            AudioTranscriptOptions options = new AudioTranscriptOptions()
                    .setNoTrace(false);

            System.out.println("Step 1: Uploading audio for transcript translation...");
            Audio audio = lara.audio.uploadForTranscription(sampleFile, sourceLang, targetLang, options);
            System.out.println("Audio uploaded with ID: " + audio.getId());
            System.out.println("Initial status: " + audio.getStatus());

            System.out.println("\nStep 2: Waiting for transcript translation to complete...");
            Audio status = audio;
            int pollingInterval = 2000;
            int maxWaitTime = 1000 * 60 * 15; // 15 minutes
            long start = System.currentTimeMillis();
            while (status.getStatus() != Audio.Status.TRANSLATED && status.getStatus() != Audio.Status.ERROR) {
                if (System.currentTimeMillis() - start >= maxWaitTime) {
                    throw new com.translated.lara.errors.TimeoutException();
                }
                Thread.sleep(pollingInterval);
                status = lara.audio.status(audio.getId());
                System.out.println("Current status: " + status.getStatus());
            }

            if (status.getStatus() == Audio.Status.ERROR) {
                throw new LaraApiException(500, "AudioError", status.getErrorReason());
            }

            System.out.println("\nStep 3: Retrieving translated transcript...");
            AudioTextResult transcript = lara.audio.getTranslatedTranscript(audio.getId());
            System.out.println("✅ Step-by-step transcript translation completed");
            System.out.println("Translation: " + transcript.getTranslation());
            System.out.println("Segments: " + transcript.getSegments().size());
        } catch (LaraException | S3Exception | InterruptedException e) {
            System.out.println("Error in step-by-step transcript process: " + e.getMessage() + "\n");
        }
    }
}
