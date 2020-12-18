package collection;

import java.io.Serializable;

public class Node<T> implements Serializable {

    private final T data;
    private Node<T> next;

    public Node(T dataValue) {
        data = dataValue;
        next = null;
    }

    public T getData() {
        return data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

}
