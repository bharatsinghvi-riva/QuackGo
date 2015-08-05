package riva.init.quackgo;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bharat.s on 8/5/15.
 */

public class HTTPSuggestions {

    private static final String BASE_URL = "https://complete.reports.mn/sugg?nResults=5&command=";

    public static void retrieveHTTPSuggestions(final SearchCallback searchCallback, String keyword) {
        Request request = new Request.Builder().url(BASE_URL+keyword).build();
        final Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                searchCallback.onFail();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    searchCallback.onFail();
                } else {
                    String result = response.body().string();
                    result = result.substring(1, result.length() - 2);
                    result = result.replaceAll("\"", "");
                    ArrayList<String> suggestions = new ArrayList<String>();
                    for(String r: result.split(",")) {
                        suggestions.add(r);
                    }
                    searchCallback.onSuccess(suggestions);
                }
            }
        });
    }

}
