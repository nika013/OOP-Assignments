import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");

	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}


	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}

		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}

	private String gridToText(int[][] grid) {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				text.append(grid[i][j]);
				if (j != grid[i].length - 1) text.append(' ');
			}
			text.append('\n');
		}
		return text.toString();
	}

	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}

	private int[][] copyGrid(int[][] grid) {
		int[][] destArray = new int[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, destArray[i], 0, grid[0].length);
		}
		return destArray;
	}

	public class Spot {

		private HashSet<Integer> possibles;
		private int row;
		private int column;
		public Spot(int i, int j) {
			row = i;
			column = j;
			possibles = new HashSet<>();
			for (int k = 1; k < 10; k++) {
				possibles.add(k);
			}
			updatePossibles();
		}

		public boolean set(int value) {
			if (value == 0) {
				currentGrid[row][column] = value;
				return true;
			}
			if (checkRow(null, value)
					&& checkSquare(null, value)
					&& checkColumn(null, value)) {
				currentGrid[row][column] = value;
				return true;
			}
			return false;
		}

		public int getPossiblesNumber() {
			return possibles.size();
		}

		public Iterator<Integer> getIterator() {
			return possibles.iterator();
		}

		private void updatePossibles() {
			checkRow(possibles, -1);
			checkColumn(possibles, -1);
			checkSquare(possibles, -1);
		}

		private boolean checkSquare(HashSet<Integer> possibles, int value) {
			int squareX = 3 * findSquare(row);
			int squareY = 3 * findSquare(column);
			for (int i = squareX; i < squareX + PART; i++) {
				for (int j = squareY; j < squareY + PART; j++) {
					int number = currentGrid[i][j];
					if (possibles != null && number != 0) {
						possibles.remove(number);
					} else if (value != -1 && value == number) {
						return false;
					}
				}
			}
			return true;
		}

		private int findSquare(int num) {
			if (num <= 2) {
				return 0;
			} else if (num <= 5) {
				return 1;
			} else {
				return 2;
			}
		}

		private boolean checkColumn(HashSet<Integer> possibles, int value) {
			for (int i = 0; i < currentGrid.length; i++) {
				int number = currentGrid[i][column];
				if (possibles != null && number != 0) {
					possibles.remove(number);
				} else if (number != -1 && number == value) {
					return false;
				}
			}
			return true;
		}

		private boolean checkRow(HashSet<Integer> possibles, int value) {
			for (int i = 0; i < currentGrid[row].length; i++) {
				int number = currentGrid[row][i];
				if (possibles != null && number != 0) {
					possibles.remove(number);
				} else if (number != -1 && number == value) {
					return false;
				}
			}
			return true;
		}
	}

	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	private int[][] originalGrid;
	private int[][] currentGrid;
	private int[][] firstSolution;
	private int solved;
	private int emptySpots;
	private long elapsedTime;
	protected ArrayList<Spot> spots;

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		originalGrid = ints;
		currentGrid = copyGrid(originalGrid);
		spots = new ArrayList<>();
		solved = 0;
		for (int i = 0; i < originalGrid.length; i++) {
			for (int j = 0; j < originalGrid[i].length; j++) {
				if (originalGrid[i][j] == 0) {
					spots.add(new Spot(i, j));
				}
			}
		}
		emptySpots = spots.size();
		Collections.sort(spots, new Comparator<Spot>() {
			public int compare(Spot s1, Spot s2) {
				return s1.getPossiblesNumber() - s2.getPossiblesNumber();
			}
		});
	}

	public Sudoku(String text) {
		this(textToGrid(text));
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		solveRec(0);
		long endTime = System.currentTimeMillis();
		elapsedTime = startTime - endTime;
		return solved;
	}
	
	private void solveRec(int spotIdx) {
		if (emptySpots == 0) {
			if (solved < 100) solved++;
			if (firstSolution == null) {
				firstSolution = copyGrid(currentGrid);
			}
		} else if (spotIdx < spots.size()) {
			Spot curSpot = spots.get(spotIdx);
			Iterator<Integer> iterator = curSpot.getIterator();
			while (iterator.hasNext()) {
				int value = iterator.next();
				if (curSpot.set(value)) {
					emptySpots--;
					solveRec(spotIdx + 1);
					/** when last iteration was successful recursion should save it
					 *  in first part. passed argument does not have meaning */
					curSpot.set(0);
					emptySpots++;
				}
			}
		}
	}
	
	public String getSolutionText() {
		return gridToText(firstSolution);
	}
	
	public long getElapsed() {
		return elapsedTime;
	}

	@Override
	public String toString() {
		return gridToText(originalGrid);
	}
}
