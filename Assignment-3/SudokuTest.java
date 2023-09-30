import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SudokuTest extends TestCase {
    @Test
    public void TestConstruct() {
        Sudoku s = new Sudoku(Sudoku.easyGrid);
        Sudoku withString = new Sudoku(
                "1 6 4 0 0 0 0 0 2" +
                " 2 0 0 4 0 3 9 1 0" +
                " 0 0 5 0 8 0 4 0 7" +
                " 0 9 0 0 0 6 5 0 0" +
                " 5 0 0 1 0 2 0 0 8" +
                " 0 0 8 9 0 0 0 3 0" +
                " 8 0 9 0 4 0 2 0 0" +
                " 0 7 3 5 0 9 0 0 1" +
                " 4 0 0 0 0 0 6 7 9"
        );
        Sudoku continuesStr = new Sudoku(
                "164000002" +
                        "200403910" +
                        "005080407" +
                        "090006500" +
                        "500102008" +
                        "008900030" +
                        "809040200" +
                        "073509001" +
                        "400000679"
        );
        assertTrue(s.toString().equals(withString.toString()));
        assertTrue(withString.toString().equals(continuesStr.toString()));
        assertEquals(withString.getElapsed(), continuesStr.getElapsed());
        assertEquals(1, withString.solve());
        assertEquals(1, continuesStr.solve());
        assertTrue(withString.getSolutionText().equals(continuesStr.getSolutionText()));
    }
    @Test
    public void testSort() {
        Sudoku s = new Sudoku(Sudoku.easyGrid);
        for (int i = 0; i < s.spots.size()-1; i++) {
            assertTrue(s.spots.get(i).getPossiblesNumber()
                    <= s.spots.get(i+1).getPossiblesNumber());
        }
    }

    @Test
    public void testSolve() {
        Sudoku s = new Sudoku(Sudoku.easyGrid);
        assertEquals(1, s.solve());
        assertTrue(!s.toString().isEmpty());
        String easySol = "1 6 4 7 9 5 3 8 2\n" +
                "2 8 7 4 6 3 9 1 5\n" +
                "9 3 5 2 8 1 4 6 7\n" +
                "3 9 1 8 7 6 5 2 4\n" +
                "5 4 6 1 3 2 7 9 8\n" +
                "7 2 8 9 5 4 1 3 6\n" +
                "8 1 9 6 4 7 2 5 3\n" +
                "6 7 3 5 2 9 8 4 1\n" +
                "4 5 2 3 1 8 6 7 9";
        String expected = easySol.replaceAll("\\s+", "");
        String actual = s.getSolutionText().replaceAll("\\s+", "");
        assertTrue(expected.equals(actual));

        Sudoku med = new Sudoku(Sudoku.mediumGrid);
        assertEquals(1, med.solve());
        assertTrue(!med.toString().isEmpty());

        String mediumSol = "5 3 4 6 7 8 9 1 2\n" +
                "6 7 2 1 9 5 3 4 8\n" +
                "1 9 8 3 4 2 5 6 7\n" +
                "8 5 9 7 6 1 4 2 3\n" +
                "4 2 6 8 5 3 7 9 1\n" +
                "7 1 3 9 2 4 8 5 6\n" +
                "9 6 1 5 3 7 2 8 4\n" +
                "2 8 7 4 1 9 6 3 5\n" +
                "3 4 5 2 8 6 1 7 9";

        expected = mediumSol.replaceAll("\\s+", "");
        actual = med.getSolutionText().replaceAll("\\s+", "");
        assertTrue(expected.equals(actual));


        Sudoku hard = new Sudoku(Sudoku.hardGrid);
        assertEquals(1, hard.solve());
        assertTrue(!hard.toString().isEmpty());
        String hardSol = "3 7 5 1 6 2 4 8 9 \n" +
                "8 6 1 4 9 3 5 2 7 \n" +
                "2 4 9 7 8 5 1 6 3 \n" +
                "4 9 3 8 5 7 6 1 2 \n" +
                "7 1 6 2 4 9 8 3 5 \n" +
                "5 2 8 3 1 6 7 9 4 \n" +
                "6 5 7 9 2 1 3 4 8 \n" +
                "1 8 2 5 3 4 9 7 6 \n" +
                "9 3 4 6 7 8 2 5 1";
        expected = hardSol.replaceAll("\\s+", "");
        actual = hard.getSolutionText().replaceAll("\\s+", "");
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testNotValid() {
        assertThrows(RuntimeException.class, () -> {
            Sudoku.textToGrid("1 6 4 0 0 0 0 0 2" +
                    " 2 0 0 4 0 3 9 1 0" +
                    " 0 0 5 0 8 0 4 0 7" +
                    " 0 9 0 0 0 6 5 0 0" +
                    " 5 0 0 1 0 2 0 0 8" +
                    " 0 0 8 9 0 0 0 3 0" +
                    " 8 0 9 0 4 0 2 0 0" +
                    " 0 7 3 5 0 9 0 0 1" +
                    " 4 0 0 0 0 0 6 7 9" +
                    " 1 2 3 4 10 0 ");
        });
        assertThrows(RuntimeException.class, () -> {
            Sudoku.textToGrid("000012" +
                    "00101234123" +
                    "4643631290");
        });
    }
}
