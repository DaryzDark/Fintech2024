import model.CustomLinkedList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testCustomLinkedListOperations() {
        CustomLinkedList<Integer> customLinkedList = new CustomLinkedList<>();

        // Test add and toString
        customLinkedList.add(1);
        customLinkedList.add(2);
        customLinkedList.add(3);
        assertEquals("[1, 2, 3]", customLinkedList.toString(), "List should contain elements [1, 2, 3]");

        // Test get
        assertEquals(2, customLinkedList.get(1), "Element at index 1 should be 2");

        // Test remove
        customLinkedList.remove(1);
        assertEquals("[1, 3]", customLinkedList.toString(), "List should contain elements [1, 3] after removal");

        // Test contains
        assertTrue(customLinkedList.contains(1), "List should contain element 1");
        assertFalse(customLinkedList.contains(2), "List should not contain element 2");
        assertTrue(customLinkedList.contains(3), "List should contain element 3");

        // Test addAll
        customLinkedList.addAll(List.of(4, 5, 6));
        assertEquals("[1, 3, 4, 5, 6]", customLinkedList.toString(), "List should contain elements [1, 3, 4, 5, 6]");
    }

    @Test
    public void testStreamToCustomLinkedListUsingReduce() {
        CustomLinkedList<Integer> customLinkedList_stream = Stream.of(1, 2, 3, 4, 5).reduce(
                new CustomLinkedList<>(),
                (list, element) -> {
                    list.add(element);
                    return list;
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                });

        assertEquals("[1, 2, 3, 4, 5]", customLinkedList_stream.toString(), "List should contain elements [1, 2, 3, 4, 5]");
    }
}