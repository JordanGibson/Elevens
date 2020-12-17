package collection;

import java.util.Arrays;
import java.util.Random;

public class PQueue<T  extends Comparable<T>> implements PQueueInterface<T> {

    private MyNode<T> front, rear;

    public PQueue(){
        front = null;
        rear = null;
    }


    public void enqueue(T anEntry) {

        MyNode<T> newNode = new MyNode<T>(anEntry);

        if(front == null){
            front = newNode;
            rear = newNode;
        }else if(front.getData().compareTo(newNode.getData()) == -1){
            newNode.setNext(front);
            front = newNode;
        }else{
            MyNode<T> currentNode = new MyNode<T>(front.getData());
            while (currentNode.getNext() != null && (currentNode.getData().compareTo(newNode.getData())==1)){
                currentNode = currentNode.getNext();
            }
            newNode.setNext(currentNode.getNext());
            currentNode.setNext(newNode);
        }
    }

    public T dequeue() {
        if (front == null){
            return null;
        }else {
            T valueToReturn = front.getData();
            front=front.getNext();
            if (front==null) rear=null;
            return valueToReturn;
        }

    }

    public T getFront() {
        if (front==null) return null;
        else return front.getData();
    }

    public boolean isEmpty() {
        return (front == null);
    }

    public void Clear() {
        front = null;
        rear = null;

    }

    public static void main(String[] args) {
        PQueue<Integer> Pqueue = new PQueue<>();
        Random random = new Random();
        Integer[] randomValues = new Integer[10];

        for (int i=0; i < 10; i++){
            randomValues[i] = random.nextInt(1000);
            Pqueue.enqueue(randomValues[i]);
        }

        System.out.println("Random array is "+Arrays.toString(randomValues));

        while(!Pqueue.isEmpty()){
            System.out.println("Removing Values: " + Pqueue.dequeue());
        }

    }
}
