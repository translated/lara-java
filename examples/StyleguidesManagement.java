import com.translated.lara.Credentials;
import com.translated.lara.errors.LaraException;
import com.translated.lara.translator.ResourceShareEntry;
import com.translated.lara.translator.Styleguide;
import com.translated.lara.translator.StyleguideShares;
import com.translated.lara.translator.Translator;

import java.util.List;

/**
 * Complete styleguide management examples for the Lara Java SDK.
 *
 * This example demonstrates:
 * - Create, list, get, update, delete styleguides
 * - Sharing a styleguide with the account or a group (add, rename, list, revoke)
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

            // Sharing requires a multi-user account and the appropriate role (account owner for
            // account-wide shares, owner/admin for group shares). Each call returns the shared
            // styleguide, whose name reflects the shared copy's name and sharedAt the share time.
            System.out.println("=== Styleguide Sharing ===");
            try {
                // Share with the whole account/team (the optional argument names the shared copy)
                Styleguide teamShare = lara.styleguides.addAccountShare(styleguideId, "Shared with the team");
                System.out.println("Shared with the account as: '" + teamShare.getName() + "' (shared at " + teamShare.getSharedAt() + ")");

                // Rename the account/team share
                Styleguide renamedTeamShare = lara.styleguides.renameAccountShare(styleguideId, "Team styleguide");
                System.out.println("Renamed account share to: '" + renamedTeamShare.getName() + "'");

                // List every share visible to the caller: the account share, group shares and user shares
                StyleguideShares shares = lara.styleguides.getShares(styleguideId);
                if (shares.getAccount() != null) {
                    System.out.println("Account share '" + shares.getAccount().getShareName() + "' (" + shares.getAccount().getPermissions() + ")");
                }
                for (ResourceShareEntry group : shares.getGroups()) {
                    System.out.println("Group " + group.getName() + ": '" + group.getShareName() + "' (" + group.getPermissions() + ")");
                }
                for (ResourceShareEntry user : shares.getUsers()) {
                    System.out.println("User " + user.getName() + ": '" + user.getShareName() + "' (" + user.getPermissions() + ")");
                }

                // Revoke the account/team share
                lara.styleguides.revokeAccountShare(styleguideId);
                System.out.println("Revoked the account share");

                // Group shares work the same way, addressed by a group ID (grp_...)
                String groupId = System.getenv("LARA_GROUP_ID"); // Replace with an actual group ID
                if (groupId != null) {
                    Styleguide groupShare = lara.styleguides.addGroupShare(styleguideId, groupId, "Shared with the group");
                    System.out.println("Shared with group " + groupId + " as: '" + groupShare.getName() + "'");

                    lara.styleguides.renameGroupShare(styleguideId, groupId, "Marketing group");
                    System.out.println("Renamed the group share");

                    lara.styleguides.revokeGroupShare(styleguideId, groupId);
                    System.out.println("Revoked the group share");
                } else {
                    System.out.println("Set LARA_GROUP_ID to try the group sharing methods.");
                }
                System.out.println();
            } catch (LaraException e) {
                System.out.println("Error sharing styleguide: " + e.getMessage() + "\n");
            }

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
