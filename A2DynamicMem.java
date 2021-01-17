// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.

public class A2DynamicMem extends A1DynamicMem {

    public A2DynamicMem() {
        super();
    }

    public A2DynamicMem(int size) {
        super(size);
    }

    public A2DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    // In A2, you need to test your implementation using BSTrees and AVLTrees.
    // No changes should be required in the A1DynamicMem functions.
    // They should work seamlessly with the newly supplied implementation of BSTrees
    // and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test
    // using BSTrees and AVLTrees.

    @Override
    public void Defragment() {
        Dictionary addrFreeBlk;
        if (type == 2)
            addrFreeBlk = new BSTree();
        else if (type == 3)
            addrFreeBlk = new AVLTree();
        else
            return;
        Dictionary curr = freeBlk.getFirst(), next;
        while (curr != null) {
            addrFreeBlk.Insert(curr.address, curr.size, curr.address);
            curr = curr.getNext();
        }
        curr = addrFreeBlk.getFirst();
        // traverse addrFreeBlk
        while (curr != null) {
            next = curr.getNext();
            if (next == null)
                break;
            // defragment the contiguous block
            if (curr.address + curr.size == next.address) {
                int address = curr.address, sizeCurr = curr.size, sizeNext = next.size;
                addrFreeBlk.Delete(curr);
                addrFreeBlk.Delete(next);
                freeBlk.Delete(new BSTree(address, sizeCurr, sizeCurr));
                freeBlk.Delete(new BSTree(address + sizeCurr, sizeNext, sizeNext));
                freeBlk.Insert(address, sizeCurr + sizeNext, sizeCurr + sizeNext);
                curr = addrFreeBlk.Insert(address, sizeCurr + sizeNext, address);
            } else
                curr = next;
        }
        curr = addrFreeBlk.getFirst();
        while (curr != null) {
            next = curr.getNext();
            addrFreeBlk.Delete(curr);
            curr = next;
        }
    }
}