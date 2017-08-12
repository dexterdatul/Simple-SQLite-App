package android.com.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dexter John Datul on 11/08/2017.
 */

public class MySqliteHandler extends SQLiteOpenHelper {

    // database version
    private static final int DATABASE_VERSION = 1;

    // database name
    private static final String DATABASE_NAME = "user_info.db";

    // user info table name
    private static final String TABLE_USER = "user";

    // user table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";

    String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_FIRST_NAME + " TEXT, " +
            COLUMN_LAST_NAME + " TEXT" + ")";

    public MySqliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);
    }


    //CREATE
    public void addUser(Profile profile) {

        SQLiteDatabase sqLiteDatabase = MySqliteHandler.this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME, profile.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, profile.getLastName());
        sqLiteDatabase.insert(TABLE_USER, null, contentValues);
        sqLiteDatabase.close();

    }

    //READ
    public Profile getProfile(int id) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_USER, new String[]{COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME}, COLUMN_ID +
                "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Profile profile = new Profile(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));

        return profile;

    }


    //get all user object
    public List<Profile> getAllUser() {

        List<Profile> userList = new ArrayList<>();

        String selectAllQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectAllQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Profile profile = new Profile();
                profile.setId(Integer.parseInt(cursor.getString(0)));
                profile.setFirstName(cursor.getString(1));
                profile.setLastName(cursor.getString(2));

                userList.add(profile);
            } while (cursor.moveToNext());
        }
        return userList;
    }


    //update
    public int updateProfile(Profile profile) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME, profile.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, profile.getLastName());

        return sqLiteDatabase.update(TABLE_USER, contentValues, COLUMN_ID + " = ? ", new String[]{String.valueOf(profile.getId())});
    }

    //delete
    public void deleteUser(Profile profile) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_USER, COLUMN_ID + "= ?", new String[]{String.valueOf(profile.getId())});
        sqLiteDatabase.close();
    }

    //get count
    public int getUserCount() {

        String profileCountQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(profileCountQuery, null);
        cursor.close();

        return cursor.getCount();
    }


}
