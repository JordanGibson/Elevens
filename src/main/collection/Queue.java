package collection;

import lombok.val;

import java.io.Serializable;

public class Queue<T extends Comparable<T>> implements QueueInterface<T>, Serializable {

    private MyNode<T> front;

    public Queue() {
        front = null;
    }


    public void enqueue(T anEntry) {

        val newNode = new MyNode<>(anEntry);

        if (front == null) {
            front = newNode;
        } else if (front.getData().compareTo(newNode.getData()) < 0) {
            newNode.setNext(front);
            front = newNode;
        } else {
            MyNode<T> currentNode = new MyNode<>(front.getData());
            while (currentNode.getNext() != null && (currentNode.getData().compareTo(newNode.getData()) > 0)) {
                currentNode = currentNode.getNext();
            }
            newNode.setNext(currentNode.getNext());
            currentNode.setNext(newNode);
        }
    }

    public T dequeue() {
        if (front == null) {
            return null;
        } else {
            val valueToReturn = front.getData();
            front = front.getNext();
            return valueToReturn;
        }

    }

}
