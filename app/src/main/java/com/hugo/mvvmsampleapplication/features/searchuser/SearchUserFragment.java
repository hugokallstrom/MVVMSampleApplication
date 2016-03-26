package com.hugo.mvvmsampleapplication.features.searchuser;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hugo.mvvmsampleapplication.app.MVVMApplication;
import com.hugo.mvvmsampleapplication.databinding.SearchUserBinding;
import com.hugo.mvvmsampleapplication.features.BaseActivity;
import com.hugo.mvvmsampleapplication.model.entities.User;
import com.squareup.leakcanary.RefWatcher;
import java.util.List;
import javax.inject.Inject;

/**
 * A passive view with the purpose of setting up UI and communicating with the
 * ActivityListener. Implements SearchUserViewModel.FragmentListener in order to receive calls
 * from {@link SearchUserViewModel}.
 */
public class SearchUserFragment extends Fragment implements SearchUserViewModel.FragmentListener {

  private static final String TAG = "SearchUserFragment";

  @Inject SearchUserViewModel searchUserViewModel;
  private SearchUserBinding binding;
  private ActivityListener activityListener;
  private UserListAdapter userListAdapter;

  public interface ActivityListener {
    void startUserDetails(String username);
  }

  public SearchUserFragment() {
    super();
  }

  public static SearchUserFragment newInstance() {
    return new SearchUserFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ActivityListener) {
      this.activityListener = (ActivityListener) context;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    ((BaseActivity)getActivity()).getUserComponent().inject(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    searchUserViewModel.setFragmentListener(this);
    binding = SearchUserBinding.inflate(inflater, container, false);
    binding.setViewModel(searchUserViewModel);
    setupUserList(binding.searchUserList);
    return binding.getRoot();
  }

  private void setupUserList(RecyclerView searchUserList) {
    if (userListAdapter == null) {
      userListAdapter = new UserListAdapter();
      userListAdapter.setOnItemClickListener(onItemClickListener);
    }
    searchUserList.setAdapter(userListAdapter);
    searchUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  private UserListAdapter.OnItemClickListener onItemClickListener =
      new UserListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String username) {
          if (activityListener != null) {
            activityListener.startUserDetails(username);
          }
        }
      };

  @Override
  public void addUsers(List<User> users) {
    userListAdapter = (UserListAdapter) binding.searchUserList.getAdapter();
    userListAdapter.setUsers(users);
    userListAdapter.notifyDataSetChanged();
  }

  @Override
  public void showMessage(String message) {
    View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
    snackbar.show();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityListener = null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    searchUserViewModel.destroy(false);
    binding.unbind();
  //  binding.executePendingBindings();
  //  binding.invalidateAll();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    searchUserViewModel.destroy(true);
  }
}
