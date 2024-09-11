package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    public void testNodeInitialization() {
        Node<Integer> node = new Node<>(5);
        assertEquals(5, node.getData(), "Data should be initialized correctly");
        assertNull(node.getNext(), "Next node should be null upon initialization");
    }

    @Test
    public void testSetData() {
        Node<String> node = new Node<>("initial");
        node.setData("changed");
        assertEquals("changed", node.getData(), "Data should be updated correctly");
    }

    @Test
    public void testSetNext() {
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        node1.setNext(node2);
        assertEquals(node2, node1.getNext(), "Next node should be set correctly");
    }

    @Test
    public void testGetNext() {
        Node<String> node1 = new Node<>("first");
        Node<String> node2 = new Node<>("second");
        node1.setNext(node2);
        assertEquals(node2, node1.getNext(), "getNext() should return the correct next node");
    }
}