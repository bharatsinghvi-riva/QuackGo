package riva.init.quackgo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import riva.init.quackgo.history.SearchHistoryDataSource;


public class QuackSearch extends ActionBarActivity {

    private static final String TAG = QuackSearch.class.getSimpleName();

    SearchAutoCompleteTextView _searchField;
    @Bind(R.id.search_button) Button _searchButton;
    @Bind(R.id.internet_state) Switch _internetState;
    @BindString(R.string.empty_search_field) String empty_search_field;

    private SearchHistoryDataSource searchHistoryDataSource;
    private ConnectivityManager connectivityManager;
    private MyApp myApp;
    private ConnectivityChangeReceiver connectivityChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented_search);
        ButterKnife.bind(this);

        myApp = (MyApp) getApplicationContext();
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        searchHistoryDataSource = new SearchHistoryDataSource(myApp.getMySQLiteDb());
        connectivityChangeReceiver = new ConnectivityChangeReceiver();

        if (isConnected()) _internetState.toggle();

        registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        _searchField = (SearchAutoCompleteTextView) findViewById(R.id.search_bar);
        SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(this, android.R.layout.simple_expandable_list_item_1);
        _searchField.setCustomAdapter(suggestionsAdapter);

        setStickyNotification();
    }

    private void setStickyNotification() {
        Intent notificationIntent = new Intent(this, QuackSearch.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification stickyNotification = new Notification(R.mipmap.ic_launcher, null, System.currentTimeMillis());
        stickyNotification.contentView = new RemoteViews(getPackageName(), R.layout.notification_custom_view);
        stickyNotification.flags = Notification.FLAG_NO_CLEAR;
        stickyNotification.contentIntent = contentIntent;
        notificationManager.notify(1, stickyNotification);
    }

    private boolean isEmpty() {
        return _searchField.getEditableText().toString().trim().equals("") || (_searchField.getEditableText() == null);
    }

    @OnClick(R.id.search_button)
    public void openSearchResults() {
        if (isEmpty()) {
            Toast.makeText(this, empty_search_field, Toast.LENGTH_SHORT).show();
        } else {
            try {
                searchHistoryDataSource.open();
                searchHistoryDataSource.insertSearchHistory(_searchField.getEditableText().toString());
                searchHistoryDataSource.close();
            } catch (Exception e) {
                Log.e(TAG, "Error in adding keyword to database. " + e.getMessage());
            }
            Intent searchResults = new Intent(QuackSearch.this, SearchWebView.class);
            searchResults.putExtra("keyword", _searchField.getEditableText().toString());
            startActivity(searchResults);
        }
    }

    private boolean isConnected() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private class ConnectivityChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = isConnected();
            if(isConnected) _internetState.setChecked(true);
            else _internetState.setChecked(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityChangeReceiver);
    }
}
