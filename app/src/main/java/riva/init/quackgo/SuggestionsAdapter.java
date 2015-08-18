package riva.init.quackgo;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import riva.init.quackgo.history.SearchHistoryDataSource;

/**
 * Created by bharat.s on 8/5/15.
 */
public class SuggestionsAdapter extends ArrayAdapter<String> implements HTTPFilterable {

    private static final String TAG = SuggestionsAdapter.class.getSimpleName();
    private ArrayList<String> localSuggestionsList;
    private List<String> httpSuggestionsList;

    private SearchHistoryDataSource searchHistoryDataSource;

    public SuggestionsAdapter(Context context, int resourceId) {
        super(context, resourceId);
        httpSuggestionsList = new ArrayList<>();
        localSuggestionsList = new ArrayList<>();
        searchHistoryDataSource = new SearchHistoryDataSource(((MyApp) context.getApplicationContext()).getMySQLiteDb());
        setNotifyOnChange(true);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    try {
                        localSuggestionsList = new ArrayList<>();
                        searchHistoryDataSource.open();
                        localSuggestionsList = searchHistoryDataSource.getRelevantSearchHistory(constraint.toString());
                        searchHistoryDataSource.close();
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to retrieve local suggestions. " + e.getMessage());
                    }
                }
                HashSet<String> localSuggestionsSet = new HashSet<>();
                localSuggestionsSet.addAll(localSuggestionsList);
                localSuggestionsList.clear();
                localSuggestionsList.addAll(localSuggestionsSet);
                FilterResults results = new FilterResults();
                results.values = localSuggestionsList;
                results.count = localSuggestionsList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                setNotifyOnChange(false);
                clear();
                if (results != null && results.count > 0) {
                    addAll((List<String>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public Filter httpSuggestionsFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(TAG, "HTTP Suggestions constraint: " + constraint.toString());
                try {
                    httpSuggestionsList = new ArrayList<>();
                    httpSuggestionsList = HTTPSuggestions.retrieveHTTPSuggestions(constraint.toString());
                    Log.d(TAG, httpSuggestionsList.toString());
                } catch (IOException e) {
                    Log.e(TAG, "Failed to retrieve remote suggestions. " + e.getMessage());
                }
                FilterResults results = new FilterResults();
                results.values = httpSuggestionsList;
                results.count = httpSuggestionsList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                setNotifyOnChange(false);
                clear();
                addAll(localSuggestionsList);
                if (results != null && results.count > 0) {
                    addAll((List<String>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }
}
