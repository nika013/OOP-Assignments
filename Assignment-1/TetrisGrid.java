//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

import java.util.*;

public class TetrisGrid {

	private boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		ArrayList<Integer> rowsToRemove = new ArrayList<>();
		HashMap<Integer, Integer> fullRows = new HashMap<>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j]) {
					countRowValue(fullRows, j);
				}
			}
		}
		// Determine which rows are filled
		fullRows.forEach((key, value) -> {
			if (value == grid.length) {
				rowsToRemove.add(key);
			}
		});
		Collections.reverse(rowsToRemove); // sort reversely to remove rows from the end
		removeRows(rowsToRemove);
	}

	/**
	 *  This method increases count of true values in hashmap, this number lated needed
	 *  to determine fullness of the row
	 *  received parameters: j - current row, HashMap: key - row, value - true values
	 */
	private void countRowValue(HashMap<Integer, Integer> fullRows, int j) {
		if (fullRows.containsKey(j)) {
			int rows = fullRows.get(j);
			rows++;
			fullRows.put(j, rows);
		} else {
			Integer put = fullRows.put(j, 1);
		}
	}

	/**
	 * This method removes filled rows in the grid
	 * Recieved parameters: ArrayList of filled rows in descending way
	 */
	private void removeRows(ArrayList<Integer> rowsToRemove) {
		for (int col: rowsToRemove) {
			for (int i = 0; i < grid.length; i++) {
				for (int j = col; j < grid[i].length-1; j++) {
					grid[i][j] = grid[i][j+1];
				}
				grid[i][grid[i].length-1] = false;
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid; // YOUR CODE HERE
	}
}
