package com.hugo.mvvmsampleapplication.util.dependencyinjection.components;

import android.content.Context;
import com.hugo.mvvmsampleapplication.features.BaseActivity;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseActivity baseActivity);

  GitHubService gitHubService();
  ThreadExecutor threadExecutor();
  PostExecutionThread postExecutionThread();

}
