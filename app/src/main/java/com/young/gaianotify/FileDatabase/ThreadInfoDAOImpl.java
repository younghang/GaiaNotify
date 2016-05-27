package com.young.gaianotify.FileDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.young.gaianotify.FileDeal.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 2016/5/8 0008.
 */
public class ThreadInfoDAOImpl implements ThreadDBDAO {
    DBHelper mHelper = null;

    public ThreadInfoDAOImpl(Context context) {
        mHelper = new DBHelper(context);
    }

    @Override
    public void insertThreadInfo(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(
                "insert into thread_info(thread_id,url,start,end,progress) values(?,?,?,?,?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(), threadInfo.getStart(),
                        threadInfo.getEnd(), threadInfo.getProgress()}
        );
        db.close();


    }

    @Override
    public void deleteThreadInfo(String url, int threadId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(
                "delete from thread_info where url = ? and thread_id = ?",
                new Object[]{url, threadId}
        );
        db.close();

    }

    @Override
    public void updateThreadInfo(String url, int thread_id, int progerss) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(
                "update thread_info set progress = ? where url = ? and thread_id = ?",
                new Object[]{progerss, url, thread_id}
        );
        db.close();

    }

    @Override
    public List<ThreadInfo> getThreads(String url) {
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor =
                db.rawQuery("select * from thread_info where url = ?", new String[]{url});
        while (cursor.moveToNext()) {
            ThreadInfo thread = new ThreadInfo();
            thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            thread.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            list.add(thread);

        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExist(String url, int thread_id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor =
                db.rawQuery("select * from thread_info where url = ? and thread_id = ?",
                        new String[]{url, thread_id + ""});
        boolean exist = cursor.moveToNext();
        cursor.close();
        db.close();
        return exist;

    }
}
