package JavaDirectorySystem;

import LinkedList.LinkedList;
import LinkedList.Node;

public class Directory extends TreeNode {
    LinkedList<TreeNode> children;

    public Directory(String name) {
        super(name);
        this.children = new LinkedList<>();
        this.accessLevel = "USER";
    }

    public Directory(Directory parent, String name, String lastModificationDate, int size, String accessLevel, LinkedList<TreeNode> children) {
        super(parent, name, lastModificationDate, size, accessLevel);
        this.children = (children != null) ? children : new LinkedList<>();
    }

    public Directory findChildren(String name) {
        Node<TreeNode> current = children.first;
        while (current != null) {
            if (current.data instanceof Directory && name.equals(current.data.name)) {
                return (Directory) current.data;
            }
            current = current.next;
        }
        return null;
    }

    public boolean hasChildWithName(String name) {
        Node<TreeNode> current = children.first;
        while (current != null) {
            if (name.equalsIgnoreCase(current.data.name)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void append(TreeNode node) {
        node.parent = this;
        children.insertLast(node);
    }

    public boolean containsSystemItem() {
        Node<TreeNode> current = children.first;
        while (current != null) {
            TreeNode child = current.data;
            if ("SYSTEM".equals(child.accessLevel)) {
                return true;
            }
            if (child instanceof Directory && ((Directory) child).containsSystemItem()) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}