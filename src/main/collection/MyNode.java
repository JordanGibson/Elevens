package collection;

import java.io.Serializable;

public class MyNode<T> implements Serializable {

    private final T data;
    private MyNode<T> next;

    public MyNode(T dataValue) {
        data = dataValue;
        next = null;
    }

    public T getData() {
        return data;
    }

    public MyNode<T> getNext() {
        return next;
    }

    public void setNext(MyNode<T> next) {
        this.next = next;
    }

}
