package com.example.naeemasghar.clickandhire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Naeem Asghar on 2/8/2016.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "clickandhire";

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        createUserTable(db);
        createCityTable(db);
        createCategoryTable(db);
        createSubCategoryTable(db);
        createProfessionalsTable(db);
        createTasksTable(db);
        createBidTable(db);
        createNewsTable(db);

    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_City);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSIONALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);

        // Create tables again
        onCreate(db);
    }
    /******************************************************************************************
     Create Database for User
     ******************************************************************************************/
    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_QUALIFICATION = "qualification";
    private static final String KEY_FEES = "fees";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_EXPERIENCE = "experience";
    private static final String KEY_SPECIALITY = "speciality";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_COVERIMAGE = "coverImage";
    private static final String KEY_PROFILEIMAGE = "profileImage";
    private static final String KEY_APIKEY = "apiKey";
    private static final String KEY_STATUS = "status";
    private static final String KEY_AVG_RATING = "avgRating";
    private static final String KEY_TOTAL_RATINGS = "totalRatings";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void createUserTable(SQLiteDatabase db)
    {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_GENDER + " TEXT,"
                + KEY_QUALIFICATION + " TEXT,"
                + KEY_FEES + " TEXT ,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_EXPERIENCE + " TEXT,"
                + KEY_SPECIALITY + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_COVERIMAGE + " TEXT,"
                + KEY_PROFILEIMAGE + " TEXT,"
                + KEY_APIKEY + " TEXT,"
                + KEY_AVG_RATING + " TEXT,"
                + KEY_TOTAL_RATINGS + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database User tables created");
    }


    /**
     * Storing user details in database
     * */
    public void addUser(String uid,String name,String phone,String email,String gender,String qualification,String fees,
                        String description,String experience,String speciality,String city, String country, String coverImage,
                        String profileImage,String apiKey,String avgRating, String totalRating, String status, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid);
        values.put(KEY_NAME, name);
        values.put(KEY_PHONE, phone);
        values.put(KEY_EMAIL, email);
        values.put(KEY_GENDER, gender);
        values.put(KEY_QUALIFICATION, qualification);
        values.put(KEY_FEES, fees);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_EXPERIENCE, experience);
        values.put(KEY_SPECIALITY, speciality);
        values.put(KEY_CITY, city);
        values.put(KEY_COUNTRY, country);
        values.put(KEY_COVERIMAGE, coverImage);
        values.put(KEY_PROFILEIMAGE, profileImage);
        values.put(KEY_APIKEY, apiKey);
        values.put(KEY_AVG_RATING, avgRating);
        values.put(KEY_TOTAL_RATINGS, totalRating);
        values.put(KEY_STATUS, status);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    /**
     * Storing New Register user details in database
     * */
    public void addNewUser(String uid,String name,String phone,String apiKey,String status, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid);
        values.put(KEY_NAME, name);
        values.put(KEY_PHONE, phone);
        values.put(KEY_APIKEY, apiKey);
        values.put(KEY_STATUS, status);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Register is user inserted into sqlite: " + id);
    }
    public void updateUser(String name,String email,String gender,String fees, String description,String experience,
                           String speciality,String city, String apiKey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_GENDER, gender);
        values.put(KEY_FEES, fees);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_EXPERIENCE, experience);
        values.put(KEY_SPECIALITY, speciality);
        values.put(KEY_CITY, city);

        // Inserting Row
        //long id = db.insert(TABLE_USER, null, values);
        long id =db.update(TABLE_USER, values, null, null);
        db.close(); // Closing database connection

        Log.d(TAG, "Update User in sqlite: " + id);
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("phone", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("gender", cursor.getString(5));
            user.put("qualification", cursor.getString(6));
            user.put("fees", cursor.getString(7));
            user.put("description", cursor.getString(8));
            user.put("experience", cursor.getString(9));
            user.put("speciality", cursor.getString(10));
            user.put("city", cursor.getString(11));
            user.put("country", cursor.getString(12));
            user.put("coverImage", cursor.getString(13));
            user.put("profileImage", cursor.getString(14));
            user.put("apiKey", cursor.getString(15));
            user.put("avgRating", cursor.getString(16));
            user.put("totalRatings", cursor.getString(17));
            user.put("status", cursor.getString(18));
            user.put("created_at", cursor.getString(19));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
     /******************************************************************************************
                    Create Database for City
     ******************************************************************************************/
     // Login table name
     private static final String TABLE_City = "city";

    // Login Table Columns names
    private static final String KEY_CID = "id";
    private static final String KEY_CITY_ID = "cid";
    private static final String KEY_CITY_NAME = "city";

    // Creating Tables

    public void createCityTable(SQLiteDatabase db) {
        String CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_City + "("
                + KEY_CID + " INTEGER PRIMARY KEY,"
                + KEY_CITY_ID + " TEXT,"
                + KEY_CITY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_CITY_TABLE);

        Log.d(TAG, "Database CIty tables created");
    }

    /**
     * Storing New Register user details in database
     * */
    public void addCity(String cid,String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CITY_ID, cid);
        values.put(KEY_CITY_NAME, city);
        // Inserting Row
        long id = db.insert(TABLE_City, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Cities List is inserted in SQLITE ");
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getCityList() {
        HashMap<String, String> city = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_City;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            city.put("id", cursor.getString(1));
            city.put("city", cursor.getString(2));
        }
        cursor.close();
        db.close();
        // return city
        Log.d(TAG, "Fetching City List from Sqlite: " + city.toString());

        return city;
    }
    /**
     * Getting user data from database BY city Id
     * */
    public String getCityById(String id) {
        HashMap<String, String> city = new HashMap<String, String>();
        String cityName="";
        String selectQuery = "SELECT  * FROM " + TABLE_City+" where cid="+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            city.put("id", cursor.getString(1));
            city.put("city", cursor.getString(2));
            cityName=cursor.getString(2);
        }
        cursor.close();
        db.close();
        // return city
        Log.d(TAG, "Fetching City List from Sqlite: " + city.toString());

        return cityName;
    }
    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllCities(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_City;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteCity() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_City, null, null);
        db.close();

        Log.d(TAG, "Deleted all Cities list from sqlite");
    }
    /******************************************************************************************
     Create Database for Category
     ******************************************************************************************/
    // Login table name
    private static final String TABLE_CATEGORY = "category";

    // Login Table Columns names
    private static final String KEY_CATID = "id";
    private static final String KEY_CAT_ID = "ctiId";
    private static final String KEY_CAT_NAME = "category";
    private static final String KEY_CAT_ICON = "icon";

    // Creating Tables

    public void createCategoryTable(SQLiteDatabase db) {
        String CREATE_CAT_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_CATID + " INTEGER PRIMARY KEY,"
                + KEY_CAT_ID + " TEXT,"
                + KEY_CAT_NAME + " TEXT,"
                + KEY_CAT_ICON + " TEXT"
                + ")";
        db.execSQL(CREATE_CAT_TABLE);

        Log.d(TAG, "Database Category tables is created");
    }

    /**
     * Storing New Register user details in database
     * */
    public void addCategory(String ctid,String cat,String icon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAT_ID, ctid);
        values.put(KEY_CAT_NAME, cat);
        values.put(KEY_CAT_ICON, icon);
        // Inserting Row
        long id = db.insert(TABLE_CATEGORY, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Category List is inserted in SQLITE ");
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getCategoryList() {
        HashMap<String, String> cat = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cat.put("id", cursor.getString(1));
            cat.put("category", cursor.getString(2));
            cat.put("icon", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return city
        Log.d(TAG, "Fetching Category List from Sqlite: " + cat.toString());

        return cat;
    }
    /**
     * Getting all labels
     * returns list of categories
     * */
    public List<String> getAllCategories(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CATEGORY, null, null);
        db.close();

        Log.d(TAG, "Deleted all Cities list from sqlite");
    }
    /******************************************************************************************
     Create Database for Sub Category
     ******************************************************************************************/
    // Login table name
    private static final String TABLE_SUB_CATEGORY = "subCategory";

    // Login Table Columns names
    private static final String KEY_SCID = "id";
    private static final String KEY_SSCAT_ID = "scid";
    private static final String KEY_SCAT_ID = "ctiId";
    private static final String KEY_SCAT_NAME = "category";

    // Creating Tables

    public void createSubCategoryTable(SQLiteDatabase db) {
        String CREATE_SUB_CAT_TABLE = "CREATE TABLE " + TABLE_SUB_CATEGORY + "("
                + KEY_SCID + " INTEGER PRIMARY KEY,"
                + KEY_SSCAT_ID + " TEXT,"
                + KEY_SCAT_ID + " TEXT,"
                + KEY_SCAT_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_SUB_CAT_TABLE);

        Log.d(TAG, "Database Sub Category tables is created");
    }

    /**
     * Storing New Register user details in database
     * */
    public void addSubCategory(String scid, String ctid,String cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SSCAT_ID, scid);
        values.put(KEY_SCAT_ID, ctid);
        values.put(KEY_SCAT_NAME, cat);
        // Inserting Row
        long id = db.insert(TABLE_SUB_CATEGORY, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Sub Category List is inserted in SQLITE ");
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getSCategoryList() {
        HashMap<String, String> cat = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_SUB_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cat.put("id", cursor.getString(1));
            cat.put("cid", cursor.getString(2));
            cat.put("category", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return city
        Log.d(TAG, "Fetching Category List from Sqlite: " + cat.toString());

        return cat;
    }
    /**
     * Getting all labels
     * returns list of categories
     * */
    public List<String> getAllSubCategories(String catId){
        List<String> labels = new ArrayList<String>();
        labels.add("All Categories");
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUB_CATEGORY+" where ctiId="+catId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public String getSubCategoryById(String id) {
        HashMap<String, String> cat = new HashMap<String, String>();
        String catName="";
        String selectQuery = "SELECT  * FROM " + TABLE_SUB_CATEGORY+" where ctiId="+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cat.put("id", cursor.getString(1));
            cat.put("cid", cursor.getString(2));
            cat.put("category", cursor.getString(3));
            catName=cursor.getString(3);
        }
        cursor.close();
        db.close();
        // return city
        Log.d(TAG, "Fetching City List from Sqlite: " + catName.toString());

        return catName;
    }

    public ArrayList fetchSubCategoryNames(String id)
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchData="select * from "+TABLE_SUB_CATEGORY+" where ctiId="+id;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchData, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteSubCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_SUB_CATEGORY, null, null);
        db.close();

        Log.d(TAG, "Deleted all Sub Category list from sqlite");
    }
    /******************************************************************************************
                             Create Database for Professionals
     ******************************************************************************************/
    private static final String TABLE_PROFESSIONALS = "professionals";

    public void createProfessionalsTable(SQLiteDatabase db)
    {
        String CREATE_PROFESSIONAL_TABLE = "CREATE TABLE " + TABLE_PROFESSIONALS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_GENDER + " TEXT,"
                + KEY_QUALIFICATION + " TEXT,"
                + KEY_FEES + " TEXT ,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_EXPERIENCE + " TEXT,"
                + KEY_SPECIALITY + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_COVERIMAGE + " TEXT,"
                + KEY_PROFILEIMAGE + " TEXT,"
                + KEY_APIKEY + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_PROFESSIONAL_TABLE);

        Log.d(TAG, "Database Professionals tables created");
    }


    /**
     * Storing user details in database
     * */
    public void addProfessional(String uid,String name,String phone,String email,String gender,String qualification,String fees,
                        String description,String experience,String speciality,String city,
                        String profileImage,String apiKey, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid);
        values.put(KEY_NAME, name);
        values.put(KEY_PHONE, phone);
        values.put(KEY_EMAIL, email);
        values.put(KEY_GENDER, gender);
        values.put(KEY_QUALIFICATION, qualification);
        values.put(KEY_FEES, fees);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_EXPERIENCE, experience);
        values.put(KEY_SPECIALITY, speciality);
        values.put(KEY_CITY, city);
        values.put(KEY_PROFILEIMAGE, profileImage);
        values.put(KEY_APIKEY, apiKey);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        long id = db.insert(TABLE_PROFESSIONALS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Professional is inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getProfessionalDetails(String id) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROFESSIONALS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("phone", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("gender", cursor.getString(5));
            user.put("qualification", cursor.getString(6));
            user.put("fees", cursor.getString(7));
            user.put("description", cursor.getString(8));
            user.put("experience", cursor.getString(9));
            user.put("speciality", cursor.getString(10));
            user.put("city", cursor.getString(11));
            user.put("country", cursor.getString(12));
            user.put("coverImage", cursor.getString(13));
            user.put("profileImage", cursor.getString(14));
            user.put("apiKey", cursor.getString(15));
            user.put("status", cursor.getString(16));
            user.put("created_at", cursor.getString(17));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching professional from Sqlite: " + user.toString());

        return user;
    }

    public ArrayList fetchId()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from professionals";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchName()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from professionals";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchSpeciality()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from professionals";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(10));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchFee()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select fees from professionals";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteProfessional() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PROFESSIONALS, null, null);
        db.close();

        Log.d(TAG, "Deleted all Professional info from sqlite");
    }

    /******************************************************************************************
     Create Database for User Tasks
     ******************************************************************************************/
    private static final String TABLE_TASKS = "tasks";

    // Login Table Columns names
    private static final String KEY_TID = "tid";
    private static final String KEY_CTID = "cid";
    private static final String KEY_USER = "user";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DETAIL = "detail";

    public void createTasksTable(SQLiteDatabase db)
    {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TID + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CTID + " TEXT,"
                + KEY_USER + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DETAIL + " TEXT,"
                + KEY_FEES + " TEXT ,"
                + KEY_CITY + " TEXT ,"
                + KEY_STATUS + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_TASKS_TABLE);

        Log.d(TAG, "Database Tasks tables created");
    }
    /**
     * Storing user details in database
     * */
    public void addTasks(String tid,String uid,String cid,String user,String title,String detail,String fees,
                                String city,String status, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TID, tid);
        values.put(KEY_UID, uid);
        values.put(KEY_CTID, cid);
        values.put(KEY_USER, user);
        values.put(KEY_TITLE, title);
        values.put(KEY_DETAIL, detail);
        values.put(KEY_FEES, fees);
        values.put(KEY_CITY, city);
        values.put(KEY_STATUS, status);
        values.put(KEY_CREATED_AT, created_at);
        // Inserting Row
        long id = db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Task is inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getTasksDetails(String id) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("tid", cursor.getString(1));
            user.put("uid", cursor.getString(2));
            user.put("cid", cursor.getString(3));
            user.put("user", cursor.getString(4));
            user.put("title", cursor.getString(5));
            user.put("detail", cursor.getString(6));
            user.put("fees", cursor.getString(7));
            user.put("city", cursor.getString(8));
            user.put("status", cursor.getString(9));
            user.put("created_at", cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching tasks from Sqlite: " + user.toString());

        return user;
    }

    public ArrayList fetchTid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from tasks";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchTuid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from tasks";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchUserName()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from tasks";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchTitle()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from tasks";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchDetail()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from tasks";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchTfee()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select fees from tasks";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchDate()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from tasks";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(10));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_TASKS, null, null);
        db.close();

        Log.d(TAG, "Deleted all Tasks info from sqlite");
    }
    /******************************************************************************************
     Create Database for News
     ******************************************************************************************/
    private static final String TABLE_NEWS = "news";

    // Login Table Columns names
    private static final String KEY_DID = "tid";

    public void createNewsTable(SQLiteDatabase db)
    {
        String CREATE_NEWS_TABLE = "CREATE TABLE " + TABLE_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DID + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_USER + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DETAIL + " TEXT,"
                + KEY_CITY + " TEXT ,"
                + KEY_STATUS + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_NEWS_TABLE);

        Log.d(TAG, "Database News tables created");
    }
    /**
     * Storing user details in database
     * */
    public void addNews(String did,String uid,String user,String title,String detail, String city,String status, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DID, did);
        values.put(KEY_UID, uid);
        values.put(KEY_USER, user);
        values.put(KEY_TITLE, title);
        values.put(KEY_DETAIL, detail);
        values.put(KEY_CITY, city);
        values.put(KEY_STATUS, status);
        values.put(KEY_CREATED_AT, created_at);
        // Inserting Row
        long id = db.insert(TABLE_NEWS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Task is inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getNewsDetails(String id) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NEWS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("did", cursor.getString(1));
            user.put("uid", cursor.getString(2));
            user.put("user", cursor.getString(3));
            user.put("title", cursor.getString(4));
            user.put("detail", cursor.getString(5));
            user.put("city", cursor.getString(6));
            user.put("status", cursor.getString(7));
            user.put("created_at", cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching news from Sqlite: " + user.toString());

        return user;
    }

    public ArrayList fetchDid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from news";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchDuid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from news";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchDuserName()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from news";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchDTitle()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from news";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchDdetail()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from news";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchDdate()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from news";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(8));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_TASKS, null, null);
        db.close();

        Log.d(TAG, "Deleted all news info from sqlite");
    }
    /******************************************************************************************
                           Create Database for Bids
     ******************************************************************************************/
    private static final String TABLE_BID = "bids";


    // Login Table Columns names
    private static final String KEY_BID = "bid";
    private static final String KEY_USER_BID = "user_bid";

    public void createBidTable(SQLiteDatabase db)
    {
        String CREATE_BIDS_TABLE = "CREATE TABLE " + TABLE_BID + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_BID + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_TID + " TEXT,"
                + KEY_USER + " TEXT,"
                + KEY_USER_BID + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_BIDS_TABLE);

        Log.d(TAG, "Database Bids tables created");
    }
    /**
     * Storing user details in database
     * */
    public void addBid(String bid,String uid,String tid,String user,String user_bid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BID, bid);
        values.put(KEY_UID, uid);
        values.put(KEY_TID, tid);
        values.put(KEY_USER, user);
        values.put(KEY_USER_BID, user_bid);
        values.put(KEY_CREATED_AT, created_at);
        // Inserting Row
        long id = db.insert(TABLE_BID, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Bid is inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getBids() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_BID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("bid", cursor.getString(1));
            user.put("uid", cursor.getString(2));
            user.put("tid", cursor.getString(3));
            user.put("user", cursor.getString(4));
            user.put("user_bid", cursor.getString(5));;
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching Bids from Sqlite: " + user.toString());

        return user;
    }

    public ArrayList fetchBid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from bids";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchBtid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from bids";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchBuid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from bids";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchBuserName()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from bids";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public ArrayList fetchUserBid()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from bids";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }

    public ArrayList fetchBdate()
    {
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fetchdata="select * from bids";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteBids() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_BID, null, null);
        db.close();

        Log.d(TAG, "Deleted all Bids info from sqlite");
    }
}
