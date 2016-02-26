package com.hugo.mvvmsampleapplication.features.userdetails;

import com.hugo.mvvmsampleapplication.features.UseCase;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import javax.inject.Inject;
import rx.Observable;

/**
 * Use case for loading a users repository.
 */
public class LoadUserDetailsUseCase extends UseCase {

  @Inject
  public LoadUserDetailsUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(gitHubService, threadExecutor, postExecutionThread);
  }

  @Override public Observable buildUseCase(String username) throws NullPointerException {
    if (username == null) {
      throw new NullPointerException("Username must not be null");
    }
    return getGitHubService().getRepositoriesFromUser(username);
  }
}
