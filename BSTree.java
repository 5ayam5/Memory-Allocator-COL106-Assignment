// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java

public class BSTree extends Tree {

    protected BSTree left, right; // Children.
    protected BSTree parent; // Parent pointer.

    public BSTree() {
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root!
        // and left child will always be null.
    }

    public BSTree(int address, int size, int key) {
        super(address, size, key);
    }

    public BSTree Insert(int address, int size, int key) {
        BSTree bsTree = this, newNode = new BSTree(address, size, key);
        // reach sentinel
        while (bsTree.parent != null)
            bsTree = bsTree.parent;
        // if empty, insert the first node
        if (bsTree.right == null) {
            bsTree.right = newNode;
            newNode.parent = bsTree;
            return newNode;
        }
        // access the actual root
        bsTree = bsTree.right;
        while (true) {
            int compare = compare(newNode, bsTree);
            if (compare == -1) {
                // traverse the left subtree
                if (bsTree.left == null) {
                    bsTree.left = newNode;
                    newNode.parent = bsTree;
                    return newNode;
                }
                bsTree = bsTree.left;
            } else if (compare == 1) {
                // traverse the right subtree
                if (bsTree.right == null) {
                    bsTree.right = newNode;
                    newNode.parent = bsTree;
                    return newNode;
                }
                bsTree = bsTree.right;
            }
            // duplicate element
            else
                return null;
        }
    }

    public boolean Delete(Dictionary e) {
        if (e == null)
            return false;
        BSTree bsTree = this;
        // reach sentinel
        while (bsTree.parent != null)
            bsTree = bsTree.parent;
        // access the actual root
        bsTree = bsTree.right;
        while (bsTree != null) {
            int compare = compare(e, bsTree);
            if (compare == -1)
                bsTree = bsTree.left;
            else if (compare == 1)
                bsTree = bsTree.right;
            else {
                // perform deletion if leaf or only one child
                if (bsTree.right == null)
                    bsTree.pointerUpdates(bsTree.left);
                else {
                    // replace current data with next's data and delete next
                    BSTree nextTree = bsTree.getNext();
                    bsTree.updateWith(nextTree);
                    nextTree.pointerUpdates(nextTree.right);
                }
                return true;
            }
        }
        return false;
    }

    public BSTree Find(int key, boolean exact) {
        BSTree bsTree = this, least = null;
        while (bsTree.parent != null)
            bsTree = bsTree.parent;
        bsTree = bsTree.right;
        while (bsTree != null) {
            if (bsTree.key < key)
                bsTree = bsTree.right;
            else {
                // assign bsTree to least since it is the current least element with key >=
                // search_key, and search in left subtree for a smaller one
                least = bsTree;
                bsTree = bsTree.left;
            }
        }
        return least != null && (least.key == key || !exact) ? least : null;
    }

    public BSTree getFirst() {
        BSTree bsTree = this;
        while (bsTree.parent != null)
            bsTree = bsTree.parent;
        bsTree = bsTree.right;
        // return null if empty tree
        if (bsTree == null)
            return null;
        // keep going left till leaf
        while (bsTree.left != null)
            bsTree = bsTree.left;
        return bsTree;
    }

    public BSTree getNext() {
        BSTree bsTree = this;
        if (bsTree.parent == null)
            return null;
        // if right subtree is non-empty, return least element in right subtree
        if (bsTree.right != null) {
            bsTree = bsTree.right;
            while (bsTree.left != null)
                bsTree = bsTree.left;
            return bsTree;
        }
        // find the first right parent
        while (bsTree.parent != null && bsTree.parent.right == bsTree)
            bsTree = bsTree.parent;
        return bsTree.parent;
    }

    protected void updateWith(BSTree nextTree) {
        this.address = nextTree.address;
        this.size = nextTree.size;
        this.key = nextTree.key;
    }

    protected void pointerUpdates(BSTree child) {
        if (this.parent.right == this)
            this.parent.right = child;
        else
            this.parent.left = child;
        if (child != null)
            child.parent = this.parent;
        this.left = this.right = this.parent = null;
    }

    protected static int compare(Dictionary d, Dictionary e) {
        if (d.key < e.key || (d.key == e.key && d.address < e.address))
            return -1;
        if (d.key > e.key || (d.key == e.key && d.address > e.address))
            return 1;
        return 0;
    }

    public boolean sanity() {
        try {
            // check for loops
            boolean[] visited = new boolean[DynamicMem.M + 1];
            if (!dfs(this, null, visited))
                return false;
            // reach sentinel
            BSTree bsTree = this;
            while (bsTree.parent != null)
                bsTree = bsTree.parent;
            // verify sentinel
            if (bsTree.left != null || compare(bsTree, new BSTree(-1, -1, -1)) != 0)
                return false;
            // check search property
            return bsTree.right == null || checkBSTree(bsTree.right, false).size == 1;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean dfs(BSTree bsTree, BSTree prevBTree, boolean[] visited) {
        if (bsTree == null)
            return true;
        int address = bsTree.address >= 0 ? bsTree.address : DynamicMem.M;
        if (visited[address])
            return false;
        visited[address] = true;
        boolean ans = true;
        if (bsTree.left != prevBTree)
            ans &= dfs(bsTree.left, bsTree, visited);
        if (bsTree.right != prevBTree)
            ans &= dfs(bsTree.right, bsTree, visited);
        if (bsTree.parent != prevBTree)
            ans &= dfs(bsTree.parent, prevBTree, visited);
        return ans;
    }

    private static BSTree checkBSTree(BSTree root, boolean left) {
        BSTree bsTree = new BSTree(root.address, 1, root.key);
        if (root.left != null) {
            if (root.left.parent != root) {
                bsTree.size = 0;
                return bsTree;
            }
            BSTree leftBSTree = checkBSTree(root.left, true);
            if (leftBSTree.size == 0 || compare(leftBSTree, root) != -1) {
                bsTree.size = 0;
                return bsTree;
            }
            if (!left)
                bsTree = leftBSTree;
        }
        if (root.right != null) {
            if (root.right.parent != root) {
                bsTree.size = 0;
                return bsTree;
            }
            BSTree rightBSTree = checkBSTree(root.right, false);
            if (rightBSTree.size == 0 || compare(rightBSTree, root) != 1) {
                bsTree.size = 0;
                return bsTree;
            }
            if (left)
                bsTree = rightBSTree;
        }
        return bsTree;
    }
}
