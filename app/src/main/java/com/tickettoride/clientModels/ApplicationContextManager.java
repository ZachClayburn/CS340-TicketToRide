package com.tickettoride.clientModels;
import com.tickettoride.application.MyApplication;

public class ApplicationContextManager {
    public static ApplicationContextManager SINGLETON = new ApplicationContextManager();

    private ApplicationContextManager() { }

    private MyApplication myApplication = null;

    public void setMyApplication(MyApplication myApplication) { this.myApplication = myApplication; }
    public MyApplication getMyApplication() { return myApplication; }
}
