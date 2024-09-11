package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {

    private CustomLinkedList<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    public void testAdd() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size(), "Size should be 3 after adding 3 elements");
        assertEquals("[1, 2, 3]", list.toString(), "List should contain elements [1, 2, 3]");
    }

    @Test
    public void testGet() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(2, list.get(1), "Element at index 1 should be 2");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5), "Index out of bounds should throw exception");
    }

    @Test
    public void testRemove() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(2, list.remove(1), "Element removed at index 1 should be 2");
        assertEquals("[1, 3]", list.toString(), "List should contain elements [1, 3] after removal");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5), "Index out of bounds should throw exception");
    }

    @Test
    public void testContains() {
        list.add(1);
        list.add(2);
        assertTrue(list.contains(1), "List should contain element 1");
        assertFalse(list.contains(3), "List should not contain element 3");
    }

    @Test
    public void testAddAll() {
        list.addAll(Arrays.asList(4, 5, 6));
        assertEquals("[4, 5, 6]", list.toString(), "List should contain elements [4, 5, 6]");
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size(), "Size should be 0 for a new list");
        list.add(1);
        list.add(2);
        assertEquals(2, list.size(), "Size should be 2 after adding 2 elements");
    }
}