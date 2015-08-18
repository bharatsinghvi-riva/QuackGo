package riva.init.quackgo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;

/**
 * Created by bharat.s on 8/18/15.
 */
public class SearchAutoCompleteTextView extends AutoCompleteTextView implements View.OnTouchListener, View.OnFocusChangeListener {

    Filter _httpFilter;

    private final Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                _httpFilter.filter((CharSequence) msg.obj, SearchAutoCompleteTextView.this);
            } else {
                _httpFilter.filter(null);
            }
        }
    };

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering(text, keyCode);
        _handler.removeMessages(0);
        _handler.sendMessageDelayed(_handler.obtainMessage(0, text), 0);
    }

    public SearchAutoCompleteTextView(Context context) {
        super(context);
        init();
    }

    public SearchAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public <T extends ListAdapter & HTTPFilterable & Filterable> void setCustomAdapter(T adapter) {
        super.setAdapter(adapter);
        _httpFilter = adapter.httpSuggestionsFilter();
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        return;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private void init() {
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
    }
}
