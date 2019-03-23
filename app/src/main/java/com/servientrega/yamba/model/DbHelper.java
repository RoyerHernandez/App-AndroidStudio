package com.servientrega.yamba.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context){
        super(context,StatusContact.DB_NAME,null,StatusContact.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String
                    .format("create table %s (%s int primary key, %s text, %s text, %s int)",
                        StatusContact.TABLE,
                        StatusContact.Column.ID,
                        StatusContact.Column.USER,
                        StatusContact.Column.MESSAGE,
                        StatusContact.Column.CREATED_AT);
        Log.d(TAG,"onCreate with SQL " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + StatusContact.TABLE);
        onCreate(db);
        Log.d(TAG,"onUpgrade");
    }
}
