package riva.init.quackgo.history;

/**
 * Created by bharat.s on 8/6/15.
 */
public class SearchHistoryItem {

    private final String searchText;

    SearchHistoryItem(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String toString() {
        return searchText + " (from local)";
    }

}
