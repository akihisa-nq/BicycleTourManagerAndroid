package net.nqlab.simple;
 
import android.app.Application;

import net.nqlab.simple.SecureSaveData;
import net.nqlab.simple.SaveData;
import net.nqlab.simple.BtmwApi;
 
public class BtmwApplication extends Application {
    private SecureSaveData mSecureSaveData = null;
    private SaveData mSaveData;
    private BtmwApi mApi;

    @Override
    public void onCreate() {
        mSecureSaveData = new SecureSaveData();
        mSaveData = new SaveData(this);
        mApi = new BtmwApi(mSecureSaveData, mSaveData);
    }
 
    @Override
    public void onTerminate() {
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
