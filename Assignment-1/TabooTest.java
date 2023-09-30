// TabooTest.java
// Taboo class tests -- nothing provided.

import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

public class TabooTest extends TestCase {
    @Test
    public void testNoFollowStrings() {
        // basic
        Taboo<String> t = new Taboo<>(Arrays.asList("a", "b", "c"));
        HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("b");
        assertTrue(expectedSet.equals(t.noFollow("a")));
        expectedSet.clear();
        expectedSet.add("c");
        assertTrue(expectedSet.equals(t.noFollow("b")));
        expectedSet.clear();
        assertTrue(expectedSet.equals(t.noFollow("c")));
    }

    public void testNoFollowStringsNull() {
        // basic
        Taboo<String> t = new Taboo<>(Arrays.asList("a", null, "b", "c", null, "taboo1", "taboo2", null, "a", "taboo1"));
        HashSet<String> expectedSet = new HashSet<>();

        expectedSet.add("taboo1");
        assertTrue(expectedSet.equals(t.noFollow("a")));
        expectedSet.clear();

        expectedSet.add("c");
        assertTrue(expectedSet.equals(t.noFollow("b")));
        expectedSet.clear();

        assertTrue(expectedSet.equals(t.noFollow("c")));
        expectedSet.clear();

        expectedSet.add("taboo2");
        assertTrue(expectedSet.equals(t.noFollow("taboo1")));
        expectedSet.clear();

        assertTrue(expectedSet.equals(t.noFollow("taboo2")));
        expectedSet.clear();
    }

    @Test
    public void testNoFollowIntegers() {
        Taboo<Integer> t = new Taboo<>(Arrays.asList(1, 2, 3, null, 5, 1, 5, null));
        HashSet<Integer> expectedSet = new HashSet<>();

        assertTrue(expectedSet.equals(t.noFollow(3)));

        expectedSet.add(3);
        assertTrue(expectedSet.equals(t.noFollow(2)));
        expectedSet.clear();

        expectedSet.add(2);
        expectedSet.add(5);
        assertTrue(expectedSet.equals(t.noFollow(1)));
        expectedSet.clear();

        expectedSet.add(1);
        assertTrue(expectedSet.equals(t.noFollow(5)));
        expectedSet.clear();

        // test on non-existing element
        assertTrue(expectedSet.equals(t.noFollow(100)));
    }

    @Test
    public void testReduce1() {
        // basic
        Taboo<Integer> t = new Taboo<>(Arrays.asList(1, 2, 5, 7, null, -1));
        Integer[] array = {1, 3, 2, 5, 1, 2, 7};
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, array);
         t.reduce(list);
        assertTrue(Arrays.asList(1, 3, 2, 1, 7).equals(list));

    }

    public void testReduce2() {
        // basic
        Taboo<Integer> t = new Taboo<>(Arrays.asList(3, 2, 1, 2, 3, 1, 6));
        Integer[] array = {1, 3, 2, 3, 6};
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, array);
        t.reduce(list);
        assertTrue(Arrays.asList(1, 3, 6).equals(list));
    }

    public void testReduce3() {
        // basic
        Taboo<Integer> t = new Taboo<>(Arrays.asList(3, 2, 1, 2, 3, 1, 6));
        Integer[] array = {1, 3, 1, 2, 6, 2, 1};
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, array);
        t.reduce(list);
        assertTrue(Arrays.asList(1, 3, 6, 2).equals(list));
    }

    public void testReduce4() {
        // basic
        Taboo<String> t = new Taboo<>(Arrays.asList("a", "c", "a", "b"));
        String[] array = {"a", "c", "b", "x", "c", "a"};
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);
        t.reduce(list);
        assertTrue(Arrays.asList("a", "x", "c").equals(list));
    }

    public void testReduce5() {
        // basic
        Taboo<String> t = new Taboo<>(Arrays.asList("a", "c", "a", "b", "x"));
        String[] array = {"a", "c", "b", "x", "c", "a"};
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);
        t.reduce(list);
        assertTrue(Arrays.asList("a").equals(list));
    }
}
