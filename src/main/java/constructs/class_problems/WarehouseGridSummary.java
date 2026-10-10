package constructs.class_problems;

public class WarehouseGridSummary {

    public static int[] warehouseSummary(int[][] grid) {
        int totalItems = 0;
        int maxValue = Integer.MIN_VALUE;
        int maxRow = 0;
        int maxCol = 0;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                totalItems += grid[r][c];

                if (grid[r][c] > maxValue) {
                    maxValue = grid[r][c];
                    maxRow = r;
                    maxCol = c;
                }
            }
        }

        return new int[]{totalItems, maxRow, maxCol};
    }

    public static void main(String[] args) {
        int[][] grid = {
            {4,  9, 2},
            {7,  1, 6},
            {3, 12, 5}
        };

        int[] result = warehouseSummary(grid);

        System.out.println("Total items : " + result[0]);
        System.out.println("Max bin at  : (" + result[1] + ", " + result[2] + ")");

        System.out.println("Result      : (" + result[0] + ", (" + result[1] + ", " + result[2] + "))");

    }
}
