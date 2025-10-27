import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import java.util.List;

public class StringPermutationsTest {

    private static String testInput1;
    private static String testInput2;
    private static String emptyInput;

    @BeforeClass
    public static void setUp() {
        testInput1 = "Usama";       // contains duplicate 'a'
        testInput2 = "ABC";         // all unique characters
        emptyInput = "";             // edge case: empty string
    }

    @AfterClass
    public static void tearDown() {
        testInput1 = null;
        testInput2 = null;
        emptyInput = null;
    }

    // Test recursive permutations including duplicates
    @Test
    public void testRecursiveAllowDuplicates() {
        List<String> perms = StringPermutations.getRecursivePermutations(testInput1, true);
        assertEquals(120, perms.size());  // 5! = 120, duplicates included
        assertTrue(perms.contains("Usama"));
        assertTrue(perms.contains("maUsa"));
    }

    // Test recursive permutations without duplicates
    @Test
    public void testRecursiveRemoveDuplicates() {
        List<String> perms = StringPermutations.getRecursivePermutations(testInput1, false);
        assertEquals(60, perms.size());  // unique permutations only
    }

    // Test iterative permutations including duplicates
    @Test
    public void testIterativeAllowDuplicates() {
        List<String> perms = StringPermutations.getIterativePermutations(testInput1, true);
        assertEquals(120, perms.size());
        assertTrue(perms.contains("Usama"));
        assertTrue(perms.contains("maUsa"));
    }

    // Test iterative permutations without duplicates
    @Test
    public void testIterativeRemoveDuplicates() {
        List<String> perms = StringPermutations.getIterativePermutations(testInput1, false);
        assertEquals(60, perms.size());
    }

    // Test both recursive and iterative produce same results for unique characters
    @Test
    public void testUniqueCharactersConsistency() {
        List<String> recursivePerms = StringPermutations.getRecursivePermutations(testInput2, false);
        List<String> iterativePerms = StringPermutations.getIterativePermutations(testInput2, false);
        assertEquals(6, recursivePerms.size()); // 3! = 6
        assertEquals(6, iterativePerms.size());
        assertTrue(recursivePerms.containsAll(iterativePerms));
        assertTrue(iterativePerms.containsAll(recursivePerms));
    }

    // Test empty input handling
    @Test
    public void testEmptyInput() {
        List<String> recursivePerms = StringPermutations.getRecursivePermutations(emptyInput, true);
        List<String> iterativePerms = StringPermutations.getIterativePermutations(emptyInput, true);
        assertEquals(0, recursivePerms.size());
        assertEquals(0, iterativePerms.size());
    }

    // Optional: test null input handling (if method allows)
    @Test
    public void testNullInput() {
        List<String> recursivePerms = StringPermutations.getRecursivePermutations(null, true);
        List<String> iterativePerms = StringPermutations.getIterativePermutations(null, true);
        assertEquals(0, recursivePerms.size());
        assertEquals(0, iterativePerms.size());
    }
}
