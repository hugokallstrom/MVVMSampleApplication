package com.hugo.mvvmsampleapplication.util.dependencyinjection.modules;

import android.content.Context;
import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.JobExecutor;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import com.hugo.mvvmsampleapplication.util.UiThread;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class ApplicationModule {

  public ApplicationModule() {

  }
/*
  @Provides
  @Singleton
  public Context provideApplicationContext() {
    return this.application;
  }
*/
  @Provides
  @Singleton
  public GitHubService provideGitHubService() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
    return retrofit.create(GitHubService.class);
  }

  @Provides
  @Singleton
  public ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides
  @Singleton
  public PostExecutionThread providePostExecutionThread(UiThread uiThread) {
    return uiThread;
  }

}
