/**
 * ADT (Abstract Data Type): QUEUE
 *
 * Queue rule: FIFO (First In, First Out), like a line at a ticket counter.
 * Used in StoryGraph.java for BFS (Breadth First Search).
 */
public interface QueueADT {
    void enqueue(String value);   // add a value at the REAR
    String dequeue();             // remove and return the value at the FRONT
    String peek();                // look at the front value without removing it
    boolean isEmpty();
    int size();
}
