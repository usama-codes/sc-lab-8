import java.io.File;

/**
 * ============================================================
 * Specification: Recursive File Search Utility (Two Arguments)
 * ============================================================
 *
 * Purpose:
 * This program recursively searches for one or more files within a given
 * directory and its subdirectories. It accepts only two command-line
 * arguments:
 * 1. Directory path
 * 2. File name(s) separated by commas, optionally with a case mode suffix.
 *
 * Features:
 * 1. Multiple file search in a single run.
 * 2. Count how many times each file appears.
 * 3. Optional case-sensitivity toggle using suffix ":sensitive" or ":insensitive".
 */

public class FileSearch {

    public static void main(String[] args) {
        // Validate argument count
        if (args.length != 2) {
            System.out.println("Usage: java -cp bin Main <directory> <file1,file2,...[:sensitive|:insensitive]>");
            return;
        }

        String directoryPath = args[0];
        String fileArg = args[1];

        boolean caseSensitive = false;
        if (fileArg.endsWith(":insensitive")) {
            caseSensitive = false;
            fileArg = fileArg.replace(":insensitive", "");
        } else if (fileArg.endsWith(":sensitive")) {
            caseSensitive = true;
            fileArg = fileArg.replace(":sensitive", "");
        }

        // Split multiple filenames by comma
        String[] targetFiles = fileArg.split(",");

        File rootDirectory = new File(directoryPath);
        System.out.println("=== Recursive File Search ===");
        System.out.println("Directory: " + rootDirectory.getAbsolutePath());
        System.out.println("Case-Sensitive: " + caseSensitive);
        System.out.print("Files to Search: ");
        for (String f : targetFiles) System.out.print(f.trim() + " ");
        System.out.println("\n=============================");

        // Validate directory existence
        if (!rootDirectory.exists() || !rootDirectory.isDirectory()) {
            System.out.println("Error: The provided path is invalid or not a directory.");
            return;
        }

        try {
            for (String filename : targetFiles) {
                String trimmed = filename.trim();
                if (trimmed.isEmpty()) continue;
                int occurrences = searchFileRecursively(rootDirectory, trimmed, caseSensitive);
                if (occurrences > 0)
                    System.out.println("File \"" + trimmed + "\" found " + occurrences + " time(s).");
                else
                    System.out.println("File \"" + trimmed + "\" not found.");
            }
        } catch (SecurityException se) {
            System.out.println("Access denied while searching: " + rootDirectory.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Recursively searches for a file in a directory and subdirectories.
     * @param dir - specifies the directory where to search for the file recursively
     * @param targetFile - the file to be searched
     * @param caseSensitive - determines whether the search in case sensitive or not
     * @return count - the number of times a file was found
     */
    public static int searchFileRecursively(File dir, String targetFile, boolean caseSensitive) {
        if (dir == null || !dir.exists()) {
            System.out.println("Invalid directory: " + (dir == null ? "null" : dir.getAbsolutePath()));
            return 0;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println("Unable to access: " + dir.getAbsolutePath());
            return 0;
        }

        int count = 0;

        for (File f : files) {
            try {
                if (f.isDirectory()) {
                    count += searchFileRecursively(f, targetFile, caseSensitive);
                } else if (matches(f.getName(), targetFile, caseSensitive)) {
                    System.out.println("Found: " + f.getAbsolutePath());
                    count++;
                }
            } catch (SecurityException e) {
                System.out.println("Access denied: " + f.getAbsolutePath());
            }
        }

        return count;
    }

    /**
     * Compares filenames based on case sensitivity.
     */
    private static boolean matches(String fileName, String target, boolean caseSensitive) {
        return caseSensitive ? fileName.equals(target) : fileName.equalsIgnoreCase(target);
    }
}
