package com.hugo.mvvmsampleapplication.view.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableInt;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hugo.mvvmsampleapplication.MVVMApplication;
import com.hugo.mvvmsampleapplication.model.entities.User;
import com.hugo.mvvmsampleapplication.model.network.DefaultSubscriber;
import com.hugo.mvvmsampleapplication.model.network.GithubApi;
import com.hugo.mvvmsampleapplication.model.network.SearchResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchUserViewModel extends BaseObservable {

    public final ObservableInt progressVisibility;
    public final ObservableInt userListVisibility;

    private Context context;
    private FragmentListener fragmentListener;
    private String username;
    private Subscription subscription;

    public SearchUserViewModel(Context context, FragmentListener fragmentListener) {
        this.context = context;
        this.fragmentListener = fragmentListener;
        progressVisibility = new ObservableInt(View.INVISIBLE);
        userListVisibility = new ObservableInt(View.INVISIBLE);
        username = "";
    }

    public TextWatcher getUsernameTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                username = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if(username.isEmpty()) {
                showSnackbarMessage(view, "Enter a username");
            } else {
                loadUsers(view);
            }
            return true;
        }
        return false;
    }

    public void onClickSearch(View view) {
        if(username.isEmpty()) {
            showSnackbarMessage(view, "Enter a username");
        } else {
            loadUsers(view);
        }
    }

    private void loadUsers(View view) {
        progressVisibility.set(View.VISIBLE);
        userListVisibility.set(View.INVISIBLE);
        MVVMApplication application = MVVMApplication.get(view.getContext());
        GithubApi githubService = application.getGithubService();
        subscription = githubService.searchUser(username)
                .delay(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new SearchUserSubscriber());
    }

    private void showSnackbarMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void destroy() {
        context = null;
        fragmentListener = null;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private final class SearchUserSubscriber extends DefaultSubscriber<SearchResponse> {

        @Override
        public void onCompleted() {
            userListVisibility.set(View.VISIBLE);
            progressVisibility.set(View.INVISIBLE);
        }

        @Override
        public void onError(Throwable e) {
            progressVisibility.set(View.INVISIBLE);
            View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
            showSnackbarMessage(rootView, "Error Loading Users");
            e.printStackTrace();
        }

        @Override
        public void onNext(SearchResponse searchResponse) {
            List<User> users = searchResponse.getUsers();
            fragmentListener.addUsers(users);
        }

    }

    public interface FragmentListener {
        void addUsers(List<User> users);
    }
}
