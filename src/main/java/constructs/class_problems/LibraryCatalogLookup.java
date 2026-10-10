package constructs.class_problems;

/**
 * ============================================================
 * Class Problem 1 – Library Catalog Lookup
 * ============================================================
 *
 * TASK:
 *   Given a catalog of (ISBN, title) records sorted by ISBN in
 *   ascending order, find the title for a given target ISBN.
 *   Return "Not Found" if the ISBN does not exist.
 *
 *   ISBNs are treated as strings so that leading zeroes are
 *   preserved. Comparison uses String.compareTo().
 *
 * APPROACH – Binary Search:
 *   Because the catalog is sorted by ISBN, we can halve the
 *   search space on every iteration by comparing the middle
 *   element's ISBN with the target.
 *
 * COMPLEXITY:
 *   Time  : O(log n)  – binary search over n records.
 *   Space : O(1)      – only a few index variables.
 * ============================================================
 */
public class LibraryCatalogLookup {

    /**
     * Searches the sorted catalog for the given ISBN.
     *
     * @param catalog    2-D String array where each row is {ISBN, title},
     *                   sorted by ISBN in ascending lexicographic order.
     * @param targetIsbn the ISBN to look up (may contain leading zeroes).
     * @return the title associated with the ISBN, or "Not Found".
     */
    public static String findBook(String[][] catalog, String targetIsbn) {
        int low = 0;
        int high = catalog.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = catalog[mid][0].compareTo(targetIsbn);

            if (cmp == 0) {
                return catalog[mid][1];
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return "Not Found";
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        String[][] catalog = {
            {"0001112223", "Introduction to Algorithms"},
            {"0003334445", "Classic Mythology"},
            {"0005556667", "Data Structures Handbook"},
            {"0007778889", "Modern Operating Systems"},
            {"0012345678", "Design Patterns"}
        };

        // Example 1 – ISBN exists
        String result1 = findBook(catalog, "0003334445");
        System.out.println("Lookup \"0003334445\" -> " + result1);
        // Expected: Classic Mythology

        // Example 2 – ISBN does not exist
        String result2 = findBook(catalog, "0009998887");
        System.out.println("Lookup \"0009998887\" -> " + result2);
        // Expected: Not Found

        // Example 3 – First element
        String result3 = findBook(catalog, "0001112223");
        System.out.println("Lookup \"0001112223\" -> " + result3);
        // Expected: Introduction to Algorithms

        // Example 4 – Last element
        String result4 = findBook(catalog, "0012345678");
        System.out.println("Lookup \"0012345678\" -> " + result4);
        // Expected: Design Patterns
    }
}
