import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Implementation of a BST.
 *
 * @author Thang Huynh
 * @version 1.0
 *
 */
public class BST<T extends Comparable<? super T>> {

    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {}

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        // The Exception
        if (data == null) {
            throw new IllegalArgumentException("The collection was null");
        }

        // Loop through the collection
        for (T elements: data) {
            if (elements == null) {
                throw new IllegalArgumentException("One of the elements in the collection was null");
            }
            add(elements);
        }

    }

    /**
     * Adds the data to the tree.
     *
     * This is done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        // The exception
        if (data == null) {
            throw new IllegalArgumentException("The data is null");
        }

        root = rAdd(root, data); // Recursive call
    }

    /**
     * Recursively adds the data to the tree.
     *
     * @param curr the current BSTNode
     * @param data the data to add
     * @return the BSTNode after adding
     */
    private BSTNode<T> rAdd(BSTNode<T> curr, T data) {
        BSTNode<T> aNode = new BSTNode<>(data); // Creates the new node with the data
        if (curr == null) {
            size++;
            return aNode;
        } else if (curr.getData().compareTo(data) > 0) {
            curr.setLeft(rAdd(curr.getLeft(), data));
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(rAdd(curr.getRight(), data));
        } else if (curr.getData().compareTo(data) == 0) {
            return null; // checks for a duplicate, and not increment the size
        }
        return curr;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Uses the predecessor to
     * replace the data. Uses recursion to find and remove the predecessor
     *
     * Does not return the same data that was passed in. Returns the data that
     * was stored in the tree.
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        // The Exceptions
        if (data == null) {
            throw new IllegalArgumentException("The data specified was null");
        }

        BSTNode<T> dummy = new BSTNode<>(null); // The Dummy Node
        root = rRemoved(root, data, dummy);
        return dummy.getData();
    }

    /**
     * Recursively removes the data in the tree.
     *
     * @param curr the current BSTNode
     * @param data the data to remove
     * @param dummy the node that is a placeholder for the removed node
     * @return the BSTNode after removing
     * @throws java.util.NoSuchElementException  if the data is not in the tree
     */
    private BSTNode<T> rRemoved(BSTNode<T> curr, T data, BSTNode<T> dummy) {
        if (curr == null) {
            throw new NoSuchElementException("That data is not in the tree");
        } else if (curr.getData().compareTo(data) > 0) {
            curr.setLeft(rRemoved(curr.getLeft(), data, dummy));
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(rRemoved(curr.getRight(), data, dummy));
        } else {
            dummy.setData(curr.getData());
            size--;

            // 0 Children Case
            if (curr.getLeft() == null && curr.getRight() == null) {
                return null;
            } else if (curr.getLeft() != null && curr.getRight() == null) { // The 1 Children Cases
                return curr.getLeft();
            } else if (curr.getRight() != null && curr.getLeft() == null) {
                return curr.getRight();
            } else { //The two children case
                BSTNode<T> dummy2 = new BSTNode<>(null);
                curr.setLeft(removePredecessor(curr.getLeft(), dummy2));
                curr.setData(dummy2.getData());
            }
        }
        return curr;
    }

    /**
     * Recursively finds and returns the predecessor node in the current tree/subtree
     *
     * @param curr the current BSTNode
     * @param dummy the node that is a placeholder for the removed node
     * @return the BSTNode predecessor
     */
    private BSTNode<T> removePredecessor(BSTNode<T> curr, BSTNode<T> dummy) {
        if (curr.getRight() == null) {
            dummy.setData(curr.getData());
            return curr.getLeft();
        } else {
            curr.setRight(removePredecessor(curr.getRight(), dummy));
        }
        return curr;
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * Done recursively.
     *
     * Does not return the same data that was passed in. Returns the data that
     * was stored in the tree.
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        // The Exceptions
        if (data == null) {
            throw new IllegalArgumentException("The data provided was null");
        }

        return rGet(root, data);
    }

    /**
     * Recursively finds and returns the data of a specific node in the tree
     *
     * @param curr the current BSTNode
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    private T rGet(BSTNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("The data was not in the tree");
        } else if (curr.getData().compareTo(data) > 0) {
            return rGet(curr.getLeft(), data);
        } else if (curr.getData().compareTo(data) < 0) {
            return rGet(curr.getRight(), data);
        } else {
            return curr.getData();
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Done recursively.
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        //The Exception
        if (data == null) {
            throw new IllegalArgumentException("The data entered was null");
        }
        return rContains(root, data);
    }

    /**
     * Recursively finds and returns if a tree contains a specific data
     *
     * @param curr the current BSTNode
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     */
    private boolean rContains(BSTNode<T> curr, T data) {
        if (curr == null) {
            return false;
        } else if (curr.getData().compareTo(data) > 0) {
            return rContains(curr.getLeft(), data);
        } else if (curr.getData().compareTo(data) < 0) {
            return rContains(curr.getRight(), data);
        } else {
            return true;
        }
    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * Done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> theList = new ArrayList<>(size);
        rPreorder(root, theList);
        return theList;
    }

    /**
     * Recursively traverse the tree in Preorder
     *
     * @param curr the current BSTNode
     * @param theList the list that the data is being added to
     */
    private void rPreorder(BSTNode<T> curr, List<T> theList) {
        if (curr != null) {
            theList.add(curr.getData());
            rPreorder(curr.getLeft(), theList);
            rPreorder(curr.getRight(), theList);
        }
    }

    /**
     * Generate an in-order traversal of the tree.
     *
     * Done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> theList = new ArrayList<>(size);
        rInorder(root, theList);
        return theList;
    }

    /**
     * Recursively traverse the tree inorder
     *
     * @param curr the current BSTNode
     * @param theList the list that the data is being added to
     */
    private void rInorder(BSTNode<T> curr, List<T> theList) {
        if (curr != null) {
            rInorder(curr.getLeft(), theList);
            theList.add(curr.getData());
            rInorder(curr.getRight(), theList);
        }
    }


    /**
     * Generate a post-order traversal of the tree.
     *
     * Done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> theList = new ArrayList<>(size);
        rPostorder(root, theList);
        return theList;
    }

    /**
     * Recursively traverse the tree in Postorder
     *
     * @param curr the current BSTNode
     * @param theList the list that the data is being added to
     */
    private void rPostorder(BSTNode<T> curr, List<T> theList) {
        if (curr != null) {
            rPostorder(curr.getLeft(), theList);
            rPostorder(curr.getRight(), theList);
            theList.add(curr.getData());
        }
    }
    /**
     * Generate a level-order traversal of the tree.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        Queue<BSTNode<T>> theList = new LinkedList<>(); //Creates a Queue
        List<T> levelOrderList = new ArrayList<>(size); //This is the list we are returning
        theList.add(root); //Adds the root of the tree to the Queue

        while (!theList.isEmpty()) { //While the Queue is not empty
            int count = 1; //Keeps track of current level
            BSTNode<T> curr = theList.remove(); //Remove deque the node and set it as the current one
            levelOrderList.add(curr.getData()); //Adds it to the returning list
            if (curr.getLeft() != null) { //Enqueue the left node
                theList.add(curr.getLeft());
            }
            if (curr.getRight() != null) { //Enqueue the right node
                theList.add(curr.getRight());
            }
        }
        return levelOrderList; //Returns the Level Order List
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return rHeight(root);
    }

    /**
     * Recursively traverse the tree to find the height of the root node
     *
     * @param curr the current BSTNode
     * @return the height value of the current node
     */
    private int rHeight(BSTNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        int leftMax = rHeight(curr.getLeft());
        int rightMax = rHeight(curr.getRight());

        if (leftMax > rightMax) {
            return leftMax + 1;
        } else {
            return rightMax + 1;
        }
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }
}
