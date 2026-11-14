public class ContestLeaderboard {
    public static class Entry {
        private final String studentName;
        private final int marks;

        public Entry(String studentName, int marks) {
            this.studentName = studentName;
            this.marks = marks;
        }

        @Override
        public String toString() {
            return studentName + " " + marks;
        }
    }

    public Entry[] leaderboard(Entry[] entries) {
        int[] counts = new int[101];
        for (Entry entry : entries) {
            counts[100 - entry.marks]++;
        }
        for (int i = 1; i < counts.length; i++) {
            counts[i] += counts[i - 1];
        }
        Entry[] result = new Entry[entries.length];
        for (int i = entries.length - 1; i >= 0; i--) {
            Entry entry = entries[i];
            int key = 100 - entry.marks;
            result[--counts[key]] = entry;
        }
        return result;
    }

    private static void print(Entry[] entries) {
        for (int i = 0; i < entries.length; i++) {
            System.out.print(entries[i] + (i == entries.length - 1 ? "" : ", "));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Entry[] entries = {
            new Entry("Asha", 90), new Entry("Ravi", 75), new Entry("Neha", 90),
            new Entry("Kiran", 100), new Entry("Meera", 75)
        };
        ContestLeaderboard board = new ContestLeaderboard();
        print(board.leaderboard(entries));
        System.out.println("Time: O(n + 101), Extra space: O(n + 101), Stable: Yes");
    }
}
