/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package binarysearchtree;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author patrick.brown
 * @param <T>
 */
public class LinkedBinarySearchTree<T extends Comparable<? super T>>
{
    private ArrayList<T> tree = new ArrayList<T>();
    int ROOT_KEY = 1;
    
    /**
     * Constructor... this may be unnecessary. 
     */
    public void LinkedBinarySearchTree()
    {
        this.tree = new ArrayList<T>();
    }
    
    /**
     * Add function. Stores node into the BST.
     * 
     * @param o element to store.
     */
    public void add(T o)
    {
        if(this.tree.isEmpty()){
            // set the object at index 0 to null
            this.tree.add(0, null); 
            this.tree.add(ROOT_KEY, o);
            growTree(ROOT_KEY);
        } else {
            addHelper(ROOT_KEY, o);
        }
        
    }
    
    /**
     * Recursive add function helper. Tests for which direction to add, 
     * following the rule:
     * Left child < Parent <= Right child.
     * 
     * Looks at the current node's child link. If null, then the current node is
     * the parent of the new element (wrapped in a BinaryTreeNode). If there is 
     * a child, then recur on that child.
     * 
     * @param node is the node we're testing.
     * @param o is the object we're trying to add.
     */
    private void addHelper(int node, T o)
    {
        T current_node = this.tree.get(node);
        int left_key = getLeft(node);
        T left_node = this.tree.get(left_key);
        int right_key = getRight(node);
        T right_node = this.tree.get(right_key);

        if (o.compareTo(current_node) < 0){
            // add to left
            if(left_node == null){
                growTree(getLeft(node));
                setLeft(node, o);
            } else {
                addHelper(left_key, o);
            }
        } else if(o.compareTo(current_node) >= 0){ 
            // add to right
            if(right_node == null){
                growTree(getRight(node));
                setRight(node, o);
            } else {
                addHelper(right_key, o);
            }
        }
    }
    
    /** 
     * Remove the element from the tree. Note that we have to find the first 
     * node containing the element (or one with the same value as determined by 
     * the compareTo method), then we remove that node. We must maintain the BST
     * property: Left child < Parent <= Right child. This method makes use of 
     * the getReplacement method to find the node that will be promoted if the 
     * root is to be removed. if not the root, then the removeElemHelper method 
     * is called. if this method returns a null, the element does not exist in 
     * the tree.
     * 
     * @param o
     * @throws java.lang.Exception
     * @return ?
     */

    public T remove (T o) //throws Exception
    {
        T root_value = this.tree.get(ROOT_KEY);

        if(o.compareTo(root_value) == 0){
            // is node
            return root_value;
        } else if(o.compareTo(root_value) < 0){
            // somewhere in left subtree
            return removeHelper(getLeft(ROOT_KEY), o);
        } else if(o.compareTo(root_value) >= 0){
            // somewhere is right subtree
            return removeHelper(getRight(ROOT_KEY), o);
        } else {
            return root_value;
        }
    }
    
    /**
     * The current node is not equal to the target, so examine the target 
     * against the current node, choose the appropriate direction to search. 
     * examine the target against the current node's child element. If found 
     * find a replacement node by calling findReplacement. If null is returned, 
     * then the child is a leaf and is deleted by setting the current node's 
     * pointer to null. If the child is not the target, recur on the child node.
     * 
     * @param key
     * @param o
     */
   
    private T removeHelper(int key, T o)
    {
        T current_value = this.tree.get(key);
        
        int child_key;
        T child_value;
        T replacement;
        T to_return = null;
        
        int left_key = getLeft(key);
        T left_value = this.tree.get(left_key);
        int right_key = getRight(key);
        T right_value = this.tree.get(right_key);
        
        if(current_value != null){
            if(o.compareTo(current_value) < 0){
                child_key = left_key;
                child_value = left_value;
                
                if (child_value != null && o.compareTo(child_value) == 0){
                    to_return = child_value;
                    
                    replacement = getReplacement(child_key);
                    if(replacement == null) {
                        setLeft(key, null);
                    } else {
                        setLeft(key, replacement);
                    }
                } else {
                    to_return = removeHelper(child_key, o);
                }   
            } else if(o.compareTo(current_value) > 0) {
                child_key = right_key;
                child_value = right_value;
                
                if(child_value != null && o.compareTo(child_value) == 0) {
                    to_return = child_value;
                    
                    replacement = getReplacement(child_key);
                    if(replacement == null){
                        setRight(key, null);
                    } else {
                        setRight(key, replacement);
                    }
             
                } else {
                    to_return = removeHelper(child_key, o);
                }
            }
        }
        return to_return;
    }

    private T getReplacement(int key)
    {
        T to_return;
        
        T current_value = this.tree.get(key);
        
        int left_key = getLeft(key);
        T left_value = this.tree.get(left_key);
        int right_key = getRight(key);
        T right_value = this.tree.get(right_key);
        
        if(left_value == null && right_value == null) {
            //node is a leaf
            to_return = null;
        } else if(left_value == null && right_value != null) {
            //node has a right child only
            to_return = right_value;
        } else if(left_value != null && right_value == null) {
            //node has a left child only
            to_return = left_value;
        } else {                          
            // node has both a right and left child- find inorder sucessor
            // go right because of the equals sign in: Left child < Parent <= 
            // Right child 
            to_return = this.tree.get(getSuccessor(right_key));
            // these next two lines connect the IOS to the departing node's
            // children.
            // result.left <- node.left;  
            setLeft(key, left_value);
            
            if(to_return != right_value) {
                // if they are the same, then you do not want to make the right 
                // child reference the parent!
                setRight(key, right_value);
            }
        }
        return to_return;
    }

    /**
     * Psuedo code from http://www.personal.kent.edu/~rmuhamma/Algorithms/MyAlgorithms/binarySearchTree.htm
     *
     * TREE-SUCCESSOR (x)
     *      if right [x] ≠ NIL
     *          then return TREE-MINIMUM (right[x])
     *      else y ← p[x]
     *          while y ≠ NIL     .AND.    x = right[y] do
     *              x ← y
     *              y ← p[y]
     *          return y
     *
     * @param key
     * @return
     */

    public int getSuccessor(int key)
    {
        //T current_value = this.tree.get(key);
        //int left_key = getLeft(key);
        //T left_value = this.tree.get(left_key);
        int right_key = getRight(key);
        T right_value = this.tree.get(right_key);

        if(right_value != null){
            return getMinimum(right_key);
        } else {
            int y = getParent(key);
            while(this.tree.get(y) != null && key == getRight(y)){
                key = y;
                y = getParent(y);
            }
            return y;
        }
    }

    /**
     * Finds the minimum valued node on subtree.
     *
     * @param key
     * @return minimum valued node.
     */

    private int getMinimum(int key)
    {
        /*
        while left[x] ≠ NIL do
            x ← left [x]
        return x
        */

        int left_key = getLeft(key);
        T left_value = this.tree.get(left_key);

        while(left_value != null){
            key = getLeft(key);
            left_value = this.tree.get(getLeft(key));
        }
        return key;
    }

    /**
     * Finds maximum value on subtree
     *
     * @param key
     * @return maximum value.
     */

    public int getMaximum(int key)
    {
        /*
        TREE-MAXIMUM (x)

        while right[x] ≠ NIL do
            x ← right [x]
            return x
        */

        int right_key = getRight(key);
        T right_value = this.tree.get(right_key);

        while(right_value != null){
            key = getRight(key);
            right_value = this.tree.get(getRight(key));
        }
        return key;
    }

    private int getParent(int key)
    {
        // java's floor function returns a *double*, not an int.
        return (int)Math.floor(key / 2);
    }

    private void setParent(int key, T o)
    {
        // java's floor function returns a *double*, not an int.
        this.tree.set((int)Math.floor(key / 2), o);
    }

    private int getLeft(int key)
    {
        return 2 * key;
    }

    private void setLeft(int key, T o)
    {
        this.tree.set(2*key, o);
    }

    private int getRight(int key)
    {   
        return 2 * key + 1;
    }

    private void setRight(int key, T o)
    {
        this.tree.set(2 * key + 1, o);
    }

    /**
     * At times we'll need to grow the tree. We'll take the index at the end of
     * an add operation and see if we need to grow the tree to accommodate the
     * changes.
     *
     * @param index
     */
    private void growTree(int index)
    {
        double power = Math.ceil((Math.log(index) / Math.log(2))) + 2;
        int required_size = (int)Math.pow(2, power);
        while(required_size > this.tree.size()){
            this.tree.add(null);
        }
    }

    public Iterator<T> inOrderIterator() {

        Iterator<T> it = new Iterator<T>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex != getMaximum(ROOT_KEY);
            }

            @Override
            public T next() {

                // The current index will be zero on the first
                // run, so we find min and then return it.
                if(currentIndex == 0){
                    currentIndex = getMinimum(ROOT_KEY);
                    return tree.get(currentIndex);
                }

                currentIndex = getSuccessor(currentIndex);
                return tree.get(currentIndex);
            }

            @Override
            public void remove() {
                // TODO Auto-generated method stub
            }
        };
        return it;
    }
}
