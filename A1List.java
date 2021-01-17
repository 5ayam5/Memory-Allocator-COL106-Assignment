// Implements Dictionary using Doubly Linked List (DLL)
// Implement the following functions using the specifications provided in the class List

public class A1List extends List {

    private A1List next; // Next Node
    private A1List prev; // Previous Node

    public A1List(int address, int size, int key) {
        super(address, size, key);
    }

    public A1List() {
        super(-1, -1, -1);
        // This acts as a head Sentinel

        A1List tailSentinel = new A1List(-1, -1, -1); // Intiate the tail sentinel

        this.next = tailSentinel;
        tailSentinel.prev = this;
    }

    public A1List Insert(int address, int size, int key) {
        // make new node
        A1List a1List = new A1List(address, size, key);
        // pointer updates
        a1List.next = this.next;
        this.next = a1List;
        a1List.next.prev = a1List;
        a1List.prev = this;
        return a1List;
    }

    public boolean Delete(Dictionary d) {
        // deleting null is not possible
        if (d == null)
            return false;
        // move to the first node to prevent birectional iteration and make use of the
        // unidirectional findNext implementation
        A1List a1List = this.getFirst();
        while (a1List != null) {
            // find the next node with same key value
            a1List = a1List.findNext(d.key, true);
            if (isEqual(d, a1List)) {
                // pointer updates
                a1List.prev.next = a1List.next;
                a1List.next.prev = a1List.prev;
                a1List.prev = a1List.next = null;
                return true;
            }
            // move one to the right to search for next occurence
            if (a1List != null)
                a1List = a1List.getNext();
        }
        return false;
    }

    public A1List Find(int k, boolean exact) {
        return this.getFirst() == null ? null : this.getFirst().findNext(k, exact);
    }

    /** @return first node that is found at or after the current location */
    private A1List findNext(int k, boolean exact) {
        A1List a1List = this;
        // loop until reaching tail or element found
        while (a1List != null) {
            if (a1List.key == k || (!exact && a1List.key > k))
                return a1List;
            a1List = a1List.getNext();
        }
        return null;
    }

    public A1List getFirst() {
        A1List a1List = this;
        // traverse back until head
        while (a1List.prev != null) {
            a1List = a1List.prev;
        }
        // return "next" node after head
        return a1List.getNext();
    }

    public A1List getNext() {
        // if next node is tail or *somehow* called at tail
        if (this.next == null || this.next.next == null)
            return null;
        return this.next;
    }

    private static boolean isEqual(Dictionary d1, Dictionary d2) {
        return d1 != null && d2 != null && d1.key == d2.key && d1.address == d2.address && d1.size == d2.size;
    }

    public boolean sanity() {
        try {
            if (this.isCyclic())
                return false;
            A1List curr = this.getFirst();
            // potentially empty list
            if (curr == null)
                curr = this;
            // not at (expected) head
            if (curr.prev != null)
                curr = curr.prev;
            // not a valid head
            if (!isEqual(curr, new A1List(-1, -1, -1)))
                return false;
            // single node
            if (curr.next == null)
                return false;
            // traverse forth
            while (curr.next != null) {
                if (curr.next.prev != curr)
                    return false;
                curr = curr.next;
            }
            return isEqual(curr, new A1List(-1, -1, -1));
        } catch (Exception e) {
            // unforseen exceptions imply in-sane A1List
            return false;
        }
    }

    private boolean isCyclic() {
        // implementation of the famous Floyd cycle detection algorithm
        A1List tort = this, hare = this;
        while (hare != null && hare.next != null) {
            tort = tort.next;
            hare = hare.next.next;
            if (tort == hare)
                return true;
        }
        tort = this;
        hare = this;
        while (hare != null && hare.prev != null) {
            tort = tort.prev;
            hare = hare.prev.prev;
            if (tort == hare)
                return true;
        }
        return false;
    }
}
