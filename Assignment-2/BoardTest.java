import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		b.place(pyr1, 0, 0);
		b.commit();
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Makre  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	public void testSimple() {
		Board b = new Board(10, 20);
		assertEquals(10, b.getWidth());
		assertEquals(20, b.getHeight());
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(4));
		assertEquals(0, b.getColumnHeight(3));
		assertTrue(b.getGrid(-1, 100));
	}

	public void testPieceSimple() {
		Board b = new Board(10, 9);
		Piece[] pieces = Piece.getPieces();

		assertEquals(Board.PLACE_OK, b.place(pieces[Piece.L1], 0, 0));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(3, b.getMaxHeight());
		b.commit();

		assertEquals(Board.PLACE_OK, b.place(pieces[Piece.L2], 8, 0));
		b.commit();
		assertEquals(4, b.getRowWidth(0));
		assertEquals(3, b.getMaxHeight());

		assertEquals(Board.PLACE_OK, b.place(pieces[Piece.S1], 6,0));
		b.commit();
		assertEquals(4, b.getRowWidth(1));
		assertEquals(6, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(3, b.getMaxHeight());

		assertEquals(Board.PLACE_ROW_FILLED, b.place(pieces[Piece.STICK].fastRotation(), 2,0));
		b.commit();
		assertEquals(b.getWidth(), b.getRowWidth(0));
	}

	public void testBadPlaces() {
		Board board = new Board(10, 24);
		Piece[] pieces = Piece.getPieces();

		assertEquals(Board.PLACE_OK, board.place(pieces[Piece.S1], 0, 0));
		board.commit();
		assertEquals(Board.PLACE_BAD, board.place(pieces[Piece.STICK], 1, 1));
		board.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, board.place(pieces[Piece.PYRAMID], 10, 3));
	}

	public void testClearSimple() {
		Board board = new Board(10, 20);
		Piece[] pieces = Piece.getPieces();

		Piece toRightPyr = pieces[Piece.PYRAMID].fastRotation().fastRotation().fastRotation();
		board.place(pieces[Piece.L1], 0, 0);
		board.commit();
		board.place(pieces[Piece.SQUARE], 2, 0);
		board.commit();
		board.place(toRightPyr, 1, 1);
		board.commit();
		board.place(pieces[Piece.STICK].fastRotation(), 6, 0);
		board.commit();
		board.place(pieces[Piece.S1], 4, 0);
		board.commit();

		assertEquals(1, board.clearRows());
		board.commit();
		assertEquals(3, board.getMaxHeight());
		assertEquals(6, board.getRowWidth(0));
		assertEquals(3, board.getColumnHeight(1));
		assertEquals(0, board.getColumnHeight(4));

		board.place(pieces[Piece.S2].fastRotation(), 4, 0);
		board.commit();
		board.place(pieces[Piece.PYRAMID], 7, 0);
		board.commit();

		assertEquals(1, board.clearRows());
		board.commit();
		assertEquals(2, board.getMaxHeight());
		assertEquals(6, board.getRowWidth(0));
		assertEquals(2, board.getColumnHeight(5));
		assertEquals(0, board.getColumnHeight(7));
	}

	public void testDoubleClear() {
		Board board = new Board(10, 20);
		Piece[] pieces = Piece.getPieces();

		// first row filled almost
		board.place(pieces[Piece.SQUARE], 0, 0);
		board.commit();

		board.place(pieces[Piece.SQUARE], 2, 0);
		board.commit();

		board.place(pieces[Piece.PYRAMID], 4, 0);
		board.commit();

		board.place(pieces[Piece.L2], 7, 0);
		board.commit();

		// fill third row almost
		board.place(pieces[Piece.PYRAMID], 0, 2);
		board.commit();

		board.place(pieces[Piece.STICK].fastRotation(), 3, 2);
		board.commit();

		board.place(pieces[Piece.S2].fastRotation(), 7, 2);
		board.commit();

		board.place(pieces[Piece.SQUARE], 2, 3);
		board.commit();

		assertEquals(3, board.getColumnHeight(6));

		// final fill of two not adjacent rows with a stick
		board.place(pieces[Piece.STICK], 9, 0);
		board.commit();

		assertEquals(5, board.getMaxHeight());
		assertEquals(2, board.clearRows());

		assertEquals(3, board.getMaxHeight());
		assertEquals(7, board.getRowWidth(0));
		assertEquals(6, board.getRowWidth(1));
		assertEquals(3, board.getRowWidth(2));

		assertEquals(0, board.getColumnHeight(4));
		assertEquals(0, board.getColumnHeight(6));
		assertEquals(3, board.getColumnHeight(3));
		assertEquals(2, board.getColumnHeight(7));

	}

	public void testDropHeight() {
		Board board = new Board(10, 10);
		Piece[] pieces = Piece.getPieces();

		assertEquals(0, board.dropHeight(pieces[Piece.PYRAMID], 6));
		board.place(pieces[Piece.PYRAMID], 6, 0);
		board.commit();

		assertEquals(2, board.dropHeight(pieces[Piece.SQUARE], 6));
		board.place(pieces[Piece.SQUARE], 6, 2);
		board.commit();

		assertEquals(3, board.dropHeight(pieces[Piece.PYRAMID]
				.fastRotation()
				.fastRotation()
				.fastRotation(), 5));

		board.place(pieces[Piece.PYRAMID]
				.fastRotation()
				.fastRotation()
				.fastRotation(), 5, 3);
		board.commit();

	}

	public void testUndo() {
		Board b = new Board(10, 15);
		Piece[] pieces = Piece.getPieces();
		String initialGrid = b.toString();

		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(pieces[Piece.L1], -1, 0));
		assertTrue(b.toString().equals(initialGrid));
		b.undo();
		assertTrue(b.toString().equals(initialGrid));

		b.place(pieces[Piece.PYRAMID], 0, 0);
		b.commit();

		b.place(pieces[Piece.STICK].fastRotation(), 2, 0);
		b.commit();

		initialGrid = b.toString();
		assertEquals(Board.PLACE_BAD, b.place(pieces[Piece.PYRAMID], 5, 0));
		assertTrue(b.toString().equals(initialGrid));
		b.undo();
		assertTrue(b.toString().equals(initialGrid));

	}
	public void testSanity() throws Exception {
		this.setUp();
		assertThrows(RuntimeException.class, () -> {
			b.widths = new int[b.height];
			b.sanityCheck();
		});

		assertThrows(RuntimeException.class, () -> {
			b.heights = new int[b.width];
			b.sanityCheck();
		});

		assertThrows(RuntimeException.class, () -> {
			b.maxHeight = -1;
			b.sanityCheck();
		});
	}
	
}
