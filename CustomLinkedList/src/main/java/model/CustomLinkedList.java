package model;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CustomLinkedList<T> extends LinkedList<T> {

    private Node<T> head;
    private int size;

    public CustomLinkedList() {
        this.head = null;
        this.size = 0;
        log.debug("CustomLinkedList initialized.");
    }

    @Override
    public boolean add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
        log.debug("Added element: {}", data);
        return true;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            log.error("Index out of bounds: {}", index);
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        T data = current.getData();
        log.debug("Retrieved element at index {}: {}", index, data);
        return data;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            log.error("Index out of bounds: {}", index);
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Node<T> removedNode;
        if (index == 0) {
            removedNode = head;
            head = head.getNext();
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            removedNode = current.getNext();
            current.setNext(current.getNext().getNext());
        }

        size--;
        log.debug("Removed element at index {}: {}", index, removedNode.getData());
        return removedNode.getData();
    }

    @Override
    public boolean contains(Object data) {
        Node<T> current = head;
        while (current != null) {
            if (current.getData().equals(data)) {
                log.debug("Element found: {}", data);
                return true;
            }
            current = current.getNext();
        }
        log.debug("Element not found: {}", data);
        return false;
    }

    public void addAll(List<T> list) {
        for (T item : list) {
            add(item);
        }
        log.debug("Added all elements from list: {}", list);
    }

    @Override
    public int size() {
        log.debug("Size of the list: {}", size);
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        sb.append("[");
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        String result = sb.toString();
        log.debug("List toString: {}", result);
        return result;
    }
}
