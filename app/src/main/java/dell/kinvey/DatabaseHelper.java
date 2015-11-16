package dell.kinvey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 10/21/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ImageManager";
    private static final String KEY_IMAGE_PATH = "imagepath";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "username";
    private static final String TABLE_IMAGE = "boutique";

    // Table Create Statement

    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_IMAGE + "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_IMAGE_PATH
            + " TEXT,"  +  KEY_NAME +" TEXT" + ")";
    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public ArrayList<GridViewItemData> getAllImagePaths() {
        ArrayList<GridViewItemData> ImagePathList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GridViewItemData contact = new GridViewItemData();
                contact.setId(cursor.getString(0));
                contact.setImagePath(cursor.getString(1));
                contact.setUserName(cursor.getString(2));

                // Adding contact to list
                ImagePathList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ImagePathList;
    }
    public ArrayList<GridViewItemData> getAllImagePathsForAUser(String name) {
        ArrayList<GridViewItemData> ImagePathList = new ArrayList<>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        String sql= "SELECT * FROM boutique WHERE username = '" + name + "'";
        Cursor cursor=db.rawQuery(sql,null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GridViewItemData contact = new GridViewItemData();
                contact.setId(cursor.getString(0));
                contact.setImagePath(cursor.getString(1));
                contact.setUserName(cursor.getString(2));

                // Adding contact to list
                ImagePathList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ImagePathList;
    }

    public void addGridViewItemData(GridViewItemData top) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, top.getId());
        values.put(KEY_IMAGE_PATH, top.getImagePath());
        values.put(KEY_NAME,top.getUserName());



        // Inserting Row
        db.insert(TABLE_IMAGE, null, values);
        db.close(); // Closing database connection
    }

    public int getTableSize(String table)
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        int size = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, table);
        return size;
    }























}
