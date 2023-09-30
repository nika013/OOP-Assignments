import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	@Test
	public void testSampleSize() throws Exception {
		// Check size of pyr piece
		this.setUp();
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}

	@Test
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() throws Exception {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		this.setUp();
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	@Test
	public void testConstructorSimple() {
		Piece p = new Piece(Piece.STICK_STR);
		assertEquals(4, p.getHeight());
		assertEquals(1, p.getWidth());
		int[] expectedSkirt = {0};
		int[] actualSkirt = p.getSkirt();
		assertTrue(Arrays.equals(actualSkirt, expectedSkirt));
		assertTrue(p.equals(new Piece(Piece.STICK_STR)));


		p = new Piece(Piece.L2_STR);
		assertEquals(3, p.getHeight());
		assertEquals(2, p.getWidth());
		expectedSkirt = new int[]{0, 0};
		actualSkirt = p.getSkirt();
		assertTrue(Arrays.equals(actualSkirt, expectedSkirt));
		assertTrue(p.equals(new Piece(Piece.L2_STR)));


		p = new Piece(Piece.S1_STR);
		assertEquals(2, p.getHeight());
		assertEquals(3, p.getWidth());
		expectedSkirt = new int[]{0, 0, 1};
		actualSkirt = p.getSkirt();
		assertTrue(Arrays.equals(actualSkirt, expectedSkirt));
		assertTrue(p.equals(new Piece(Piece.S1_STR)));
	}

	@Test
	public void testRotationOnStick() {
		// Test on Stick
		Piece p = new Piece(Piece.STICK_STR);
		Piece next = p.computeNextRotation();
		int[] expectedSkirt = new int[]{0, 0, 0, 0};
		testBodyProps(next, 4, 1, expectedSkirt);

		next = next.computeNextRotation();
		expectedSkirt = new int[]{0};
		testBodyProps(next, 1, 4, expectedSkirt);

		next = next.computeNextRotation();
		expectedSkirt = new int[]{0, 0, 0, 0};
		testBodyProps(next, 4, 1, expectedSkirt);
	}

	@Test
	public void testRotationOnL1() {
		Piece p = new Piece(Piece.L1_STR);
		Piece next = p.computeNextRotation();
		int[] expectedSkirt = new int[]{0, 0, 0};
		testBodyProps(next, 3, 2, expectedSkirt);

		next = next.computeNextRotation();
		expectedSkirt = new int[]{2, 0};
		testBodyProps(next, 2, 3, expectedSkirt);

		next = next.computeNextRotation();
		expectedSkirt = new int[]{0, 1, 1};
		testBodyProps(next, 3, 2, expectedSkirt);
	}

	// test for getBody()

	// test for fastRotation()
	@Test
	public void testFastRotation() {
		Piece[] pieces = Piece.getPieces();
		Piece leftPyramid = pieces[Piece.PYRAMID].fastRotation();
		testBodyProps(leftPyramid, 2, 3, new int[]{1, 0});

		Piece lowerPyramid = leftPyramid.fastRotation();
		testBodyProps(lowerPyramid, 3, 2, new int[]{1, 0, 1});
	}


	// test for equals()
	@Test
	public void testEquals() {
		Piece[] pieces = Piece.getPieces();
		assertTrue(pieces[Piece.S2].equals(new Piece(Piece.S2_STR)));
		assertTrue(pieces[Piece.L1].equals(pieces[Piece.L1].fastRotation().fastRotation()
				.fastRotation().fastRotation()));
		assertFalse(pieces[Piece.S2].fastRotation().equals(new Piece(Piece.S2_STR)));
		assertFalse(pieces[Piece.L1].fastRotation().equals(pieces[Piece.PYRAMID]));
		assertFalse(pieces[Piece.L1].fastRotation().equals(pieces[Piece.PYRAMID]));
		Piece p = new Piece(Piece.SQUARE_STR);
		assertTrue(p.equals(p));
		assertFalse(p.equals(5));
	}

	@Test
	public void testExceptions() {
		assertThrows(RuntimeException.class, () -> {
			Piece p = new Piece("IN VA LI D!");
		});
	}

	// test getPieces()
	@Test
	private void testBodyProps(Piece p, int width, int height, int[] skirt) {
		assertEquals(width, p.getWidth());
		assertEquals(height, p.getHeight());
		assertTrue(Arrays.equals(skirt, p.getSkirt()));
	}
}
