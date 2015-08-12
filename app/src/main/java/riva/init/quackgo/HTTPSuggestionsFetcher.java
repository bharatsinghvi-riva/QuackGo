package riva.init.quackgo;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by bharat.s on 8/7/15.
 */
public interface HTTPSuggestionsFetcher {
    @GET("/sugg")
    ArrayList<String> getHTTPSuggestions(@Query("nResults") int numResults, @Query("command") String keyword);
}
