package com.hugo.mvvmsampleapplication.features.userdetails;

import android.view.View;
import android.widget.TextView;
import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.features.DefaultSubscriber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsViewModelTest {

  @Mock
  private LoadUserDetailsUseCase mockLoadUserDetailsUseCase;
  @Mock
  private TextView textView;
  @Mock
  private View view;
  @Mock
  private UserDetailsViewModel.FragmentListener fragmentListener;
  private UserDetailsViewModel userDetailsViewModel;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    userDetailsViewModel = new UserDetailsViewModel(mockLoadUserDetailsUseCase);
    userDetailsViewModel.setFragmentListener(fragmentListener);
  }

  @Test
  public void loadRepositoriesShouldLoadUserRepositoriesFromUseCase() throws Exception {
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    verify(mockLoadUserDetailsUseCase).execute(any(DefaultSubscriber.class), eq(MockFactory.TEST_USERNAME));
  }

  @Test
  public void loadRepositoriesShouldShowProgress() throws Exception {
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    assertEquals(userDetailsViewModel.getProgressVisibility().get(), View.VISIBLE);
  }

  @Test
  public void loadRepositoriesShouldHideRepoList() throws Exception {
    userDetailsViewModel.loadRepositories(MockFactory.TEST_USERNAME);
    assertEquals(userDetailsViewModel.getRepoListVisibility().get(), View.INVISIBLE);
  }

  @Test
  public void testDestroy() throws Exception {

  }
}