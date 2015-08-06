package riva.init.quackgo;

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

    public static ArrayList<String> retrieveHTTPSuggestions(String keyword) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + keyword).build();
        Response response = new OkHttpClient().newCall(request).execute();
        if(!response.isSuccessful()) throw new IOException();
        String result = response.body().string();
        result = result.substring(1, result.length()-2).replaceAll("\"","");
        ArrayList<String> suggestions = new ArrayList<>();
        for(String res: result.split(",")) suggestions.add(res);
        return suggestions;
    }

}
