package percolation;

/**
 * Created by alex on 08.02.15.
 */

import java.util.*;

public class Percolation {

    private final static int BLOCKED = 0;
    private final static int OPENED = 1;

    private int[][] grid;
    private WeightedQuickUnionUF alg;

    private final int virtualTop;
    private final int virtualBottom;

    /**
     * Create N-by-N grid, with all sites blocked
     */
    public Percolation(int n) {
        grid = new int[n][n];
        alg = new WeightedQuickUnionUF(n * n + 2);

        virtualTop = 0;
        virtualBottom = n * n + 1;
    }

    /**
     * Open site (row i, column j) if it is not open already
     *
     * @param i
     * @param j
     * @return
     */

    public void open(int i, int j) {
        Cell cell = new Cell(i, j);
        if (!cell.in())
            throw new IndexOutOfBoundsException("Cell is out");
        // if cell is open -> skip
        if (cell.isOpen())
            return;
        // open cell
        cell.open();
        // connect to adjacent
        for (Cell adjacent : cell.adjacents()) {
            if (adjacent.isOpen()) {
                alg.union(cell.index(), adjacent.index());
            }
        }

    }

    /**
     * Is site (row i, column j) open?
     *
     * @param i
     * @param j
     * @return
     */

    public boolean isOpen(int i, int j) {
        return new Cell(i, j).isOpen();
    }

    /**
     * Is site (row i, column j) full?
     *
     * @param i
     * @param j
     * @return
     */

    public boolean isFull(int i, int j) {
        return alg.connected(new Cell(i, j).index(), virtualTop);
    }

    // does the system percolate?
    public boolean percolates() {
        return alg.connected(virtualBottom, virtualTop);
    }

    private class Cell {
        int row;
        int col;

        Cell(int i, int j) {
            this.row = i - 1;
            this.col = j - 1;
        }

        boolean in() {
            return row >= 0 && row < grid.length && col >= 0 && col < grid.length;
        }

        boolean isVirtual() {
            return col >= 0 && col < grid.length && (row == -1 || row == grid.length);
        }

        public boolean isOpen() {
            return isVirtual() || grid[row][col] == OPENED;
        }

        public void open() {
            grid[row][col] = OPENED;
        }

        public Cell[] adjacents() {

            List<Cell> adjacentsList = new ArrayList<>(Arrays.asList(
                    new Cell[]{new Cell(row, col + 1), new Cell(row + 1, col + 2), new Cell(row + 2, col + 1), new Cell(row + 1, col)}));

            Iterator<Cell> iterator = adjacentsList.iterator();

            while (iterator.hasNext()) {
                Cell cell = iterator.next();
                if (cell.col < 0 || cell.col >= grid.length)
                    iterator.remove();
            }

            Cell cells[] = new Cell[adjacentsList.size()];
            return adjacentsList.toArray(cells);
        }


        public int index() {
            int index = grid.length * row + col + 1;
            if (index < 0)
                return virtualTop;
            else if (index > grid.length * grid.length)
                return virtualBottom;
            else return index;
        }
    }
}
