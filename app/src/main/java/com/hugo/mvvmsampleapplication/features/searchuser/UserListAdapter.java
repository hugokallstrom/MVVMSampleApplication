package com.hugo.mvvmsampleapplication.features.searchuser;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.databinding.ItemUserBinding;
import com.hugo.mvvmsampleapplication.model.entities.User;
import java.util.Collections;
import java.util.List;

/**
 * Adapter for the list displayed in search_user.xml. This adapter uses the {@link UserViewModel} to
 * bind
 * data to the view. The onClickListener needs to be handled by this adapter (not the
 * UserViewModel) in order to communicate with the {@link SearchUserFragment} which in turn calls
 * its ActivityListener.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

  private List<User> users;
  private OnItemClickListener onItemClickListener;

  public interface OnItemClickListener {
    void onItemClick(String username);
  }

  public UserListAdapter() {
    this.users = Collections.emptyList();
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  @Override public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final ItemUserBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_user,
            parent, false);
    return new UserViewHolder(binding);
  }

  @Override public void onBindViewHolder(UserViewHolder holder, int position) {
    final User user = users.get(position);
    holder.bindUser(user);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (onItemClickListener != null) {
          onItemClickListener.onItemClick(user.getLogin());
        }
      }
    });
  }

  @Override public int getItemCount() {
    return users.size();
  }

  public class UserViewHolder extends RecyclerView.ViewHolder {
    final ItemUserBinding binding;

    public UserViewHolder(ItemUserBinding binding) {
      super(binding.cardView);
      this.binding = binding;
    }

    void bindUser(User user) {
      if (binding.getViewModel() == null) {
        binding.setViewModel(new UserViewModel(user));
      } else {
        binding.getViewModel().setUser(user);
      }
    }

    public String getUsername() {
      return binding.getViewModel().getUsername();
    }
  }
}
