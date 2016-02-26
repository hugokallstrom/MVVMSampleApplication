package com.hugo.mvvmsampleapplication.util;

import org.junit.Test;
import rx.android.schedulers.AndroidSchedulers;

import static org.junit.Assert.*;

/**
 * Created by hugo on 2/24/16.
 */
public class UiThreadTest {

  @Test
  public void getSchedulerShouldReturnAndroidSchedulersMainThread() {
    UiThread uiThread = new UiThread();
    assertEquals(AndroidSchedulers.mainThread(), uiThread.getScheduler());
  }
}