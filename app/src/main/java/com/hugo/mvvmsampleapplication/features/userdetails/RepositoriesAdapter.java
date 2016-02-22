package com.hugo.mvvmsampleapplication.features.userdetails;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.databinding.ItemRepoBinding;
import com.hugo.mvvmsampleapplication.model.entities.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Adapter for the list displayed in user_details.xml. This adapter uses the {@link RepoViewModel}
 * to bind
 * data to the view. The onClickListener needs to be handled by this adapter (not the
 * UserViewModel) in order to communicate with the {@link UserDetailsFragment} which in turn calls
 * its ActivityListener.
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepoViewHolder> {

  private List<Repository> repositories;

  public RepositoriesAdapter() {
    this.repositories = Collections.emptyList();
  }

  public void setRepositories(List<Repository> repositories) {
    this.repositories = repositories;
  }

  @Override public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ItemRepoBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_repo,
            parent, false);
    return new RepoViewHolder(binding);
  }

  @Override public void onBindViewHolder(RepoViewHolder holder, int position) {
    Repository repository = repositories.get(position);
    holder.bindRepo(repository);
  }

  @Override public int getItemCount() {
    return repositories.size();
  }

  public class RepoViewHolder extends RecyclerView.ViewHolder {

    ItemRepoBinding binding;

    public RepoViewHolder(ItemRepoBinding binding) {
      super(binding.cardView);
      this.binding = binding;
    }

    void bindRepo(Repository repository) {
      if (binding.getViewModel() == null) {
        binding.setViewModel(new RepoViewModel(repository));
      } else {
        binding.getViewModel().setRepository(repository);
      }
    }
  }
}
