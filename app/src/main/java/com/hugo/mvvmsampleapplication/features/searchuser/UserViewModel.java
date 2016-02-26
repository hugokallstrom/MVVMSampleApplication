package com.hugo.mvvmsampleapplication.features.searchuser;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.model.entities.User;
import com.squareup.picasso.Picasso;

/**
 * Bound to the view item_user.xml.
 */
public class UserViewModel extends BaseObservable {

  private User user;

  public UserViewModel(User user) {
    this.user = user;
  }

  public String getUsername() {
    return user.getLogin();
  }

  public String getImageUrl() {
    return user.getAvatarUrl();
  }

  @BindingAdapter({ "bind:imageUrl" })
  public static void loadImage(ImageView view, String imageUrl) {
    Picasso.with(view.getContext()).load(imageUrl).placeholder(R.drawable.placeholder).fit().into(view);
  }

  public void setUser(User user) {
    this.user = user;
    notifyChange();
  }
}
