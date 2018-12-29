import java.util.ArrayList;

/**
 * BST class
 *  -
 */

public class BST{
    private SearchResult root;

    BST(){ root = null; }

    public SearchResult getRoot() { return root; }

    public void setRoot(SearchResult root) { this.root = root; }

    public SearchResult getParent(SearchResult node, int totalScore){
        SearchResult parent = new SearchResult();
        if (node == null) //check if the root is null
            return null;
        while(node != null){
            if(totalScore <= node.getTotalScore()){
                parent = node;
                node = node.getLeft();
            } else if (totalScore > node.getTotalScore()){
                parent = node;
                node = node.getRight();
            }else break;
        }
        return parent;
    }

    /**
     * inorderTreeWalk method traverse the tree and print from min to max total score
     * @param node - root of the tree
     * @return none
     * @time_complexity O(n)
     */
    public void inorderTreeWalk(SearchResult node){
        if (node != null)
        {
            inorderTreeWalk(node.getLeft());
            node.printOne();
            inorderTreeWalk(node.getRight());
        }
    }

    /**
     * treeMinimum method find the node with minimum total score
     * @param node - root of the tree
     * @return node with minimum total score
     * @time_complexity O(h)
     */
    public SearchResult treeMinimum(SearchResult node){
        while (node.getLeft()!=null)
            node = node.getLeft();
        return node;
    }

    /**
     * treeMaximum method find the node with maximum total score
     * @param node - root of the tree
     * @return node with maximum total score
     * @time_complexity O(h)
     */
    public SearchResult treeMaximum(SearchResult node){
        while (node.getRight()!=null)
            node = node.getRight();
        return node;
    }

    /**
     * treeSuccessor method find successor of a node
     * @param node - find successor of this node
     * @return successor of node
     * @time_complexity O(h)
     */
    public SearchResult treeSuccessor(SearchResult node){
        if (node.getRight()!=null)
            return treeMinimum(node.getRight());
        SearchResult parent = node.getParent();
        while (parent != null && node == parent.getRight()){
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * treePredecesor method find predecessor of a node
     * @param node - find predecessor of this node
     * @return predecessor of node
     * @time_complexity O(h)
     */
    public SearchResult treePredecesor(SearchResult node){
        if (node.getLeft()!=null)
            return treeMinimum(node.getLeft());
        SearchResult parent = node.getParent();
        while (parent != null && node == parent.getLeft()){
            node = parent;
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * treeInsert method insert new node into the tree
     * @param z = node to be inserted
     * @return none
     * @time_complexity O(h)
     */
    public void treeInsert(SearchResult z){
        SearchResult y = null;    //trailing pointer y as a parent of x
        SearchResult x = root;
        while (x != null){
            y = x;
            if (z.getTotalScore() < x.getTotalScore())
                x = x.getLeft();
            else x = x.getRight();
        }
        z.setParent(y);
        if (y == null)
            root = z;
        else if (z.getTotalScore() < y.getTotalScore())
            y.setLeft(z);
        else y.setRight(z);
    }

    /**
     * treeDelete method delete a node
     * @param z - node to be deleted
     * @return none
     * @time_complexity O(h)
     */
    public void treeDelete(SearchResult z){
        SearchResult y = null;
        /** z has no left child */
        if (z.getLeft() == null)
            Transplant(z, z.getRight());
        /** z has a left child, but no right child */
        else if (z.getRight() == null)
            Transplant(z, z.getLeft());
        /** z has two children */
        else {
            y = treeMinimum(z.getRight());
            /** y lies within y's right subtree but is not the root of this subtree */
            if (y.getParent() != z){
                Transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }
            /** if y is z's right child */
            Transplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
        }
    }

    /**
     * Transplant method assist treeDelete method
     * to move subtree around within BST
     * @param u - subtree rooted at u to be replaced by subtree rooted at v
     * @param v - subtree rooted at v replacing subtree rooted at u
     * @return none
     * @time_complexity O(1)
     */
    public void Transplant(SearchResult u, SearchResult v){
        /** Handle u is root of T */
        if (u.getParent() == null)
            root = v;
        /** if u is the left child */
        else if (u == u.getParent().getLeft())
            u.getParent().setLeft(v);
        /** if u is a right child */
        else u.getParent().setRight(v);
        /** update v.p if v is non-NIL */
        if (v != null)
            v.setParent(u.getParent());
    }

    /**
     * treeSearch method
     * @param node - root of the tree
     * @param key_rank - key of the node searching for
     * @return node - if found, null - if not found
     * @time_complexity O(h)
     */
    public SearchResult treeSearch(SearchResult node, int key_rank){ //Pass in tree root
        if (node == null || key_rank == node.getPageRank())
            return node;
        if (key_rank > node.getPageRank())
            return treeSearch(node.getLeft(), key_rank);
        else return treeSearch(node.getRight(), key_rank);
    }
}


