package constructs.assignment_problems;

public class HotWeatherAlertWindows {

    public static int countAlertWindows(int[] readings, int k, int threshold) {
        int count = 0;

        long windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += readings[i];
        }

        long target = (long) k * threshold;
        if (windowSum >= target) {
            count++;
        }

        for (int i = k; i < readings.length; i++) {
            windowSum += readings[i] - readings[i - k];
            if (windowSum >= target) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] readings = {2, 2, 2, 2, 5, 5, 5, 8};
        int k = 3;
        int threshold = 4;

        int result = countAlertWindows(readings, k, threshold);
        System.out.println("Alert windows: " + result);

        int[] readings2 = {10, 10, 10};
        System.out.println("Alert windows: "
                + countAlertWindows(readings2, 2, 10));

    }
}
