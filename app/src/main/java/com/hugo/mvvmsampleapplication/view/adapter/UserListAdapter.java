package com.hugo.mvvmsampleapplication.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.databinding.ItemUserBinding;
import com.hugo.mvvmsampleapplication.model.entities.User;
import com.hugo.mvvmsampleapplication.view.viewmodel.UserViewModel;

import java.util.Collections;
import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<User> users;

    public UserListAdapter() {
        this.users = Collections.emptyList();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bindUser(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        final ItemUserBinding binding;

        public UserViewHolder(ItemUserBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }

        void bindUser(User user) {
            if(binding.getViewModel() == null) {
                binding.setViewModel(new UserViewModel(itemView.getContext(), user));
            } else {
                binding.getViewModel().setUser(user);
            }
        }

    }

}
