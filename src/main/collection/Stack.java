package main.collection;

import java.util.EmptyStackException;

public class Stack<T> implements StackInterface<T> {
    private MyNode<T> topNode;


    public void push(T newEntry) {
        MyNode newNode = new MyNode(newEntry);
        newNode.setNext(topNode);
        topNode = newNode;
    }

    public T pop() {
        T dataToReturn = peek();
        topNode = topNode.getNext();
        return dataToReturn;
    }

    public T peek() {
        if(topNode == null) throw new EmptyStackException();
        else return topNode.getData();
    }

    public boolean isEmpty() {
        return (topNode == null);
    }

    public void clear() {
        topNode = null;
    }

}
