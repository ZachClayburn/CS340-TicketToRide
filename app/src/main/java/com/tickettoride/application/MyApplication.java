package com.tickettoride.application;

import android.app.Activity;
import android.app.Application;

import com.tickettoride.clientModels.DataManager;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
    }
    private DataManager mDataManager = DataManager.getSINGLETON();
    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}
