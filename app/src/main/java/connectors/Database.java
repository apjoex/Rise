package connectors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import models.Appliance;
import models.Home;

/**
 * Created by AKINDE-PETERS on 3/16/2016.
 */
public class Database extends SQLiteOpenHelper {

    static String DATABASE_NAME = "local_db";
    static int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory)null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE appliances (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,count TEXT,wattage TEXT,duration TEXT);");
        db.execSQL("CREATE TABLE customhomes (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,appliances TEXT,location TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void saveAppliance(ArrayList<Appliance> appliances){
        for(int g = 0;g< appliances.size();g++){
            String query = " SELECT * FROM appliances WHERE name = '"+appliances.get(g).getName()+"'";
            Cursor cursor = getReadableDatabase().rawQuery(query,null);

            ContentValues cv = new ContentValues();
            cv.put("name", appliances.get(g).getName());
            cv.put("count", appliances.get(g).getCount());
            cv.put("wattage", appliances.get(g).getWattage());
            cv.put("duration", appliances.get(g).getDuration());
            if(cursor.getCount() + 0 == 0){
                getWritableDatabase().insert("appliances","name", cv);
            }else{
                String[] args={appliances.get(g).getName()};
                getWritableDatabase().update("appliances",cv,"name=?", args);
            }
            cursor.close();
        }
    }

    public ArrayList <Appliance> getAppliances(){
        ArrayList <Appliance> appliance = new ArrayList<Appliance>();
        String query = " SELECT * FROM appliances";
        Cursor cursor = getReadableDatabase().rawQuery(query,null);
        while(cursor.moveToNext()){
            appliance.add(new Appliance(
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("count")),
                    cursor.getString(cursor.getColumnIndex("wattage")),
                    cursor.getString(cursor.getColumnIndex("duration"))
                    ));
        }
        close();
        return appliance;
    }

    public void saveCustomHome(ArrayList <Home> customhomes){
        for(int g = 0;g< customhomes.size();g++){
            String query = " SELECT * FROM customhomes WHERE name = '"+customhomes.get(g).getName()+"'";
            Cursor cursor = getReadableDatabase().rawQuery(query,null);

            ContentValues cv = new ContentValues();
            cv.put("name", customhomes.get(g).getName());
            cv.put("appliances", customhomes.get(g).getAppliances());
            cv.put("location", customhomes.get(g).getLocation());
            if(cursor.getCount() + 0 == 0){
                getWritableDatabase().insert("customhomes","name", cv);
            }else{
                String[] args={customhomes.get(g).getName()};
                getWritableDatabase().update("customhomes",cv,"name=?", args);
            }
            cursor.close();
        }
    }

    public ArrayList <Home> getCustomhomes(){
        ArrayList <Home> customhome = new ArrayList<Home>();
        String query = " SELECT * FROM customhomes";
        Cursor cursor = getReadableDatabase().rawQuery(query,null);
        while(cursor.moveToNext()){
            customhome.add(new Home(
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("appliances")),
                    cursor.getString(cursor.getColumnIndex("location"))
                    ));
        }
        close();
        return customhome;
    }


    public void deleteHome(String name) {
        String query = "DELETE FROM customhomes WHERE name ='"+name+"'";
        getWritableDatabase().execSQL(query);
    }
}
