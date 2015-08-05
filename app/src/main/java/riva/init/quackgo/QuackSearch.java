package riva.init.quackgo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;


public class QuackSearch extends ActionBarActivity {

    private static final String searchEngine = "http://www.google.com/#q=";

    private AutoCompleteTextView _searchField;
    private Button _searchButton;
    private Switch _internetState;

    private void instantiate() {
        _searchField = (AutoCompleteTextView) findViewById(R.id.search_bar);
        _searchButton = (Button) findViewById(R.id.search_button);
        _internetState = (Switch) findViewById(R.id.internet_state);
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

        HTTPSuggestionsAdapter httpSuggestionsAdapter = new HTTPSuggestionsAdapter(this, android.R.layout.simple_expandable_list_item_1);
        _searchField.setAdapter(httpSuggestionsAdapter);
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

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quack_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
