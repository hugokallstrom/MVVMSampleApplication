package com.hugo.mvvmsampleapplication.features.searchuser;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;

import android.test.suitebuilder.annotation.LargeTest;
import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.dependencyinjection.testcomponent.TestApplicationComponent;
import com.hugo.mvvmsampleapplication.dependencyinjection.testmodule.TestApplicationModule;
import com.hugo.mvvmsampleapplication.model.network.GitHubService;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.ApplicationModule;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import rx.Observable;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hugo.mvvmsampleapplication.OrientationChangeAction.orientationLandscape;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchUserActivityTest {

  @Rule public DaggerMockRule<ApplicationComponent> daggerMockRule =
      new DaggerMockRule<>(ApplicationComponent.class, new ApplicationModule()).set(
          new DaggerMockRule.ComponentSetter<ApplicationComponent>() {
            @Override
            public void setComponent(ApplicationComponent applicationComponent) {
              getApp().setApplicationComponent(applicationComponent);
            }
          });

  @Rule public final ActivityTestRule<SearchUserActivity> activityTestRule =
      new ActivityTestRule<>(SearchUserActivity.class, false, false);

  @Mock GitHubService mockGitHubService;

  private MVVMApplication getApp() {
    return (MVVMApplication) InstrumentationRegistry.getInstrumentation()
        .getTargetContext()
        .getApplicationContext();
  }

  @Before
  public void setUp() {
    setUpMockGitHubService();
    activityTestRule.launchActivity(null);
  }

  @SuppressWarnings("unchecked")
  private void setUpMockGitHubService() {
    when(mockGitHubService.searchUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockSearchResponse()));
    when(mockGitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockUserDetailsResponse()));
    when(mockGitHubService.searchUser(MockFactory.TEST_USERNAME_NO_RESULTS)).thenReturn(
        Observable.just(MockFactory.buildEmptyMockSearchResponse()));
    Observable error = Observable.error(new Throwable("Error"));
    when(mockGitHubService.searchUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(error);
    when(mockGitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(
        error);
  }

  @Test
  public void clickSearchWithNoQueryInputShouldReturnErrorMessage() throws Exception {
    onView(withId(R.id.button_search)).perform(click());
    onView(allOf(withId(android.support.design.R.id.snackbar_text),
        withText("Enter a username"))).check(matches(isDisplayed()));
  }

  @Test
  public void networkErrorShouldBeDisplayedInDialog() throws Exception {
    performSearch(MockFactory.TEST_USERNAME_ERROR);
    onView(allOf(withId(android.support.design.R.id.snackbar_text),
        withText("Error loading users"))).check(matches(isDisplayed()));
  }

  @Test
  public void searchResultsShouldBeHiddenIfNoSearch() throws Exception {
    onView(withId(R.id.search_user_list)).check(matches(not(isDisplayed())));
  }

  @Test
  public void clickSearchShouldDisplayResults() throws Exception {
    performSearch(MockFactory.TEST_USERNAME);
    onView(withId(R.id.search_user_list)).check(matches(isDisplayed()));
  }

  @Test
  public void clickOnSearchResultItemShouldDisplayUserDetails() throws Exception {
    performSearch(MockFactory.TEST_USERNAME);
    onView(withId(R.id.search_user_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withText(MockFactory.TEST_REPOSITORY)).check(matches(isDisplayed()));
  }

  @Test
  public void searchQueryTextShouldPersistDuringOrientationChange() throws Exception {
    onView(withId(R.id.edit_text_username)).perform(typeText("test"));
    onView(isRoot()).perform(orientationLandscape());
    onView(withId(R.id.edit_text_username)).check(matches(withText("test")));
  }

  @Test
  public void shouldDisplayProgressIndicatorAfterOrientationChange() throws Exception {
    // TODO: how to test?
  }

  @Test
  public void shouldDisplayResultsInNewConfigurationAfterLoading() throws Exception {
    performSearch(MockFactory.TEST_USERNAME);
    onView(isRoot()).perform(orientationLandscape());
    onView(withId(R.id.search_user_list)).check(matches(isDisplayed()));
  }

  private void performSearch(String query) {
    onView(withId(R.id.edit_text_username)).perform(typeText(query));
    onView(withId(R.id.button_search)).perform(click());
  }

  //TODO: no way of checking if soft keyboard is visible?
}