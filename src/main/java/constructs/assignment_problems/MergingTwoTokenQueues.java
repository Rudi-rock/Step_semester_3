package constructs.assignment_problems;

import java.util.Arrays;

public class MergingTwoTokenQueues {

    public static int[] mergeSortedArrays(int[] first, int[] second) {
        int[] merged = new int[first.length + second.length];
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < first.length && j < second.length) {
            if (first[i] <= second[j]) {
                merged[k++] = first[i++];
            } else {
                merged[k++] = second[j++];
            }
        }

        while (i < first.length) {
            merged[k++] = first[i++];
        }

        while (j < second.length) {
            merged[k++] = second[j++];
        }

        return merged;
    }

    public static void main(String[] args) {

        int[] first1 = {3, 8, 15, 20};
        int[] second1 = {5, 8, 12};
        int[] result1 = mergeSortedArrays(first1, second1);
        System.out.println("Merge [3,8,15,20] and [5,8,12]  -> "
                + Arrays.toString(result1));

        int[] first2 = {};
        int[] second2 = {4, 9};
        int[] result2 = mergeSortedArrays(first2, second2);
        System.out.println("Merge [] and [4,9]              -> "
                + Arrays.toString(result2));

        int[] first3 = {1, 2, 3};
        int[] second3 = {};
        int[] result3 = mergeSortedArrays(first3, second3);
        System.out.println("Merge [1,2,3] and []            -> "
                + Arrays.toString(result3));

        int[] result4 = mergeSortedArrays(new int[]{}, new int[]{});
        System.out.println("Merge [] and []                 -> "
                + Arrays.toString(result4));

    }
}
