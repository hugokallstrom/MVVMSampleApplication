package com.hugo.mvvmsampleapplication.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.databinding.UserDetailsBinding;
import com.hugo.mvvmsampleapplication.model.entities.Repository;
import com.hugo.mvvmsampleapplication.view.adapter.RepositoriesAdapter;
import com.hugo.mvvmsampleapplication.view.viewmodel.UserDetailsViewModel;

import java.util.List;


public class UserDetailsFragment extends Fragment implements UserDetailsViewModel.FragmentListener{

    private static final String EXTRA_USERNAME = "username";
    private UserDetailsBinding binding;
    private UserDetailsViewModel userDetailsViewModel;
    private String username;

    public UserDetailsFragment() {}

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
        this.username = getArguments().getString(EXTRA_USERNAME);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(userDetailsViewModel == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.user_details, container, false);
            userDetailsViewModel = new UserDetailsViewModel(getContext(), this);
            binding.setViewModel(userDetailsViewModel);
            setupRepositoriesList(binding.repositoriesList);
            userDetailsViewModel.loadRepositories(username);
        }
        return binding.getRoot();
    }

    public void setupRepositoriesList(RecyclerView repositoriesList) {
        RepositoriesAdapter userDetailsAdapter = new RepositoriesAdapter();
        repositoriesList.setAdapter(userDetailsAdapter);
        repositoriesList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void addRepositories(List<Repository> repositories) {
        RepositoriesAdapter adapter = (RepositoriesAdapter) binding.repositoriesList.getAdapter();
        adapter.setRepositories(repositories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userDetailsViewModel.destroy();
    }

}
