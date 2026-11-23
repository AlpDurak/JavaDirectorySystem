package LinkedList;

import java.util.Objects;

public class LinkedList<T> {
    public Node<T> first;
    public Node<T> last;

    public void insertFirst(T x) {
        Node<T> newNode = new Node<>(x);
        newNode.next = first;
        first = newNode;
        if (last == null) {
            last = newNode;
        }
    }

    public void insertLast(T x) {
        Node<T> newNode = new Node<>(x);
        if (last == null) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
    }

    public void insertafter(T x, Node<T> prev) {
        if (prev == null) {
            return;
        }
        Node<T> newNode = new Node<>(x);
        newNode.next = prev.next;
        prev.next = newNode;
        if (prev == last) {
            last = newNode;
        }
    }

    public Node<T> search(T x) {
        Node<T> tmp = first;
        while (tmp != null) {
            if (Objects.equals(tmp.data, x)) {
                return tmp;
            }
            tmp = tmp.next;
        }
        return null;
    }

    public void removeFirst() {
        if (first == null) {
            System.out.println("The list is already empty");
            return;
        }
        first = first.next;
        if (first == null) {
            last = null;
        }
    }

    public void removeLast() {
        if (first == null) {
            System.out.println("The list is already empty");
            return;
        }
        if (first == last) {
            first = null;
            last = null;
            return;
        }
        Node<T> tmp = first.next;
        Node<T> prev = first;
        while (tmp.next != null) {
            tmp = tmp.next;
            prev = prev.next;
        }
        prev.next = null;
        last = prev;
    }

    public void removeAfter(Node<T> prev) {
        if (first == null) {
            System.out.println("The list is already empty");
            return;
        }
        if (prev == null || prev.next == null) {
            System.out.println("There are no elements after the prev");
            return;
        }
        if (prev.next == last) {
            last = prev;
        }
        prev.next = prev.next.next;
    }

    public boolean remove(T x) {
        if (first == null) {
            return false;
        }
        if (Objects.equals(first.data, x)) {
            first = first.next;
            if (first == null) {
                last = null;
            }
            return true;
        }
        Node<T> prev = first;
        Node<T> curr = first.next;
        while (curr != null) {
            if (Objects.equals(curr.data, x)) {
                prev.next = curr.next;
                if (curr == last) {
                    last = prev;
                }
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Node<T> tmp = first;
        while (tmp != null) {
            s.append(tmp.data).append("->");
            tmp = tmp.next;
        }
        s.append("Null");
        return s.toString();
    }
}