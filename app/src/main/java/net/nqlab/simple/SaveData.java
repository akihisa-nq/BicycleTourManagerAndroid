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
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(
            "tokens",                   // from
            new String[] { "data" },    // select
            "key = '" + key + "'",      // where
            null,                       // group by
            null,                       // having
            null,                       // order by
            null                        // limit
            );
        if (cur.moveToFirst()) {
            db.execSQL("update tokens set value = '" + value + "' where key = '" + key + "';");
        } else {
            db.execSQL("insert into tokens(key, data) values  ('" + key + "', '" + value + "');");
        }
    }

	public String load(String key)
	{
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.query(
            "tokens",                   // from
            new String[] { "data" },    // select
            "key = '" + key + "'",      // where
            null,                       // group by
            null,                       // having
            null,                       // order by
            null                        // limit
            );
        if (cur.moveToFirst()) {
			return cur.getString(0);
		} else {
			return null;
		}
	}
}

