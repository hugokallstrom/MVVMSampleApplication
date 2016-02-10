package com.hugo.mvvmsampleapplication.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.view.fragment.SearchUserFragment;

public class SearchUserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        if(savedInstanceState == null) {
            addFragment(R.id.content_activity_search_user, SearchUserFragment.newInstance());
        }
    }

}
