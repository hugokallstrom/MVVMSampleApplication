package com.hugo.mvvmsampleapplication.features.searchuser;

import android.widget.ImageView;
import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.model.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserViewModelTest {

  @Mock private ImageView imageView;
  private UserViewModel userViewModel;
  private User mockUser;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mockUser = MockFactory.buildMockUser();
    userViewModel = new UserViewModel(mockUser);
  }

  @Test
  public void testGetUsername() throws Exception {
    Assert.assertEquals(mockUser.getLogin(), userViewModel.getUsername());
  }

  @Test
  public void testGetImageUrl() throws Exception {
    Assert.assertEquals(mockUser.getAvatarUrl(), userViewModel.getImageUrl());
  }

  @Test
  public void testSetUser() throws Exception {
    mockUser.setLogin("NewName");
    userViewModel.setUser(mockUser);
    Assert.assertEquals("NewName", userViewModel.getUsername());
  }
}