package net.nqlab.simple.model;

import net.nqlab.btmw.TourPlan;
import net.nqlab.btmw.TourPlanSchedule;

import java.util.ArrayList;
import java.util.List;

public class TourPlanScheduleStore {
    private SaveData mSaveData;
    private SecureSaveData mSecureSaveData;
    private BtmwApi mApi;

    public  TourPlanScheduleStore(SaveData saveData, SecureSaveData secureSaveData, BtmwApi api)
    {
        mSaveData = saveData;
        mSecureSaveData = secureSaveData;
        mApi = api;
    }

    private byte[] getInitialVector() {
        String data = mSaveData.loadToken("tour_plan_iv");
        if (data != null) {
            return mSecureSaveData.fromStringToByteArray(data);

        } else {
            byte[] iv = mSecureSaveData.generateInitialVector();
            mSaveData.saveToken("tour_plan_iv", mSecureSaveData.fromByteArrayToString(iv));
            return iv;

        }
    }

    private byte[] getPassword() {
        String data = mSaveData.loadToken("tour_plan_password");
        if (data != null) {
            return mSecureSaveData.fromStringToByteArray(data);

        } else {
            byte[] digest = mSecureSaveData.generatePassword("tour_plan_password");
            mSaveData.saveToken("tour_plan_password", mSecureSaveData.fromByteArrayToString(digest));
            return digest;

        }
    }

    public void store(TourPlanSchedule schedule)
    {
        String strData = mApi.toJson(schedule);
        byte[] iv = getInitialVector();
        byte[] password = getPassword();
        String strSaveData = mSecureSaveData.encryptString(password, iv, strData);
        mSaveData.saveTourPlanSchedle(
                schedule.getId(),
                schedule.getName(),
                strSaveData
            );
    }

    public TourPlanSchedule load(int id)
    {
        String entryptedJson = mSaveData.loadTourPlanSchedle(id);
        byte[] iv = getInitialVector();
        byte[] password = getPassword();
        String json = mSecureSaveData.decryptString(password, iv, entryptedJson);
        return (TourPlanSchedule) mApi.fromJson(json, TourPlanSchedule.class);
    }

    public List<TourPlanSchedule> getList()
    {
        final List<TourPlanSchedule> schedules = new ArrayList<TourPlanSchedule>();

        mSaveData.listTourPlanSchedle(new SaveData.TourPlanScheduleVisitor() {
            @Override
            public void visit(int id, String name) {
                TourPlanSchedule schedule = new TourPlanSchedule();
                schedule.setId(id);
                schedule.setName(name);
                schedules.add(schedule);
            }
        });

        return schedules;
    }
}