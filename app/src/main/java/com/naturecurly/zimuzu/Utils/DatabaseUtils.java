package com.naturecurly.zimuzu.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.naturecurly.zimuzu.Bean.Favs;
import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Databases.FavDataScheme;
import com.naturecurly.zimuzu.Databases.FavDataScheme.FavTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class DatabaseUtils {
    public static ContentValues generateFavContentValues(Series series) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavTable.Cols.ID, series.getId());
        contentValues.put(FavTable.Cols.CNTITLE, series.getCnname());
        contentValues.put(FavTable.Cols.ENTITLE, series.getEnname());
        contentValues.put(FavTable.Cols.POSTER, series.getPoster());
        return contentValues;
    }

    public static List<Favs> fetchFavs(SQLiteDatabase database) {
        Cursor cursor = database.query(FavTable.NAME, null, null, null, null, null, null);
        List<Favs> favList = new ArrayList<>();
        while (cursor.moveToNext()) {
            favList.add(new Favs(cursor.getString(cursor.getColumnIndex(FavTable.Cols.ID)), cursor.getString(cursor.getColumnIndex(FavTable.Cols.CNTITLE)), cursor.getString(cursor.getColumnIndex(FavTable.Cols.ENTITLE)), cursor.getString(cursor.getColumnIndex(FavTable.Cols.POSTER))));
        }
        return favList;
    }
}
