// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {

    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return;
    }

    // In A1, you need to implement the Allocate and Free functions for the class
    // A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only
    // (A1List.java).

    public int Allocate(int blockSize) {
        // faulty input
        if (blockSize <= 0)
            return -1;
        // find the required free block
        Dictionary free = freeBlk.Find(blockSize, false);
        // required block not found
        if (free == null)
            return -1;
        int address = free.address;
        // insert the new node in allocBlk
        allocBlk.Insert(address, blockSize, address);
        // insert the split node in freeBlk
        if (free.size != blockSize)
            freeBlk.Insert(address + blockSize, free.size - blockSize, free.size - blockSize);
        // delete the original node from freeBlk
        freeBlk.Delete(free);
        return address;
    }

    public int Free(int startAddr) {
        // find the block to be freed
        Dictionary toFree = allocBlk.Find(startAddr, true);
        // such a block DNE
        if (toFree == null)
            return -1;
        // add it to freeBlk
        freeBlk.Insert(startAddr, toFree.size, toFree.size);
        // remove it from allocBlk
        allocBlk.Delete(toFree);
        return 0;
    }
}