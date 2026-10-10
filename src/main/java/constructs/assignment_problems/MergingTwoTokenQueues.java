package constructs.assignment_problems;

import java.util.Arrays;

/**
 * ============================================================
 * Assignment Problem 2 – Merging Two Token Queues
 * ============================================================
 *
 * TASK:
 *   Merge two already-sorted integer arrays into one sorted
 *   array. Preserve duplicate elements. Either input may be
 *   empty.
 *
 * APPROACH – Two Pointers:
 *   Maintain one pointer per input array. At each step,
 *   compare the elements at the two pointers and append the
 *   smaller one to the result. After one array is exhausted,
 *   copy the remainder of the other.
 *
 *   This is the merge step of merge-sort.
 *
 *   NOTE: We do NOT concatenate and re-sort – that would be
 *   O((m+n) log(m+n)), which is slower.
 *
 * COMPLEXITY:
 *   Time  : O(m + n)  – each element is visited exactly once.
 *   Space : O(m + n)  – for the returned merged array.
 * ============================================================
 */
public class MergingTwoTokenQueues {

    /**
     * Merges two sorted arrays into a single sorted array.
     *
     * @param first  sorted integer array (may be empty).
     * @param second sorted integer array (may be empty).
     * @return a new sorted array containing all elements from both inputs.
     */
    public static int[] mergeSortedArrays(int[] first, int[] second) {
        int[] merged = new int[first.length + second.length];
        int i = 0; // pointer for first
        int j = 0; // pointer for second
        int k = 0; // pointer for merged

        while (i < first.length && j < second.length) {
            if (first[i] <= second[j]) {
                merged[k++] = first[i++];
            } else {
                merged[k++] = second[j++];
            }
        }

        // Copy remaining elements from first
        while (i < first.length) {
            merged[k++] = first[i++];
        }

        // Copy remaining elements from second
        while (j < second.length) {
            merged[k++] = second[j++];
        }

        return merged;
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        // Example 1
        int[] first1 = {3, 8, 15, 20};
        int[] second1 = {5, 8, 12};
        int[] result1 = mergeSortedArrays(first1, second1);
        System.out.println("Merge [3,8,15,20] and [5,8,12]  -> "
                + Arrays.toString(result1));
        // Expected: [3, 5, 8, 8, 12, 15, 20]

        // Example 2 – first array is empty
        int[] first2 = {};
        int[] second2 = {4, 9};
        int[] result2 = mergeSortedArrays(first2, second2);
        System.out.println("Merge [] and [4,9]              -> "
                + Arrays.toString(result2));
        // Expected: [4, 9]

        // Example 3 – second array is empty
        int[] first3 = {1, 2, 3};
        int[] second3 = {};
        int[] result3 = mergeSortedArrays(first3, second3);
        System.out.println("Merge [1,2,3] and []            -> "
                + Arrays.toString(result3));
        // Expected: [1, 2, 3]

        // Example 4 – both empty
        int[] result4 = mergeSortedArrays(new int[]{}, new int[]{});
        System.out.println("Merge [] and []                 -> "
                + Arrays.toString(result4));
        // Expected: []
    }
}
