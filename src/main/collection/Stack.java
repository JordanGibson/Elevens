package collection;

import lombok.val;

import java.io.Serializable;
import java.util.EmptyStackException;

public class Stack<T> implements StackInterface<T>, Serializable {
    private Node<T> topNode;

    public void push(T newEntry) {
        val newNode = new Node<>(newEntry);
        newNode.setNext(topNode);
        topNode = newNode;
    }

    public T pop() {
        val dataToReturn = peek();
        topNode = topNode.getNext();
        return dataToReturn;
    }

    public T peek() {
        if (topNode == null) throw new EmptyStackException();
        else return topNode.getData();
    }

    public boolean isEmpty() {
        return (topNode == null);
    }

}
