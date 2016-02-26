package com.hugo.mvvmsampleapplication.app;

import android.app.Application;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.components.DaggerApplicationComponent;
import com.hugo.mvvmsampleapplication.util.dependencyinjection.modules.ApplicationModule;

/**
 * Handles singleton instances which the rest of the system uses.
 */
public class MVVMApplication extends Application {

  private ApplicationComponent applicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    if(applicationComponent == null) {
      applicationComponent =
          DaggerApplicationComponent.builder().applicationModule(new ApplicationModule()).build();
    }
  }

  public void setApplicationComponent(ApplicationComponent applicationComponent) {
    this.applicationComponent = applicationComponent;
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }

}
