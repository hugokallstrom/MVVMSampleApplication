package com.hugo.mvvmsampleapplication.features.userdetails;

import com.hugo.mvvmsampleapplication.MockFactory;
import com.hugo.mvvmsampleapplication.model.entities.Repository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class RepoViewModelTest {

  private RepoViewModel repoViewModel;
  private Repository mockRepository;

  @Before
  public void setUp() {
    mockRepository = MockFactory.buildMockRepository();
    repoViewModel = new RepoViewModel(mockRepository);
  }

  @Test public void testSetRepository() throws Exception {
    mockRepository.setName("NewName");
    repoViewModel.setRepository(mockRepository);
    Assert.assertEquals("NewName", repoViewModel.getRepoTitle());
  }

  @Test public void testGetRepoTitle() throws Exception {
    Assert.assertEquals(mockRepository.getName(), repoViewModel.getRepoTitle());
  }

  @Test public void testGetRepoDescription() throws Exception {
    Assert.assertEquals(mockRepository.getDescription(), repoViewModel.getRepoDescription());
  }

  @Test public void testGetWatchers() throws Exception {
    Assert.assertEquals(mockRepository.getWatchers() + "\n Watchers", repoViewModel.getWatchers());
  }

  @Test public void testGetStars() throws Exception {
    Assert.assertEquals(mockRepository.getStars() + "\n Stars", repoViewModel.getStars());
  }

  @Test public void testGetForks() throws Exception {
    Assert.assertEquals(mockRepository.getForks() + "\n Forks", repoViewModel.getForks());
  }
}