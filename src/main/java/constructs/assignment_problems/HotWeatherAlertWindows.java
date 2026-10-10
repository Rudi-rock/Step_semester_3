package constructs.assignment_problems;

/**
 * ============================================================
 * Assignment Problem 4 – Hot Weather Alert Windows
 * ============================================================
 *
 * TASK:
 *   Given temperature readings, a window size k, and a
 *   threshold, count the number of consecutive windows of
 *   size k whose average temperature >= threshold.
 *
 * APPROACH – Sliding Window:
 *   1. Compute the sum of the first k elements.
 *   2. Compare the window sum against (long) k * threshold
 *      to avoid floating-point issues and integer overflow
 *      in the multiplication.
 *   3. Slide the window by adding the next element and
 *      subtracting the element that falls off the left end.
 *   4. Repeat until the window reaches the end of the array.
 *
 *   NOTE: Recalculating every window sum from scratch would
 *   take O(n * k) time. The sliding window reuses the previous
 *   sum, reducing the cost to O(n).
 *
 * COMPLEXITY:
 *   Time  : O(n)  – single pass after the initial window sum.
 *   Space : O(1)  – only scalar variables.
 * ============================================================
 */
public class HotWeatherAlertWindows {

    /**
     * Counts windows of size k whose average >= threshold.
     *
     * @param readings   array of temperature readings.
     * @param k          window size.
     * @param threshold  the alert threshold for the average.
     * @return the number of qualifying windows.
     */
    public static int countAlertWindows(int[] readings, int k, int threshold) {
        int count = 0;

        // Compute the sum of the first window
        long windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += readings[i];
        }

        // Compare: average >= threshold  ⟺  windowSum >= k * threshold
        long target = (long) k * threshold;
        if (windowSum >= target) {
            count++;
        }

        // Slide the window across the rest of the array
        for (int i = k; i < readings.length; i++) {
            windowSum += readings[i] - readings[i - k];
            if (windowSum >= target) {
                count++;
            }
        }

        return count;
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        int[] readings = {2, 2, 2, 2, 5, 5, 5, 8};
        int k = 3;
        int threshold = 4;

        int result = countAlertWindows(readings, k, threshold);
        System.out.println("Alert windows: " + result);
        // Expected: 3
        //
        // Windows of size 3:
        //   [2, 2, 2] sum=6,  avg=2.0   < 4  ✗
        //   [2, 2, 5] sum=9,  avg=3.0   < 4  ✗
        //   [2, 5, 5] sum=12, avg=4.0   >= 4 ✓
        //   [5, 5, 5] sum=15, avg=5.0   >= 4 ✓
        //   [5, 5, 8] sum=18, avg=6.0   >= 4 ✓
        //   [5, 8, ?] – only 5 complete windows of size 3 exist
        //
        // Count = 3

        // Additional test
        int[] readings2 = {10, 10, 10};
        System.out.println("Alert windows: "
                + countAlertWindows(readings2, 2, 10));
        // Expected: 2  (both windows [10,10] have avg = 10 >= 10)
    }
}
