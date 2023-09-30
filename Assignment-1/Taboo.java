
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	private HashMap<T, HashSet<T>> noFollowElems;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		noFollowElems = new HashMap<>();
		for (int i = 0; i < rules.size()-1; i++) {
			T curElem = rules.get(i);
			if (rules.get(i+1) == null) continue;
			if (noFollowElems.containsKey(curElem)) {
				HashSet<T> set = noFollowElems.get(curElem);
				set.add(rules.get(i+1));
				noFollowElems.put(curElem, set);
			} else {
				HashSet<T> set = new HashSet<>();
				set.add(rules.get(i+1));
				noFollowElems.put(curElem, set);
			}
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if (noFollowElems.containsKey(elem)) {
			Set<T> set = noFollowElems.get(elem);
			return set;
		}
		return Collections.emptySet();
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		int removed = 1;
		while (removed != 0) {
			removed = 0;
			for (int i = list.size()-2; i >= 0; i--) {
				Set<T> items = noFollowElems.get(list.get(i));
				if (items != null && items.contains(list.get(i+1))) {
					list.remove(i+1);
					removed++;
				}
			}
		}

	}
}
