package net.nqlab.simple.controller;
 
import android.app.Application;

import net.nqlab.simple.model.SecureSaveData;
import net.nqlab.simple.model.SaveData;
import net.nqlab.simple.model.BtmwApi;
 
public class BtmwApplication extends Application {
    private SecureSaveData mSecureSaveData = null;
    private SaveData mSaveData;
    private BtmwApi mApi;

    @Override
    public void onCreate() {
		super.onCreate();

        mSecureSaveData = new SecureSaveData();
        mSaveData = new SaveData(this);
        mApi = new BtmwApi(mSecureSaveData, mSaveData);
    }
 
    @Override
    public void onTerminate() {
		super.onTerminate();
    }

    public SecureSaveData getSecureSaveData()
    {
        return mSecureSaveData;
    }

    public SaveData getSaveData()
    {
        return mSaveData;
    }

    public BtmwApi getApi()
    {
        return mApi;
    }
}
