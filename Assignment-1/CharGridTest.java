
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;
import org.junit.Test;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	public void testCharAreaOneChar() {
		char[][] grid = new char[][] {
				{'a'}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.charArea('a'));
		assertEquals(0, cg.charArea('c'));
		assertEquals(0, cg.charArea(' '));
	}

	public void testCharAreaRow() {
		char[][] grid = new char[][] {
				{'a', 'b', 'c', 'a', 'z', 'z'}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('c'));
		assertEquals(0, cg.charArea(' '));
		assertEquals(2, cg.charArea('z'));
	}

	public void testCharAreaColumn() {
		char[][] grid = new char[][] {
				{'c'},
				{'z'},
				{'m'},
				{'n'},
				{'c'},
				{'m'},
				{'k'},
				{'z'},
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(0, cg.charArea('a'));
		assertEquals(5, cg.charArea('c'));
		assertEquals(0, cg.charArea(' '));
		assertEquals(7, cg.charArea('z'));
		assertEquals(4, cg.charArea('m'));
		assertEquals(1, cg.charArea('k'));
	}

	public void testCharAreaSquare() {
		char[][] grid = new char[][] {
				{'a', 'b', 'c', 'm'},
				{' ', 'c', 'm', 'c'},
				{'k', 'b', 'k', 'a'},
				{'z', 'z', ' ', ' '},
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(12, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(4, cg.charArea('m'));
		assertEquals(2, cg.charArea('z'));
		assertEquals(3, cg.charArea('k'));
		assertEquals(12, cg.charArea(' '));
	}

	@Test
	public void testPlusesOnZero() {
		char[][] grid = new char[][] {
				{'a'}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(0, cg.countPlus());

		grid = new char[][] {
				{'a', 'a', 'a', 'b', 'c', 'd', ' ', 'm'}
		};
		cg = new CharGrid(grid);

		assertEquals(0, cg.countPlus());

		grid = new char[][] {
				{'a'},
				{'b'},
				{'b'},
				{'b'}
		};
		cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}

	public void testPluses1() {
		char[][] grid = new char[][] {
				{'a', 'b', 'b', 'c', ' ', 'x'},
				{'b', 'b', 'b', 'b', ' ', 'l'},
				{' ', 'b', 'b', 'c', 'm', 'x'},
				{'a', 'a', ' ', 'b', 'b', ' '},
				{'l', ' ', ' ', ' ', 'm', 'a'},
				{'c', 'b', ' ', 'b', ' ', 'c'},

		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.countPlus());
	}
	@Test
	public void testPluses2() {
		char[][] grid = new char[][] {
				{'a', 'b', 'b', 'c', ' ', 'x'},
				{' ', 'x', 'b', 'b', ' ', 'l'},
				{'b', 'b', 'b', 'b', 'b', 'x'},
				{'a', 'a', 'b', 'b', 'y', ' '},
				{'l', 'c', 'b', 'y', 'y', 'y'},
				{'c', 'c', 'c', 'b', 'y', 'c'},
				{'l', 'c', 'x', 'a', 'y', 'm'},
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(2, cg.countPlus());
	}
	
}
