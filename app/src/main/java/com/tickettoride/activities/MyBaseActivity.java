package com.tickettoride.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tickettoride.application.MyApplication;
import com.tickettoride.clientModels.ApplicationContextManager;
import com.tickettoride.command.ClientCommunicator;

public class MyBaseActivity extends AppCompatActivity {
    protected MyApplication mMyApp;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClientCommunicator.SINGLETON.connect();
        mMyApp = (MyApplication) this.getApplicationContext();
        ApplicationContextManager.SINGLETON.setMyApplication((mMyApp));
    }
    protected void onResume() {
        super.onResume();
        mMyApp.setCurrentActivity(this);
    }
    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = mMyApp.getCurrentActivity();
        if (this.equals(currActivity))
            mMyApp.setCurrentActivity(null);
    }

    public void moveToJoin() {
        Intent intent = new Intent(MyBaseActivity.this, MyBaseActivity.class);
        startActivity(intent);
    }
}
