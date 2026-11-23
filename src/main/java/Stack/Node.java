
package Stack;


public class Node<T> {
    public T data;
    public Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    @Override
    public String toString() {
        String s = "";
        s += "Node with data: " + data.toString();
        return s; 
    }

    
    
    
    
    
}
