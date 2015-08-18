package riva.init.quackgo;

import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by bharat.s on 8/5/15.
 */

public class HTTPSuggestions {

    private static final String BASE_URL = "https://complete.reports.mn/";
    private static final int NUMBER_OF_SUGGESTIONS = 5;

    public static List<String> retrieveHTTPSuggestions(String keyword) throws IOException {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();
        HTTPSuggestionsFetcher httpSuggestionsFetcher = restAdapter.create(HTTPSuggestionsFetcher.class);
        return httpSuggestionsFetcher.getHTTPSuggestions(NUMBER_OF_SUGGESTIONS, keyword);
    }

}
