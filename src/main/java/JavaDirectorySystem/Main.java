package JavaDirectorySystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import LinkedList.Node;
import Stack.LinkedListStack;

public class Main {

    public static void main(String[] args) {
        FileTree tree = new FileTree();
        LinkedListStack<String> parentStack = new LinkedListStack<>();
        File f = new File("myfiles.txt");

        try {
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;

                int indentCount = 0;
                int charIndex = 0;
                while (charIndex < line.length() && line.charAt(charIndex) == '\t') {
                    indentCount++;
                    charIndex++;
                }

                line = line.trim();
                boolean isDirectory = (line.charAt(0) == '\\');

                while (parentStack.size() > indentCount) {
                    parentStack.pop();
                }

                if (isDirectory) {
                    String name = line.substring(1);
                    Directory dir = new Directory(name);
                    if (tree.root == null) {
                        tree.appendRoot(dir);
                    } else {
                        tree.appendByPath(parentStack.toReverseString(), dir);
                    }
                    parentStack.push(name);
                } else {
                    String[] lineSplit = line.split("##");
                    TreeNode file = new TreeNode(
                        lineSplit[0],
                        lineSplit[1],
                        Integer.parseInt(lineSplit[2]),
                        lineSplit[3]
                    );
                    tree.appendByPath(parentStack.toReverseString(), file);
                }
            }
            sc.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + ex.getMessage());
            return;
        }

        if (tree.root == null) {
            System.out.println("File system is empty.");
            return;
        }

        tree.calculateDirectoryData(tree.root);

        Scanner input = new Scanner(System.in);
        Directory currentDir = tree.root;

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("LOCATION: " + tree.getFullPath(currentDir) +
                               " [" + currentDir.size + " bytes | " +
                               (currentDir.lastModificationDate != null ? currentDir.lastModificationDate : "-") +
                               " | " + currentDir.accessLevel + "]");
            System.out.println("------------------------------------------");

            if ("SYSTEM".equals(currentDir.accessLevel)) {
                System.out.println("[!] Access denied: This directory has SYSTEM access level. Contents cannot be listed.");
            } else {
                int index = 1;
                Node<TreeNode> currNode = currentDir.children.first;
                if (currNode == null) {
                    System.out.println("  (Empty directory)");
                } else {
                    while (currNode != null) {
                        TreeNode item = currNode.data;
                        String typeTag = (item instanceof Directory) ? "[D]" : "[F]";
                        String mod = (item.lastModificationDate != null) ? item.lastModificationDate : "-";
                        System.out.printf("  [%d] %s %-20s | %d bytes | %s | %s%n",
                                index, typeTag, item.name, item.size, mod, item.accessLevel);
                        currNode = currNode.next;
                        index++;
                    }
                }
            }

            System.out.println("------------------------------------------");
            System.out.println("Commands:");
            System.out.println("  [1..N]   : Open Directory / Display File Path");
            System.out.println("  [-1..-N] : Delete Item (e.g., -2)");
            System.out.println("  [0]      : Go to Parent Directory");
            System.out.println("  [d]      : Add New Directory");
            System.out.println("  [f]      : Add New File");
            System.out.println("  [p]      : Display Path");
            System.out.println("  [sn]     : Search by Name");
            System.out.println("  [se]     : Search by Extension");
            System.out.println("  [t]      : Print Full Tree");
            System.out.println("  [q]      : Quit");
            System.out.print("Choice: ");

            String choice = input.nextLine().trim();

            if (choice.equalsIgnoreCase("q")) {
                System.out.println("Program terminated.");
                break;
            } else if (choice.equals("0")) {
                if (currentDir.parent != null) {
                    currentDir = currentDir.parent;
                } else {
                    System.out.println("Already at the root directory.");
                }
            } else if (choice.equalsIgnoreCase("d")) {
                if (!"USER".equals(currentDir.accessLevel)) {
                    System.out.println("ERROR: Cannot add a directory inside a SYSTEM directory!");
                    continue;
                }
                String dirName;
                while (true) {
                    System.out.print("New directory name: ");
                    dirName = input.nextLine().trim();
                    if (!dirName.isEmpty()) {
                        break;
                    }
                    System.out.println("ERROR: Directory name cannot be empty!");
                }
                if (currentDir.hasChildWithName(dirName)) {
                    System.out.println("ERROR: An item named '" + dirName + "' already exists in this directory!");
                    continue;
                }
                currentDir.append(new Directory(dirName));
                tree.calculateDirectoryData(tree.root);
                System.out.println("Directory added.");

            } else if (choice.equalsIgnoreCase("f")) {
                if (!"USER".equals(currentDir.accessLevel)) {
                    System.out.println("ERROR: Cannot add a file inside a SYSTEM directory!");
                    continue;
                }
                String fileName;
                while (true) {
                    System.out.print("File name with extension (e.g., notes.txt): ");
                    fileName = input.nextLine().trim();
                    if (fileName.isEmpty()) {
                        System.out.println("ERROR: File name cannot be empty!");
                        continue;
                    }
                    int dotIndex = fileName.lastIndexOf('.');
                    if (dotIndex <= 0 || dotIndex == fileName.length() - 1) {
                        System.out.println("ERROR: File must have a valid extension (e.g., file.txt)!");
                        continue;
                    }
                    break;
                }
                if (currentDir.hasChildWithName(fileName)) {
                    System.out.println("ERROR: An item named '" + fileName + "' already exists in this directory!");
                    continue;
                }

                int size;
                while (true) {
                    System.out.print("Size (bytes): ");
                    try {
                        size = Integer.parseInt(input.nextLine().trim());
                        if (size < 0) {
                            System.out.println("ERROR: Size cannot be negative!");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: Please enter a valid integer for size!");
                    }
                }

                String today = LocalDate.now().format(DateTimeFormatter.ofPattern("d.M.yyyy"));

                currentDir.append(new TreeNode(fileName, today, size, "USER"));
                tree.calculateDirectoryData(tree.root);
                System.out.println("File added.");

            } else if (choice.equalsIgnoreCase("p")) {
                System.out.print("Enter item number (in current directory) or item name: ");
                String targetInput = input.nextLine().trim();
                if (targetInput.isEmpty()) {
                    System.out.println("ERROR: Input cannot be empty!");
                    continue;
                }
                boolean displayed = false;
                if ("USER".equals(currentDir.accessLevel)) {
                    try {
                        int num = Integer.parseInt(targetInput);
                        if (num > 0) {
                            TreeNode selected = getChildByIndex(currentDir, num);
                            if (selected != null) {
                                System.out.println("Full Path: " + tree.getFullPath(selected));
                                displayed = true;
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                if (!displayed) {
                    if (!tree.displayPathByName(tree.root, targetInput)) {
                        System.out.println("Item not found: " + targetInput);
                    }
                }

            } else if (choice.equalsIgnoreCase("sn")) {
                System.out.print("Enter file/directory name to search: ");
                String qName = input.nextLine().trim();
                System.out.println("--- Search Results (under " + currentDir.name + ") ---");
                if (!tree.searchByName(currentDir, qName)) {
                    System.out.println("No matching items found.");
                }

            } else if (choice.equalsIgnoreCase("se")) {
                System.out.print("Enter extension to search (e.g., pdf or .txt): ");
                String qExt = input.nextLine().trim();
                System.out.println("--- Extension Search Results (under " + currentDir.name + ") ---");
                if (!tree.searchByExtension(currentDir, qExt)) {
                    System.out.println("No matching files found.");
                }

            } else if (choice.equalsIgnoreCase("t")) {
                System.out.println("\n--- FULL FILE SYSTEM TREE ---");
                tree.printTree();

            } else {
                try {
                    int num = Integer.parseInt(choice);
                    if (!"USER".equals(currentDir.accessLevel)) {
                        System.out.println("ERROR: Cannot access or modify contents of a SYSTEM directory!");
                        continue;
                    }
                    if (num > 0) {
                        TreeNode selected = getChildByIndex(currentDir, num);
                        if (selected == null) {
                            System.out.println("Invalid index!");
                        } else if (selected instanceof Directory) {
                            if ("SYSTEM".equals(selected.accessLevel)) {
                                System.out.println("ERROR: Cannot open '" + selected.name + "' because it has SYSTEM access level!");
                            } else {
                                currentDir = (Directory) selected;
                            }
                        } else {
                            System.out.println("Full File Path: " + tree.getFullPath(selected));
                        }
                    } else if (num < 0) {
                        int delIndex = Math.abs(num);
                        TreeNode target = getChildByIndex(currentDir, delIndex);
                        if (target == null) {
                            System.out.println("Invalid delete index!");
                        } else if ("SYSTEM".equals(target.accessLevel)) {
                            System.out.println("ERROR: '" + target.name + "' has SYSTEM access level and cannot be deleted!");
                        } else if (target instanceof Directory && ((Directory) target).containsSystemItem()) {
                            System.out.println("ERROR: '" + target.name + "' contains SYSTEM items and cannot be deleted!");
                        } else {
                            currentDir.children.remove(target);
                            tree.calculateDirectoryData(tree.root);
                            System.out.println("'" + target.name + "' deleted successfully.");
                            while ("SYSTEM".equals(currentDir.accessLevel) && currentDir.parent != null) {
                                System.out.println("WARNING: '" + currentDir.name + "' now has SYSTEM access level. Returning to parent directory.");
                                currentDir = currentDir.parent;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Unknown command!");
                }
            }
        }
        input.close();
    }

    private static TreeNode getChildByIndex(Directory dir, int targetIndex) {
        int count = 1;
        Node<TreeNode> curr = dir.children.first;
        while (curr != null) {
            if (count == targetIndex) return curr.data;
            count++;
            curr = curr.next;
        }
        return null;
    }
}