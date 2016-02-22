package com.hugo.mvvmsampleapplication.features;

import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.model.network.SearchResponse;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UseCaseTest {

    @Mock
    private GitHubService gitHubService;
    @Mock
    private PostExecutionThread mockPostExecutionThread;
    @Mock
    private ThreadExecutor mockThreadExecutor;

    private UseCase useCase;

    @Before
    public void setUp() {
      MockitoAnnotations.initMocks(this);
      when(mockPostExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
      when(mockThreadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
      useCase = new UseCase(gitHubService, mockThreadExecutor, mockPostExecutionThread) {
        @Override public Observable buildUseCase(String query) {
          return Observable.just(MockFactory.buildMockSearchResponse());
        }
      };
    }

  @Test
  public void buildUseCaseShouldReturnObservable() throws Exception {
    Observable observable = useCase.buildUseCase(MockFactory.TEST_USERNAME);
    Assert.assertNotNull(observable);
  }
  @Test
  public void getGitHubServiceShouldNotReturnNull() throws Exception {
    GitHubService gitHubService = useCase.getGitHubService();
    org.junit.Assert.assertNotNull(gitHubService);
  }

  @Test
  public void unsubcribeShouldUnsubscribeSubscription() throws Exception {
    TestSubscriber<SearchResponse> testSubscriber = new TestSubscriber<>();
    useCase.execute(testSubscriber, MockFactory.TEST_USERNAME);
    useCase.unsubscribe();
    Subscription subscription = useCase.getSubscription();
    org.junit.Assert.assertEquals(true, subscription.isUnsubscribed());
  }
}
