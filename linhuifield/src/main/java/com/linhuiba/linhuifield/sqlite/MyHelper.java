package com.linhuiba.linhuifield.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/7/24.
 */

public class MyHelper extends SQLiteOpenHelper {
    private static MyHelper mInstance;
    public MyHelper(Context context) {
        super(context, "config.db", null, 1);
    }
    //获取单例
    public synchronized static MyHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyHelper(context.getApplicationContext());
        }
        return mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cities(id INTEGER PRIMARY KEY AUTOINCREMENT,city_id integer,city nvarchar(255),code integer,province_id integer, province nvarchar(255),latitude varchar(100),longitude varchar(100),service_phone varchar(100),default_city integer)");
        db.execSQL("create table districts(district_id integer primary key,name nvarchar(255),code integer,city_id integer)");
        db.execSQL("create table labels(label_id INTEGER PRIMARY KEY AUTOINCREMENT,id integer,name nvarchar(255),display_name nvarchar(255),city_id integer)");
        db.execSQL("create table trading_areas(id integer primary key,name nvarchar(255),lat varchar(100),lng varchar(100),city_id integer)");
        db.execSQL("create table subway_stations(id integer primary key,name nvarchar(255),city_id integer)");
        db.execSQL("create table stations(id integer primary key,subway_line_id integer,station_name nvarchar(255),detail_address nvarchar(255),lng varchar(100),lat varchar(100),seq integer,city_id integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cities");
        db.execSQL("DROP TABLE IF EXISTS districts");
        db.execSQL("DROP TABLE IF EXISTS labels");
        db.execSQL("DROP TABLE IF EXISTS trading_areas");
        db.execSQL("DROP TABLE IF EXISTS subway_stations");
        db.execSQL("DROP TABLE IF EXISTS stations");
        onCreate(db);
    }
}
