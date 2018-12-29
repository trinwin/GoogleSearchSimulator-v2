import java.util.ArrayList;
/**
 * OneSearch class
 *  - stores the data of one search (search keyword, list of websites
 * counter (number of occurrence of keyword), and BST tree (BinarySearchTree))
 *  - has constructor, setters and getters to access the private variables,
 * and print the value of the object.
 *  -  Quicksort/Partition to sort ArrayList of SearchResult based on total score
 *
 */

public class OneSearch {
    private String keyword;
    private ArrayList<SearchResult> list = new ArrayList<SearchResult>();
    private int counter; //Counting number of search for the same keyword
    private BST tree = new BST();

    public BST getTree() { return tree; }

    public void setTree(BST tree) { this.tree = tree; }

    /** Default Constructor */
    public OneSearch(){ }

    /** Setters */
    public void setCounter(int counter) { this.counter = counter; }

    public void setKeyword(String keyword) { this.keyword = keyword; }

    public void setList(ArrayList<SearchResult> list) { this.list = list; }

    /** Getters */
    public String getKeyword() { return keyword; }

    public ArrayList<SearchResult> getList() { return list; }

    public int getCounter() { return counter; }

    public void incrementCounter(){ counter++; }

    /**
     * QuickSort_Keyword method sort ArrayList of SearchResult from smallest totalscore
     * to max totalscore
     * @param low - smallest index
     * @param high - largest index
     * @time_complexity O(n^2)
     */
    public void QuickSort(int low, int high){
        if (low < high) {
            int q = Partition(low, high);
            QuickSort(low, q-1);
            QuickSort(q+1, high);
        }
    }

    /**
     * Partition method rearranges the sub-ArrayList in place
     * @param low - smallest index
     * @param high - largest index
     * @time_complexity O(n^2)
     */
    public int Partition(int low, int high){
        SearchResult pivot = this.list.get(high);
        int index = low - 1; //Index of smaller element
        for (int i = low; i < high; i++) {
            if (this.list.get(i).getTotalScore() <= pivot.getTotalScore()) {
                index++;
                swapNode(index, i);
            }
        }
        swapNode(index + 1, high);
        return index + 1;
    }

    /**
     * swapNode method is used to swap two SearchResult object in ArrayList
     * @param a index of the first object
     * @param b index of the second object
     * @time_complexity O(1)
     */
    private void swapNode(int a, int b){
        SearchResult temp = this.list.get(a);
        this.list.set(a, this.list.get(b));
        this.list.set(b, temp);
    }

    /**
     * setPageRank() method set pagerank of all SearchResult object in arrayList
     * @time_complexity O(n)
     */
    public void setPageRank(){
        for (int i = 1; i < list.size(); i++)
            list.get(i).buildPageRank(list.size()-i); //O(n)
    }

    /**
     * print method used StringBuffer to display all domain names
     * @time_complexity O(h)
     */
    public void print(){
        StringBuffer buff = new StringBuffer();
        buff.append("\n\nKEYWORD: ").append(keyword).append("\n");
        System.out.print(buff);
        for (int i = 1; i < list.size(); i++)
            list.get(i).print_Domain(); //O(n)
    }

    /**
     * printList() method is used StringBuffer to display one object
     * OneSearch (Keyword) and SearchResult object by calling SearchResult's
     * printAll function in Reverse order
     * @time_complexity O(n^2)
     */
    public void printList(){
        StringBuffer buff = new StringBuffer();
        buff.append("KEYWORD: ").append(keyword).append("\n");
        System.out.print(buff);
        for (int i = 1; i < list.size(); i++)
            list.get(i).printOne(); //O(n)
    }

    /**
     * printKeyword() method is used StringBuffer to display keyword
     * and the number of occurrence the keyword has been searched
     * @param rank used to display the position of keyword on the list
     * @return none
     * @time_complexity O(1)
     */
    public void printKeyword(int rank){
        StringBuffer buff = new StringBuffer();
        buff.append("\n").append(rank).append(". ").append("Keyword: ").append(keyword);
        buff.append(" [").append(counter).append(" time(s)]\n");
        System.out.print(buff);
    }
}