/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Stack;

/**
 *
 * @author gameb
 */
public class LinkedListStack<T> {

    private int N; // size of the stack 
    private Node<T> first; // this is the TOP of the stack

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return N;
    }

    public void push(T t) {
        Node<T> oldfirst = first;
        first = new Node<T>(t);
        first.next = oldfirst;
        N++;
    }

    public void push2(T t) {
        Node<T> newNode = new Node<T>(t);
        newNode.next = first;
        first = newNode;
        N++;
    }

    public Node<T> top() {
        return first;
    }

    public T pop() {
        if (isEmpty()) {
            System.out.println("The list is already empty");
            return null;
        }
        T return_value = first.data;
        first = first.next;
        N--;
        return return_value;
    }
    
    public LinkedListStack<T> duplicate() {
        LinkedListStack<T> temp = new LinkedListStack<>();
        LinkedListStack<T> copy = new LinkedListStack<>();

        Node<T> cur = first;
        while (cur != null) {
            temp.push(cur.data);
            cur = cur.next;
        }

        while (!temp.isEmpty()) {
            copy.push(temp.pop());
        }

        return copy;
    }

    public LinkedListStack<T> reverseStack() {
        LinkedListStack<T> copy = this.duplicate();
        LinkedListStack<T> reverse = new LinkedListStack<>();
        
        while (!copy.isEmpty()) {
            reverse.push(copy.pop());
        }

        return reverse;
    }
    
    public String toReverseString() {
        Node<T> cur = reverseStack().first;
        
        StringBuilder s = new StringBuilder();
        while (cur != null) {
            s.append(cur.data);
            if (cur.next != null) {
                s.append(" -> ");
            } else {
                break;
            }
            
            cur = cur.next;
        }
        
        return s.toString();
    }

    @Override
    public String toString() {
        Node<T> cur = first;
        
        StringBuilder s = new StringBuilder();
        while (cur != null) {
            s.append(cur.data);
            if (cur.next != null) {
                s.append(" -> ");
            } else {
                break;
            }
            
            cur = cur.next;
        }
        
        return s.toString();
    }
}
