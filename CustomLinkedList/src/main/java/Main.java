import model.CustomLinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // CustomLinkedList methods using
        CustomLinkedList<Integer> customLinkedList = new CustomLinkedList<>();

        customLinkedList.add(1);
        customLinkedList.add(2);
        customLinkedList.add(3);
        System.out.println("List: " + customLinkedList);

        System.out.println("The element at index 1: " + customLinkedList.get(1));

        customLinkedList.remove(1);
        System.out.println("The list after deleting an item at index 1: " + customLinkedList);

        System.out.println("Contains 2? " + customLinkedList.contains(2));
        System.out.println("Contains 3? " + customLinkedList.contains(3));

        customLinkedList.addAll(List.of(4, 5, 6));
        System.out.println("The customlist after adding items from list: " + customLinkedList);


        // Converting a stream to a CustomLinkedList using reduce
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


        System.out.println("The customlist after conversion from the stream using reduce: " + customLinkedList_stream);
    }
}
