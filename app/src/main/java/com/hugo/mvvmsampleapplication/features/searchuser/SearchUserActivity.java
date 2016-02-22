package com.hugo.mvvmsampleapplication.features.searchuser;

import android.os.Bundle;

import android.util.Log;
import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.features.BaseActivity;
import com.hugo.mvvmsampleapplication.features.userdetails.UserDetailsActivity;
import com.hugo.mvvmsampleapplication.features.userdetails.UserDetailsFragment;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.DaggerApplicationComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.DaggerUserComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.UserComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.UserModule;

/**
 * Sets the content view to activity_search_user which host SearchUserFragment and/or
 * UserDetailsFragment. In order to communicate between the two fragments this class
 * implements SearchUserFragment.ActivityListener and implements startUserDetails. If
 * UserDetailsFragment is currently in the view, loading repositories directly is possible,
 * otherwise a new UserDetailsActivity is started.
 */
public class SearchUserActivity extends BaseActivity
    implements SearchUserFragment.ActivityListener {

  private static final String TAG = "SearchUserActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_user);
  }

  @Override
  public void startUserDetails(String username) {
    UserDetailsFragment userDetailsFragment =
        (UserDetailsFragment) getSupportFragmentManager().findFragmentById(
            R.id.userDetailsFragment);
    if (userDetailsFragment == null) {
      Log.d(TAG, "starting activity, no fragment in view");
      startActivity(UserDetailsActivity.newIntent(this, username));
    } else {
      Log.d(TAG, "fragment in view");
      userDetailsFragment.loadRepositories(username);
    }
  }
}
