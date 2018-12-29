/**
 * The GoogleSearch Program implements an application that
 *  - Search for a keyword using webcrawler and display the top 30 websites with randomly score generation.
 *  - Implement Quicksort to sort the total score of 30 URLs .
 *  - Implement Binary Search Tree to manipulate the data.
 *  - Implement Bucket sort to sort company’s names 30 URLs of the top search keyword.
 *
 * @author  Trinh Nguyen
 * @class   CS146-07
 * @Programming_Assignment_2
 * @version 1.0
 * @date    2018-11-16
 */


/**
 * This is the main method which call function Searching, which
 * Searching() method perform all the tasks of this assignment.
 * @param args - Unused
 * @time_complexity O(n^2)
 */

public class Main {
    public static void main(String[] args) throws Exception {
        GoogleSearch google = new GoogleSearch();
        google.Searching(); //O(n^2)
    }
}