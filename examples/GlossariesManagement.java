import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.translator.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Complete glossary management examples for the Lara Java SDK
 * 
 * This example demonstrates:
 * - Create, list, update, delete glossaries
 * - CSV import with status monitoring
 * - Glossary export
 * - Glossary terms count
 * - Import status checking
 */
public class GlossariesManagement {
    
    public static void main(String[] args) {
        // Set your credentials here
        String accessKeyId = "your-access-key-id";
        String accessKeySecret = "your-access-key-secret";

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);
        
        String glossaryId = null;
        
        System.out.println("🗒️  Glossaries require a specific subscription plan.");
        System.out.println("   If you encounter errors, please check your subscription level.\n");
        
        try {
            // Example 1: Basic glossary management
            System.out.println("=== Basic Glossary Management ===");
            Glossary glossary = lara.glossaries.create("MyDemoGlossary");
            System.out.println("✅ Created glossary: " + glossary.getName() + " (ID: " + glossary.getId() + ")");
            glossaryId = glossary.getId();
            
            // List all glossaries
            List<Glossary> glossaries = lara.glossaries.list();
            System.out.println("📝 Total glossaries: " + glossaries.size());
            System.out.println();

            // Example 2: Glossary operations
            System.out.println("=== Glossary Operations ===");
            // Get glossary details
            Glossary retrievedGlossary = lara.glossaries.get(glossaryId);
            if (retrievedGlossary != null) {
                System.out.println("📖 Glossary: " + retrievedGlossary.getName() + " (Owner: " + retrievedGlossary.getOwnerId() + ")");
            }

            // Get glossary terms count
            GlossaryCounts counts = lara.glossaries.counts(glossaryId);
            for (Map.Entry<String, Integer> entry : counts.getUnidirectional().entrySet()) {
                System.out.println("   " + entry.getKey() + ": " + entry.getValue() + " entries");
            }
            
            // Update glossary
            Glossary updatedGlossary = lara.glossaries.update(glossaryId, "UpdatedDemoGlossary");
            System.out.println("📝 Updated name: '" + glossary.getName() + "' -> '" + updatedGlossary.getName() + "'");

            // Example 3: CSV import functionality
            System.out.println("=== CSV Import Functionality ===");
            
            // Replace with your actual CSV file path
            String csvFilePath = "sample_glossary.csv";  // Create this file with your glossary data
            File csvFile = createSampleCsvFile(csvFilePath);
            
            if (csvFile != null && csvFile.exists()) {
                System.out.println("Importing CSV file: " + csvFile.getName());
                GlossaryImport csvImport = lara.glossaries.importCsv(glossaryId, csvFile);
                System.out.println("Import started with ID: " + csvImport.getId());
                System.out.println("Initial progress: " + (csvImport.getProgress() * 100) + "%");
                
                // Check import status manually
                System.out.println("Checking import status...");
                GlossaryImport importStatus = lara.glossaries.getImportStatus(csvImport.getId());
                System.out.println("Current progress: " + (importStatus.getProgress() * 100) + "%");
                
                // Wait for import to complete
                try {
                    GlossaryImport completedImport = lara.glossaries.waitForImport(csvImport, 10000L); // 10 seconds timeout
                    System.out.println("✅ Import completed!");
                    System.out.println("Final progress: " + (completedImport.getProgress() * 100) + "%");
                } catch (InterruptedException e) {
                    System.out.println("Import timeout: The import process took too long to complete.");
                }
                System.out.println();
                
                // Clean up sample file
                csvFile.delete();
            } else {
                System.out.println("CSV file not found: " + csvFilePath);
            }

            // Example 4: Export functionality
            System.out.println("=== Export Functionality ===");
            try {
                // Export as CSV table unidirectional format
                System.out.println("📤 Exporting as CSV table unidirectional...");
                String csvUniData = lara.glossaries.export(glossaryId, "csv/table-uni", "en-US");
                System.out.println("✅ CSV unidirectional export successful (" + csvUniData.length() + " bytes)");

                // Save sample exports to files - replace with your desired output paths
                String exportFilePath = "exported_glossary.csv";  // Replace with actual path
                saveToFile(csvUniData.getBytes(), exportFilePath);
                System.out.println("💾 Sample export saved to: " + exportFilePath);
                System.out.println();
            } catch (LaraException | IOException e) {
                System.out.println("Error with export: " + e.getMessage() + "\n");
            }

            // Example 5: Glossary Terms Count
            System.out.println("=== Glossary Terms Count ===");
            try {
                // Get detailed counts
                GlossaryCounts detailedCounts = lara.glossaries.counts(glossaryId);

                if (detailedCounts.getUnidirectional() != null) {
                    System.out.println("   Unidirectional entries by language pair:");
                    for (Map.Entry<String, Integer> entry : detailedCounts.getUnidirectional().entrySet()) {
                        System.out.println("     " + entry.getKey() + ": " + entry.getValue() + " terms");
                    }
                } else {
                    System.out.println("No unidirectional entries found");
                }
                
                int totalEntries = 0;
                if (detailedCounts.getUnidirectional() != null) {
                    totalEntries = detailedCounts.getUnidirectional().values().stream().mapToInt(Integer::intValue).sum();
                }
                System.out.println("   Total entries: " + totalEntries);
                System.out.println();
            } catch (LaraException e) {
                System.out.println("Error getting terms count: " + e.getMessage() + "\n");
            }

        } catch (LaraException e) {
            System.out.println("Error creating glossary: " + e.getMessage() + "\n");
            return;
        } finally {
            // Cleanup
            System.out.println("=== Cleanup ===");
            if (glossaryId != null) {
                try {
                    Glossary deletedGlossary = lara.glossaries.delete(glossaryId);
                    System.out.println("🗑️  Deleted glossary: " + deletedGlossary.getName());
                    
                    // Clean up export files - replace with actual cleanup if needed
                    String exportFilePath = "exported_glossary.csv";
                    File exportFile = new File(exportFilePath);
                    if (exportFile.exists()) {
                        exportFile.delete();
                        System.out.println("🗑️  Cleaned up export file");
                    }
                } catch (LaraException e) {
                    System.out.println("Error deleting glossary: " + e.getMessage());
                }
            }
        }
        
        System.out.println("\n🎉 Glossary management examples completed!");
    }
    
    /**
     * Helper method to create a sample CSV file for testing
     */
    private static File createSampleCsvFile(String filePath) {
        try {
            File csvFile = new File(filePath);
            try (FileOutputStream fos = new FileOutputStream(csvFile)) {
                String csvContent = "en-US,es-ES,it-IT\n" +
                                  "Hello,Hola,Ciao\n" +
                                  "Goodbye,Adiós,Arrivederci\n" +
                                  "Thank you,Gracias,Grazie\n" +
                                  "Welcome,Bienvenido,Benvenuto\n";
                fos.write(csvContent.getBytes());
                System.out.println("📝 Created sample CSV file: " + filePath);
            }
            return csvFile;
        } catch (IOException e) {
            System.out.println("❌ Error creating sample CSV file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Helper method to save byte data to file
     */
    private static void saveToFile(byte[] data, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }
} 