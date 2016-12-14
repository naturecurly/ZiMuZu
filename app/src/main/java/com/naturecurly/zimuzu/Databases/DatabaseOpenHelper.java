package com.naturecurly.zimuzu.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.naturecurly.zimuzu.Bean.Episode;
import com.naturecurly.zimuzu.Databases.EpisodeDataScheme.EpisodeTable;
import com.naturecurly.zimuzu.Databases.FavDataScheme.FavTable;
import com.naturecurly.zimuzu.Databases.UpdateDataScheme.UpdateTable;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 3;
    public static final String DATABASE_NAME = "zimuzu.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDB(sqLiteDatabase);
    }

    private void createDB(SQLiteDatabase sqLiteDatabase) {
        //create fav table
        sqLiteDatabase.execSQL("create table " + FavTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                FavTable.Cols.ID + " unique, " +
                FavTable.Cols.CNTITLE + ", " +
                FavTable.Cols.ENTITLE + ", " +
                FavTable.Cols.POSTER +
                ")"
        );
        //create update table
        sqLiteDatabase.execSQL("create table " + UpdateTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                UpdateTable.Cols.ID + " unique, " +
                UpdateTable.Cols.RESOURCE + ", " +
                UpdateTable.Cols.NAME + ", " +
                UpdateTable.Cols.FORMAT + ", " +
                UpdateTable.Cols.SEASON + ", " +
                UpdateTable.Cols.EPISODE + ", " +
                UpdateTable.Cols.SIZE + ", " +
                UpdateTable.Cols.CNNAME + ", " +
                UpdateTable.Cols.CHANNEL + ", " +
                UpdateTable.Cols.WAY +
                ")"
        );
        //create episode table
        sqLiteDatabase.execSQL("create table " + EpisodeTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                EpisodeTable.Cols.RESOURCE + " unique, " +
                EpisodeTable.Cols.WATCHED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            System.out.printf("update database");
            sqLiteDatabase.execSQL("create table " + UpdateTable.NAME + "(" +
                    "_id integer primary key autoincrement, " +
                    UpdateTable.Cols.ID + " unique, " +
                    UpdateTable.Cols.RESOURCE + ", " +
                    UpdateTable.Cols.NAME + ", " +
                    UpdateTable.Cols.FORMAT + ", " +
                    UpdateTable.Cols.SEASON + ", " +
                    UpdateTable.Cols.EPISODE + ", " +
                    UpdateTable.Cols.SIZE + ", " +
                    UpdateTable.Cols.CNNAME + ", " +
                    UpdateTable.Cols.CHANNEL + ", " +
                    UpdateTable.Cols.WAY +
                    ")"
            );

            sqLiteDatabase.execSQL("create table " + EpisodeTable.NAME + "(" +
                    "_id integer primary key autoincrement, " +
                    EpisodeTable.Cols.RESOURCE + " unique, " +
                    EpisodeTable.Cols.WATCHED +
                    ")"
            );
        }

        if (oldVersion == 2) {
            sqLiteDatabase.execSQL("create table " + EpisodeTable.NAME + "(" +
                    "_id integer primary key autoincrement, " +
                    EpisodeTable.Cols.RESOURCE + " unique, " +
                    EpisodeTable.Cols.WATCHED +
                    ")"
            );
        }
    }
}
