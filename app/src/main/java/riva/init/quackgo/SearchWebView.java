package riva.init.quackgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchWebView extends Activity {

    private static final String SEARCH_ENGINE = "http://www.google.com/#q=";

    @Bind(R.id.webview_parent) LinearLayout webviewParent;
    @Bind(R.id.address_bar) EditText addressBar;
    @Bind(R.id.search_button_webview) Button searchButton;
    WebView searchWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_web_view);
        ButterKnife.bind(this);
        searchWebView = new WebView(this);
        webviewParent.addView(searchWebView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        searchWebView.getSettings().setJavaScriptEnabled(true);
        searchWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String URL = SEARCH_ENGINE + extras.getString("keyword");
        searchWebView.loadUrl(URL);
        hideKeyBoard();
    }

    private void hideKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @OnClick(R.id.search_button_webview)
    protected void loadURL() {
        searchWebView.loadUrl(addressBar.getText().toString());
        hideKeyBoard();
    }

}
