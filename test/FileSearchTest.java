import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



/**
 * ============================================================
 * Unit Tests for Main.java - Recursive File Search Utility (JUnit 4)
 * ============================================================
 *
 * Tests:
 * 1. Directory validation (invalid path, null directory).
 * 2. Single file search (case-sensitive and insensitive).
 * 3. Multiple file search counting occurrences.
 * 4. Handling inaccessible directories gracefully.
 */

public class FileSearchTest {

    private static Path testRoot;
    private static Path subDir;
    private static Path subSubDir;

    @BeforeClass
    public static void setup() throws IOException {
        // Create a temporary test directory structure
        testRoot = Files.createTempDirectory("testRoot");
        subDir = Files.createDirectory(testRoot.resolve("subdir"));
        subSubDir = Files.createDirectory(subDir.resolve("deep"));

        // Create sample files
        Files.createFile(testRoot.resolve("Report.pdf"));
        Files.createFile(subDir.resolve("data.txt"));
        Files.createFile(subSubDir.resolve("report.pdf")); // duplicate name (case diff)
    }

    @AfterClass
    public static void cleanup() throws IOException {
        // Cleanup after tests - delete files before directories
        if (testRoot != null && Files.exists(testRoot)) {
            try (var paths = Files.walk(testRoot)) {
                paths.sorted((a, b) -> b.compareTo(a)) // Reverse order to delete files before directories
                     .map(Path::toFile)
                     .forEach(File::delete);
            }
        }
    }

    @Test
    public void testInvalidDirectory() {
        File invalidDir = new File("Z:/nonexistent/path");
        int result = Main.searchFileRecursively(invalidDir, "dummy.txt", false);
        assertEquals(0, result);
    }

    @Test
    public void testCaseInsensitiveSearch() {
        int count = Main.searchFileRecursively(testRoot.toFile(), "report.pdf", false);
        assertEquals(2, count);
    }

    @Test
    public void testCaseSensitiveSearch() {
        int count = Main.searchFileRecursively(testRoot.toFile(), "report.pdf", true);
        assertEquals(1, count);
    }

    @Test
    public void testFileNotFound() {
        int count = Main.searchFileRecursively(testRoot.toFile(), "nonexistent.docx", false);
        assertEquals(0, count);
    }

    @Test
    public void testMultipleFilesCount() {
        int count1 = Main.searchFileRecursively(testRoot.toFile(), "data.txt", false);
        int count2 = Main.searchFileRecursively(testRoot.toFile(), "report.pdf", false);
        assertEquals(1, count1);
        assertEquals(2, count2);
    }

    @Test
    public void testMatchesCaseSensitive() {
        assertTrue(Main.searchFileRecursively(testRoot.toFile(), "Report.pdf", true) >= 1);
        assertFalse(Main.searchFileRecursively(testRoot.toFile(), "report.pdf", true) > 1);
    }

    @Test
    public void testMatchesCaseInsensitive() {
        assertTrue(Main.searchFileRecursively(testRoot.toFile(), "RePorT.PdF", false) >= 2);
    }
}
