// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	protected int width;
	protected int height;
	protected int maxHeight;
	private int backUpMaxHeight;
	private boolean[][] grid;
	private boolean[][] backUpGrid;
	private boolean DEBUG = true;
	boolean committed;
	protected int[] widths;
	protected int[] heights;
	private int[] backUpWidths;
	private int[] backUpHeights;

	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		backUpGrid = new boolean[width][height];
		committed = true;
		
		widths = new int[height];
		backUpWidths = new int[height];
		heights = new int[width];
		backUpHeights = new int[width];

		maxHeight = 0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			checkHeight();
			checkWidth();
		}
	}

	private void checkWidth() {
		for (int i = 0; i < height; i++) {
			int curW = 0;
			for (int j = 0; j < width; j++) {
				if (grid[j][i]) {
					curW++;
				}
			}
			if (curW != widths[i]) {
				throw new RuntimeException("some problem in widths");
			}
		}
	}

	private void checkHeight() {
		boolean throwException = false;
		int maxH = 0;
		int error = 0;
		for (int i = 0; i < width; i++) {
			int curH = 0;
			for (int j = 0; j < height; j++) {
				if (grid[i][j]) {
					curH = j + 1;
				}
			}
			// check heights
			if (curH != heights[i]) {
				throwException = true;
			}
			// for max height check
			if (curH > maxH) {
				maxH = curH;
			}
		}
		if (maxHeight != maxH) {
			throwException = true;
		}

		if (throwException) {
			System.out.println(error);
			throw new RuntimeException("some error in heights");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int y = 0;
		for (int i = 0; i < skirt.length; i++) {
			int deltaH = heights[x+i]  - skirt[i];
			int newY = deltaH < 0 ? 0 : deltaH;
			if (newY > y) {
				y = newY;
			}
		}
		return y;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x >= width || x < 0 || y >= height || y < 0) {
			return true;
		}
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		int result = PLACE_OK;
		
		storeState();
		TPoint[] points = piece.getBody();

		for (int i = 0; i < points.length; i++) {
			TPoint curP = points[i];
			int realX = x + curP.x, realY = y + curP.y;
			int pointState = checkPoint(realX, realY);

			if (pointState == PLACE_OK) {
				grid[realX][realY] = true;

				if (++widths[realY] == width) {
					result = PLACE_ROW_FILLED;
				}

				if (heights[realX] < realY + 1) heights[realX] = realY + 1;

			} else {
				result = pointState;
				break;
			}
		}

		updateMaxHeight();
		if (result == PLACE_OK) {
			sanityCheck();
		}
		return result;
	}

	private void updateMaxHeight() {
		int curMaxH = 0;
		for (int i = 0; i < width; i++) {
			if (heights[i] > curMaxH) {
				curMaxH = heights[i];
			}
		}
		maxHeight = curMaxH;
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		// YOUR CODE HERE
		committed = false;
		storeState();

		for (int i = 0; i < maxHeight; i++) {
			if (widths[i] == width) {
				rowsCleared += copyDown(i);
				break;
			}
		}

		updateHeightsArray();
		updateMaxHeight();
		sanityCheck();
		return rowsCleared;
	}

	private void updateHeightsArray() {
		int[] newHeights = new int[width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (grid[i][j]) {
					newHeights[i] = j + 1;
				}
			}
		}
		heights = newHeights;
	}

	private int copyDown(int to) {
		int cleared = 1;
		for (int from = to + 1; from < maxHeight; from++) {
			if (widths[from] != width) {
				arrayCopy(from, to);
				widths[to] = widths[from];
				widths[from] = 0;
				if (to + 1 < maxHeight) to++;
			} else {
				cleared++;
				arrayClear(from);
			}
		}
		return cleared;
	}

	private void arrayClear(int from) {
		for (int i = 0; i < width; i++) {
			grid[i][from] = false;
		}
		widths[from] = 0;
	}


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		// YOUR CODE HERE
		if (!committed) {
			committed = true;

			boolean[][] tempGrid = grid;
			grid = backUpGrid;
			backUpGrid = tempGrid;

			int[] tempW = widths;
			widths = backUpWidths;
			backUpWidths = tempW;

			int[] tempH = heights;
			heights = backUpHeights;
			backUpHeights = tempH;

			maxHeight = backUpMaxHeight;

			sanityCheck();
		}
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}

	private void arrayCopy(int from, int to) {
		for (int i = 0; i < width; i++) {
			grid[i][to] = grid[i][from];
			grid[i][from] = false;
		}
	}

	private void storeState() {
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, backUpGrid[i], 0, grid[0].length);
		}
		System.arraycopy(widths, 0, backUpWidths, 0, widths.length);
		System.arraycopy(heights, 0, backUpHeights, 0, heights.length);
		backUpMaxHeight = maxHeight;
	}

	private int checkPoint(int x, int y) {
		if (x >= width || x < 0 || y >= height || y < 0) {
			return PLACE_OUT_BOUNDS;
		}

		if (grid[x][y]) {
			return PLACE_BAD;
		}

		return PLACE_OK;
	}
	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


