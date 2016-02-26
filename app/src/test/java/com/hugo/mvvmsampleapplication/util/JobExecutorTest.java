package com.hugo.mvvmsampleapplication.util;

import org.junit.Test;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by hugo on 2/24/16.
 */
public class JobExecutorTest {

  @Test
  public void getSchedulerShouldReturnIoScheduler() {
    JobExecutor jobExecutor = new JobExecutor();
    assertEquals(Schedulers.io(), jobExecutor.getScheduler());
  }
}