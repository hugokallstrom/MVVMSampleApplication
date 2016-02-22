package com.hugo.mvvmsampleapplication.util;

import javax.inject.Inject;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Implementation of PostExecutionThread which returns AndroidSchedulers.mainThread().
 */
public class UiThread implements PostExecutionThread {

  @Inject
  public UiThread() {}

  @Override
  public Scheduler getScheduler() {
    return AndroidSchedulers.mainThread();
  }
}
