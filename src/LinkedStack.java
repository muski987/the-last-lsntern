/**
 * IMPLEMENTATION of the Stack ADT using a LINKED LIST.
 *
 * "top" points to the most recently pushed node.
 *   push : new node becomes the new top           O(1)
 *   pop  : remove the top node, top moves down    O(1)
 *
 *   top -> [SHRINE] -> [WELL] -> [GATE] -> null
 */
public class LinkedStack implements StackADT {
    private Node top = null;
    private int count = 0;

    public void push(String value) {
        Node newNode = new Node(value);
        newNode.next = top;     // new node points to the old top
        top = newNode;          // and becomes the new top
        count++;
    }

    public String pop() {
        if (isEmpty()) {
            System.out.println("Stack underflow: the stack is empty.");
            return null;
        }
        String value = top.data;
        top = top.next;         // the node below becomes the top
        count--;
        return value;
    }

    public String peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return count;
    }
}
