import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Implementation of an AVL Tree.
 *
 * @author Thang Huynh
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> {
    private AVLNode<T> root;
    private int size;

    /**
     * a no-argument constructor that should initialize an empty AVL.
     * <p>
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {}

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it appears in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data entered was null");
        }
        for (T i : data) {
            if (i == null) {
                throw new IllegalArgumentException("One of the elements in the collection is null");
            }
            add(i);
        }
    }

    /**
     * Adds the data to the AVL. Starts by adding it as a leaf like in a regular
     * BST and then rotate the tree as needed.
     * 
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     * 
     * Recalculates heights and balance factors going up the tree, rebalancing if necessary.
     *
     * @param data the data to be added
     * @throws java.lang.IllegalArgumentException if the data is null
     */
    public void add(T data) {
        //The Exception
        if (data == null) {
            throw new IllegalArgumentException("The data entered was null");
        }
        root = rAdd(root, data); // Recursive call
    }

    /**
     * Recursively adds the data to the tree.
     *
     * Also updates the Height and Balance factor of the current node
     * and performs a rotation when the Balance Factor is > 1 or < -1
     *
     * @param curr the current AVLNode
     * @param data the data to add
     * @return the AVLNode after adding
     */
    private AVLNode<T> rAdd(AVLNode<T> curr, T data) {
        if (curr == null) {
            size++;
            return new AVLNode<>(data);
        } else if (curr.getData().compareTo(data) > 0) {
            curr.setLeft(rAdd(curr.getLeft(), data));
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(rAdd(curr.getRight(), data));
        } else if (curr.getData().compareTo(data) == 0) {
            return null; // checks for a duplicate, and not increment the size
        }

        updateHeight(curr);
        updateBalFactor(curr);

        //Left Heavy Single Rotation
        if (curr.getBalanceFactor() > 1 && curr.getLeft().getBalanceFactor() >= 0) {
            curr = rightRotate(curr);
        }

        //Right Heavy Single Rotation
        if (curr.getBalanceFactor() < -1 && curr.getRight().getBalanceFactor() <= 0) {
            curr = leftRotate(curr);
        }

        // Left Heavy Double Rotation (Left Right Case)
        if (curr.getBalanceFactor() > 1 && curr.getLeft().getBalanceFactor() < 0) {
            curr.setLeft(leftRotate(curr.getLeft()));
            curr = rightRotate(curr);
        }

        // Right Heavy Double Rotation (Right Left Case)
        if (curr.getBalanceFactor() < -1 && curr.getRight().getBalanceFactor() > 0) {
            curr.setRight(rightRotate(curr.getRight()));
            curr = leftRotate(curr);
        }

        return curr;
    }

    /**
     * Updates the height of the inputted node
     *
     * @param curr the current AVLNode
     */
    private void updateHeight(AVLNode<T> curr) {
        if (curr.getLeft() == null && curr.getRight() == null) {
            curr.setHeight(0);
        } else if (curr.getLeft() == null) {
            curr.setHeight(1 + curr.getRight().getHeight());
        } else if (curr.getRight() == null) {
            curr.setHeight(1 + curr.getLeft().getHeight());
        } else {
            curr.setHeight(1 + Math.max(curr.getLeft().getHeight(), curr.getRight().getHeight()));
        }
    }

    /**
     * Updates the balance factor of the inputted node
     *
     * @param curr the current AVLNode
     */
    private void updateBalFactor(AVLNode<T> curr) {
        if (curr.getLeft() == null && curr.getRight() == null) {
            curr.setBalanceFactor(0);
        } else if (curr.getLeft() == null) {
            curr.setBalanceFactor((-1) - curr.getRight().getHeight());
        } else if (curr.getRight() == null) {
            curr.setBalanceFactor(curr.getLeft().getHeight() - (-1));
        } else {
            curr.setBalanceFactor(curr.getLeft().getHeight() - curr.getRight().getHeight());
        }
    }

    /**
     * Rotates the current tree/subtree right
     *
     * @param c the current AVLNode to rotate
     * @return the new root of the tree/subtree after rotation
     */
    private AVLNode<T> rightRotate(AVLNode<T> c) {
        AVLNode<T> b = c.getLeft();
        c.setLeft(b.getRight());
        b.setRight(c);

        updateHeight(c);
        updateBalFactor(c);
        updateHeight(b);
        updateBalFactor(b);

        return b;
    }

    /**
     * Rotates the current tree/subtree left
     *
     * @param a the current AVLNode to rotate
     * @return the new root of the tree/subtree after rotation
     */
    private AVLNode<T> leftRotate(AVLNode<T> a) {
        AVLNode<T> b = a.getRight();
        a.setRight(b.getLeft());
        b.setLeft(a);

        updateHeight(a);
        updateBalFactor(a);
        updateHeight(b);
        updateBalFactor(b);

        return b;
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf. In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the successor to replace the data,
     * not the predecessor. Rotations can occur after removing the successor node.
     *
     * Recalculates heights going up the tree, rebalancing if necessary.
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in.  Return the data that was stored in the tree.
     */
    public T remove(T data) {
        // The Exceptions
        if (data == null) {
            throw new IllegalArgumentException("The data specified was null");
        }

        AVLNode<T> dummy = new AVLNode<>(null); // The Dummy Node
        root = rRemoved(root, data, dummy);
        return dummy.getData();
    }

    /**
     * Recursively removes the data in the tree.
     *
     * Also updates the Height and Balance factor of the current node
     * and performs a rotation when the Balance Factor is > 1 or < -1
     *
     * @param curr the current AVLNode
     * @param data the data to remove
     * @param dummy the node that is a placeholder for the removed node
     * @return the AVLNode after removing
     * @throws java.util.NoSuchElementException  if the data is not in the tree
     */
    private AVLNode<T> rRemoved(AVLNode<T> curr, T data, AVLNode<T> dummy) {
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
                AVLNode<T> dummy2 = new AVLNode<>(null);
                curr.setRight(removeSuccessor(curr.getRight(), dummy2));
                curr.setData(dummy2.getData());
            }
        }

        updateHeight(curr);
        updateBalFactor(curr);

        //Left Heavy Single Rotation
        if (curr.getBalanceFactor() > 1 && curr.getLeft().getBalanceFactor() >= 0) {
            curr = rightRotate(curr);
        }

        //Right Heavy Single Rotation
        if (curr.getBalanceFactor() < -1 && curr.getRight().getBalanceFactor() <= 0) {
            curr = leftRotate(curr);
        }

        // Left Heavy Double Rotation (Left Right Case)
        if (curr.getBalanceFactor() > 1 && curr.getLeft().getBalanceFactor() < 0) {
            curr.setLeft(leftRotate(curr.getLeft()));
            curr = rightRotate(curr);
        }

        // Right Heavy Double Rotation (Right Left Case)
        if (curr.getBalanceFactor() < -1 && curr.getRight().getBalanceFactor() > 0) {
            curr.setRight(rightRotate(curr.getRight()));
            curr = leftRotate(curr);
        }

        return curr;
    }

    /**
     * Recursively finds and returns the Successor node in the current tree/subtree
     *
     * @param curr the current AVLNode
     * @param dummy the node that is a placeholder for the removed node
     * @return the AVLNode predecessor
     */
    private AVLNode<T> removeSuccessor(AVLNode<T> curr, AVLNode<T> dummy) {
        if (curr.getLeft() == null) {
            dummy.setData(curr.getData());
            return curr.getRight();
        } else {
            curr.setLeft(removeSuccessor(curr.getLeft(), dummy));
        }
        return curr;
    }

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     * same data that was passed in.  Return the data that was stored in the
     * tree.
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data entered was null");
        }

        return rGet(root, data);
    }

    /**
     * Recursively finds and returns the data of a specific node in the tree
     *
     * @param curr the current AVLNode
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    private T rGet(AVLNode<T> curr, T data) {
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
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data entered was null");
        }
        return rContains(root, data);
    }

    /**
     * Recursively finds and returns if a tree contains a specific data
     *
     * @param curr the current AVLNode
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     */
    private boolean rContains(AVLNode<T> curr, T data) {
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
     * Clears the tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Since this is an AVL, this method does not need to traverse the tree
     * and should be O(1)
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

}