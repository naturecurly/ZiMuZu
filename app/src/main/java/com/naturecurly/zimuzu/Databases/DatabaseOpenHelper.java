package com.naturecurly.zimuzu.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.naturecurly.zimuzu.Databases.FavDataScheme.FavTable;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "zimuzu.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDB(sqLiteDatabase);
    }

    private void createDB(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + FavTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                FavTable.Cols.ID + " unique, " +
                FavTable.Cols.CNTITLE + ", " +
                FavTable.Cols.ENTITLE + ", " +
                FavTable.Cols.POSTER +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
