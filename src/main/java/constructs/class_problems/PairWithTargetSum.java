package constructs.class_problems;

/**
 * ============================================================
 * Class Problem 3 – Pair With Target Sum (Brute Force)
 * ============================================================
 *
 * TASK:
 *   Determine whether two distinct elements in an unsorted
 *   integer array sum to a given target. The array may contain
 *   negative numbers and duplicates.
 *
 * ────────────────────────────────────────────────────────────
 * APPROACH DISCUSSION
 * ────────────────────────────────────────────────────────────
 *
 * Approach 1 – Brute Force (implemented here):
 *   Check every pair (i, j) where i < j and see if
 *   nums[i] + nums[j] == target.
 *
 *   Time  : O(n²) – two nested loops.
 *   Space : O(1)  – no extra data structures.
 *
 * Approach 2 – HashSet Complement Lookup:
 *   Iterate through the array. For each element, check whether
 *   (target - element) already exists in a HashSet. If yes,
 *   return true. Otherwise, add the current element to the set.
 *
 *   Time  : O(n) – single pass with O(1) average set operations.
 *   Space : O(n) – for the HashSet.
 *
 * Approach 3 – Sort + Two Pointers:
 *   Sort the array, then use two pointers (one at the start,
 *   one at the end) to find a pair.
 *
 *   Time  : O(n log n) – dominated by sorting.
 *   Space : O(1) additional (if sorting in-place).
 *
 * ============================================================
 */
public class PairWithTargetSum {

    /**
     * Brute-force check for a pair summing to the target.
     *
     * @param nums   unsorted integer array (may have negatives / duplicates).
     * @param target the desired sum.
     * @return true if two distinct elements sum to target; false otherwise.
     */
    public static boolean hasPairWithSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        // Example 1
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        System.out.println("hasPairWithSum([2,7,11,15], 9) -> "
                + hasPairWithSum(nums1, target1));
        // Expected: true

        // Example 2
        int[] nums2 = {3, 4, 6};
        int target2 = 20;
        System.out.println("hasPairWithSum([3,4,6], 20)    -> "
                + hasPairWithSum(nums2, target2));
        // Expected: false

        // Example 3 – negative numbers
        int[] nums3 = {-5, 2, 3, 8};
        int target3 = -3;
        System.out.println("hasPairWithSum([-5,2,3,8], -3) -> "
                + hasPairWithSum(nums3, target3));
        // Expected: true  (-5 + 2 = -3)

        // Example 4 – duplicates
        int[] nums4 = {5, 5, 10};
        int target4 = 10;
        System.out.println("hasPairWithSum([5,5,10], 10)   -> "
                + hasPairWithSum(nums4, target4));
        // Expected: true  (5 + 5 = 10)
    }
}
