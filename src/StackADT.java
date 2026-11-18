/**
 * ADT (Abstract Data Type): STACK
 *
 * An ADT says WHAT operations a data structure offers, not HOW they are
 * done. In Java we write an ADT as an INTERFACE. Any class that implements
 * it must provide these methods.
 *
 * Stack rule: LIFO (Last In, First Out), like a pile of plates.
 * Used in Game.java for the UNDO feature.
 */
public interface StackADT {
    void push(String value);   // put a value on top
    String pop();              // remove and return the top value
    String peek();             // look at the top value without removing it
    boolean isEmpty();         // true if the stack has no values
    int size();                // how many values are in the stack
}
