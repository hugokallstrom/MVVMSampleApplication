package com.hugo.mvvmsampleapplication.features.searchuser;

import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.hugo.mvvmsampleapplication.BuildConfig;
import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.features.DefaultSubscriber;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.model.network.SearchResponse;
import com.hugo.mvvmsampleapplication.util.PostExecutionThread;
import com.hugo.mvvmsampleapplication.util.ThreadExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import retrofit.HttpException;
import retrofit.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchUserViewModelTest {

  @Mock private SearchUserUseCase mockSearchUserUseCase;
  @Mock private GitHubService gitHubService;
  @Mock private ThreadExecutor threadExecutor;
  @Mock private PostExecutionThread postExecutionThread;
  @Mock private TextView textView;
  @Mock private View view;
  @Mock private SearchUserViewModel.FragmentListener fragmentListener;
  @Captor private ArgumentCaptor<DefaultSubscriber> subscriberCaptor;

  private SearchUserViewModel searchUserViewModel;
  private SearchUserUseCase searchUserUseCase;
  private HttpException mockHttpException;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    setUpMocks();
    searchUserUseCase = new SearchUserUseCase(gitHubService, threadExecutor, postExecutionThread);
    searchUserViewModel = new SearchUserViewModel(mockSearchUserUseCase);
    searchUserViewModel.setFragmentListener(fragmentListener);
  }

  private void setUpMocks() {
    when(gitHubService.searchUser(MockFactory.TEST_USERNAME_NO_RESULTS)).thenReturn(
        Observable.just(MockFactory.buildEmptyMockSearchResponse()));
    when(gitHubService.searchUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockSearchResponse()));
    mockHttpException = new HttpException(Response.error(404, null));
    when(gitHubService.searchUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(
        Observable.<SearchResponse>error(mockHttpException));
    when(postExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
    when(threadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
  }

  @Test
  public void getUsernameTextWatcherShouldNotReturnNull() {
    TextWatcher usernameTextWatcher = searchUserViewModel.getUsernameTextWatcher();
    assertNotNull(usernameTextWatcher);
  }

  @Test
  public void onSearchActionShouldDisplayMessageWhenNoUsernameInput() {
    searchUserViewModel.onSearchAction(textView, EditorInfo.IME_ACTION_SEARCH, null);
    verify(fragmentListener).showMessage("Enter a username");
  }

  @Test
  public void onClickSearchShouldDisplayMessageWhenNoUsernameInput() {
    searchUserViewModel.onClickSearch(view);
    verify(fragmentListener).showMessage("Enter a username");
  }

  @Test
  public void onClickSearchShouldLoadUsersFromUseCase() {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    verify(mockSearchUserUseCase).execute(any(DefaultSubscriber.class),
        eq(MockFactory.TEST_USERNAME));
  }

  @Test
  public void onSearchActionShouldLoadUsersFromUseCase() {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onSearchAction(textView, EditorInfo.IME_ACTION_SEARCH, null);
    verify(mockSearchUserUseCase).execute(any(DefaultSubscriber.class),
        eq(MockFactory.TEST_USERNAME));
  }

  @Test
  public void onClickSearchShouldShowProgressIndicatorWhenLoadingUsers() {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    assertEquals(View.VISIBLE, searchUserViewModel.getProgressVisibility().get());
  }

  @Test
  public void onClickSearchShouldHideUserListWhileLoadingUsers() {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    assertEquals(View.INVISIBLE, searchUserViewModel.getUserListVisibility().get());
  }

  @Test
  public void onClickSearchShouldAddUsersToViewAfterReceivedUsers() {
    searchUserViewModel = new SearchUserViewModel(searchUserUseCase);
    searchUserViewModel.setFragmentListener(fragmentListener);
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    verify(fragmentListener).addUsers(anyList());
  }

  @Test
  public void onClickSearchShouldDisplayErrorMessageIfErrorWhenLoadingUsers() {
    searchUserViewModel = new SearchUserViewModel(searchUserUseCase);
    searchUserViewModel.setFragmentListener(fragmentListener);
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME_ERROR);
    searchUserViewModel.onClickSearch(view);
    verify(fragmentListener).showMessage("Error loading users");
  }

  @Test
  public void onClickSearchShouldDisplayErrorMessageIfNoUsersReceived() {
    searchUserViewModel = new SearchUserViewModel(searchUserUseCase);
    searchUserViewModel.setFragmentListener(fragmentListener);
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME_NO_RESULTS);
    searchUserViewModel.onClickSearch(view);
    verify(fragmentListener).showMessage("No users found");
  }

  @Test
  public void onClickSearchShouldHideProgressIndicatorAfterCompleted() {
    searchUserViewModel = new SearchUserViewModel(searchUserUseCase);
    searchUserViewModel.setFragmentListener(fragmentListener);
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    assertEquals(View.VISIBLE, searchUserViewModel.getUserListVisibility().get());
    assertEquals(View.INVISIBLE, searchUserViewModel.getProgressVisibility().get());
  }

  @Test
  public void onDestroyTrueShouldUnsubscribeFromUseCase() {
    searchUserViewModel.destroy(true);
    verify(mockSearchUserUseCase).unsubscribe();
  }

  @Test
  public void onDestroyFalseShouldNotUnsubscribeFromUseCase() {
    searchUserViewModel.destroy(false);
    verifyZeroInteractions(mockSearchUserUseCase);
  }
}