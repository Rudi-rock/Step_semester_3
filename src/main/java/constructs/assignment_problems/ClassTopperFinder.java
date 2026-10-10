package constructs.assignment_problems;

/**
 * ============================================================
 * Assignment Problem 1 – Class Topper Finder
 * ============================================================
 *
 * TASK:
 *   Given a 2-D array where each row represents a student and
 *   each column represents subject marks, find the student
 *   (row index) with the highest total marks.
 *
 *   If multiple students share the same highest total, select
 *   the one with the smallest row index.
 *
 * APPROACH:
 *   Iterate through every cell in row-major order, accumulating
 *   each student's total. Track the best total and its row
 *   index; update only when a strictly greater total is found,
 *   which naturally preserves the smallest index on ties.
 *
 * COMPLEXITY:
 *   Time  : O(m * n)  – m students, n subjects.
 *   Space : O(1)      – only scalar tracking variables.
 * ============================================================
 */
public class ClassTopperFinder {

    /**
     * Finds the student with the highest total marks.
     *
     * @param marks 2-D array where marks[i][j] is the mark of
     *              student i in subject j.
     * @return an int array {topperRowIndex, topperTotalMarks}.
     */
    public static int[] findTopper(int[][] marks) {
        int topperIndex = 0;
        int topperTotal = Integer.MIN_VALUE;

        for (int i = 0; i < marks.length; i++) {
            int total = 0;
            for (int j = 0; j < marks[i].length; j++) {
                total += marks[i][j];
            }

            if (total > topperTotal) {
                topperTotal = total;
                topperIndex = i;
            }
        }

        return new int[]{topperIndex, topperTotal};
    }

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        int[][] marks = {
            {78, 85, 90},
            {88, 92, 79},
            {65, 70, 95}
        };

        int[] result = findTopper(marks);

        System.out.println("Topper row index : " + result[0]);
        System.out.println("Topper total     : " + result[1]);
        System.out.println("Result           : (" + result[0] + ", " + result[1] + ")");
        // Expected output: (1, 259)
        // Student 0 total = 253, Student 1 total = 259, Student 2 total = 230

        // Tie-breaking example
        int[][] marksTie = {
            {80, 80, 80},  // total = 240
            {90, 70, 80},  // total = 240
            {60, 60, 60}   // total = 180
        };
        int[] resultTie = findTopper(marksTie);
        System.out.println("Tie result       : (" + resultTie[0] + ", " + resultTie[1] + ")");
        // Expected: (0, 240) – smallest row index wins on tie
    }
}
