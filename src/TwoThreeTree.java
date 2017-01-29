import java.io.*;
import java.util.*;

public class TwoThreeTree
{
    //Node construction.

    private class TwoThreeNode {
        public String[] key;
        public ProductRecord data;
        public TwoThreeNode[] child;
        public int numKeys;
        public TwoThreeNode parent;

        //Create a new leaf for data d with key k. The leaf should have parent p.

        public TwoThreeNode(String k, ProductRecord d, TwoThreeNode p) {
            key = new String[1];
            key[0] = k;
            data = d;
            numKeys = 1;
            child = null;
            parent = p;
        }
        // create a new interior Node to contain index Key k with parent p
        // and two children 1 and r.
        public TwoThreeNode( String k, TwoThreeNode p,
                             TwoThreeNode l, TwoThreeNode r )
        {
            key = new String [2];
            key [0] = k; // index value
            key [1] = null;
            data = null;  //interior nodes never contains real data.
            numKeys = 1;
            child = new TwoThreeNode [3]; // May later have 3 children.
            child [0] = l;
            child [1] = r;
            child [2] = null;
            parent = p;
        }

        /************************************************************
         *
         *  printInorder
         *    Do an inorder traversal of the subtree rooted at
         *    the calling TwoThreeNode, printing the data values
         *    (i.e., only the data stored in the leaves)
         *
         **************************************************************/
        public void printInorder( )
        {
            if (this.isLeaf())
            {
                System.out.println (this.data.toString());
            }

            else if (this.numKeys == 1)
            {
                this.child[0].printInorder();
                this.child[1].printInorder();
            }

            else if (this.numKeys == 2)
            {
                this.child[0].printInorder();
                this.child[1].printInorder();
                this.child[2].printInorder();
            }

        } // end printInorder

        /************************************************************
         *
         * correctChild
         *
         * Figure out which child to move to in the search for searchKey.
         * Return a pointer to that child.

         **************************************************************/
        public TwoThreeNode correctChild (String searchKey)
        {
            TwoThreeNode result = null ; // pointer to correct child

            if (this.numKeys == 1)// (one key Node)
            {
                if (searchKey.compareTo(this.key[0]) < 0)
                    result = this.child[0];

                else
                    result = this.child[1];
            }
            else if (this.numKeys == 2) // (two key nodes)
            {
                if (searchKey.compareTo(this.key[0]) < 0) //left child
                    result = this.child[0];

                else if ( searchKey.compareTo (this.key[1]) >= 0 ) //right child
                    result = this.child[2];

                else // middle child
                    result = this.child[1];

            }
            return result;
        }



        /************************************************************
         *
         *  isLeaf
         *    Return true if the TwoThreeNode is a leaf; false
         *    otherwise.
         *
         *    Note: A TwoThreeNode is a leaf if it has no children
         *    and if it has no children, then child is null.
         *
         **************************************************************/
        public boolean isLeaf()
        {
            return (this.child == null);
        }

    }// end class TwoThreeNode .

    private TwoThreeNode root;

    // constructor ThreeTreeTable.
    public TwoThreeTree()
    {
        root = null;
    }

    /************************************************************
     * findLeaf
     *
     * Return the leaf where searchKey should be
     * (if it is in the tree at all).
     *
     * (A private helper method for search and insert.
     *
     **************************************************************/
    private TwoThreeNode findLeaf( String searchKey )
    {
        TwoThreeNode currNode = this.root;

        if (currNode == null)
        {
            System.out.println ("TwoThreeTree is Empty !!");
        }
        else
        {
            while (!currNode.isLeaf())
            {
                currNode = currNode.correctChild(searchKey);
            }
        }

        return currNode;


    }

    /************************************************************
     *
     * find
     *    Find and return the ProductRecord stored with key
     *    searchKey (or return null if searchKey is not in
     *    any leaf in the tree).
     *
     **************************************************************/
    public ProductRecord find( String searchKey )
    {
        ProductRecord result = null;
        TwoThreeNode  leaf = findLeaf (searchKey);

        if (leaf.key[0].equals(searchKey))
        {
            result  = leaf.data;
        }

        return result;
    }


    /************************************************************
     *
     * insert
     *    Insert ProductRecord p with key newKey into the tree.
     *     - First, search for newKey all the way to the leaves.
     *     - If the leaf contains newKey, simply return.
     *     - Otherwise, call recursive method addNewLeaf to handle
     *       the insertion (including any splitting and
     *       pushing up required).
     *
     **************************************************************/

    public void insert( String newKey, ProductRecord p  )
    {
        TwoThreeNode curr;
        TwoThreeNode nextCurr;
        boolean found = false;
        int i;

        if ( root == null )
        {
            // Empty tree: Add first node as the root (it has no parent)

            root = new TwoThreeNode( newKey, p, null );
        }
        else
        {
            // Tree is not empty.
            // Find the leaf that would contain newKey if newKey is already in the tree.

            curr = findLeaf( newKey );

            if ( curr != null && !curr.key[0].equals( newKey ) )
            {
                // The leaf at which the search ended does not contain searchKey.
                // Insert!

                addNewLeaf( newKey, p, curr );
            }
            else if ( curr == null )
            {
                System.out.println( "Not inserting " + newKey
                        + ": search failed with curr == null in non-empty tree" );
            }

        } // end else root != null

    } // end insert

    /************************************************************
     *
     * addNewLeaf
     *    Add a new leaf containing newKey and ProductRecord p into the tree.
     *    Add the new leaf as a child of the parent of leaf lsearch
     *    (where the search for newKey ended) if there's room.
     *    Otherwise, if the parent of lsearch has no room,
     *    split the parent and push the problem up to the grandparent.
     *    All work at the grandparent or above (where all nodes ---
     *    parent or child --- are interior nodes) is handled by
     *    helper method addIndexValueAndChild.
     *
     **************************************************************/

    private void addNewLeaf( String newKey, ProductRecord p, TwoThreeNode lsearch )
    {
        TwoThreeNode lsParent = lsearch.parent;
        TwoThreeNode newChild = new TwoThreeNode( newKey, p, lsParent );
        int lsIndex = -1; // (will be) index of pointer to lsearch in lsParent.child array
        // in case we have to split lsParent:
        TwoThreeNode newParent;
        String middleValue, largestValue;
        TwoThreeNode secondLargestChild, largestChild;

        if ( lsParent == null )
        {

            if ( newKey.compareTo( lsearch.key[0] ) < 0 )
            {
                // newChild should be the left child, lsearch the right
                root = new TwoThreeNode( lsearch.key[0], null, newChild, lsearch );
            }
            else
            {
                root = new TwoThreeNode( newKey, null, lsearch, newChild );
            }
            lsearch.parent = root;
            newChild.parent = root;
        }
        else
        {
            if ( lsearch == lsParent.child[0] )
                lsIndex = 0;
            else if ( lsearch == lsParent.child[1] )
                lsIndex = 1;
            else if ( lsParent.numKeys == 2 && lsearch == lsParent.child[2] )
                lsIndex = 2;
            else
                System.out.println( "ERROR in addNewLeaf: Leaf lsearch containing " + lsearch.key[0]
                        + " is not a child of its parent" );

            if ( lsParent.numKeys == 1 )
            {
                // Parent has room for another leaf child
                if ( newKey.compareTo( lsearch.key[0] ) < 0 )
                {
                    if ( lsIndex == 1 )
                    {
                        lsParent.child[2] = lsearch;
                        lsParent.child[1] = newChild;
                        lsParent.key[1] = lsearch.key[0];
                    }
                    else
                    {
                        lsParent.child[2] = lsParent.child[1];
                        lsParent.key[1] = lsParent.key[0];
                        lsParent.child[1] = lsearch;
                        lsParent.child[0] = newChild;
                        lsParent.key[0] = lsearch.key[0];
                    }
                }
                else // lsearch's key is < newKey
                {
                    if ( lsIndex == 1 )
                    {
                        lsParent.child[2] = newChild;
                        lsParent.key[1] = newKey;
                    }
                    else
                    {
                        lsParent.child[2] = lsParent.child[1];
                        lsParent.key[1] = lsParent.key[0];
                        lsParent.child[1] = newChild;
                        lsParent.key[0] = newKey;
                    }
                }
                lsParent.numKeys = 2;
                newChild.parent = lsParent;
            }
            else
            {
                // Parent has NO room for another leaf child --- split and push up
                if ( lsIndex == 2 )   // lsearch is rightmost of 3 children
                {
                    if ( lsearch.key[0].compareTo( newKey ) < 0 )
                    {
                        largestChild = newChild;
                        secondLargestChild = lsearch;
                        largestValue = newKey;
                        middleValue = lsParent.key[1];
                    }
                    else
                    {
                        largestChild = lsearch;
                        secondLargestChild = newChild;
                        largestValue = lsearch.key[0];
                        middleValue = lsParent.key[1];
                    }
                }
                else if ( lsIndex == 1 ) // lsearch is middle of 3 children
                {
                    largestChild = lsParent.child[2];
                    largestValue = lsParent.key[1];
                    if ( lsearch.key[0].compareTo( newKey ) < 0 )
                    {
                        secondLargestChild = newChild;
                        middleValue = newKey;
                    }
                    else // newKey < lsearch.key[0]
                    {
                        secondLargestChild = lsearch;
                        middleValue = lsearch.key[0];
                        lsParent.child[1] = newChild;
                        newChild.parent = lsParent;
                    }
                }
                else // lsIndex == 0   lsearch is leftmost of 3 children
                {
                    largestChild = lsParent.child[2];
                    secondLargestChild = lsParent.child[1];
                    largestValue = lsParent.key[1];
                    middleValue = lsParent.key[0];
                    if ( lsearch.key[0].compareTo( newKey ) < 0 )
                    {
                        lsParent.child[1] = newChild;
                        lsParent.key[0] = newKey;
                    }
                    else // newKey < lsearch.key[0]
                    {
                        lsParent.child[1] = lsearch;
                        lsParent.child[0] = newChild;
                        lsParent.key[0] = lsearch.key[0];
                    }
                    newChild.parent = lsParent;
                }
                newParent = new TwoThreeNode( largestValue, lsParent.parent, secondLargestChild, largestChild );
                lsParent.numKeys = 1;
                lsParent.key[1] = null;
                lsParent.child[2] = null;
                largestChild.parent = newParent;
                secondLargestChild.parent = newParent;
                // add new parent to grandparent:
                if ( lsParent.parent == null )
                {
                    root = new TwoThreeNode( middleValue, null, lsParent, newParent );
                    lsParent.parent = root;
                    newParent.parent = root;
                }
                else
                    addIndexValueAndChild( lsParent.parent, middleValue, newParent );
            }
        } // end else lsearch has a parent
    }


    /************************************************************
     *
     *  addIndexValueAndChild
     *    Insert index value m and the corresponding new child (mChild)
     *  into TwoThreeNode curr.
     *
     *  (A child of curr was split, and index value m and new child mChild
     *  are the result of the split and must be added to curr, if possible.
     *  If they can't be added to curr (because curr is already full), then
     *  curr must also be split and the problem pushed up to curr's parent.)
     *
     **************************************************************/
    private void addIndexValueAndChild( TwoThreeNode curr,
                                        String m, TwoThreeNode mChild )
    {
        TwoThreeNode newNode;
        String midKey;

        if ( curr.numKeys == 1 )
        {
            // There's room for m and its child in curr.

            if ( m.compareTo( curr.key[0] ) < 0 )
            {
                curr.key[1] = curr.key[0];
                curr.child[2] = curr.child[1];
                curr.key[0] = m;
                curr.child[1] = mChild;
            }
            else
            {
                curr.key[1] = m;
                curr.child[2] = mChild;
            }
            curr.numKeys = 2;
            mChild.parent = curr;
        }
        else
        {

            if ( m.compareTo( curr.key[0] ) < 0 )
            {

                midKey = curr.key[0];
                newNode = new TwoThreeNode( curr.key[1], curr.parent, curr.child[1], curr.child[2] );
                curr.child[1].parent = newNode;
                curr.child[2].parent = newNode;
                mChild.parent = curr;
                curr.key[0] = m;
                curr.child[1] = mChild;
            }
            else if ( m.compareTo( curr.key[1] ) < 0 )
            {

                midKey = m;
                newNode = new TwoThreeNode( curr.key[1], curr.parent, mChild, curr.child[2] );
                mChild.parent = newNode;
                curr.child[2].parent = newNode;
            }
            else
            {

                midKey = curr.key[1];
                newNode = new TwoThreeNode( m, curr.parent, curr.child[2], mChild );
                curr.child[2].parent = newNode;
                mChild.parent = newNode;
            }
            curr.numKeys = 1;
            curr.key[1] = null;
            curr.child[2] = null;
            if ( curr != root )
                addIndexValueAndChild( curr.parent, midKey, newNode );
            else
            {
                root = new TwoThreeNode( midKey, null, curr, newNode );
                curr.parent = root;
                newNode.parent = root;
            }
        }
    } // end addIndexValueAndChild

    /************************************************************
     *
     *  printTable
     *    Print an appropriate message if the tree is empty;
     *    otherwise, call a recursive method to print the
     *    data values in an inorder traversal.
     *
     **************************************************************/
    public void printTable()
    {
        TwoThreeNode curr = root;

        if ( curr == null )
            System.out.println(" TwoThreeTree is Empty !!");

        else
            curr.printInorder();

    } // end printTree

}// end class TwoThreeTree





