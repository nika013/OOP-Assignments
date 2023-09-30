// CS108 HW1 -- String static methods

import java.util.HashSet;

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int maxLength = 0;
		for (int i = 0; i < str.length(); i++) {
			int curSize = 1;
			for (int j = i+1; j < str.length(); j++) {
				if (str.charAt(i) == str.charAt(j)) {
					curSize++;
				} else {
					break;
				}
			}
			if (curSize > maxLength) {
				maxLength = curSize;
			}
			i += curSize-1;
		}
		return maxLength;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		if (str.isEmpty()) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		int size = str.length();
		for (int i = 0; i < size-1; i++) {
			char ch = str.charAt(i);
			if (Character.isDigit(ch)) {
				int reps = (int) (ch - '0');
				for (int j = 0; j < reps; j++) {
					 sb.append(str.charAt(i+1));}
			} else {
				 sb.append(ch);
			}
		}
		char lastCh = str.charAt(size-1);
		if (!Character.isDigit(lastCh)) {
			sb.append(lastCh);
		}
		String blowedUpStr = sb.toString();
		return blowedUpStr;
	}

	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if (a.isEmpty() || b.isEmpty()) {
			return len == 0; // intersection is empty string
		}

		HashSet<int> subStrings = new HashSet<>();
		int hashCode = 0;
		for (int i = 0; i < len; i++) {
			hashCode *= 26;
			hashCode += (int) Integer.valueOf(a.charAt(i) - 'a');
		}

		for (int i = 0; i < a.length(); i++) {
			if (i + len < a.length()) {

			}
		}

//		for (int i = 0; i < a.length(); i++) {
//			if (i + len <= a.length()) {
//				String substr = a.substring(i, i + len);
//				subStrings.add(substr);
//			}
//		}
//
//		for (int i = 0; i < b.length(); i++) {
//			if (i + len <= b.length()) {
//				String substr = b.substring(i, i + len);
//				if (subStrings.contains(substr)) {
//					return true;
//				}
//			}
//		}

		return false;
	}
}
