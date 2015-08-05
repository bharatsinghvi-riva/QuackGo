package riva.init.quackgo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;


public class AugmentedSearch extends ActionBarActivity {

    private static final String TAG = AugmentedSearch.class.getSimpleName();
    private static final String searchEngine = "https://www.google.com/#q=";

    private AutoCompleteTextView _searchField;
    private Button _searchButton;
    private Switch _internetState;

    private ArrayList<String> httpSuggestionsArray;
    private HTTPSuggestions httpSuggestions;
    ArrayAdapter<String> httpSuggestionsAdapter;

    private void instantiate() {
        _searchField = (AutoCompleteTextView) findViewById(R.id.search_bar);
        _searchButton = (Button) findViewById(R.id.search_button);
        _internetState = (Switch) findViewById(R.id.internet_state);
        httpSuggestions = new HTTPSuggestions();
        httpSuggestionsArray = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented_search);

        instantiate();

        if (isConnected()) _internetState.toggle();

        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOnBrowser();
            }
        });

        httpSuggestionsAdapter = new ArrayAdapter<String>(AugmentedSearch.this, android.R.layout.simple_list_item_1, httpSuggestionsArray);
        httpSuggestionsAdapter.setNotifyOnChange(true);

        _searchField.setThreshold(3);
        _searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currKeyword = _searchField.getEditableText().toString();
                if (currKeyword.length() > 2) {
                    httpSuggestions.retrieveHTTPSuggestions(getSearchCallback(), currKeyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _searchField.setAdapter(httpSuggestionsAdapter);
        _searchField.setDropDownHeight(100);

    }

    public SearchCallback getSearchCallback() {
        return new SearchCallback() {
            @Override
            public void onFail() {
                Toast.makeText(AugmentedSearch.this, TAG + ":" + "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                httpSuggestionsArray.clear();
                httpSuggestionsArray.addAll(result);
                httpSuggestionsAdapter.notifyDataSetChanged();
            }
        };
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean isEmpty() {
        return _searchField.getEditableText().toString().trim().equals("") || (_searchField.getEditableText() == null);
    }

    private void searchOnBrowser() {
        if (isEmpty()) {
            Toast.makeText(this, R.string.empty_search_field, Toast.LENGTH_SHORT);
        } else {
            String url = searchEngine + _searchField.getEditableText();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_augmented_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
