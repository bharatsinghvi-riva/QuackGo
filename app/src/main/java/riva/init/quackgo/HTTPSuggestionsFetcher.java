package riva.init.quackgo;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by bharat.s on 8/7/15.
 */
public interface HTTPSuggestionsFetcher {
    @GET("/sugg")
    List<String> getHTTPSuggestions(@Query("nResults") int numResults, @Query("command") String keyword);
}
