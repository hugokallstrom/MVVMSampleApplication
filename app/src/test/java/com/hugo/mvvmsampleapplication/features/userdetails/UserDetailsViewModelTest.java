package com.hugo.mvvmsampleapplication.features.userdetails;

import android.view.View;
import android.widget.TextView;
import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.features.DefaultSubscriber;
import com.hugo.mvvmsampleapplication.model.entities.Repository;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.model.network.SearchResponse;
import com.hugo.mvvmsampleapplication.util.JobExecutor;
import com.hugo.mvvmsampleapplication.util.UiThread;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit.HttpException;
import retrofit.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsViewModelTest {

  @Mock private LoadUserDetailsUseCase mockLoadUserDetailsUseCase;
  @Mock private TextView textView;
  @Mock private View view;
  @Mock private UserDetailsViewModel.FragmentListener fragmentListener;
  @Mock private GitHubService gitHubService;
  @Mock private JobExecutor threadExecutor;
  @Mock private UiThread postExecutionThread;

  private HttpException mockHttpException;
  private UserDetailsViewModel userDetailsViewModel;
  private LoadUserDetailsUseCase loadUserDetailsUseCase;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    setUpMocks();
    loadUserDetailsUseCase =
        new LoadUserDetailsUseCase(gitHubService, threadExecutor, postExecutionThread);
    userDetailsViewModel = new UserDetailsViewModel(mockLoadUserDetailsUseCase);
    userDetailsViewModel.setFragmentListener(fragmentListener);
  }

  private void setUpMocks() {
    when(gitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME_NO_RESULTS)).thenReturn(
        Observable.just(MockFactory.buildEmptyRepositoryList()));
    when(gitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockUserDetailsResponse()));
    mockHttpException = new HttpException(Response.error(404, null));
    when(gitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(
        Observable.<List<Repository>>error(mockHttpException));
    when(threadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
    when(postExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
  }

  @Test
  public void loadRepositoriesShouldLoadUserRepositoriesFromUseCase() {
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    verify(mockLoadUserDetailsUseCase).execute(any(DefaultSubscriber.class),
        eq(MockFactory.TEST_USERNAME));
  }

  @Test
  public void loadRepositoriesShouldShowProgress() {
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    assertEquals(userDetailsViewModel.getProgressVisibility().get(), View.VISIBLE);
  }

  @Test
  public void loadRepositoriesShouldHideRepoList() {
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    assertEquals(userDetailsViewModel.getRepoListVisibility().get(), View.INVISIBLE);
  }

  @Test
  public void loadRepositoriesShouldShowMessageIfEmptyResponse() {
    userDetailsViewModel = new UserDetailsViewModel(loadUserDetailsUseCase);
    userDetailsViewModel.setFragmentListener(fragmentListener);
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME_NO_RESULTS);
    verify(fragmentListener).showMessage("No public repositories");
  }

  @Test
  public void loadRepositoriesShouldCallAddRepositories() {
    userDetailsViewModel = new UserDetailsViewModel(loadUserDetailsUseCase);
    userDetailsViewModel.setFragmentListener(fragmentListener);
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    verify(fragmentListener).addRepositories(anyList());
  }

  @Test
  public void loadRepositoriesShouldShowMessageIfError() {
    userDetailsViewModel = new UserDetailsViewModel(loadUserDetailsUseCase);
    userDetailsViewModel.setFragmentListener(fragmentListener);
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME_ERROR);
    verify(fragmentListener).showMessage("Error loading repositories");
  }

  @Test
  public void loadRepositoriesShouldHideProgressIndicatorWhenComplete() {
    userDetailsViewModel = new UserDetailsViewModel(loadUserDetailsUseCase);
    userDetailsViewModel.setFragmentListener(fragmentListener);
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    assertEquals(View.VISIBLE, userDetailsViewModel.getRepoListVisibility().get());
    assertEquals(View.INVISIBLE, userDetailsViewModel.getProgressVisibility().get());
  }

  @Test
  public void onDestroyTrueShouldUnsubscribeFromUseCase() {
    userDetailsViewModel.destroy(true);
    verify(mockLoadUserDetailsUseCase).unsubscribe();
  }

  @Test
  public void onDestroyFalseShouldNotUnsubscribeFromUseCase() {
    userDetailsViewModel.destroy(false);
    verifyZeroInteractions(mockLoadUserDetailsUseCase);
  }
}