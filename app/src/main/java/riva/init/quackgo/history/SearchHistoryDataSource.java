package riva.init.quackgo.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by bharat.s on 8/6/15.
 */

public class SearchHistoryDataSource {

    private static MySQLiteDb mySQLiteDb;
    private SQLiteDatabase database;
    private String[] allcols = { MySQLiteDb.TABLE_COLUMN };

    public SearchHistoryDataSource(MySQLiteDb mySQLiteDb) {
        this.mySQLiteDb = mySQLiteDb;
    }

    public void open() throws SQLException {
        database = mySQLiteDb.getWritableDatabase();
    }

    public void close() {
        mySQLiteDb.close();
    }

    public void insertSearchHistory(String searchText) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteDb.TABLE_COLUMN, searchText);
        database.insert(MySQLiteDb.TABLE_NAME, null, contentValues);
    }

    public ArrayList<String> getRelevantSearchHistory(String keyword) {
        ArrayList<String> relevantLocalSearch = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteDb.TABLE_NAME, allcols, MySQLiteDb.TABLE_COLUMN + " LIKE '" + keyword + "%'", null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            relevantLocalSearch.add(cursorToSearchHistory(cursor).toString());
        cursor.close();
        return relevantLocalSearch;
    }

    public SearchHistoryItem cursorToSearchHistory(Cursor cursor) {
        SearchHistoryItem searchHistoryItem = new SearchHistoryItem(cursor.getString(cursor.getColumnIndex(MySQLiteDb.TABLE_COLUMN)));
        return searchHistoryItem;
    }
}
