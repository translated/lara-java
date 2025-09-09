import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.translator.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Complete memory management examples for the Lara Java SDK
 * 
 * This example demonstrates:
 * - Create, list, update, delete memories
 * - Add individual translations
 * - Multiple memory operations
 * - TMX file import with progress monitoring
 * - Translation deletion
 * - Translation with TUID and context
 */
public class MemoriesManagement {
    
    public static void main(String[] args) {
        // Set your credentials here
        String accessKeyId = "your-access-key-id";
        String accessKeySecret = "your-access-key-secret";

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);
        
        String memoryId = null;
        String externalMemoryId = null;
        String memory2ToDelete = null;
        
        try {
            // Example 1: Basic memory management
            System.out.println("=== Basic Memory Management ===");
            Memory memory = lara.memories.create("MyDemoMemory");
            System.out.println("✅ Created memory: " + memory.getName() + " (ID: " + memory.getId() + ")");
            memoryId = memory.getId();

            // Get memory details
            Memory retrievedMemory = lara.memories.get(memoryId);
            if (retrievedMemory != null) {
                System.out.println("📖 Memory: " + retrievedMemory.getName() + " (Owner: " + retrievedMemory.getOwnerId() + ")");
            }

            // Update memory
            Memory updatedMemory = lara.memories.update(memoryId, "UpdatedDemoMemory");
            System.out.println("📝 Updated name: '" + memory.getName() + "' -> '" + updatedMemory.getName() + "'");
            System.out.println();

            // List all memories
            List<Memory> memories = lara.memories.list();
            System.out.println("📝 Total memories: " + memories.size());
            System.out.println();

            // Example 2: Adding translations
            // Important: To update/overwrite a translation unit you must provide a tuid. Calls without a tuid always create a new unit and will not update existing entries.
            System.out.println("=== Adding Translations ===");
            try {
                // Basic translation addition (with TUID)
                MemoryImport memImport1 = lara.memories.addTranslation(memoryId, "en-US", "fr-FR", "Hello", "Bonjour", "greeting_001");
                System.out.println("✅ Added: 'Hello' -> 'Bonjour' with TUID 'greeting_001' (Import ID: " + memImport1.getId() + ")");

                // Translation with context
                MemoryImport memImport2 = lara.memories.addTranslation(
                    memoryId, "en-US", "fr-FR", "How are you?", "Comment allez-vous?", "greeting_002",
                    "Good morning", "Have a nice day"
                );
                System.out.println("✅ Added with context (Import ID: " + memImport2.getId() + ")");
                System.out.println();
            } catch (LaraException e) {
                System.out.println("Error adding translations: " + e.getMessage() + "\n");
            }

            // Example 3: Multiple memory operations
            System.out.println("=== Multiple Memory Operations ===");
            try {
                // Create second memory for multi-memory operations
                Memory memory2 = lara.memories.create("SecondDemoMemory");
                String memory2Id = memory2.getId();
                System.out.println("✅ Created second memory: " + memory2.getName());
                
                // Add translation to multiple memories (with TUID)
                List<String> memoryIds = Arrays.asList(memoryId, memory2Id);
                MemoryImport multiImportJob = lara.memories.addTranslation(memoryIds, "en-US", "it-IT", "Hello World!", "Ciao Mondo!", "greeting_003");
                System.out.println("✅ Added translation to multiple memories (Import ID: " + multiImportJob.getId() + ")");
                System.out.println();
                
                // Store for cleanup
                memory2ToDelete = memory2Id;
            } catch (LaraException e) {
                System.out.println("Error with multiple memory operations: " + e.getMessage() + "\n");
                memory2ToDelete = null;
            }

            // Example 4: TMX import functionality
            System.out.println("=== TMX Import Functionality ===");
            
            // Replace with your actual TMX file path
            String tmxFilePath = "sample_memory.tmx";  // Create this file with your TMX content
            File tmxFile = new File(tmxFilePath);
            
            if (tmxFile.exists()) {
                try {
                    System.out.println("Importing TMX file: " + tmxFile.getName());
                    MemoryImport tmxImport = lara.memories.importTmx(memoryId, tmxFile);
                    System.out.println("Import started with ID: " + tmxImport.getId());
                    System.out.println("Initial progress: " + (tmxImport.getProgress() * 100) + "%");
                    
                    // Wait for import to complete
                    try {
                        MemoryImport completedImport = lara.memories.waitForImport(tmxImport, 10000L); // 10 seconds timeout
                        System.out.println("✅ Import completed!");
                        System.out.println("Final progress: " + (completedImport.getProgress() * 100) + "%");
                    } catch (InterruptedException e) {
                        System.out.println("Import timeout: The import process took too long to complete.");
                    }
                    System.out.println();
                } catch (LaraException e) {
                    System.out.println("Error with TMX import: " + e.getMessage() + "\n");
                }
            } else {
                System.out.println("TMX file not found: " + tmxFilePath);
            }

            // Example 5: Translation deletion
            System.out.println("=== Translation Deletion ===");
            try {
                // Delete a specific translation unit (with TUID)
                // Important: if you omit tuid, all entries that match the provided fields will be removed
                MemoryImport deleteJob = lara.memories.deleteTranslation(memoryId,
                        "en-US",
                        "fr-FR",
                        "Hello",
                        "Bonjour",
                        tuid="greeting_001" // Specify the TUID to delete a specific translation unit
                );
                System.out.println("🗑️  Deleted translation unit (Job ID: " + deleteJob.getId() + ")");
                System.out.println();
            } catch (LaraException e) {
                System.out.println("Error deleting translation: " + e.getMessage() + "\n");
            }

        } catch (LaraException e) {
            System.out.println("Error creating memory: " + e.getMessage() + "\n");
            return;
        } finally {
            // Cleanup
            System.out.println("=== Cleanup ===");
            if (memoryId != null) {
                try {
                    Memory deletedMemory = lara.memories.delete(memoryId);
                    System.out.println("🗑️  Deleted memory: " + deletedMemory.getName());
                } catch (LaraException e) {
                    System.out.println("Error deleting memory: " + e.getMessage());
                }
            }
            
            if (externalMemoryId != null) {
                try {
                    Memory deletedExternalMemory = lara.memories.delete(externalMemoryId);
                    System.out.println("🗑️  Deleted external memory: " + deletedExternalMemory.getName());
                } catch (LaraException e) {
                    System.out.println("Error deleting external memory: " + e.getMessage());
                }
            }
            
            if (memory2ToDelete != null) {
                try {
                    Memory deletedMemory2 = lara.memories.delete(memory2ToDelete);
                    System.out.println("🗑️  Deleted second memory: " + deletedMemory2.getName());
                } catch (LaraException e) {
                    System.out.println("Error deleting second memory: " + e.getMessage());
                }
            }
        }
        
        System.out.println("\n🎉 Memory management examples completed!");
    }
} 