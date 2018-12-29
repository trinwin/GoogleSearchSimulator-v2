/**
 * SearchResult class
 *  - stores the data of a website (Title, URL, domainName, PageRank object,
 *  and left, right, parent node for implementing BST)
 *  - has constructor, setters and getters to access the private variables,
 * and print the value of the object.
 */

public class SearchResult {
    private String title;
    private String url;
    private String domainName;
    private PageRank score;
    private SearchResult left, right, parent;

    /** Constructor */
    public SearchResult(){
        PageRank newScore = new PageRank();
        newScore.generateScore();
        this.score = newScore;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public SearchResult(String title, String url, String domainName, int index){
        this.title = title;
        this.url = url;
        this.domainName = domainName;
        PageRank newScore = new PageRank();
        newScore.generateScore();
        newScore.setIndex(index);
        this.score = newScore;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    /** Setters */
    public void setTitle(String title) { this.title = title; }

    public void setUrl(String url) { this.url = url; }

    public void setDomainName(String domainName) { this.domainName = domainName; }

    public void setScores(PageRank score) { this.score = score; }

    public void setOneScore(int index, int value) { this.score.setOneScore(index, value); }

    public void setTotalScore(int total) { this.score.setTotalScore(total); }

    public void setPageRank(int pageRank) { this.score.setPageRank(pageRank); }

    public void setLeft(SearchResult left) { this.left = left; }

    public void setRight(SearchResult right) { this.right = right; }

    public void setParent(SearchResult parent){ this.parent = parent; }

    /** Getters */
    public String getTitle() { return title; }

    public String getUrl() { return url; }

    public String getDomainName() { return domainName; }

    public PageRank getScores() { return score; }

    public int getTotalScore() { return score.getTotalScore(); }

    public int getPageRank() { return score.getPageRank();}

    public SearchResult getLeft() { return left; }

    public SearchResult getRight() { return right; }

    public SearchResult getParent() { return parent; }


    /**
     * buildPageRank() method set pagerank
     * @param rank - rank of the object
     * @time_complexity O(1)
     */
    public void buildPageRank(int rank){
        getScores().setPageRank(rank);
    }

    /**
     * printOne() method is used StringBuffer to display Website title, url,
     * pagerank, index, each factors, and totalscore
     * @time_complexity O(n)
     */
    public void printOne(){
        StringBuffer buff = new StringBuffer();
        buff.append("\nTitle: ").append(title);
        buff.append("\nURL: ").append(url);//.append("\n");
        buff.append("\nPage Rank: ").append(getScores().getPageRank());
        buff.append(" - Index: ").append(getScores().getIndex());
        System.out.print(buff);
        score.print(); //O(n)
    }

    /**
     * printOne() method is used StringBuffer to display Website title,
     * url, domain name, pagerank, and index
     * @time_complexity O(n)
     */
    public void print_Domain(){
        StringBuffer buff = new StringBuffer();
        buff.append("\nTitle: ").append(title);
        buff.append("\nDomain name: ").append(domainName);
        buff.append(" - URL: ").append(url);
        buff.append("\nPage Rank: ").append(getScores().getPageRank());
        buff.append(" - Index: ").append(getScores().getIndex());
        System.out.print(buff);
        score.print(); //O(n)
    }

    /**
     * printOne() method is used StringBuffer to display Website title, url
     * pagerank, index, and domain name
     * @time_complexity O(n)
     */
    public void print_New(){
        StringBuffer buff = new StringBuffer();
        buff.append("Title: ").append(title);
        buff.append("\nDomain name: ").append(domainName);
        buff.append("\nURL: ").append(url);
        buff.append("\nPage Rank: ").append(getScores().getPageRank());
        buff.append(" - Index: ").append(getScores().getIndex());
        System.out.print(buff);
        score.print(); //O(n)
    }
}
