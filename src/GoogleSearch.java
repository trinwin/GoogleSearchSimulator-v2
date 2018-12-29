import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.URI;

/**
 * OneSearch class
 *  - stores the data of all searches
 *  - constructor, setters and getters to access the private variables,
 * and print the value of the object.
 *  - Search for a keyword using webcrawler and display the top 30 websites with randomly score generation.
 *  - Implement Quicksort to sort the total score of 30 URLs.
 *  - Implement Binary Search Tree to manipulate the data.
 *  - Implement Bucket sort to sort company’s names 30 URLs of the top search keyword.
 */

public class GoogleSearch {
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private ArrayList<OneSearch> SearchList = new ArrayList<>();

    /** Default Constructor */
    public GoogleSearch() { }

    /** Setters */
    private void setSearchList(ArrayList<OneSearch> searchList) { SearchList = searchList; }

    /** Getters */
    private ArrayList<OneSearch> getSearchList() { return SearchList; }

    /**
     * Searching() method perform search with keyword input and display top
     * 30 websites, allow user to search, insert or delete a website, and
     * display a sorted list of domain name for the top search
     * @exception URISyntaxException, IOException
     * @time_complexity O(n^2)
     */
    public void Searching() throws URISyntaxException, IOException {
        Scanner scanner = new Scanner(System.in);
        int searchIndex = 0;
        String repeat;
        SearchList.add(searchIndex, null);
        int numResult = 40;
        do {    //O(n)
            OneSearch oneSearch = new OneSearch();
            ArrayList<SearchResult> resultList = new ArrayList<SearchResult>();
            resultList.add(0, null);
            String searchKeyword = "";
            System.out.print("Please enter the search term: ");
            if (scanner.hasNextLine())
                searchKeyword = scanner.nextLine();

            String searchURL = GOOGLE_SEARCH_URL + "?q=" + searchKeyword + "&num=" + numResult;
            Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
            Elements results = doc.select("h3.r > a");

            BST tree = new BST();
            int index = 1;
            for (Element result : results) {
                SearchResult tempSearchResult = new SearchResult();
                String url = result.attr("href");
                String title = result.text();
                if (url.substring(7, 11).equals("http")) {
                    url = url.substring(7, url.indexOf("&"));
                    String domainName = getDomainName(url);
                    tempSearchResult.setDomainName(domainName);
                    tempSearchResult.setTitle(title);
                    tempSearchResult.setUrl(url);
                    tempSearchResult.getScores().setIndex(index);
                    resultList.add(index, tempSearchResult);            //Add node to ArrayList
                    index++;
                    tree.treeInsert(tempSearchResult);                  //Add node to BST
                    if(index == 31) break;
                }
            }
            oneSearch.setList(resultList);
            oneSearch.setTree(tree);
            oneSearch.setKeyword(searchKeyword);
            oneSearch.setCounter(1);
            int keywordCheck = processKeyword(searchKeyword.trim());                //Check whether keyword already existed //O(n)
            searchIndex++;
            searchIndex = BuildSearchList(oneSearch, searchIndex, keywordCheck);    //Add new OneSearch object to ArrayList SearchList

            /** Main Menu */
            Menu(scanner, oneSearch);  //O(nlgn)

            System.out.print("\nWould you like to do another search (Yes/No)? --> ");
            repeat = scanner.nextLine();
            scanner.reset();
        }while (repeat.trim().equalsIgnoreCase("yes") );

        /** Bucket Sort domain name in alphabetical order for the top keyword */
        QuickSort_Keyword(1,SearchList.size()-1);
        System.out.println( "\n--------------------- DOMAIN NAMES OF TOP SEARCH --------------------\n" +
                "\nThere are ["+ (SearchList.size()-1) +"] keywords in the database.");
        printKeyword();
        SearchList.get(SearchList.size()-1).setList(BucketSort(SearchList.get(SearchList.size()-1)));
        SearchList.get(SearchList.size()-1).print();
        scanner.close();
    }

    /**
     * getDomainName method extract the domain name from url
     * @param url
     * @return domain name
     * @exception URISyntaxException
     * @time_complexity O(1)
     */
    private static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    /**
     * processKeyword() method traverse the SearchList array list
     * to check if the new keyword has already been search before.
     * @param newKeyword new keyword to be compare with all existed keyword
     * @return  1 = if found the similar keyword (also increment the counter
     * of that keyword)
     *          -1 = if it is unique
     * @time_complexity O(n)
     */
    private int processKeyword(String newKeyword){
        for (int i=1; i<SearchList.size();i++){ //O(n)
            if (newKeyword.trim().equalsIgnoreCase(SearchList.get(i).getKeyword())) {
                SearchList.get(i).incrementCounter();
                return 1;
            }
        }
        return -1;
    }

    /**
     * BuildSearchList method insert a new OneSearch object into ArrayList and check if
     * its keyword is already existed in database
     * @param oneSearch - object to be inserted
     * @param searchIndex - - index of new object in array list
     * @param action - action performed if search keyword already existed or not
     * @time_complexity O(n^2)
     */
    private int BuildSearchList(OneSearch oneSearch, int searchIndex, int action){
        SearchList.add(searchIndex, oneSearch);     //O(1)
        System.out.println("\n--------------------------- TOP 30 URLs ------------------------\n");
        SearchList.get(searchIndex).QuickSort(1,SearchList.get(searchIndex).getList().size()-1);
        SearchList.get(searchIndex).setPageRank();
        SearchList.get(searchIndex).printList();
        if (action==1) {                                //if the keyword already existed
            SearchList.remove(searchIndex);             //Display top 10 websites - Remove it from the list since the keyword existed
            return searchIndex-1;                       //The size and index decrease and return current index-1 for adding the next object to array list
        }
        return searchIndex;                             //return current index for adding the next object to array list
    }

    /**
     * Menu method performs search, insert, delete
     * @param oneSearch - where the data is stored
     * @param scanner - get user input
     * @exception URISyntaxException for insertWebsite
     * @time_complexity O(n^2)
     */
    private void Menu(Scanner scanner, OneSearch oneSearch) throws URISyntaxException {
        String mess =  "\n------------------------------ MENU ------------------------------" +
                "\nWhat would you like to do?" +
                "\nOption 1: Search a Website based on PageRank" +
                "\nOption 2: Add a new Website" +
                "\nOption 3: Delete a Website" +
                "\nOption 4: Exit" +
                "\nEnter the option (1-4): ";
        int option;
        do{
            option = MenuOptionInputValidation(mess, scanner); //O(n)
            switch (option) {
                case 1: searchTree(scanner, oneSearch); break;
                case 2: insertWebsite(scanner, oneSearch); break;
                case 3: deleteWebsite(scanner, oneSearch); break;
                case 4: break;
                default:
                    System.out.println("Error: Input must be a positive integer (1-3).");
            }
        }while (option != 4);
    }

    /**
     * searchTree method find a website given its pageRank
     * @param oneSearch - contains the website to be searched
     * @param scanner - get user input
     * @time_complexity O(h)
     */
    private void searchTree(Scanner scanner, OneSearch oneSearch){
        System.out.println("\n------------------------------ SEARCH ------------------------------\n");
        int search_PageRank = PageRankInputValidation("Enter PageRank: ", scanner, oneSearch);
        SearchResult findNode = oneSearch.getTree().treeSearch(oneSearch.getTree().getRoot(), search_PageRank);
        if (findNode != null) findNode.printOne();
        else System.out.println("Website is NOT found!");
    }

    /**
     * insertWebsite method insert a new website to database
     * @param oneSearch - where to insert a new website
     * @param scanner - get user input
     * @time_complexity O(n^2)
     */
    private void insertWebsite(Scanner scanner, OneSearch oneSearch)throws URISyntaxException {
        System.out.println("\n------------------------------- ADD -------------------------------\n");
        System.out.print("Enter Title of website: ");
        String title = scanner.nextLine();
        System.out.print("Enter URL of website [https://www....]: ");
        String url = scanner.nextLine();
        String domainName = getDomainName(url);
        SearchResult newNode = new SearchResult(title, url, domainName, oneSearch.getList().size());
        System.out.println("WARNING: The score of your new website will be evaluated and auto-generated." +
                "\nConfirm your new Website information:");
        newNode.print_New();
        oneSearch.getTree().treeInsert(newNode);
        oneSearch.getList().add(newNode);
        System.out.println("\n\n----------------------- Updated List of URLs -----------------------");
        oneSearch.QuickSort(1, oneSearch.getList().size()-1);
        oneSearch.setPageRank();
        oneSearch.getTree().inorderTreeWalk(oneSearch.getTree().getRoot());
    }

    /**
     * deleteWebsite method delete a a website given its PageRank
     * @param oneSearch - contains the website to be deleted
     * @param scanner - get user input
     * @time_complexity O(h)
     */
    private void deleteWebsite(Scanner scanner, OneSearch oneSearch){
        System.out.println("\n------------------------------ DELETE ------------------------------\n");
        SearchResult findNode;
        do{
            int search_PageRank = PageRankInputValidation("Enter PageRank: ", scanner, oneSearch);
            findNode = oneSearch.getTree().treeSearch(oneSearch.getTree().getRoot(), search_PageRank);
            if (findNode==null)     System.out.println("Website is NOT found!");
        }while (findNode == null);
        oneSearch.getTree().treeDelete(findNode);
        oneSearch.getList().remove(findNode);
        oneSearch.setPageRank();
        oneSearch.getTree().inorderTreeWalk(oneSearch.getTree().getRoot());
    }

    /**
     * QuickSort_Keyword method sort ArrayList of SearchList from smallest counter
     * to largest counter (number of search for the same keyword)
     * @param low - smallest index
     * @param high - largest index
     * @time_complexity O(n^2)
     */
    private void QuickSort_Keyword(int low, int high){
        if (low < high) {
            int q = Partition_Keyword(low, high);
            QuickSort_Keyword(low, q-1);
            QuickSort_Keyword(q+1, high);
        }
    }

    /**
     * Partition_Keyword method rearranges the sub-ArrayList in place
     * @param low - smallest index
     * @param high - largest index
     * @time_complexity O(n^2)
     */
    private int Partition_Keyword(int low, int high){
        OneSearch pivot = this.SearchList.get(high);
        int index = low - 1; //Index of smaller element
        for (int i = low; i < high; i++) {
            if (this.SearchList.get(i).getCounter() <= pivot.getCounter()) {
                index++;
                swapSearch(index, i);
            }
        }
        swapSearch(index + 1, high);
        return index + 1;
    }

    /**
     * swapList method is used to swap two OneSearh object in ArrayList
     * @param index1 index of the first object
     * @param index2 index of the second object
     * @time_complexity O(1)
     */
    private void swapSearch(int index1, int index2){
        OneSearch temp = this.SearchList.get(index1);
        this.SearchList.set(index1, SearchList.get(index2));
        this.SearchList.set(index2, temp);
    }

    /**
     * BucketSort method sort all domain name in alphabetical order
     * @param oneSearch - contains a list of domain name to be sorted
     * @return finalList - sorted arrayList
     * @time_complexity O(n)
     */
    private ArrayList<SearchResult> BucketSort(OneSearch oneSearch){
        ArrayList<ArrayList<SearchResult>> bucket = new ArrayList<ArrayList<SearchResult>>();
        int n = 26; //26 words in alphabet
        for(int i = 0; i < n; i++){

            bucket.add(i,new ArrayList<SearchResult>());
        }
        for(int i = 1; i < oneSearch.getList().size(); i++){
            int letterAscii = (int) oneSearch.getList().get(i).getDomainName().charAt(0)-97;
            bucket.get(letterAscii).add(oneSearch.getList().get(i));
        }
        for (int i = 0; i < n-1; i++){
            insertionSort(bucket.get(i));
        }
        ArrayList<SearchResult> finalList = new ArrayList<SearchResult>();
        for (int i = 0; i < n-1; i++){
            for (int j = 0; j < bucket.get(i).size(); j++)
                finalList.add(bucket.get(i).get(j));
        }
        return finalList;
    }

    /**
     * insertionSort method sort each bucket (arraylist) of BucketSort ArrayList
     * @param oneBucket - arraylist of SearchResult to be sorted according to their domain name
     * @time_complexity O(n^2)
     */
    private void insertionSort(ArrayList<SearchResult> oneBucket){
        SearchResult key;
        int i, j;
        for (j = 1; j < oneBucket.size(); j++){
            key = oneBucket.get(j);
            i = j-1;
            while (i >= 0 && oneBucket.get(i).getDomainName().compareTo(key.getDomainName())>0){
                oneBucket.set(i+1,oneBucket.get(i));
                i--;
            }
            oneBucket.set(i+1,key);
        }
    }

    /**
     * printKeyword() method print the list of top 10 unique keywords
     * in reverse order (highest to lowest occurrence) after sorting (HeapSort).
     * @time_complexity O(n)
     */
    private void printKeyword(){
        StringBuffer buff = new StringBuffer();
        System.out.print(buff);
        int order = 1;
        for (int i = SearchList.size()-1; i>0; i--) //O(n)
        {
            SearchList.get(i).printKeyword(order);
            order++;
        }
    }

    /**---------------- UserInputValidation Functions ----------------*/

    /**
     * PageRankInputValidation() method makes sure user enter valid integer for pageRank
     * @param message - instruction for user
     * @param scanner - get user input
     * @time_complexity O(1)
     */
    private int PageRankInputValidation(String message, Scanner scanner, OneSearch oneSearch) {
        int num;
        String errorMessage = "Error: Input must be a positive integer (1-30).";
        System.out.print(message);
        while (!scanner.hasNextInt() || (num = scanner.nextInt()) <= 0 || num >= oneSearch.getList().size()) {
            System.out.print(errorMessage + "\n" + message);
            scanner.nextLine();
        }
        scanner.nextLine();
        return num;
    }

    /**
     * UserOptionInputValidation() method makes sure user enter valid integer for menu option
     * @param message - instruction for user
     * @param scanner - get user input
     * @time_complexity O(1)
     */
    private int MenuOptionInputValidation(String message, Scanner scanner) {
        int num;
        String errorMessage = "Error: Input must be a positive integer (1-2).";
        System.out.print(message);
        while (!scanner.hasNextInt() || (num = scanner.nextInt()) < 0 || num > 4) {
            System.out.print(errorMessage + "\n" + message);
            scanner.nextLine();
        }
        scanner.nextLine();
        return num;
    }

}

