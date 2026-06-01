import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.translator.Styleguide;
import com.translated.lara.translator.Translator;

import java.util.List;

/**
 * Complete styleguide management examples for the Lara Java SDK.
 *
 * This example demonstrates:
 * - Create, list, get, update, delete styleguides
 */
public class StyleguidesManagement {

    public static void main(String[] args) {
        String accessKeyId = System.getenv("LARA_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LARA_ACCESS_KEY_SECRET");

        if (accessKeyId == null || accessKeySecret == null) {
            System.out.println("Set LARA_ACCESS_KEY_ID and LARA_ACCESS_KEY_SECRET environment variables.");
            return;
        }

        Credentials credentials = new Credentials(accessKeyId, accessKeySecret);
        Translator lara = new Translator(credentials);

        String styleguideId = null;

        System.out.println("Styleguides require a specific subscription plan.");

        try {
            System.out.println("=== Basic Styleguide Management ===");
            String initialContent = "Use a formal tone. Prefer British English spelling. Avoid contractions.";
            Styleguide styleguide = lara.styleguides.create("MyDemoStyleguide", initialContent);
            System.out.println("Created styleguide: " + styleguide.getName() + " (ID: " + styleguide.getId() + ")");
            styleguideId = styleguide.getId();

            List<Styleguide> styleguides = lara.styleguides.list();
            System.out.println("Total styleguides: " + styleguides.size());
            System.out.println();

            System.out.println("=== Styleguide Operations ===");
            Styleguide retrieved = lara.styleguides.get(styleguideId);
            if (retrieved != null) {
                System.out.println("Styleguide: " + retrieved.getName() + " (Owner: " + retrieved.getOwnerId() + ")");
                if (retrieved.getContent() != null) {
                    int previewLength = Math.min(80, retrieved.getContent().length());
                    System.out.println("   Content preview: "
                            + retrieved.getContent().substring(0, previewLength) + "...");
                }
            }
            System.out.println();

            System.out.println("=== Update Styleguide ===");
            Styleguide renamed = lara.styleguides.update(styleguideId, "UpdatedDemoStyleguide");
            System.out.println("Updated name: '" + styleguide.getName() + "' -> '" + renamed.getName() + "'");

            String updatedContent = "Use a casual tone. Prefer American English spelling. Contractions are welcome.";
            Styleguide contentUpdated = lara.styleguides.update(styleguideId, null, updatedContent);
            System.out.println("Updated content for styleguide: " + contentUpdated.getName());

            Styleguide fullyUpdated = lara.styleguides.update(styleguideId, "FinalDemoStyleguide",
                    "Use clear and concise language. Avoid jargon.");
            System.out.println("Updated name and content: " + fullyUpdated.getName());
            System.out.println();

            System.out.println("=== Get Non-Existent Styleguide ===");
            Styleguide missing = lara.styleguides.get("non-existent-id");
            if (missing == null) {
                System.out.println("Styleguide not found (returned null as expected)");
            }
            System.out.println();

        } catch (LaraException e) {
            System.out.println("Error during styleguide management: " + e.getMessage());
            return;
        } finally {
            System.out.println("=== Cleanup ===");
            if (styleguideId != null) {
                try {
                    Styleguide deleted = lara.styleguides.delete(styleguideId);
                    System.out.println("Deleted styleguide: " + deleted.getName() + " (ID: " + deleted.getId() + ")");
                } catch (LaraException e) {
                    System.out.println("Error deleting styleguide: " + e.getMessage());
                }
            }
        }

        System.out.println("\nStyleguide management examples completed!");
    }
}
