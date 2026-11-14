public class NoticeBoardShuffle {
    public int[] bubbleStats(int[] scores) {
        int passes = 0;
        int swaps = 0;
        for (int end = scores.length - 1; end > 0; end--) {
            passes++;
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                if (scores[i] > scores[i + 1]) {
                    int temporary = scores[i];
                    scores[i] = scores[i + 1];
                    scores[i + 1] = temporary;
                    swaps++;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return new int[] {passes, swaps};
    }

    private static void print(int[] values) {
        System.out.print("[");
        for (int i = 0; i < values.length; i++) {
            System.out.print(values[i] + (i == values.length - 1 ? "" : ", "));
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        NoticeBoardShuffle sorter = new NoticeBoardShuffle();
        int[] first = {5, 1, 4, 2, 8};
        int[] firstStats = sorter.bubbleStats(first);
        print(first);
        System.out.println("passes = " + firstStats[0] + ", swaps = " + firstStats[1]);
        int[] second = {1, 2, 3, 4};
        int[] secondStats = sorter.bubbleStats(second);
        print(second);
        System.out.println("passes = " + secondStats[0] + ", swaps = " + secondStats[1]);
        System.out.println("Best: O(n), Average: O(n^2), Worst: O(n^2), Extra space: O(1), Stable: Yes");
    }
}
