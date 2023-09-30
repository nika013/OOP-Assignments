// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int top = Integer.MAX_VALUE, left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE, bottom = Integer.MIN_VALUE;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (ch == grid[i][j]) {
					if (i < top) top = i;
					if (j < left) left = j;
					if (j > right) right = j;
					if (i > bottom) bottom = i;
				}
			}
		}
		int area = getArea(top, left, right, bottom);
		return area;
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int pluses = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				int up = countArm(grid[i][j], i, j, -1, 0);
				int down = countArm(grid[i][j], i, j, 1, 0);
				int left = countArm(grid[i][j], i, j, 0, -1);
				int right = countArm(grid[i][j], i, j, 0, 1);
				if (up == down && left == right && up == left && up != 0) {
					pluses++;
				}
			}
		}
		return pluses;
	}

	private boolean isInBorders(int i, int j) {
		return i >= 0 && i < grid.length && j >= 0 && j < grid[i].length;
	}

	private int countArm(char ch, int i, int j, int vert, int hor) {
		int countChars = 0;
		i += vert;
		j += hor;
		while (isInBorders(i, j)) {
			if (grid[i][j] == ch) {
				countChars++;
			} else {
				break;
			}
			i += vert;
			j += hor;
		}
		return countChars;
	}

	private int getArea(int top, int left, int right, int bottom) {
		if (top == Integer.MAX_VALUE || left == Integer.MAX_VALUE) return 0;
		return (right - left + 1) * (bottom - top + 1);
	}
}
