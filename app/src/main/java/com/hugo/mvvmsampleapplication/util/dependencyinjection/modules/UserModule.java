package com.hugo.mvvmsampleapplication.util.dependencyinjection.modules;

import com.hugo.mvvmsampleapplication.features.UseCase;
import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserUseCase;
import com.hugo.mvvmsampleapplication.features.userdetails.LoadUserDetailsUseCase;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.PerActivity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
public class UserModule {

  public UserModule() {
  }

  @Provides
  @PerActivity
  @Named("searchUser")
  public UseCase provideSearchUserUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new SearchUserUseCase(gitHubService, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named("userDetails")
  public UseCase provideUserDetailsUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new LoadUserDetailsUseCase(gitHubService, threadExecutor, postExecutionThread);
  }
}
