package com.hugo.mvvmsampleapplication.util;

import rx.Scheduler;

/**
 * Interface for retrieving a Scheduler.
 */
public interface PostExecutionThread {
  Scheduler getScheduler();
}
