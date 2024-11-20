package model;

import java.util.Iterator;
import java.util.function.Consumer;

public class CustomIterator<T> implements Iterator<T> {

    private Node<T> current;

    public CustomIterator(Node<T> head) {
        this.current = head;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        T data = current.getData();
        current = current.getNext();
        return data;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        while (hasNext()) {
            action.accept(next());
        }
    }
}
