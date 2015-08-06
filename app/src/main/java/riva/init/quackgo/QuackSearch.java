package riva.init.quackgo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import riva.init.quackgo.history.SearchHistoryDataSource;


public class QuackSearch extends ActionBarActivity {

    private static final String searchEngine = "http://www.google.com/#q=";
    private static final String TAG = QuackSearch.class.getSimpleName();

    private AutoCompleteTextView _searchField;
    private Button _searchButton;
    private Switch _internetState;

    private SearchHistoryDataSource searchHistoryDataSource;

    private void instantiate() {
        _searchField = (AutoCompleteTextView) findViewById(R.id.search_bar);
        _searchButton = (Button) findViewById(R.id.search_button);
        _internetState = (Switch) findViewById(R.id.internet_state);
        searchHistoryDataSource = new SearchHistoryDataSource(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented_search);
        instantiate();

        if (isConnected()) _internetState.toggle();

        registerReceiver(new ConnectivityChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        _searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOnBrowser();
            }
        });

        SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(this, android.R.layout.simple_expandable_list_item_1);
        _searchField.setAdapter(suggestionsAdapter);
    }

    private boolean isEmpty() {
        return _searchField.getEditableText().toString().trim().equals("") || (_searchField.getEditableText() == null);
    }

    private void searchOnBrowser() {
        if (isEmpty()) {
            Toast.makeText(this, R.string.empty_search_field, Toast.LENGTH_SHORT);
        } else {
            try {
                searchHistoryDataSource.open();
                searchHistoryDataSource.insertSearchHistory(_searchField.getEditableText().toString());
                searchHistoryDataSource.close();
            } catch (Exception e) {
                Log.e(TAG, "Error in database operations. " + e.getMessage());
            }
            String url = searchEngine + _searchField.getEditableText().toString();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quack_search, menu);
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

    private class ConnectivityChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = isConnected();
            if(isConnected) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _internetState.setChecked(true);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _internetState.setChecked(false);
                    }
                });
            }
        }
    }
}
