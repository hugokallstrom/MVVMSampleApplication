package com.hugo.mvvmsampleapplication;

import android.app.Application;
import android.content.Context;

import com.hugo.mvvmsampleapplication.model.network.GithubApi;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class MVVMApplication extends Application {

    private GithubApi githubService;
    private Scheduler defaultSubscribeScheduler;

    public static MVVMApplication get(Context context) {
        return (MVVMApplication) context.getApplicationContext();
    }

    public GithubApi getGithubService() {
        if (githubService == null) {
            githubService = GithubApi.Factory.create();
        }
        return githubService;
    }

    //For setting mocks during testing
    public void setGithubService(GithubApi githubService) {
        this.githubService = githubService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }
}
