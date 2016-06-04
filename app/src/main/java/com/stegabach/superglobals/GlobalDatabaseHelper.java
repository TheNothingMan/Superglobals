package com.stegabach.superglobals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stegabach.superglobals.SuperglobalsContract.GlobalEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by basti on 22.05.2016.
 */
public class GlobalDatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES ="CREATE TABLE " + GlobalEntry.TABLE_NAME + " ("
            + GlobalEntry._ID + " INTEGER PRIMARY KEY, "
            + GlobalEntry.COLUMN_NAME_GLOBAL_ID + TEXT_TYPE + " unique" + COMMA_SEP
            + GlobalEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP
            + GlobalEntry.COLUMN_NAME_VALUE + TEXT_TYPE+")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GlobalEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Globals";

    public GlobalDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * Create a new Superglobal
     * @param superglobal
     * @return long new ID
     */
    public long createSuperglobal(Superglobal superglobal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(GlobalEntry.COLUMN_NAME_GLOBAL_ID,superglobal.getName());
        values.put(GlobalEntry.COLUMN_NAME_TYPE,superglobal.getType());
        values.put(GlobalEntry.COLUMN_NAME_VALUE,superglobal.getValueAsString());

        try {
            long newRowID = db.insert(GlobalEntry.TABLE_NAME, null, values);
            closeDatabase();
            return newRowID;
        }catch (SQLiteConstraintException e){
            Log.e("SuperglobalsDebug", "createSuperglobal: ", e);
            closeDatabase();
            return -1;
        }
    }

    /**
     * Get a list of all Superglobals in database
     *
     * @return List<Superglobal>
     */
    public List<Superglobal> getSuperglobals(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {GlobalEntry._ID,GlobalEntry.COLUMN_NAME_GLOBAL_ID,GlobalEntry.COLUMN_NAME_TYPE,GlobalEntry.COLUMN_NAME_VALUE};
        String sortOrder = GlobalEntry.COLUMN_NAME_TYPE + " ASC, " + GlobalEntry.COLUMN_NAME_GLOBAL_ID + " ASC";
        List<Superglobal> result = new ArrayList<Superglobal>();

        Cursor c = db.query(GlobalEntry.TABLE_NAME,
                projection, null,null,null,null,sortOrder);

        while (c.moveToNext()){
            Superglobal h = new Superglobal(c.getLong(c.getColumnIndexOrThrow(GlobalEntry._ID)),
                    c.getString(c.getColumnIndexOrThrow(GlobalEntry.COLUMN_NAME_GLOBAL_ID)),
                    c.getString(c.getColumnIndexOrThrow(GlobalEntry.COLUMN_NAME_TYPE)),
                    c.getString(c.getColumnIndexOrThrow(GlobalEntry.COLUMN_NAME_VALUE)));
            result.add(h);
        }

        closeDatabase();
        return result;
    }

    /**
     * Delete a Superglobal
     * @param id - ID of the global to delete
     */
    public void deleteSuperglobal(long id){
        String selection = GlobalEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GlobalEntry.TABLE_NAME, selection,selectionArgs);

        closeDatabase();
    }

    /**
     * Get a single Superglobal by ID
     * @param id
     * @return Superglobal with specified ID
     */
    public Superglobal getSuperglobalById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {GlobalEntry._ID,GlobalEntry.COLUMN_NAME_GLOBAL_ID,GlobalEntry.COLUMN_NAME_TYPE,GlobalEntry.COLUMN_NAME_VALUE};
        String selection = GlobalEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor c = db.query(GlobalEntry.TABLE_NAME,
                projection, selection,selectionArgs,null,null,null);

        c.moveToFirst();
        Superglobal result = new Superglobal(c.getLong(c.getColumnIndexOrThrow(GlobalEntry._ID)),
                c.getString(c.getColumnIndexOrThrow(GlobalEntry.COLUMN_NAME_GLOBAL_ID)),
                c.getString(c.getColumnIndexOrThrow(GlobalEntry.COLUMN_NAME_TYPE)),
                c.getString(c.getColumnIndexOrThrow(GlobalEntry.COLUMN_NAME_VALUE)));

        closeDatabase();
        return result;
    }


    /**
     * Update a superglobal. Passed element must have an ID set!
     * @param superglobal
     * @return
     */
    public long updateSuperglobal(Superglobal superglobal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String selection = GlobalEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(superglobal.getId())};

        values.put(GlobalEntry.COLUMN_NAME_GLOBAL_ID,superglobal.getName());
        values.put(GlobalEntry.COLUMN_NAME_TYPE,superglobal.getType());
        values.put(GlobalEntry.COLUMN_NAME_VALUE,superglobal.getValueAsString());

        try {
            int count = db.update(GlobalEntry.TABLE_NAME, values, selection, selectionArgs);
            closeDatabase();
            return count;
        }catch (SQLiteConstraintException e){
            Log.e("SuperglobalsDebug", "updateSuperglobal: ", e);
            closeDatabase();
            return -1;
        }
    }

    //close database
    public void closeDatabase ()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
        {
            db.close();
        }
    }
}
