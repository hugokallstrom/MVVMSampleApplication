package com.hugo.mvvmsampleapplication.features.userdetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.databinding.UserDetailsBinding;
import com.hugo.mvvmsampleapplication.features.BaseActivity;
import com.hugo.mvvmsampleapplication.features.searchuser.SearchUserViewModel;
import com.hugo.mvvmsampleapplication.model.entities.Repository;

import com.squareup.leakcanary.RefWatcher;
import java.util.List;
import javax.inject.Inject;

/**
 * A passive view with the purpose of setting up UI and communicating with the
 * ActivityListener. Implements UserDetailsUserViewModel.FragmentListener in order to receive calls
 * from {@link UserDetailsViewModel}.
 */
public class UserDetailsFragment extends Fragment implements UserDetailsViewModel.FragmentListener {

  private static final String EXTRA_USERNAME = "USERNAME";

  @Inject UserDetailsViewModel userDetailsViewModel;
  private UserDetailsBinding binding;
  private RepositoriesAdapter userDetailsAdapter;

  public UserDetailsFragment() {

  }

  public static UserDetailsFragment newInstance(String username) {
    UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_USERNAME, username);
    userDetailsFragment.setArguments(bundle);
    return userDetailsFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    ((BaseActivity)getActivity()).getUserComponent().inject(this);
    userDetailsViewModel.setFragmentListener(this);
    if (getArguments() != null) {
      String username = getArguments().getString(EXTRA_USERNAME);
      userDetailsViewModel.loadRepositories(username);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = UserDetailsBinding.inflate(inflater, container, false);
    binding.setViewModel(userDetailsViewModel);
    setupRepositoriesList(binding.repositoriesList);
    return binding.getRoot();
  }

  public void setupRepositoriesList(RecyclerView repositoriesList) {
    if (userDetailsAdapter == null) {
      userDetailsAdapter = new RepositoriesAdapter();
    }
    repositoriesList.setAdapter(userDetailsAdapter);
    repositoriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  @Override
  public void addRepositories(List<Repository> repositories) {
    RepositoriesAdapter adapter = (RepositoriesAdapter) binding.repositoriesList.getAdapter();
    adapter.setRepositories(repositories);
    adapter.notifyDataSetChanged();
  }

  public void loadRepositories(String username) {
    userDetailsViewModel.loadRepositories(username);
  }

  @Override
  public void showMessage(String message) {
    View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
    snackbar.show();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    userDetailsViewModel.destroy(false);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    userDetailsViewModel.destroy(true);
  }
}
