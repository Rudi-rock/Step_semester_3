package constructs.assignment_problems;

public class ClassTopperFinder {

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

        int[][] marksTie = {
            {80, 80, 80},
            {90, 70, 80},
            {60, 60, 60}
        };
        int[] resultTie = findTopper(marksTie);
        System.out.println("Tie result       : (" + resultTie[0] + ", " + resultTie[1] + ")");

    }
}
