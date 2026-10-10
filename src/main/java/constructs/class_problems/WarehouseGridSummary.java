package constructs.class_problems;

/**
 * ============================================================
 * Class Problem 2 – Warehouse Grid Summary
 * ============================================================
 *
 * TASK:
 *   Given a 2-D grid where each cell represents the number of
 *   items in a warehouse bin, compute:
 *     1. The total number of items across all bins.
 *     2. The (row, col) coordinates of the bin with the most items.
 *        If there is a tie, pick the first one in row-major order.
 *
 * APPROACH:
 *   Single pass through every cell in row-major order.
 *   Maintain a running total and track the maximum value seen
 *   so far along with its coordinates.
 *
 * COMPLEXITY:
 *   Time  : O(m * n)  – visit every cell once.
 *   Space : O(1)      – only scalar tracking variables
 *                        (excluding the result).
 * ============================================================
 */
public class WarehouseGridSummary {

    /**
     * Computes the warehouse summary.
     *
     * @param grid 2-D integer array (m rows × n cols) of item counts.
     * @return an int array of length 3: {totalItems, maxRow, maxCol}.
     */
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

    // ==========================================
    // Runnable Example
    // ==========================================

    public static void main(String[] args) {
        int[][] grid = {
            {4,  9, 2},
            {7,  1, 6},
            {3, 12, 5}
        };

        int[] result = warehouseSummary(grid);

        System.out.println("Total items : " + result[0]);
        System.out.println("Max bin at  : (" + result[1] + ", " + result[2] + ")");
        // Expected output:
        // Total items : 49
        // Max bin at  : (2, 1)

        System.out.println("Result      : (" + result[0] + ", (" + result[1] + ", " + result[2] + "))");
        // Expected: (49, (2, 1))
    }
}
