package com.hugo.mvvmsampleapplication.features.userdetails;

import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.model.entities.Repository;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hugo on 2/15/16.
 */
@RunWith(MockitoJUnitRunner.class) public class LoadUserDetailsUseCaseTest {

  @Mock private GitHubService gitHubService;
  @Mock private PostExecutionThread mockPostExecutionThread;
  @Mock private ThreadExecutor mockThreadExecutor;

  private LoadUserDetailsUseCase loadUserDetailsUseCase;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(mockPostExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
    when(mockThreadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
    List<Repository> mockRepositories = MockFactory.buildMockUserDetailsResponse();
    when(gitHubService.getRepositoriesFromUser(any(String.class))).thenReturn(
        Observable.just(mockRepositories));
    loadUserDetailsUseCase =
        new LoadUserDetailsUseCase(gitHubService, mockThreadExecutor, mockPostExecutionThread);
  }

  @Test public void buildUseCaseShouldGetReposFromUser() throws Exception {
    loadUserDetailsUseCase.buildUseCase(MockFactory.TEST_USERNAME);
    verify(gitHubService).getRepositoriesFromUser(MockFactory.TEST_USERNAME);
  }

  @Test(expected = NullPointerException.class)
  public void buildUseCaseShouldThrowNullPointerExceptionIfUsernameNotSet() throws Exception {
    loadUserDetailsUseCase.buildUseCase(null);
  }

  @Test public void executeShouldReturnRepositoryList() {
    TestSubscriber<Repository> testSubscriber = new TestSubscriber<>();
    loadUserDetailsUseCase.execute(testSubscriber, MockFactory.TEST_USERNAME);
    testSubscriber.assertCompleted();
    testSubscriber.assertNoErrors();
    List<Repository> searchResponseList = testSubscriber.getOnNextEvents();
    Assert.assertEquals(1, searchResponseList.size());
  }
}
