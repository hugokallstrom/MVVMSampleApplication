package com.hugo.mvvmsampleapplication.model.network;

import com.hugo.mvvmsampleapplication.model.entities.Repository;
import java.util.List;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Interface for connecting to GitHub API.
 */
public interface GitHubService {

  @GET("users/{username}/repos")
  Observable<List<Repository>> getRepositoriesFromUser(@Path("username") String username);

  @GET("search/users")
  Observable<SearchResponse> searchUser(@Query("q") String username);

}
