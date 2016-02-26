package com.hugo.mvvmsampleapplication.features.searchuser;

import com.hugo.mvvmsampleapplication.features.UseCase;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import javax.inject.Inject;
import rx.Observable;

/**
 * Use case for searching for users.
 */
public class SearchUserUseCase extends UseCase {

  @Inject
  public SearchUserUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(gitHubService, threadExecutor, postExecutionThread);
  }

  @Override
  public Observable buildUseCase(String username) throws NullPointerException {
    if (username == null) {
      throw new NullPointerException("Query must not be null");
    }
    return getGitHubService().searchUser(username);
  }
}
