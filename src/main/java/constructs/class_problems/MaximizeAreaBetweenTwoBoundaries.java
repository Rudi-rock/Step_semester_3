package constructs.class_problems;

/**
 * ============================================================
 * Class Problem 5 – Maximize Area Between Two Boundaries
 * ============================================================
 *
 * TASK:
 *   Given an array of heights, find the maximum water container
 *   area formed between two lines.
 *
 *   Area = min(heights[i], heights[j]) * (j - i)
 *
 * ────────────────────────────────────────────────────────────
 * APPROACH DISCUSSION
 * ────────────────────────────────────────────────────────────
 *
 * Approach 1 – Brute Force:
 *   Try every pair (i, j) and compute the area. Keep track
 *   of the maximum.
 *
 *   Time  : O(n²)
 *   Space : O(1)
 *
 * Approach 2 – Two Pointers (implemented here):
 *   Place a left pointer at the start and a right pointer at
 *   the end. Compute the area. Move the pointer pointing to
 *   the shorter line inward, because moving the taller one
 *   can never increase the area (the width shrinks and the
 *   height is still limited by the shorter line).
 *
 *   Time  : O(n)  – each pointer moves at most n steps total.
 *   Space : O(1)  – only a few variables.
 *
 * ============================================================
 */
public class MaximizeAreaBetweenTwoBoundaries {

    /**
     * Returns the maximum container area.
     *
     * @param heights array of non-negative integer heights.
     * @return maximum area between any two lines.
     */
    public static int maxContainerArea(int[] heights) {
        int left = 0;
        int right = heights.length - 1;
        int maxArea = 0;

        while (left < right) {
            int width = right - left;
            int height = Math.min(heights[left], heights[right]);
            int area = height * width;

            if (area > maxArea) {
                maxArea = area;
            }

            // Move the pointer at the shorter line inward.
            if (heights[left] < heights[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};

        int result = maxContainerArea(heights);
        System.out.println("maxContainerArea([1,8,6,2,5,4,8,3,7]) -> " + result);
        // Expected: 49
        // Explanation: Between index 1 (height 8) and index 8 (height 7):
        //   area = min(8, 7) * (8 - 1) = 7 * 7 = 49

        // Additional test
        int[] heights2 = {1, 1};
        System.out.println("maxContainerArea([1,1]) -> "
                + maxContainerArea(heights2));
        // Expected: 1

        int[] heights3 = {4, 3, 2, 1, 4};
        System.out.println("maxContainerArea([4,3,2,1,4]) -> "
                + maxContainerArea(heights3));
        // Expected: 16  -> min(4,4) * (4-0) = 16
    }
}
