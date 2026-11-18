/**
 * IMPLEMENTATION of the Queue ADT using a LINKED LIST.
 *
 * We keep two pointers so both operations are O(1):
 *   front -> the node that will leave next (dequeue happens here)
 *   rear  -> the last node that joined     (enqueue happens here)
 *
 *   front -> [GATE] -> [WELL] -> [SHRINE] <- rear
 */
public class LinkedQueue implements QueueADT {
    private Node front = null;
    private Node rear = null;
    private int count = 0;

    public void enqueue(String value) {
        Node newNode = new Node(value);
        if (rear == null) {          // queue was empty: new node is front AND rear
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;     // link it after the current rear
            rear = newNode;          // it becomes the new rear
        }
        count++;
    }

    public String dequeue() {
        if (isEmpty()) {
            System.out.println("Queue underflow: the queue is empty.");
            return null;
        }
        String value = front.data;
        front = front.next;          // the next node moves to the front
        if (front == null) {         // queue became empty, so rear must be reset too
            rear = null;
        }
        count--;
        return value;
    }

    public String peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return count;
    }
}
