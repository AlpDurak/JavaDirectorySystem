/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaDirectorySystem;

/**
 *
 * @author alpdu
 */
public class TreeNode {
    Directory parent;
    String name;
    String lastModificationDate;
    int size;
    String accessLevel;
    
    public TreeNode(String name) {
        this.name = name;
    }
    
    public TreeNode(String name, String lastModificationDate, int size, String accessLevel) {
        this.name = name;
        this.lastModificationDate = lastModificationDate;
        this.size = size;
        this.accessLevel = accessLevel;
    }

    public TreeNode(Directory parent, String name, String lastModificationDate, int size, String accessLevel) {
        this.parent = parent;
        this.name = name;
        this.lastModificationDate = lastModificationDate;
        this.size = size;
        this.accessLevel = accessLevel;
    }
}
