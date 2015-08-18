package riva.init.quackgo;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import riva.init.quackgo.history.SearchHistoryDataSource;

/**
 * Created by bharat.s on 8/5/15.
 */
public class SuggestionsAdapter extends ArrayAdapter<String> implements Filterable {

    private static final String TAG = SuggestionsAdapter.class.getSimpleName();
    private ArrayList<String> localSuggestionsList;
    private List<String> httpSuggestionsList;
    private ArrayList<String> allSuggestionsList;

    private SearchHistoryDataSource searchHistoryDataSource;

    public SuggestionsAdapter(Context context, int resourceId) {
        super(context, resourceId);
        httpSuggestionsList = new ArrayList<>();
        localSuggestionsList = new ArrayList<>();
        allSuggestionsList = new ArrayList<>();
        searchHistoryDataSource = new SearchHistoryDataSource(((MyApp)context.getApplicationContext()).getMySQLiteDb());
        setNotifyOnChange(true);
    }

    @Override
    public int getCount() {
        return allSuggestionsList.size();
    }

    @Override
    public String getItem(int index) {
        return allSuggestionsList.get(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    try {
                        httpSuggestionsList = new ArrayList<>();
                        httpSuggestionsList = HTTPSuggestions.retrieveHTTPSuggestions(constraint.toString());
                        localSuggestionsList = new ArrayList<>();
                        searchHistoryDataSource.open();
                        localSuggestionsList = searchHistoryDataSource.getRelevantSearchHistory(constraint.toString());
                        searchHistoryDataSource.close();
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to retrieve suggestions. " + e.getMessage());
                    }
                }
                HashSet<String> allSuggestionsSet = new HashSet<>();
                allSuggestionsSet.addAll(localSuggestionsList);
                allSuggestionsSet.addAll(httpSuggestionsList);
                allSuggestionsList = new ArrayList<>();
                allSuggestionsList.addAll(allSuggestionsSet);
                FilterResults results = new FilterResults();
                results.values = allSuggestionsList;
                results.count = allSuggestionsList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) notifyDataSetChanged();
                else notifyDataSetInvalidated();
            }
        };
    }
}
