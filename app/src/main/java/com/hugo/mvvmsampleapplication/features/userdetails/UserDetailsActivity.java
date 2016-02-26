package com.hugo.mvvmsampleapplication.features.userdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.features.BaseActivity;
import com.squareup.leakcanary.RefWatcher;

public class UserDetailsActivity extends BaseActivity {

  private static final String EXTRA_USERNAME = "USERNAME";

  public static Intent newIntent(Context context, String username) {
    Intent intent = new Intent(context, UserDetailsActivity.class);
    intent.putExtra(EXTRA_USERNAME, username);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_details);
    if (savedInstanceState == null) {
      String username = getIntent().getStringExtra(EXTRA_USERNAME);
      addFragment(R.id.content_activity_user_details, UserDetailsFragment.newInstance(username));
    }
  }
}
