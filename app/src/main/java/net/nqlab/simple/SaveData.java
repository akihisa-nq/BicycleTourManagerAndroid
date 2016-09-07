package net.nqlab.simple;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import android.database.Cursor;

public class SaveData extends SQLiteOpenHelper {
    private static final String DB = "sqlite_sample.db";
    private static final int DB_VERSION_INITIAL = 1;
    private static final int DB_VERSION_ADD_TOUR_PLAN = 2;

    public SaveData(Context c) {
        super(c, DB, null, DB_VERSION_ADD_TOUR_PLAN);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
			"create table tokens ("
			+ "_id integer primary key autoincrement, "
			+ "key text not null, "
			+ "data text not null );"
			);
        db.execSQL(
			"create table tour_plans ("
			+ "_id integer primary key, "
			+ "name text not null, "
			+ "json text not null );"
			);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			// up
			if (oldVersion < DB_VERSION_ADD_TOUR_PLAN && DB_VERSION_ADD_TOUR_PLAN <= newVersion) {
				db.execSQL(
					"create table tour_plans ("
					+ "_id integer primary key, "
					+ "name text not null, "
					+ "json text not null );"
					);
			}

		} else if (oldVersion > newVersion) {
			// down
		}
    }

    public void saveToken(String key, String value)
    {
        Cursor cur = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("delete from tokens where  key = '" + key + "';");
            db.execSQL("insert into tokens(key, data) values  ('" + key + "', '" + value + "');");
        } catch (Exception e) {

        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }

	public String loadToken(String key)
	{
        String ret = null;
        Cursor cur = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            cur = db.query(
                    "tokens",                   // from
                    new String[]{"data"},    // select
                    "key = '" + key + "'",      // where
                    null,                       // group by
                    null,                       // having
                    null,                       // order by
                    null                        // limit
            );
            if (cur.moveToFirst()) {
                ret = cur.getString(0);
            }
        } catch (Exception e) {

        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return ret;
	}

    public void saveTourPlan(int id, String name, String json)
    {
        Cursor cur = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("delete from tour_plans where  id = '" + id + "';");

			SQLiteStatement stmt = db.compileStatement("INSERT INTO cat(id, name, json) VALUES(?, ?, ?)");
			stmt.bindLong(1, id);
			stmt.bindString(2, name);
			stmt.bindString(3, json);
			stmt.executeInsert();

        } catch (Exception e) {

        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }

	public String loadTourPlan(int id)
	{
        String ret = null;
        Cursor cur = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            cur = db.query(
                    "tour_plans",               // from
                    new String[]{"json"},       // select
                    "id = '" + id + "'",        // where
                    null,                       // group by
                    null,                       // having
                    null,                       // order by
                    null                        // limit
            );
            if (cur.moveToFirst()) {
                ret = cur.getString(0);
            }
        } catch (Exception e) {

        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return ret;
	}
}

