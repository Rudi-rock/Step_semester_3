package constructs.assignment_problems;

/**
 * ============================================================
 * Assignment Problem 5 – Ticket Price Slot Finder
 * ============================================================
 *
 * TASK:
 *   Given a sorted array of distinct ticket prices and a new
 *   price, return the index where the price exists or where it
 *   should be inserted to preserve sorted order.
 *
 * APPROACH – Binary Search:
 *   Use binary search to find the target. If found, return its
 *   index. If not found, the "low" pointer will be at the
 *   correct insertion position after the search terminates.
 *
 *   NOTE: A linear scan through the array would also work but
 *   takes O(n) time. Binary search reduces this to O(log n).
 *
 * COMPLEXITY:
 *   Time  : O(log n)  – binary search halves the range each step.
 *   Space : O(1)      – only a few index variables.
 * ============================================================
 */
public class TicketPriceSlotFinder {

    /**
     * Finds the index of the price, or the insertion index.
     *
     * @param prices   sorted array of distinct ticket prices.
     * @param newPrice the price to find or insert.
     * @return the index where newPrice exists or should be inserted.
     */
    public static int findSlot(int[] prices, int newPrice) {
        int low = 0;
        int high = prices.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (prices[mid] == newPrice) {
                return mid;
            } else if (prices[mid] < newPrice) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return low; // insertion point
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        int[] prices = {120, 150, 200, 260};

        // Example 1 – price exists
        System.out.println("findSlot(prices, 150) -> " + findSlot(prices, 150));
        // Expected: 1

        // Example 2 – insert between existing elements
        System.out.println("findSlot(prices, 210) -> " + findSlot(prices, 210));
        // Expected: 3

        // Example 3 – insert at the end
        System.out.println("findSlot(prices, 300) -> " + findSlot(prices, 300));
        // Expected: 4

        // Example 4 – insert at the beginning
        System.out.println("findSlot(prices, 100) -> " + findSlot(prices, 100));
        // Expected: 0

        // Example 5 – first element exact match
        System.out.println("findSlot(prices, 120) -> " + findSlot(prices, 120));
        // Expected: 0
    }
}
