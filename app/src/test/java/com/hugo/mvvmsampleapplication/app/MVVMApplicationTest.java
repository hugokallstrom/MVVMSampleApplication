package com.hugo.mvvmsampleapplication.app;

import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.ApplicationComponent;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by hugo on 2/24/16.
 */
public class MVVMApplicationTest {

  @Mock
  private ApplicationComponent applicationComponent;
  private MVVMApplication mvvmApplication;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvvmApplication = new MVVMApplication();
  }

  @Test
  public void testSetAndGetApplicationComponent() throws Exception {
    ApplicationComponent nullApplicationComponent = mvvmApplication.getApplicationComponent();
    Assert.assertNull(nullApplicationComponent);

    mvvmApplication.setApplicationComponent(applicationComponent);
    applicationComponent = mvvmApplication.getApplicationComponent();
    Assert.assertNotNull(applicationComponent);
  }

}