/**
 * One NODE of a singly linked list.
 * It stores one value (a scene id) and a link to the next node.
 *
 * Both our LinkedStack and LinkedQueue are built from these nodes,
 * so we do not need Java's ready-made Stack or Queue classes.
 */
public class Node {
    String data;   // the value stored, for example "GATE"
    Node next;     // the node after this one (null if this is the last node)

    public Node(String data) {
        this.data = data;
        this.next = null;
    }
}
