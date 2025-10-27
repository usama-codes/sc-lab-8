import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class StringPermutations {

    // Recursive function to generate all permutations
    private static void generatePermutations(char[] str, int index, List<String> result, boolean allowDuplicates) {
    if (index == str.length - 1) {
        String perm = new String(str);
        if (allowDuplicates || !result.contains(perm)) {
            result.add(perm);
        }
        return;
    }

    if (allowDuplicates) {
        // No duplicate check, generate all permutations
        for (int i = index; i < str.length; i++) {
            swap(str, index, i);
            generatePermutations(str, index + 1, result, true);
            swap(str, index, i); // backtrack
        }
    } else {
        // Prevent swapping identical characters
        HashSet<Character> used = new HashSet<>();
        for (int i = index; i < str.length; i++) {
            if (!used.contains(str[i])) {
                used.add(str[i]);
                    swap(str, index, i);
                    generatePermutations(str, index + 1, result, false);
                    swap(str, index, i); // backtrack
                }
            }
        }
    }


    private static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static List<String> getRecursivePermutations(String input, boolean allowDuplicates) {
        List<String> result = new ArrayList<>();
        if (input == null || input.isEmpty()) {
            return result; // Handle empty input
        }
        generatePermutations(input.toCharArray(), 0, result, allowDuplicates);
        return result;
    }

    // Non-recursive approach using iterative algorithm (Heap's algorithm) with duplicate control
    public static List<String> getIterativePermutations(String input, boolean allowDuplicates) {
        List<String> result = new ArrayList<>();
        if (input == null || input.isEmpty()) return result;

        char[] arr = input.toCharArray();
        int n = arr.length;
        int[] c = new int[n]; // control array
        HashSet<String> seen = new HashSet<>();

        // Add first permutation
        String first = new String(arr);
        if (allowDuplicates || !seen.contains(first)) {
            result.add(first);
            seen.add(first);
        }

        int i = 0;
        while (i < n) {
            if (c[i] < i) {
                if (i % 2 == 0) swap(arr, 0, i);
                else swap(arr, c[i], i);

                String perm = new String(arr);
                if (allowDuplicates || !seen.contains(perm)) {
                    result.add(perm);
                    seen.add(perm);
                }

                c[i]++;
                i = 0;
            } else {
                c[i] = 0;
                i++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = scanner.nextLine();

        System.out.print("Allow duplicate permutations? (true/false): ");
        boolean allowDuplicates = scanner.nextBoolean();

        // Recursive
        long startRec = System.nanoTime();
        List<String> recursivePerms = getRecursivePermutations(input, allowDuplicates);
        long endRec = System.nanoTime();

        System.out.println("\nRecursive permutations (" + recursivePerms.size() + "):");
        System.out.println(recursivePerms);
        System.out.println("Recursive Time: " + (endRec - startRec) + " ns");

        // Iterative
        long startIter = System.nanoTime();
        List<String> iterativePerms = getIterativePermutations(input, allowDuplicates);
        long endIter = System.nanoTime();

        System.out.println("\nIterative permutations (" + iterativePerms.size() + "):");
        System.out.println(iterativePerms);
        System.out.println("Iterative Time: " + (endIter - startIter) + " ns");
    }
}
