package riva.init.quackgo;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.client.Response;

/**
 * Created by bharat.s on 8/5/15.
 */

public class HTTPSuggestions {

    private static final String BASE_URL = "https://complete.reports.mn/";

    public static ArrayList<String> retrieveHTTPSuggestions(String keyword) throws IOException {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();
        HTTPSuggestionsFetcher httpSuggestionsFetcher = restAdapter.create(HTTPSuggestionsFetcher.class);
        return httpSuggestionsFetcher.getHTTPSuggestions(5, keyword);
//        String result = response.getBody().toString();
//        System.out.println(response.toString());
//        result = result.substring(1, result.length()-2).replaceAll("\"","");
//        ArrayList<String> suggestions = new ArrayList<>();
//        for(String res: result.split(",")) suggestions.add(res);
//        return suggestions;
    }

}
