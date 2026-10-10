package constructs.assignment_problems;

import java.util.HashMap;

/**
 * ============================================================
 * Assignment Problem 3 – Most Popular Canteen Order
 * ============================================================
 *
 * TASK:
 *   Find the most frequent item in a list and return the item
 *   and its frequency.
 *
 *   Tie-breaking: if multiple items share the highest frequency,
 *   select the item whose first occurrence appears earliest in
 *   the original input.
 *
 * APPROACH:
 *   1. Build a HashMap counting the frequency of each item
 *      in a single pass — O(n).
 *   2. Traverse the original list again. For each item, look
 *      up its frequency in the map. Update the winner only
 *      when a strictly greater frequency is found. This
 *      guarantees the earliest-first-occurrence item wins on
 *      ties.
 *
 *   NOTE: A naïve approach that counts the frequency for every
 *   item by scanning the entire list each time would run in
 *   O(n²) time. Using a HashMap reduces this to O(n).
 *
 * COMPLEXITY:
 *   Expected time : O(n)   – two passes over the list.
 *   Space         : O(u)   – where u is the number of unique items.
 * ============================================================
 */
public class MostPopularCanteenOrder {

    /**
     * Finds the most popular order.
     *
     * @param orders array of order names.
     * @return a String array {mostPopularItem, frequency}.
     */
    public static String[] findMostPopular(String[] orders) {
        // Step 1: Count frequencies
        HashMap<String, Integer> freqMap = new HashMap<>();
        for (String order : orders) {
            freqMap.put(order, freqMap.getOrDefault(order, 0) + 1);
        }

        // Step 2: Traverse original list to find the winner
        String bestItem = orders[0];
        int bestFreq = freqMap.get(orders[0]);

        for (String order : orders) {
            int freq = freqMap.get(order);
            if (freq > bestFreq) {
                bestFreq = freq;
                bestItem = order;
            }
        }

        return new String[]{bestItem, String.valueOf(bestFreq)};
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        // Example 1
        String[] orders1 = {
            "dosa", "idli", "vada", "dosa",
            "idli", "dosa", "tea"
        };
        String[] result1 = findMostPopular(orders1);
        System.out.println("Most popular: (\"" + result1[0] + "\", " + result1[1] + ")");
        // Expected: ("dosa", 3)

        // Example 2 – tie-breaking by first occurrence
        String[] orders2 = {"tea", "coffee", "coffee", "tea"};
        String[] result2 = findMostPopular(orders2);
        System.out.println("Most popular: (\"" + result2[0] + "\", " + result2[1] + ")");
        // Expected: ("tea", 2)
        // "tea" and "coffee" both have frequency 2, but "tea"
        // appears first in the original list.
    }
}
