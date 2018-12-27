package com.linhuiba.linhuifield.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/24.
 */

public class DBManager {
    private static MyHelper mHelper;
    private static SQLiteDatabase db;
    private static  int mCount;
    private static DBManager sDBManager;
    public DBManager(Context context) {
        mHelper = MyHelper.getInstance(context);
    }
    //单例
    public static synchronized DBManager getIntance(Context context){
        if(sDBManager == null){
            return new DBManager(context);
        }
        return sDBManager;
    }
    public synchronized SQLiteDatabase openDb(){
        if(mCount==0){
            db = mHelper.getWritableDatabase();
        }
        mCount++;
        return db;
    }
    public synchronized void closeDb(SQLiteDatabase database) {
        mCount--;
        if (mCount == 0) {
            database.close();
        }
    }
}
