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

public interface GithubApi {

    String endpoint = "https://api.github.com/";
    String getRepoUrl = "users/{username}/repos";
    String searchUserUrl = "search/users";

    @GET(getRepoUrl)
    Observable<List<Repository>> getReposetoriesFromUser(@Path("username") String username);

    @GET(searchUserUrl)
    Observable<SearchResponse> searchUser(@Query("q") String username);

    class Factory {
        public static GithubApi create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(GithubApi.class);
        }
    }
}