package com.hugo.mvvmsampleapplication.features.userdetails;

import android.databinding.ObservableInt;
import android.view.View;

import com.hugo.mvvmsampleapplication.features.UseCase;
import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserUseCase;
import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserViewModel;
import com.hugo.mvvmsampleapplication.model.entities.Repository;
import com.hugo.mvvmsampleapplication.features.DefaultSubscriber;

import com.hugo.mvvmsampleapplication.util.dependencyinjection.PerActivity;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Bound to the view search_user.xml. All user interactions are handled by this class and updates
 * UI elements depending on the action. SearchUserViewModel fetches data from the model via
 * {@link LoadUserDetailsUseCase}, and when data is received in {@link LoadRepositoriesSubscriber}
 * the view is updated
 * accordingly. Since the list displaying results has its own ViewModel, this class has to
 * send the result back to the FragmentListener which can update the adapter in the fragment.
 */
@PerActivity
public class UserDetailsViewModel {

  public final ObservableInt progressVisibility;
  public final ObservableInt repoListVisibility;

  private FragmentListener fragmentListener;
  private UseCase loadUserDetailsUseCase;

  @Inject
  public UserDetailsViewModel(@Named("userDetails") UseCase loadUserDetailsUseCase) {
    this.loadUserDetailsUseCase = loadUserDetailsUseCase;
    progressVisibility = new ObservableInt(View.INVISIBLE);
    repoListVisibility = new ObservableInt(View.INVISIBLE);
  }

  public void setFragmentListener(FragmentListener fragmentListener) {
    this.fragmentListener = fragmentListener;
  }

  public void loadRepositories(String username) {
    showProgressIndicator(true);
    loadUserDetailsUseCase.execute(new LoadRepositoriesSubscriber(), username);
  }

  private final class LoadRepositoriesSubscriber extends DefaultSubscriber<List<Repository>> {

    @Override
    public void onCompleted() {
      showProgressIndicator(false);
    }

    @Override
    public void onError(Throwable e) {
      showProgressIndicator(false);
      fragmentListener.showMessage("Error loading repositories");
    }

    @Override
    public void onNext(List<Repository> repositories) {
      if (repositories.isEmpty()) {
        fragmentListener.showMessage("No public repositories");
      } else {
        fragmentListener.addRepositories(repositories);
      }
    }
  }

  private void showProgressIndicator(boolean showProgress) {
    if (showProgress) {
      progressVisibility.set(View.VISIBLE);
      repoListVisibility.set(View.INVISIBLE);
    } else {
      repoListVisibility.set(View.VISIBLE);
      progressVisibility.set(View.INVISIBLE);
    }
  }

  public void destroy(Boolean unsubscribe) {
    fragmentListener = null;
    if (unsubscribe) {
      loadUserDetailsUseCase.unsubscribe();
    }
  }

  public ObservableInt getProgressVisibility() {
    return progressVisibility;
  }

  public ObservableInt getRepoListVisibility() {
    return repoListVisibility;
  }

  public interface FragmentListener {
    void addRepositories(List<Repository> repositories);

    void showMessage(String message);
  }
}
