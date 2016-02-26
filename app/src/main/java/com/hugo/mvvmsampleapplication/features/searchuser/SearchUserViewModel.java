package com.hugo.mvvmsampleapplication.features.searchuser;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.features.UseCase;
import com.hugo.mvvmsampleapplication.model.entities.User;
import com.hugo.mvvmsampleapplication.features.DefaultSubscriber;
import com.hugo.mvvmsampleapplication.model.network.SearchResponse;

import com.hugo.mvvmsampleapplication.util.dependencyinjection.PerActivity;
import com.squareup.leakcanary.RefWatcher;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Subscriber;

/**
 * Bound to the view search_user.xml. All user interactions are handled by this class and updates
 * UI elements depending on the action. SearchUserViewModel fetches data from the model via
 * {@link SearchUserUseCase}, and when data is received in {@link SearchUserSubscriber} the view is
 * updated
 * accordingly. Since the list displaying results has its own ViewModel, this class has to
 * send the result back to the FragmentListener which can update the adapter in the fragment.
 */
@PerActivity
public class SearchUserViewModel extends BaseObservable {

  public final ObservableInt progressVisibility;
  public final ObservableInt userListVisibility;

  private FragmentListener fragmentListener;
  private String username;
  private UseCase searchUserUseCase;

  @Inject
  public SearchUserViewModel(@Named("searchUser") UseCase searchUserUseCase) {
    this.searchUserUseCase = searchUserUseCase;
    progressVisibility = new ObservableInt(View.INVISIBLE);
    userListVisibility = new ObservableInt(View.INVISIBLE);
  }

  public void setFragmentListener(FragmentListener fragmentListener) {
    this.fragmentListener = fragmentListener;
  }

  public TextWatcher getUsernameTextWatcher() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        username = charSequence.toString();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };
  }

  public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
      loadUsers();
      return true;
    }
    return false;
  }

  public void onClickSearch(View view) {
    loadUsers();
  }

  private void loadUsers() {
    if (username == null) {
      fragmentListener.showMessage("Enter a username");
    } else {
      showProgressIndicator(true);
      searchUserUseCase.execute(new SearchUserSubscriber(), username);
    }
  }

  private final class SearchUserSubscriber extends DefaultSubscriber<SearchResponse> {

    @Override
    public void onCompleted() {
      showProgressIndicator(false);
    }

    @Override
    public void onError(Throwable e) {
      showProgressIndicator(false);
      fragmentListener.showMessage("Error loading users");
    }

    @Override
    public void onNext(SearchResponse searchResponse) {
      List<User> users = searchResponse.getUsers();
      if (users.isEmpty()) {
        fragmentListener.showMessage("No users found");
      } else {
        fragmentListener.addUsers(users);
      }
    }
  }

  private void showProgressIndicator(boolean showProgress) {
    if(showProgress) {
      progressVisibility.set(View.VISIBLE);
      userListVisibility.set(View.INVISIBLE);
    } else {
      userListVisibility.set(View.VISIBLE);
      progressVisibility.set(View.INVISIBLE);
    }
  }

  public void destroy(Boolean unsubsricbe) {
    fragmentListener = null;
    if(unsubsricbe) {
      searchUserUseCase.unsubscribe();
    }
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public ObservableInt getProgressVisibility() {
    return progressVisibility;
  }

  public ObservableInt getUserListVisibility() {
    return userListVisibility;
  }

  public interface FragmentListener {

    void addUsers(List<User> users);

    void showMessage(String message);
  }
}
