package com.hugo.mvvmsampleapplication.view.viewmodel;

import android.databinding.BaseObservable;

import com.hugo.mvvmsampleapplication.model.entities.Repository;

public class RepoViewModel extends BaseObservable {

    private Repository repository;

    public RepoViewModel(Repository repository) {
        this.repository = repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
        notifyChange();
    }

    public String getRepoTitle() {
        return repository.getName();
    }

    public String getRepoDescription() {
        return repository.getDescription();
    }

    public String getWatchers() {
        String watchersString = Integer.toString(repository.getWatchers()) + "\n Watchers";
        return watchersString;
    }

    public String getStars() {
        String starsString = Integer.toString(repository.getStars()) + "\n Stars";
        return starsString;
    }

    public String getForks() {
        String forksString = Integer.toString(repository.getForks()) + "\n Forks";
        return forksString;
    }




}
