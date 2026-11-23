package JavaDirectorySystem;

import LinkedList.Node;

public class FileTree {
    Directory root;

    public FileTree() {}

    public FileTree(Directory root) {
        this.root = root;
    }

    public void appendRoot(Directory node) {
        if (this.root == null) {
            this.root = node;
        }
    }

    public void appendByPath(String path, TreeNode node) {
        if (root == null || path == null) {
            return;
        }
        String[] pathDirectories = path.split(" -> ");
        Directory current = root;
        for (int i = 1; i < pathDirectories.length; i++) {
            current = current.findChildren(pathDirectories[i]);
            if (current == null) {
                return;
            }
        }
        current.append(node);
    }

    public void calculateDirectoryData(Directory focus) {
        if (focus == null) {
            return;
        }

        int totalSize = 0;
        String latestModDate = null;
        boolean hasChildren = (focus.children != null && focus.children.first != null);
        boolean allSystem = hasChildren;

        if (hasChildren) {
            Node<TreeNode> current = focus.children.first;
            while (current != null) {
                TreeNode child = current.data;

                if (child instanceof Directory) {
                    calculateDirectoryData((Directory) child);
                }

                totalSize += child.size;

                if (child.lastModificationDate != null &&
                   (latestModDate == null || isNewerDate(child.lastModificationDate, latestModDate))) {
                    latestModDate = child.lastModificationDate;
                }

                if (!"SYSTEM".equals(child.accessLevel)) {
                    allSystem = false;
                }

                current = current.next;
            }
        }

        focus.size = totalSize;
        focus.lastModificationDate = latestModDate;
        focus.accessLevel = allSystem ? "SYSTEM" : "USER";
    }

    private boolean isNewerDate(String candidate, String currentLatest) {
        String[] c = candidate.split("\\.");
        String[] l = currentLatest.split("\\.");
        int cYear = Integer.parseInt(c[2]), lYear = Integer.parseInt(l[2]);
        if (cYear != lYear) return cYear > lYear;
        int cMonth = Integer.parseInt(c[1]), lMonth = Integer.parseInt(l[1]);
        if (cMonth != lMonth) return cMonth > lMonth;
        return Integer.parseInt(c[0]) > Integer.parseInt(l[0]);
    }

    public String getFullPath(TreeNode node) {
        if (node == null) return "";
        if (node.parent == null) return "\\" + node.name;
        return getFullPath(node.parent) + "\\" + node.name;
    }

    public boolean displayPathByName(Directory startDir, String targetName) {
        if (startDir == null) return false;
        boolean found = false;
        if (startDir.name.equalsIgnoreCase(targetName)) {
            System.out.println("Full Path [D]: " + getFullPath(startDir));
            found = true;
        }
        Node<TreeNode> current = startDir.children.first;
        while (current != null) {
            TreeNode child = current.data;
            if (child instanceof Directory) {
                if (displayPathByName((Directory) child, targetName)) {
                    found = true;
                }
            } else {
                String nameWithoutExt = child.name;
                int dotIndex = child.name.lastIndexOf('.');
                if (dotIndex > 0) {
                    nameWithoutExt = child.name.substring(0, dotIndex);
                }
                if (child.name.equalsIgnoreCase(targetName) || nameWithoutExt.equalsIgnoreCase(targetName)) {
                    System.out.println("Full Path [F]: " + getFullPath(child));
                    found = true;
                }
            }
            current = current.next;
        }
        return found;
    }

    public boolean searchByName(Directory startDir, String targetName) {
        if (startDir == null) return false;
        boolean found = false;
        if (startDir.name.equalsIgnoreCase(targetName)) {
            System.out.println("Found [Directory]: " + getFullPath(startDir));
            found = true;
        }
        Node<TreeNode> current = startDir.children.first;
        while (current != null) {
            TreeNode child = current.data;
            if (child instanceof Directory) {
                if (searchByName((Directory) child, targetName)) {
                    found = true;
                }
            } else {
                String nameWithoutExt = child.name;
                int dotIndex = child.name.lastIndexOf('.');
                if (dotIndex > 0) {
                    nameWithoutExt = child.name.substring(0, dotIndex);
                }
                if (child.name.equalsIgnoreCase(targetName) || nameWithoutExt.equalsIgnoreCase(targetName)) {
                    System.out.println("Found [File]: " + getFullPath(child));
                    found = true;
                }
            }
            current = current.next;
        }
        return found;
    }

    public boolean searchByExtension(Directory startDir, String ext) {
        if (startDir == null) return false;
        boolean found = false;
        String cleanExt = ext.startsWith(".") ? ext.toLowerCase() : "." + ext.toLowerCase();

        Node<TreeNode> current = startDir.children.first;
        while (current != null) {
            TreeNode child = current.data;
            if (child instanceof Directory) {
                if (searchByExtension((Directory) child, cleanExt)) {
                    found = true;
                }
            } else if (child.name.toLowerCase().endsWith(cleanExt)) {
                System.out.println("Found [File]: " + getFullPath(child));
                found = true;
            }
            current = current.next;
        }
        return found;
    }

    public void printTree() {
        if (root == null) {
            System.out.println("Tree is empty.");
            return;
        }
        printRecursive(root, 0);
    }

    private void printRecursive(TreeNode node, int depth) {
        String indent = "  ".repeat(depth);
        String modDate = (node.lastModificationDate != null) ? node.lastModificationDate : "-";
        String details = " [" + node.size + " bytes | " + modDate + " | " + node.accessLevel + "]";

        if (node instanceof Directory) {
            System.out.println(indent + "[D] " + node.name + details);
            Node<TreeNode> current = ((Directory) node).children.first;
            while (current != null) {
                printRecursive(current.data, depth + 1);
                current = current.next;
            }
        } else {
            System.out.println(indent + "[F] " + node.name + details);
        }
    }
}