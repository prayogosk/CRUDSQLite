package com.hacktiv8.sesi11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    //version db
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "CountryData";

    //Country Table Name
    private static final String TABLE_COUNTRY = "Country";

    //Country Table Columns Names
    private static final String KEY_ID = "id";
    private static final String COUNTRY_NAME = "country_name";
    private static final String POPULATION = "population";

    public SQLiteDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String tableCountrySql = " CREATE TABLE "+TABLE_COUNTRY+" ( "+
                                 KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                 COUNTRY_NAME + " TEXT, "+
                                 POPULATION + " LONG ) ";

        db.execSQL(tableCountrySql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+TABLE_COUNTRY);
        onCreate(db);
    }

    //Method Tambah data Country
    public void addCountry(Country country){
        SQLiteDatabase db  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, country.getCountryName());
        values.put(POPULATION, country.getPopulation());

        db.insert(TABLE_COUNTRY, null, values);
        db.close();

    }

    //Method Get Country
    public Country getCountry(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String querySql = "  ";
        Cursor cursor = db.query(TABLE_COUNTRY,
                                 new String[] {KEY_ID, COUNTRY_NAME, POPULATION},
                                KEY_ID +" =? ",
                                 new String[] { String.valueOf(id)},
                                 null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        Country country = new Country(Integer.parseInt(cursor.getString(0)),
                                      cursor.getString(1),
                                      cursor.getLong(2));
        return country;

    }

    //get All Data Country
    public List<Country> getAllCountries(){

        List<Country> countryList = new ArrayList<>();
        String querySelectAll = " SELECT * FROM "+TABLE_COUNTRY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelectAll, null);

        if(cursor.moveToFirst()){
            do{

                Country country = new Country();
                country.setId(Integer.parseInt(cursor.getString(0)));
                country.setCountryName(cursor.getString(1));
                country.setPopulation(cursor.getLong(2));

                countryList.add(country);

            }
            while (cursor.moveToNext());
        }

        return  countryList;
    }

    public void deleteCountry(Country country){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY, KEY_ID + " =? ", new String[] { String.valueOf(country.getId())});
        db.close();

    }

    public void updateCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, country.getCountryName());
        values.put(POPULATION, country.getPopulation());

        // updating row
        db.update(TABLE_COUNTRY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(country.getId()) });
        db.close();
    }

}
