// Class: Height balanced AVL Tree
// Binary Search Tree

public class AVLTree extends BSTree {

    private int height; // The height of the subtree

    public AVLTree() {
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root!
        // and left child will always be null.

    }

    public AVLTree(int address, int size, int key) {
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions.
    // Some of the functions may be directly inherited from the BSTree class and
    // nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.

    @Override
    public AVLTree Insert(int address, int size, int key) {
        BSTree bsTree = super.Insert(address, size, key);
        AVLTree avlTree = new AVLTree(address, size, key);
        avlTree.parent = bsTree.parent;
        if (avlTree.parent.left == bsTree)
            avlTree.parent.left = avlTree;
        else
            avlTree.parent.right = avlTree;
        bsTree.left = bsTree.right = bsTree.parent = null;
        heightBalance(avlTree);
        return avlTree;
    }

    @Override
    public boolean Delete(Dictionary e) {
        AVLTree avlTree = this.findNode(e);
        if (avlTree == null)
            return false;
        if (avlTree.right == null) {
            AVLTree deleteAvlTree = avlTree;
            avlTree = (AVLTree) deleteAvlTree.parent;
            deleteAvlTree.pointerUpdates(deleteAvlTree.left);
        } else {
            AVLTree deleteAvlTree = avlTree.getNext();
            avlTree.updateWith(deleteAvlTree);
            avlTree = (AVLTree) deleteAvlTree.parent;
            deleteAvlTree.pointerUpdates(deleteAvlTree.right);
        }
        int balance = avlTree.getBalance();
        if (balance > 0)
            heightBalance((AVLTree) avlTree.right);
        else if (balance < 0)
            heightBalance((AVLTree) avlTree.left);
        else
            heightBalance(avlTree);
        return true;
    }

    @Override
    public AVLTree Find(int key, boolean exact) {
        return (AVLTree) super.Find(key, exact);
    }

    @Override
    public AVLTree getFirst() {
        return (AVLTree) super.getFirst();
    }

    @Override
    public AVLTree getNext() {
        return (AVLTree) super.getNext();
    }

    private AVLTree findNode(Dictionary e) {
        if (e == null)
            return null;
        AVLTree avlTree = this;
        while (avlTree.parent != null)
            avlTree = (AVLTree) avlTree.parent;
        avlTree = (AVLTree) avlTree.right;
        while (avlTree != null) {
            int compare = compare(e, avlTree);
            if (compare == 1)
                avlTree = (AVLTree) avlTree.right;
            else if (compare == -1)
                avlTree = (AVLTree) avlTree.left;
            else
                break;
        }
        return avlTree;
    }

    private static void heightBalance(AVLTree avlTree) {
        while (avlTree.parent != null) {
            avlTree.updateHeight();
            int balance = avlTree.getBalance();
            if (balance < -1) {
                AVLTree cAvlTree = (AVLTree) avlTree.left;
                if (cAvlTree.getBalance() <= 0)
                    avlTree = avlTree.rightRotate(cAvlTree);
                else
                    avlTree = avlTree.rightRotate(cAvlTree.leftRotate((AVLTree) cAvlTree.right));
            } else if (balance > 1) {
                AVLTree cAvlTree = (AVLTree) avlTree.right;
                if (cAvlTree.getBalance() >= 0)
                    avlTree = avlTree.leftRotate(cAvlTree);
                else
                    avlTree = avlTree.leftRotate(cAvlTree.rightRotate((AVLTree) cAvlTree.left));
            } else
                avlTree = (AVLTree) avlTree.parent;
        }
        avlTree.updateHeight();
    }

    private void updateHeight() {
        this.height = 1 + Math.max(this.left == null ? -1 : ((AVLTree) this.left).height,
                this.right == null ? -1 : ((AVLTree) this.right).height);
    }

    private int getBalance() {
        return (this.right == null ? -1 : ((AVLTree) this.right).height)
                - (this.left == null ? -1 : ((AVLTree) this.left).height);
    }

    private AVLTree leftRotate(AVLTree cAvlTree) {
        cAvlTree.parent = this.parent;
        if (this.parent.left == this)
            this.parent.left = cAvlTree;
        else
            this.parent.right = cAvlTree;
        this.parent = cAvlTree;
        this.right = cAvlTree.left;
        if (this.right != null)
            this.right.parent = this;
        cAvlTree.left = this;
        this.updateHeight();
        return cAvlTree;
    }

    private AVLTree rightRotate(AVLTree cAvlTree) {
        cAvlTree.parent = this.parent;
        if (this.parent.left == this)
            this.parent.left = cAvlTree;
        else
            this.parent.right = cAvlTree;
        this.parent = cAvlTree;
        this.left = cAvlTree.right;
        if (this.left != null)
            this.left.parent = this;
        cAvlTree.right = this;
        this.updateHeight();
        return cAvlTree;
    }

    @Override
    public boolean sanity() {
        try {
            if (!super.sanity())
                return false;
            AVLTree avlTree = this;
            while (avlTree.parent != null)
                avlTree = (AVLTree) avlTree.parent;
            if (avlTree.height != (avlTree.right == null ? -1 : ((AVLTree) avlTree.right).height) + 1)
                return false;
            return checkHeightProperty((AVLTree) avlTree.right);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkHeightProperty(AVLTree avlTree) {
        if (avlTree == null)
            return true;
        int balance = avlTree.getBalance();
        return balance <= 1 && balance >= -1 && checkHeightProperty((AVLTree) avlTree.left)
                && checkHeightProperty((AVLTree) avlTree.right);

    }
}
