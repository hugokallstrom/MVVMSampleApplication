package com.hugo.mvvmsampleapplication.model.entities;

import com.google.gson.annotations.SerializedName;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;

/**
 * A POJO representing the JSON data of a user witch is fetched from {@link GitHubService}.
 */
public class User {

    private long id;
    private String login;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("repos_url")
    private String reposUrl;

    public User() {}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }
}
