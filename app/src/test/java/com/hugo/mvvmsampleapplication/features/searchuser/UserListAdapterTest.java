package com.hugo.mvvmsampleapplication.features.searchuser;

import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.model.entities.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserListAdapterTest {

  private List<User> users = new ArrayList<>();
  private UserListAdapter userListAdapter;

  @Before
  public void setUp() {
    User user = MockFactory.buildMockUser();
    users.add(user);
    userListAdapter = new UserListAdapter();
  }

  @Test
  public void getItemCountShouldReturnNumberOfUsersInList() {
    userListAdapter.setUsers(users);
    int itemCount = userListAdapter.getItemCount();
    assertEquals(users.size(), itemCount);
  }

}