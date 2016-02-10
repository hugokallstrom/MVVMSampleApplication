package com.hugo.mvvmsampleapplication.view.viewmodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.View;

import com.hugo.mvvmsampleapplication.MVVMApplication;
import com.hugo.mvvmsampleapplication.model.entities.Repository;
import com.hugo.mvvmsampleapplication.model.network.DefaultSubscriber;
import com.hugo.mvvmsampleapplication.model.network.GithubApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDetailsViewModel {

    public final ObservableInt progressVisibility;
    public final ObservableInt repoListVisibility;

    private FragmentListener fragmentListener;
    private Context context;
    private Subscription subscription;

    public UserDetailsViewModel(Context context, FragmentListener fragmentListener) {
        this.context = context;
        this.fragmentListener = fragmentListener;
        progressVisibility = new ObservableInt(View.INVISIBLE);
        repoListVisibility = new ObservableInt(View.INVISIBLE);
    }

    public void loadRepositories(String username) {
        progressVisibility.set(View.VISIBLE);
        repoListVisibility.set(View.INVISIBLE);
        MVVMApplication application = MVVMApplication.get(context);
        GithubApi githubService = application.getGithubService();
        subscription = githubService.getReposetoriesFromUser(username)
                .delay(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new LoadRepositoriesSubscriber());
    }

    public void destroy() {
        context = null;
        fragmentListener = null;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private final class LoadRepositoriesSubscriber extends DefaultSubscriber<List<Repository>> {
        @Override
        public void onCompleted() {
            repoListVisibility.set(View.VISIBLE);
            progressVisibility.set(View.INVISIBLE);
        }

        @Override
        public void onError(Throwable e) {
            progressVisibility.set(View.INVISIBLE);
            e.printStackTrace();
        }

        @Override
        public void onNext(List<Repository> repositories) {
            fragmentListener.addRepositories(repositories);
        }
    }

    public interface FragmentListener {
        void addRepositories(List<Repository> repositories);
    }

}
