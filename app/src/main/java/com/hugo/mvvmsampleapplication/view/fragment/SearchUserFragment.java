package com.hugo.mvvmsampleapplication.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.databinding.SearchUserBinding;
import com.hugo.mvvmsampleapplication.model.entities.User;
import com.hugo.mvvmsampleapplication.view.viewmodel.SearchUserViewModel;
import com.hugo.mvvmsampleapplication.view.adapter.UserListAdapter;

import java.util.List;

public class SearchUserFragment extends Fragment implements SearchUserViewModel.FragmentListener {

    private SearchUserBinding binding;
    private SearchUserViewModel searchUserViewModel;

    public SearchUserFragment() {}

    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(searchUserViewModel == null) {
            searchUserViewModel = new SearchUserViewModel(getActivity(), this);
            binding = DataBindingUtil.inflate(inflater, R.layout.search_user, container, false);
            binding.setViewModel(searchUserViewModel);
            setupUserList(binding.searchUserList);
        }
        return binding.getRoot();
    }

    private void setupUserList(RecyclerView searchUserList) {
        UserListAdapter userListAdapter = new UserListAdapter();
        searchUserList.setAdapter(userListAdapter);
        searchUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void addUsers(List<User> users) {
        UserListAdapter userListAdapter = (UserListAdapter) binding.searchUserList.getAdapter();
        userListAdapter.setUsers(users);
        userListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchUserViewModel.destroy();
    }

}
