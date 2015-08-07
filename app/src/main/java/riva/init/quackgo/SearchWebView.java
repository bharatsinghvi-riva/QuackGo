package riva.init.quackgo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchWebView extends Activity {

    private static final String SEARCH_ENGINE = "http://www.google.com/#q=";
    @Bind(R.id.search_webview) WebView searchWebView;
    @Bind(R.id.address_bar) EditText addressBar;
    @Bind(R.id.search_button_webview) Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_web_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String URL = SEARCH_ENGINE + extras.getString("keyword");
        searchWebView.getSettings().setJavaScriptEnabled(true);
        searchWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        System.out.println(URL);
        searchWebView.loadUrl(URL);
    }

    @OnClick(R.id.search_button_webview)
    protected void loadURL() {
        searchWebView.loadUrl(addressBar.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_web_view, menu);
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
