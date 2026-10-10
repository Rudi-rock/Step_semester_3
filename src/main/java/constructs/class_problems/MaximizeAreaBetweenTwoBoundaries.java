package constructs.class_problems;

public class MaximizeAreaBetweenTwoBoundaries {

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

            if (heights[left] < heights[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    public static void main(String[] args) {
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};

        int result = maxContainerArea(heights);
        System.out.println("maxContainerArea([1,8,6,2,5,4,8,3,7]) -> " + result);

        int[] heights2 = {1, 1};
        System.out.println("maxContainerArea([1,1]) -> "
                + maxContainerArea(heights2));

        int[] heights3 = {4, 3, 2, 1, 4};
        System.out.println("maxContainerArea([4,3,2,1,4]) -> "
                + maxContainerArea(heights3));

    }
}
