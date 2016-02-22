package com.hugo.mvvmsampleapplication.model.network;

import com.google.gson.annotations.SerializedName;
import com.hugo.mvvmsampleapplication.model.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO representing the JSON data of a search response witch is fetched from {@link GitHubService}.
 */
public class SearchResponse {
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("incomplete_results")
    private boolean incompleteResults;
    @SerializedName("items")
    private List<User> users = new ArrayList<User>();

    public SearchResponse() {}

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }
}
