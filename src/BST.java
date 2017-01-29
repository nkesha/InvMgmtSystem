class BST
{
    private class BSTNode
    {
        String key;
        ProductRecord item;
        BSTNode left, right;

        public BSTNode (String key, ProductRecord item)
        {
            this.key = key;
            this.item = item;
            left = right = null;

        }
        /**************************
         *
         *   CLASS BSTNode
         *   - each node contains a key (a String) and the product record
         *     associated with that key.
         *
         ********************************/

        public void insertBelow (String newKey, ProductRecord newItem)
        {

            if (newKey.compareTo ( key ) < 0)
            {
                if (left == null)
                {
                    left = new BSTNode(newKey, newItem);
                }
                else
                {
                    left.insertBelow (newKey,newItem);
                }
            }
            else
            {
                if (right == null)
                {
                    right = new BSTNode(newKey, newItem);
                }
                else
                {
                    right.insertBelow(newKey, newItem);
                }
            }
        }// end insertBelow

        public ProductRecord search (String searchKey)
        {
            ProductRecord found = null;

            if (searchKey.equals(key))
            {
                found = this.item;
            }
            else if (searchKey.compareTo(key) < 0)
            {
                if (left != null)
                {
                    found = left.search (searchKey);
                }
            }
            else
            {
                if (right != null)
                {
                    found = right.search (searchKey);
                }
            }
            return found;
        }

        // helper method for printKeyMatches.
        public void printAllMatches ( String searchKey )
        {
            if ((key.compareTo(searchKey)) == 0)
            {
                System.out.println(this.item.toString());

                if (this.right != null)
                {
                    this.right.printAllMatches(searchKey);
                }
            }

            else if ( searchKey.compareTo(key) < 0)
            {
                if (this.left != null)
                {
                    this.left.printAllMatches(searchKey);
                }
            }
            else
            {
                if (this.right != null)
                {
                    this.right.printAllMatches(searchKey);
                }
            }
        }//end printAllMatches.

        // using Inorder to print the tree.
        public void printInorder ()
        {
            if (this.left != null)
            {
                left.printInorder();
            }
            System.out.println( this.item.toString());

            if (this.right != null)
            {
                right.printInorder();
            }

        }

    }// end class BSTNode

    BSTNode root;

    //constructor Binary Search Tree
    public BST ()
    {
        root = null;
    }

    /****************************
     * Method insert
     *
     * PURPOSE : Insert ProductRecord newItem with key newKey into the BST
     *
     ****************************/
    public void insert (String newKey, ProductRecord newItem)
    {
        if (root == null)
        {
            root = new BSTNode(newKey, newItem);
        }
        else
        {
            root.insertBelow(newKey, newItem);
        }
    }//end insert

    /****************************
     * Method find
     *
     * PURPOSE : Searches for and returns the product record associated with searchKey
     * -param searchKey  The key (string) associated with the product record we want.
     * -return The product record associated with searchKey
     *
     ****************************/
    public ProductRecord find (String searchKey)
    {
        ProductRecord found = null;

        if (root != null)
        {
            found = root.search(searchKey);
        }

        return found;

    }//end find

    /*************************************
     *
     * Method printKeyMatches
     *
     * PURPOSE: Print all ProductRecord with key :
     *          -Perform a search for searchKey
     *          -If searchKey is found , print out the associated product record
     *          AND continue the search for searchKey in the right child
     *            (items with equal keys are stored only in the right child)
     *          - The searching continues until we "fall off the tree"
     *            --- that is, until we want to move to a child that is null.
     *          - Assumes: (potentially) multiple records with the same key.
     ***************************************/

    public void printKeyMatches( String searchKey )
    {
        if (root != null)
        {
            root.printAllMatches(searchKey);
        }
        else
        {
            System.out.println ("ERROR NO MATCHES FOUND !!");
        }

    }// end printKeyMatches.

    /*********************
     * Method printTable
     *
     * PURPOSE : Traverses the table, printing out all the keys and their
     *            associated product records.
     *
     **********************/

    public void printTree()
    {
        if (root != null)
        {
            root.printInorder();
        }

    }

}













