import junit.framework.TestCase;

import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}
	
	public void testSameCountEmpty() {
		List<Integer> arr = Arrays.asList();
		assertEquals(0, Appearances.sameCount(arr, Arrays.asList(9, 1)));
	}

	public void testSameCount4() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(0, 10, 203, 12312, -120, -17, 49, 10, -17);
		assertEquals(0, Appearances.sameCount(a, Arrays.asList()));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(0)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(-17, -17, 203, 10)));
		assertEquals(4, Appearances.sameCount(a, Arrays.asList(-17, -17, 203, 10, 12312, 10)));
	}

	// Check on spaces
	public void testSameCount5() {
		List<String> a = stringToList("a b cc");
		List<String> b = stringToList("cc bba ");
		assertEquals(3, Appearances.sameCount(a, b));
	}
}
