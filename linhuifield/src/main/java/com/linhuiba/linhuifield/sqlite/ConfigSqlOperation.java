package com.linhuiba.linhuifield.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConfigSqlOperation {
    public static synchronized void addCityParameter(List<ConfigCityParameterModel> persons, int type, int city_id, boolean delete, Context context){
        DBManager manager = DBManager.getIntance(context);
        SQLiteDatabase db = manager.openDb();
        db.beginTransaction();
        try {
            if (type == 1) {
                if (delete) {
                    db.execSQL("delete from districts");
                }
                for (ConfigCityParameterModel p:persons){
                    ContentValues values = new ContentValues();
                    values.put("district_id", p.getDistrict_id());
                    values.put("name", p.getName());
                    values.put("code", p.getCode());
                    if (city_id == 0) {
                        values.put("city_id", p.getCity_id());
                    } else {
                        values.put("city_id", city_id);
                    }
                    db.insert("districts", null, values);
                }
            } else if (type == 2) {
                if (delete) {
                    db.execSQL("delete from labels");
                }
                for (ConfigCityParameterModel p:persons){
                    ContentValues values = new ContentValues();
                    values.put("id", p.getId());
                    values.put("name", p.getName());
                    values.put("display_name", p.getDisplay_name());
                    if (city_id == 0) {
                        values.put("city_id", p.getCity_id());
                    } else {
                        values.put("city_id", city_id);
                    }
                    db.insert("labels", null, values);
                }
            } else if (type == 3) {
                if (delete) {
                    db.execSQL("delete from trading_areas");
                }
                for (ConfigCityParameterModel p:persons){
                    ContentValues values = new ContentValues();
                    values.put("id", p.getId());
                    values.put("name", p.getName());
                    values.put("lat", p.getLat());
                    values.put("lng", p.getLng());
                    if (city_id == 0) {
                        values.put("city_id", p.getCity_id());
                    } else {
                        values.put("city_id", city_id);
                    }
                    db.insert("trading_areas", null, values);
                }
            } else if (type == 4) {
                if (delete) {
                    db.execSQL("delete from subway_stations");
                }
                for (ConfigCityParameterModel p:persons){
                    ContentValues values = new ContentValues();
                    values.put("id", p.getId());
                    values.put("name", p.getName());
                    if (city_id == 0) {
                        values.put("city_id", p.getCity_id());
                    } else {
                        values.put("city_id", city_id);
                    }
                    db.insert("subway_stations", null, values);
                }
            } else if (type == 5) {
                if (delete) {
                    db.execSQL("delete from stations");
                }
                for (ConfigCityParameterModel p:persons){
                    ContentValues values = new ContentValues();
                    values.put("id", p.getId());
                    values.put("subway_line_id", p.getSubway_line_id());
                    values.put("station_name", p.getStation_name());
                    values.put("detail_address", p.getDetail_address());
                    values.put("seq", p.getSeq());
                    values.put("city_id", p.getCity_id());
                    db.insert("stations", null, values);
                }
            } else if (type == 6) {
                if (delete) {
                    db.execSQL("delete from cities");
                }
                for (ConfigCityParameterModel p:persons){
                    ContentValues values = new ContentValues();
                    values.put("code", p.getCode());
                    values.put("city", p.getCity());
                    values.put("province", p.getProvince());
                    values.put("city_id", p.getCity_id());
                    values.put("province_id", p.getProvince_id());
                    values.put("latitude", p.getLatitude());
                    values.put("longitude", p.getLongitude());
                    values.put("service_phone", p.getService_phone());
                    values.put("default_city", p.getDefault_city());
                    db.insert("cities", null, values);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            manager.closeDb(db);
        }
    }
    // 查询
    public static List<ConfigCityParameterModel> selectSQL(int type, int city_id, Context context) {
        DBManager manager = DBManager.getIntance(context);
        SQLiteDatabase db = manager.openDb();
        List<ConfigCityParameterModel> list = new ArrayList<ConfigCityParameterModel>();
        // 获取一个光标对象
        Cursor cursor = null;
        try {
            if (type == 1) {
                String[] columns = new String[]{"name", "district_id","city_id"};
                cursor = db.query("districts", columns, "city_id="+String.valueOf(city_id), null, null, null, null);
                if (cursor != null) {
                    ConfigCityParameterModel configCityParameterModel = null;
                    while (cursor.moveToNext()) {
                        configCityParameterModel = new ConfigCityParameterModel();
                        configCityParameterModel.setName(cursor.getString(cursor
                                .getColumnIndex("name")));
                        configCityParameterModel.setDistrict_id(cursor.getInt(cursor
                                .getColumnIndex("district_id")));
                        list.add(configCityParameterModel);
                    }
                }
            } else if (type == 2) {
                String[] columns = new String[]{"display_name", "id","city_id"};
                cursor = db.query("labels", columns, "city_id="+String.valueOf(city_id), null, null, null, null);
                if (cursor != null) {
                    ConfigCityParameterModel configCityParameterModel = null;
                    while (cursor.moveToNext()) {
                        configCityParameterModel = new ConfigCityParameterModel();
                        configCityParameterModel.setDisplay_name(cursor.getString(cursor
                                .getColumnIndex("display_name")));
                        configCityParameterModel.setId(cursor.getInt(cursor
                                .getColumnIndex("id")));
                        list.add(configCityParameterModel);
                    }
                }
            } else if (type == 3) {
                String[] columns = new String[]{"name", "id","city_id"};
                cursor = db.query("trading_areas", columns, "city_id="+String.valueOf(city_id), null, null, null, null);
                if (cursor != null) {
                    ConfigCityParameterModel configCityParameterModel = null;
                    while (cursor.moveToNext()) {
                        configCityParameterModel = new ConfigCityParameterModel();
                        configCityParameterModel.setName(cursor.getString(cursor
                                .getColumnIndex("name")));
                        configCityParameterModel.setId(cursor.getInt(cursor
                                .getColumnIndex("id")));
                        list.add(configCityParameterModel);
                    }
                }
            } else if (type == 4) {
                String[] columns = new String[]{"name", "id","city_id"};
                cursor = db.query("subway_stations", columns, "city_id="+String.valueOf(city_id), null, null, null, null);
                if (cursor != null) {
                    ConfigCityParameterModel configCityParameterModel = null;
                    while (cursor.moveToNext()) {
                        configCityParameterModel = new ConfigCityParameterModel();
                        configCityParameterModel.setName(cursor.getString(cursor
                                .getColumnIndex("name")));
                        configCityParameterModel.setId(cursor.getInt(cursor
                                .getColumnIndex("id")));
                        list.add(configCityParameterModel);
                    }
                }
            } else if (type == 5) {
                String[] columns = new String[]{"station_name", "id", "subway_line_id",
                        "detail_address", "lng", "lat", "seq", "city_id"};
                cursor = db.query("stations", columns, "subway_line_id="+String.valueOf(city_id), null, null, null, null);
                if (cursor != null) {
                    ConfigCityParameterModel configCityParameterModel = null;
                    while (cursor.moveToNext()) {
                        configCityParameterModel = new ConfigCityParameterModel();
                        configCityParameterModel.setStation_name(cursor.getString(cursor
                                .getColumnIndex("station_name")));
                        configCityParameterModel.setId(cursor.getInt(cursor
                                .getColumnIndex("id")));
                        configCityParameterModel.setSubway_line_id(cursor.getInt(cursor
                                .getColumnIndex("subway_line_id")));
                        configCityParameterModel.setDetail_address(cursor.getString(cursor
                                .getColumnIndex("detail_address")));
                        configCityParameterModel.setLng(cursor.getDouble(cursor
                                .getColumnIndex("lng")));
                        configCityParameterModel.setLat(cursor.getDouble(cursor
                                .getColumnIndex("lat")));
                        configCityParameterModel.setSeq(cursor.getInt(cursor
                                .getColumnIndex("seq")));
                        configCityParameterModel.setCity_id(cursor.getInt(cursor
                                .getColumnIndex("city_id")));
                        list.add(configCityParameterModel);
                    }
                }
            } else if (type == 6) {
                String[] columns = new String[]{"city", "city_id", "province",
                        "province_id", "latitude", "longitude", "service_phone", "code", "default_city"};
                cursor = db.query("cities", null, null, null, null, null, null);
                if (cursor != null) {
                    ConfigCityParameterModel configCityParameterModel = null;
                    while (cursor.moveToNext()) {
                        configCityParameterModel = new ConfigCityParameterModel();
                        configCityParameterModel.setCode(cursor.getInt(cursor
                                .getColumnIndex("code")));
                        configCityParameterModel.setCity(cursor.getString(cursor
                                .getColumnIndex("city")));
                        configCityParameterModel.setCity_id(cursor.getInt(cursor
                                .getColumnIndex("city_id")));
                        configCityParameterModel.setProvince(cursor.getString(cursor
                                .getColumnIndex("province")));
                        configCityParameterModel.setProvince_id(cursor.getInt(cursor
                                .getColumnIndex("province_id")));
                        configCityParameterModel.setLatitude(cursor.getDouble(cursor
                                .getColumnIndex("latitude")));
                        configCityParameterModel.setLongitude(cursor.getDouble(cursor
                                .getColumnIndex("longitude")));
                        configCityParameterModel.setService_phone(cursor.getString(cursor
                                .getColumnIndex("service_phone")));
                        configCityParameterModel.setDefault_city(cursor.getInt(cursor
                                .getColumnIndex("default_city")));
                        list.add(configCityParameterModel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            manager.closeDb(db);
        }
        return list;
    }
    // 查询
        public static List<ConfigCitiesModel> selectCitySQL(Context context) {
            DBManager manager = DBManager.getIntance(context);
        SQLiteDatabase db = manager.openDb();
        List<ConfigCitiesModel> list = new ArrayList<ConfigCitiesModel>();
        // 获取一个光标对象
        Cursor cursor = null;
        try {
            String[] columns = new String[]{"city", "city_id", "province",
                    "province_id", "latitude", "longitude", "service_phone", "code", "default_city"};
            cursor = db.query("cities", null, null, null, null, null, null);
            if (cursor != null) {
                ConfigCitiesModel configCityParameterModel = null;
                while (cursor.moveToNext()) {
                    configCityParameterModel = new ConfigCitiesModel();
                    configCityParameterModel.setCode(cursor.getInt(cursor
                            .getColumnIndex("code")));
                    configCityParameterModel.setCity(cursor.getString(cursor
                            .getColumnIndex("city")));
                    configCityParameterModel.setCity_id(cursor.getInt(cursor
                            .getColumnIndex("city_id")));
                    configCityParameterModel.setProvince(cursor.getString(cursor
                            .getColumnIndex("province")));
                    configCityParameterModel.setProvince_id(cursor.getInt(cursor
                            .getColumnIndex("province_id")));
                    configCityParameterModel.setLatitude(cursor.getDouble(cursor
                            .getColumnIndex("latitude")));
                    configCityParameterModel.setLongitude(cursor.getDouble(cursor
                            .getColumnIndex("longitude")));
                    configCityParameterModel.setService_phone(cursor.getString(cursor
                            .getColumnIndex("service_phone")));
                    configCityParameterModel.setDefault_city(cursor.getInt(cursor
                            .getColumnIndex("default_city")));
                    list.add(configCityParameterModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            manager.closeDb(db);
        }
        return list;
    }
}
