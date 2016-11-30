package com.naturecurly.zimuzu.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.JsonObject;
import com.naturecurly.zimuzu.Bean.Favs;
import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Bean.Update;
import com.naturecurly.zimuzu.Databases.FavDataScheme;
import com.naturecurly.zimuzu.Databases.FavDataScheme.FavTable;
import com.naturecurly.zimuzu.Databases.UpdateDataScheme;
import com.naturecurly.zimuzu.Databases.UpdateDataScheme.UpdateTable;

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

    public static ContentValues generateUpdateContentValues(Update update) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UpdateTable.Cols.ID, update.getId());
        contentValues.put(UpdateTable.Cols.RESOURCE, update.getResourceid());
        contentValues.put(UpdateTable.Cols.NAME, update.getName());
        contentValues.put(UpdateTable.Cols.FORMAT, update.getFormat());
        contentValues.put(UpdateTable.Cols.SEASON, update.getSeason());
        contentValues.put(UpdateTable.Cols.EPISODE, update.getEpisode());
        contentValues.put(UpdateTable.Cols.SIZE, update.getSize());
        contentValues.put(UpdateTable.Cols.CHANNEL, update.getChannel());
        contentValues.put(UpdateTable.Cols.CNNAME, update.getCnname());
        JsonObject jsonObject = update.getWays();
        String link = "";
        if (jsonObject.has("2")) {
            link = jsonObject.get("2").getAsString();
        }
        contentValues.put(UpdateTable.Cols.WAY, link);
        return contentValues;
    }

}
