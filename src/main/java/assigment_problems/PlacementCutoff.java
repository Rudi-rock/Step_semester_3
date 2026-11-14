public class PlacementCutoff {
    public int kthHighest(int[] marks, int k) {
        int target = marks.length - k;
        int left = 0;
        int right = marks.length - 1;
        while (left <= right) {
            int pivotIndex = partition(marks, left, right);
            if (pivotIndex == target) {
                return marks[pivotIndex];
            }
            if (pivotIndex < target) {
                left = pivotIndex + 1;
            } else {
                right = pivotIndex - 1;
            }
        }
        throw new IllegalArgumentException("k must be between 1 and the number of marks");
    }

    private int partition(int[] values, int left, int right) {
        int pivot = values[right];
        int smallerEnd = left;
        for (int index = left; index < right; index++) {
            if (values[index] <= pivot) {
                swap(values, smallerEnd, index);
                smallerEnd++;
            }
        }
        swap(values, smallerEnd, right);
        return smallerEnd;
    }

    private void swap(int[] values, int first, int second) {
        int temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }

    public static void main(String[] args) {
        PlacementCutoff selector = new PlacementCutoff();
        System.out.println(selector.kthHighest(new int[] {72, 95, 60, 88, 79}, 2));
        System.out.println(selector.kthHighest(new int[] {50, 90, 90, 70}, 2));
        System.out.println("Average: O(n), Worst: O(n^2), Extra space: O(1), Stability: Not applicable");
    }
}
