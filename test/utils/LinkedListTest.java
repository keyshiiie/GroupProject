package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    private LinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new LinkedList<>();
    }

    @Test
    void testAdd() {
        list.add("First");
        assertEquals(1, list.size());
        assertEquals("First", list.get(0));
    }

    @Test
    void testAddAtIndex() {
        list.add("First");
        list.add("Second");
        list.add(1, "Middle");

        assertEquals(3, list.size());
        assertEquals("First", list.get(0));
        assertEquals("Middle", list.get(1));
        assertEquals("Second", list.get(2));
    }

    @Test
    void testAddAtIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "Test"));
    }

    @Test
    void testAddAll() {
        List<String> items = new ArrayList<>();
        items.add("One");
        items.add("Two");
        items.add("Three");

        list.addAll(items);
        assertEquals(3, list.size());
        assertEquals("One", list.get(0));
        assertEquals("Two", list.get(1));
        assertEquals("Three", list.get(2));
    }

    @Test
    void testAddAllAtIndex() {
        list.add("First");
        list.add("Last");

        List<String> items = new ArrayList<>();
        items.add("Middle1");
        items.add("Middle2");

        list.addAll(1, items);
        assertEquals(4, list.size());
        assertEquals("First", list.get(0));
        assertEquals("Middle1", list.get(1));
        assertEquals("Middle2", list.get(2));
        assertEquals("Last", list.get(3));
    }

    @Test
    void testRemove() {
        list.add("One");
        list.add("Two");
        list.add("Three");

        assertTrue(list.remove("Two"));
        assertEquals(2, list.size());
        assertEquals("One", list.get(0));
        assertEquals("Three", list.get(1));
    }

    @Test
    void testRemoveNonExistent() {
        list.add("One");
        assertFalse(list.remove("NonExistent"));
        assertEquals(1, list.size());
    }

    @Test
    void testRemoveAt() {
        list.add("One");
        list.add("Two");
        list.add("Three");

        String removed = list.removeAt(1);
        assertEquals("Two", removed);
        assertEquals(2, list.size());
        assertEquals("One", list.get(0));
        assertEquals("Three", list.get(1));
    }

    @Test
    void testRemoveAtOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(0));
    }

    @Test
    void testGet() {
        list.add("First");
        list.add("Second");

        assertEquals("First", list.get(0));
        assertEquals("Second", list.get(1));
    }

    @Test
    void testGetOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void testSet() {
        list.add("First");
        list.add("Second");

        String old = list.set(0, "New First");
        assertEquals("First", old);
        assertEquals("New First", list.get(0));
    }

    @Test
    void testSetOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, "Test"));
    }

    @Test
    void testContains() {
        list.add("One");
        list.add("Two");

        assertTrue(list.contains("One"));
        assertTrue(list.contains("Two"));
        assertFalse(list.contains("Three"));
    }

    @Test
    void testContainsAll() {
        list.add("One");
        list.add("Two");

        List<String> items = new ArrayList<>();
        items.add("One");
        items.add("Two");

        assertTrue(list.containsAll(items));

        items.add("Three");
        assertFalse(list.containsAll(items));
    }

    @Test
    void testRemoveAll() {
        list.add("One");
        list.add("Two");
        list.add("Three");

        List<String> toRemove = new ArrayList<>();
        toRemove.add("One");
        toRemove.add("Three");

        assertTrue(list.removeAll(toRemove));
        assertEquals(1, list.size());
        assertEquals("Two", list.get(0));
    }

    @Test
    void testRetainAll() {
        list.add("One");
        list.add("Two");
        list.add("Three");

        List<String> toRetain = new ArrayList<>();
        toRetain.add("One");
        toRetain.add("Three");

        assertTrue(list.retainAll(toRetain));
        assertEquals(2, list.size());
        assertEquals("One", list.get(0));
        assertEquals("Three", list.get(1));
    }

    @Test
    void testClear() {
        list.add("One");
        list.add("Two");
        list.add("Three");

        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(list.isEmpty());
        list.add("One");
        assertFalse(list.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, list.size());
        list.add("One");
        assertEquals(1, list.size());
        list.add("Two");
        assertEquals(2, list.size());
    }

    @Test
    void testToArray() {
        list.add("One");
        list.add("Two");

        Object[] array = list.toArray();
        assertEquals(2, array.length);
        assertEquals("One", array[0]);
        assertEquals("Two", array[1]);
    }

    @Test
    void testToArrayWithParameter() {
        list.add("One");
        list.add("Two");

        String[] array = new String[2];
        String[] result = list.toArray(array);

        assertSame(array, result);
        assertEquals("One", array[0]);
        assertEquals("Two", array[1]);
    }

    @Test
    void testFromArray() {
        String[] array = {"One", "Two", "Three"};
        list.fromArray(array);

        assertEquals(3, list.size());
        assertEquals("One", list.get(0));
        assertEquals("Two", list.get(1));
        assertEquals("Three", list.get(2));
    }

    @Test
    void testShuffle() {
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        list.add("Five");

        list.shuffle();
        assertEquals(5, list.size());

        assertTrue(list.contains("One"));
        assertTrue(list.contains("Two"));
        assertTrue(list.contains("Three"));
        assertTrue(list.contains("Four"));
        assertTrue(list.contains("Five"));
    }

    @Test
    void testShuffleEmptyList() {
        list.shuffle();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testShuffleSingleElement() {
        list.add("One");
        list.shuffle();
        assertEquals(1, list.size());
        assertEquals("One", list.get(0));
    }

    @Test
    void testIterator() {
        list.add("One");
        list.add("Two");
        list.add("Three");

        int count = 0;
        for (String item : list) {
            assertNotNull(item);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testIteratorRemoveThrowsException() {
        list.add("One");
        var iterator = list.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    void testRemoveAllWithEmptyList() {
        list.add("One");
        list.add("Two");

        List<String> emptyList = new ArrayList<>();
        assertFalse(list.removeAll(emptyList));
        assertEquals(2, list.size());
    }

    @Test
    void testRetainAllWithEmptyList() {
        list.add("One");
        list.add("Two");

        List<String> emptyList = new ArrayList<>();
        assertTrue(list.retainAll(emptyList));
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }
}