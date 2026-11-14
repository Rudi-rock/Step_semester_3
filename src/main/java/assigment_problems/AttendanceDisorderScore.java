public class AttendanceDisorderScore {
    public long countInversions(int[] roll) {
        if (roll.length < 2) {
            return 0;
        }
        int[] temporary = new int[roll.length];
        return mergeSort(roll, temporary, 0, roll.length - 1);
    }

    private long mergeSort(int[] values, int[] temporary, int left, int right) {
        if (left >= right) {
            return 0;
        }
        int middle = left + (right - left) / 2;
        long inversions = mergeSort(values, temporary, left, middle);
        inversions += mergeSort(values, temporary, middle + 1, right);
        inversions += merge(values, temporary, left, middle, right);
        return inversions;
    }

    private long merge(int[] values, int[] temporary, int left, int middle, int right) {
        int i = left;
        int j = middle + 1;
        int position = left;
        long inversions = 0;
        while (i <= middle && j <= right) {
            if (values[i] <= values[j]) {
                temporary[position++] = values[i++];
            } else {
                temporary[position++] = values[j++];
                inversions += middle - i + 1L;
            }
        }
        while (i <= middle) {
            temporary[position++] = values[i++];
        }
        while (j <= right) {
            temporary[position++] = values[j++];
        }
        for (int index = left; index <= right; index++) {
            values[index] = temporary[index];
        }
        return inversions;
    }

    public static void main(String[] args) {
        AttendanceDisorderScore scorer = new AttendanceDisorderScore();
        System.out.println(scorer.countInversions(new int[] {2, 4, 1, 3, 5}));
        System.out.println(scorer.countInversions(new int[] {5, 4, 3, 2, 1}));
        System.out.println("Time: O(n log n), Extra space: O(n), Stable merge: Yes");
    }
}
