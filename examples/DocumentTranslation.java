import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.errors.S3Exception;
import com.translated.lara.translator.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Complete document translation examples for the Lara Java SDK
 * 
 * This example demonstrates:
 * - Basic document translation
 * - Advanced options with memories and glossaries
 * - Step-by-step document translation with status monitoring
 */
public class DocumentTranslation {
    
    public static void main(String[] args) {
        // All examples can use environment variables for credentials:
        // export LARA_ACCESS_KEY_ID="your-access-key-id"
        // export LARA_ACCESS_KEY_SECRET="your-access-key-secret"

        // Set your credentials here
        String accessKeyId = System.getenv("LARA_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LARA_ACCESS_KEY_SECRET");

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);

        // Replace with your actual document file path
        String sampleFilePath = "sample_document.docx";  // Create this file with your content
        File sampleFile = new File(sampleFilePath);
        
        if (!sampleFile.exists()) {
            System.out.println("Please create a sample document file at: " + sampleFilePath);
            System.out.println("Add some sample text content to translate.\n");
            return;
        }

        // Example 1: Basic document translation
        System.out.println("=== Basic Document Translation ===");
        String sourceLang = "en-US";
        String targetLang = "de-DE";
        
        System.out.println("Translating document: " + sampleFile.getName() + " from " + sourceLang + " to " + targetLang);
        
        try {
            InputStream fileStream = lara.documents.translate(sampleFile, sourceLang, targetLang);
            
            // Save translated document - replace with your desired output path
            String outputPath = "sample_document_translated.docx";
            Files.copy(fileStream, Paths.get(outputPath));
            fileStream.close();
            
            System.out.println("✅ Document translation completed");
            System.out.println("📄 Translated document saved to: " + outputPath + "\n");
        } catch (LaraException | S3Exception | InterruptedException | IOException e) {
            System.out.println("Error translating document: " + e.getMessage() + "\n");
            return;
        }

        // Example 2: Document translation with advanced options
        System.out.println("=== Document Translation with Advanced Options ===");
        try {
            DocumentTranslateOptions options = new DocumentTranslateOptions()
                .setAdaptTo("mem_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual memory IDs
                .setGlossaries("gls_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual glossary IDs
                .setStyle(TranslationStyle.FLUID)
                .setNoTrace(false);
            
            InputStream fileStream = lara.documents.translate(sampleFile, sourceLang, "fr-FR", options);
            
            String outputPath = "advanced_document_translated.docx";
            Files.copy(fileStream, Paths.get(outputPath));
            fileStream.close();
            
            System.out.println("✅ Advanced document translation completed");
            System.out.println("📄 Translated document saved to: " + outputPath + "\n");
        } catch (LaraException | S3Exception | InterruptedException | IOException e) {
            System.out.println("Error in advanced translation: " + e.getMessage() + "\n");
            return;
        }

        // Example 3: Step-by-step document translation
        System.out.println("=== Step-by-Step Document Translation ===");
        try {
            // Upload document
            DocumentUploadOptions uploadOptions = new DocumentUploadOptions()
                .setAdaptTo("mem_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual memory ID
                .setGlossaries("gls_1A2b3C4d5E6f7G8h9I0jKl")  // Replace with actual glossary ID
                .setStyle(TranslationStyle.CREATIVE);
            
            System.out.println("Step 1: Uploading document...");
            Document document = lara.documents.upload(sampleFile, sourceLang, "es-ES", uploadOptions);
            System.out.println("Document uploaded with ID: " + document.getId());
            System.out.println("Initial status: " + document.getStatus());
            
            // Check status
            System.out.println("\nStep 2: Checking status...");
            Document status = lara.documents.status(document.getId());
            System.out.println("Current status: " + status.getStatus());
            
            // Download translated document
            System.out.println("\nStep 3: Downloading would happen after translation completes...");
            
            InputStream fileStream = lara.documents.download(document.getId());
            
            String outputPath = "monitored_translated.docx";
            Files.copy(fileStream, Paths.get(outputPath));
            fileStream.close();
            
            System.out.println("✅ Step-by-step translation completed");

        } catch (LaraException | S3Exception | IOException e) {
            System.out.println("Error in step-by-step process: " + e.getMessage() + "\n");
        }
    }
} 