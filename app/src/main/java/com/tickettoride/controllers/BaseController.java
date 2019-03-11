package com.tickettoride.controllers;

import android.app.Activity;

import com.tickettoride.activities.LoginActivity;
import com.tickettoride.application.MyApplication;
import com.tickettoride.clientModels.ApplicationContextManager;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.models.Player;

public abstract class BaseController {
    protected Activity getCurrentActivity() {
        MyApplication myApplication = ApplicationContextManager.SINGLETON.getMyApplication();
        return myApplication.getCurrentActivity();
    }

    protected boolean isUserPlayer(Player player){
        return DataManager.getSINGLETON().getPlayer().equals(player);
    }
}
