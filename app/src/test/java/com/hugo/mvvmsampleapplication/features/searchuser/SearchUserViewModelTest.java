package com.hugo.mvvmsampleapplication.features.searchuser;

import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserUseCase;
import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserViewModel;
import com.hugo.mvvmsampleapplication.features.DefaultSubscriber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchUserViewModelTest {

  @Mock
  private SearchUserUseCase mockSearchUserUseCase;

  @Mock
  private TextView textView;

  @Mock
  private View view;

  @Mock
  private SearchUserViewModel.FragmentListener fragmentListener;
  private SearchUserViewModel searchUserViewModel;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    searchUserViewModel = new SearchUserViewModel(mockSearchUserUseCase);
    searchUserViewModel.setFragmentListener(fragmentListener);
  }

  @Test
  public void getUsernameTextWatcherShouldNotReturnNull() throws Exception {
    TextWatcher usernameTextWatcher = searchUserViewModel.getUsernameTextWatcher();
    assertNotNull(usernameTextWatcher);
  }

  @Test
  public void onSearchActionShouldDisplayMessageWhenNoUsernameInput() throws Exception {
    searchUserViewModel.onSearchAction(textView, EditorInfo.IME_ACTION_SEARCH, null);
    verify(fragmentListener).showMessage("Enter a username");
  }

  @Test
  public void onClickSearchShouldDisplayMessageWhenNoUsernameInput() throws Exception {
    searchUserViewModel.onClickSearch(view);
    verify(fragmentListener).showMessage("Enter a username");
  }

  @Test
  public void onClickSearchShouldLoadUsersFromUseCase() throws Exception {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    verify(mockSearchUserUseCase).execute(any(DefaultSubscriber.class), eq(MockFactory.TEST_USERNAME));
  }

  @Test
  public void onSearchActionShouldLoadUsersFromUseCase() throws Exception {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onSearchAction(textView, EditorInfo.IME_ACTION_SEARCH, null);
    verify(mockSearchUserUseCase).execute(any(DefaultSubscriber.class), eq(MockFactory.TEST_USERNAME));
  }

  @Test
  public void shouldDisplayProgressIndicatorWhileLoadingUsers() {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    assertEquals(View.VISIBLE, searchUserViewModel.getProgressVisibility().get());
  }

  @Test
  public void shouldHideUserListWhileLoadingUsers() {
    searchUserViewModel.setUsername(MockFactory.TEST_USERNAME);
    searchUserViewModel.onClickSearch(view);
    assertEquals(View.INVISIBLE, searchUserViewModel.getUserListVisibility().get());
  }

  @Test
  public void onDestroyShouldUnsubscribeFromUseCase() throws Exception {
    searchUserViewModel.destroy();
    verify(mockSearchUserUseCase).unsubscribe();
  }
}