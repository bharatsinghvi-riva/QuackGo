package riva.init.quackgo;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by bharat.s on 8/5/15.
 */
public class HTTPSuggestionsAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> httpSuggestionsList;
    private HTTPSuggestions httpSuggestions;

    public HTTPSuggestionsAdapter(Context context, int resourceId) {
        super(context, resourceId);
        httpSuggestions = new HTTPSuggestions();
    }

    @Override
    public int getCount() {
        return httpSuggestionsList.size();
    }

    @Override
    public String getItem(int index) {
        return httpSuggestionsList.get(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if(constraint != null) {
                    httpSuggestions.retrieveHTTPSuggestions(getSearchCallback(), constraint.toString());
                }
                results.values = httpSuggestionsList;
                results.count = httpSuggestionsList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) notifyDataSetChanged();
                else notifyDataSetInvalidated();
            }
        };
    }

    public SearchCallback getSearchCallback() {
        return new SearchCallback() {
            @Override
            public void onFail() {
                httpSuggestionsList = new ArrayList<String>();
                System.out.println(httpSuggestionsList);
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                httpSuggestionsList = new ArrayList<>(result);
                System.out.println(httpSuggestionsList);
            }
        };
    }

}
