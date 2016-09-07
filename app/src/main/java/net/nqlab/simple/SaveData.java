package net.nqlab.simple;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.Cursor;

public class SaveData extends SQLiteOpenHelper {
    private static final String DB = "sqlite_sample.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "create table tokens ( _id integer primary key autoincrement, key text not null, data text not null );";
    private static final String DROP_TABLE = "drop table tokens;";

    public SaveData(Context c) {
        super(c, DB, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void save(String key, String value)
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

	public String load(String key)
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
}

