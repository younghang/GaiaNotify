package com.young.gaianotify.SaveDatabaseSQL;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by young on 2016/3/22 0022.
 */
 class GaiaDataSQL extends SQLiteOpenHelper {
    public GaiaDataSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    private static final int VERSION = 1;
    private static final String DB_NAME = "gaia.db";
    public GaiaDataSQL(Context context, String name, SQLiteDatabase.CursorFactory factory ,int versin) {
        super(context, name, factory, versin);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table gaiatime(_id int INTEGER PRIMARY KEY,title text,time text,image text)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    GaiaDataSQL(Context context){

        //当调用getWritableDatabase()
        //或 getReadableDatabase()方法时
        //则创建一个数据库
        super(context, DB_NAME, null, VERSION);
    }
}
