package com.hugo.mvvmsampleapplication.dependencyinjection.testcomponent;

import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserActivityTest;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface TestApplicationComponent extends ApplicationComponent {
  void inject(SearchUserActivityTest searchUserActivityTest);
}
