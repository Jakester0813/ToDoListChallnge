package com.example.jake.todolistchallenge.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Jake on 7/23/2017.
 */

public class ToDoApplication extends Application {

    private static ToDoApplication instance;


    public static ToDoApplication getInstance(){
        return instance != null ? instance : new ToDoApplication();
    }

    @Override
    public void onCreate(){
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }

}
