package com.hugo.mvvmsampleapplication.view.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hugo.mvvmsampleapplication.R;
import com.hugo.mvvmsampleapplication.model.entities.User;
import com.hugo.mvvmsampleapplication.view.activity.UserDetailsActivity;
import com.squareup.picasso.Picasso;

public class UserViewModel extends BaseObservable {

    private Context context;
    private User user;

    public UserViewModel(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    public String getUsername() {
        return user.getLogin();
    }

    public String getImageUrl() {
        return user.getAvatarUrl();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(view);
    }

    public void onClickUser(View v) {
        String username = user.getLogin();
        context.startActivity(UserDetailsActivity.newIntent(context, username));
    }

    public void setUser(User user) {
        this.user = user;
        notifyChange();
    }
}
