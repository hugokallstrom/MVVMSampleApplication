package com.hugo.mvvmsampleapplication.dependencyinjection.testmodule;

import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.ApplicationModule;
import org.mockito.Mockito;

/**
 * Created by hugo on 2/19/16.
 */
public class TestApplicationModule extends ApplicationModule {

  public TestApplicationModule() {
    super();
  }

  @Override
  public GitHubService provideGitHubService() {
    return Mockito.mock(GitHubService.class);
  }

}
