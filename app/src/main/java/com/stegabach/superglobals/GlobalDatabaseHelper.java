package com.stegabach.superglobals;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.stegabach.superglobals.SuperglobalsContract.GlobalEntry;
/**
 * Created by basti on 22.05.2016.
 */
public class GlobalDatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES ="CREATE TABLE " + GlobalEntry.TABLE_NAME + " ("
            + GlobalEntry._ID + " INTEGER PRIMARY KEY, "
            + GlobalEntry.COLUMN_NAME_GLOBAL_ID + TEXT_TYPE + COMMA_SEP
            + GlobalEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP
            + GlobalEntry.COLUMN_NAME_VALUE + TEXT_TYPE ;
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GlobalEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
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
}
