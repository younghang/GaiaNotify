package com.young.gaianotify.SaveDatabaseSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by young on 2016/3/22 0022.
 */
public class GaiaDataManage {

    private static final String DB_TABLE ="gaiatime" ;
    private static final String KEY_TITLE ="title" ;
    private static final String KEY_TIME ="time" ;
    private static final String KEY_IMAGE ="image" ;
    private static final String KEY_ID = "_id";
    // 执行open（）打开数据库时，保存返回的数据库对象
    private SQLiteDatabase mSQLiteDatabase = null;

    // 本地Context对象
    private Context mContext = null;
    // 由SQLiteOpenHelper继承过来
    private GaiaDataSQL mDatabaseHelper = null;
    public GaiaDataManage(Context context){

        mContext = context;
    }
    // 打开数据库，返回数据库对象
    public void open() throws SQLException {

        mDatabaseHelper = new GaiaDataSQL(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }


    // 关闭数据库
    public void close(){

        mDatabaseHelper.close();
    }
    public long insertData(  String title,String time,String image){

        ContentValues initialValues = new ContentValues();

        initialValues.put("title", title);
        initialValues.put("time", time);
        initialValues.put("image", image);

        return mSQLiteDatabase.insert("gaiatime", "_id", initialValues);
    }


    public boolean deleteData(long rowId){

        return mSQLiteDatabase.delete("gaiatime", "_id" + "=" + rowId, null) > 0;
    }


    public Cursor fetchAllData(){

        return mSQLiteDatabase.query("gaiatime", new String[] { "_id", "title", "time","image" }, null, null, null, null, null);
    }


    public Cursor fetchData(long rowId) throws SQLException{

        Cursor mCursor = mSQLiteDatabase.query(true, "gaiatime", new String[] { KEY_ID, KEY_TITLE, KEY_TIME,KEY_IMAGE }, KEY_ID + "=" + rowId, null, null, null, null, null);

        if (mCursor != null){

            mCursor.moveToFirst();
        }
        return mCursor;

    }


    public boolean updateData(long rowId,  String title,String time,String image){

        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_TIME, time);
        args.put(KEY_IMAGE, image);

        return mSQLiteDatabase.update("gaiatime", args, KEY_ID + "=" + rowId, null) > 0;
    }
}
