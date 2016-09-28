package net.nqlab.btmw.handheld.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.github.gfx.android.orma.AccessThreadConstraint;

public class SaveData {
    private OrmaDatabase mOrma;

    public SaveData(Context context) {
        mOrma = OrmaDatabase.builder(context)
            .readOnMainThread(AccessThreadConstraint.WARNING) // optional
            .writeOnMainThread(AccessThreadConstraint.FATAL) // optional
            .build();
    }

    public void saveToken(String key, String value)
    {
        try {
            mOrma.deleteFromToken()
                .keyEq(key)
                .execute();

            Token token = new Token();
            token.key = key;
            token.data = value;
            mOrma.insertIntoToken(token);

        } catch (Exception e) {
            Log.e("SaveData", e.getMessage());
        }
    }

    public String loadToken(String key) {
        String ret = null;
        Cursor cur = null;
        try {
            cur = mOrma.selectFromToken()
                .keyEq(key)
                .executeWithColumns("data");
            if (cur.moveToFirst()) {
                ret = cur.getString(0);
            }

        } catch (Exception e) {
            Log.e("SaveData", e.getMessage());

       } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return ret;
    }

    public void saveTourPlanSchedle(int id, String name, String json) {
        try {
            mOrma.deleteFromTourPlan()
                ._idEq(id)
                .execute();

            TourPlan plan = new TourPlan();
            plan._id = id;
            plan.name = name;
            plan.json = json;
            mOrma.insertIntoTourPlan(plan);

        } catch (Exception e) {
            Log.e("SaveData", e.getMessage());

        }
    }

    public String loadTourPlanSchedle(int id) {
        String ret = null;
        Cursor cur = null;
        try {
            cur = mOrma.selectFromTourPlan()
                ._idEq(id)
                .executeWithColumns("json");
            if (cur.moveToFirst()) {
                ret = cur.getString(0);
            }

        } catch (Exception e) {
            Log.e("SaveData", e.getMessage());

        }

        return ret;
    }

    public interface TourPlanScheduleVisitor {
        void visit(int id, String name);
    }

    public void listTourPlanSchedle(TourPlanScheduleVisitor visitor) {
        Cursor cur = null;
        try {
            cur = mOrma.selectFromTourPlan()
                .executeWithColumns("_id", "name");
            if (cur.moveToFirst()) {
                do {
                    int id = cur.getInt(0);
                    String name = cur.getString(1);
                    visitor.visit(id, name);
                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            Log.e("SaveData", e.getMessage());

        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }
}

