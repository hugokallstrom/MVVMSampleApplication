package com.hugo.mvvmsampleapplication.util.dependencyinjection.components;

import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserFragment;
import com.hugo.mvvmsampleapplication.features.userdetails.UserDetailsFragment;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.PerActivity;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.ApplicationModule;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.UserModule;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = UserModule.class)
public interface UserComponent {
  void inject(SearchUserFragment searchUserFragment);
  void inject(UserDetailsFragment userDetailsFragment);
}
