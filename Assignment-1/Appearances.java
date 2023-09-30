import java.util.*;

public class Appearances {
	private Collection<Object> coll;
	private HashMap<Object, Integer> map;

	/**
	 *
	 * @param coll - Collection of generic items
	 * @param map - frequencies of each item
	 */
	private static <T> void countFrequencies(Collection<T> coll, HashMap<T, Integer> map) {
		coll.forEach((item) -> {
			if (map.containsKey(item)) {
				int freq = map.get(item);
				freq++;
				map.put(item, freq);
			} else {
				map.put(item, 1);
			}
		});
	}


	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		HashMap<T, Integer> freqsOfA = new HashMap<>();
		HashMap<T, Integer> freqsOfB = new HashMap<>();
		int count = 0;
		countFrequencies(a, freqsOfA);
		countFrequencies(b, freqsOfB);
		for (T key: freqsOfA.keySet()) {
			if (freqsOfB.containsKey(key) && freqsOfA.get(key) == freqsOfB.get(key)) {
				count++;
			}
		}
		return count;
	}
}
