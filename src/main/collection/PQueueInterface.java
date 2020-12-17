package collection;

public interface PQueueInterface<T> {
    public void enqueue(T newEntry);

    public T dequeue();

    public T getFront();

    public boolean isEmpty();

    public void Clear();

}
