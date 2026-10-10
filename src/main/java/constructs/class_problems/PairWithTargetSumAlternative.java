package constructs.class_problems;

import java.util.HashSet;

public class PairWithTargetSumAlternative {

    public static boolean hasPairWithSum(int[] nums, int target) {
        HashSet<Integer> seen = new HashSet<>();

        for (int num : nums) {
            int complement = target - num;
            if (seen.contains(complement)) {
                return true;
            }
            seen.add(num);
        }

        return false;
    }

    public static void main(String[] args) {

        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        System.out.println("hasPairWithSum([2,7,11,15], 9) -> "
                + hasPairWithSum(nums1, target1));

        int[] nums2 = {3, 4, 6};
        int target2 = 20;
        System.out.println("hasPairWithSum([3,4,6], 20)    -> "
                + hasPairWithSum(nums2, target2));

        int[] nums3 = {-5, 2, 3, 8};
        int target3 = -3;
        System.out.println("hasPairWithSum([-5,2,3,8], -3) -> "
                + hasPairWithSum(nums3, target3));

        int[] nums4 = {5, 5, 10};
        int target4 = 10;
        System.out.println("hasPairWithSum([5,5,10], 10)   -> "
                + hasPairWithSum(nums4, target4));

    }
}
