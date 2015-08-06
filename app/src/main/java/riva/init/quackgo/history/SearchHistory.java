package riva.init.quackgo.history;

/**
 * Created by bharat.s on 8/6/15.
 */
public class SearchHistory {

    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String toString() {
        return searchText;
    }

}
