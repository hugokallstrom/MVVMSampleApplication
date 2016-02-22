package com.hugo.mvvmsampleapplication.features;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.DaggerUserComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.UserComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.UserModule;

/**
 * Base class used by other activities.
 */
public abstract class BaseActivity extends AppCompatActivity {

  private UserComponent userComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getApplicationComponent().inject(this);
    initializeInject();
  }

  protected void addFragment(int containerViewId, Fragment fragment) {
    FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(containerViewId, fragment);
    fragmentTransaction.commit();
  }

  public ApplicationComponent getApplicationComponent() {
    return ((MVVMApplication) getApplication()).getApplicationComponent();
  }

  private void initializeInject() {
    userComponent = DaggerUserComponent.builder()
        .applicationComponent(getApplicationComponent())
        .userModule(new UserModule()).build();
  }

  public UserComponent getUserComponent() {
    return userComponent;
  }



}
