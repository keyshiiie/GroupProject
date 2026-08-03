package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringListTest {

    private StringList stringList;
    private String str1;
    private String str2;
    private String str3;

    @BeforeEach
    void setUp() {
        stringList = new StringList();
        str1 = "Hello";
        str2 = "World";
        str3 = "Java";
    }

    @Test
    void testDefaultConstructor() {
        StringList emptyList = new StringList();
        assertNotNull(emptyList);
        assertEquals(0, emptyList.size());
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void testConstructorWithCollection() {
        List<String> strings = new ArrayList<>();
        strings.add(str1);
        strings.add(str2);

        StringList list = new StringList(strings);
        assertEquals(2, list.size());
        assertEquals(str1, list.get(0));
        assertEquals(str2, list.get(1));
    }

    @Test
    void testAddString() {
        stringList.add(str1);
        assertEquals(1, stringList.size());
        assertEquals(str1, stringList.get(0));
    }

    @Test
    void testAddAllStrings() {
        List<String> strings = new ArrayList<>();
        strings.add(str1);
        strings.add(str2);
        strings.add(str3);

        stringList.addAll(strings);
        assertEquals(3, stringList.size());
        assertEquals(str1, stringList.get(0));
        assertEquals(str2, stringList.get(1));
        assertEquals(str3, stringList.get(2));
    }

    @Test
    void testRemoveString() {
        stringList.add(str1);
        stringList.add(str2);
        assertEquals(2, stringList.size());

        stringList.remove(str1);
        assertEquals(1, stringList.size());
        assertEquals(str2, stringList.get(0));
    }

    @Test
    void testGetString() {
        stringList.add(str1);
        stringList.add(str2);

        assertEquals(str1, stringList.get(0));
        assertEquals(str2, stringList.get(1));
    }

    @Test
    void testGetIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> stringList.get(0));
    }

    @Test
    void testClear() {
        stringList.add(str1);
        stringList.add(str2);
        assertEquals(2, stringList.size());

        stringList.clear();
        assertEquals(0, stringList.size());
        assertTrue(stringList.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(stringList.isEmpty());
        stringList.add(str1);
        assertFalse(stringList.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, stringList.size());
        stringList.add(str1);
        assertEquals(1, stringList.size());
        stringList.add(str2);
        assertEquals(2, stringList.size());
    }

    @Test
    void testContains() {
        stringList.add(str1);
        stringList.add(str2);

        assertTrue(stringList.contains(str1));
        assertTrue(stringList.contains(str2));
        assertFalse(stringList.contains(str3));
    }

    @Test
    void testContainsAll() {
        stringList.add(str1);
        stringList.add(str2);

        List<String> strings = new ArrayList<>();
        strings.add(str1);
        strings.add(str2);

        assertTrue(stringList.containsAll(strings));

        strings.add(str3);
        assertFalse(stringList.containsAll(strings));
    }

    @Test
    void testToArray() {
        stringList.add(str1);
        stringList.add(str2);

        Object[] array = stringList.toArray();
        assertEquals(2, array.length);
        assertEquals(str1, array[0]);
        assertEquals(str2, array[1]);
    }

    @Test
    void testIterator() {
        stringList.add(str1);
        stringList.add(str2);

        int count = 0;
        for (String str : stringList) {
            assertNotNull(str);
            count++;
        }
        assertEquals(2, count);
    }
}