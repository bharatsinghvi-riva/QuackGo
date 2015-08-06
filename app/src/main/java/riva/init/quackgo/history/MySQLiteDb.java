package riva.init.quackgo.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bharat.s on 8/6/15.
 */
public class MySQLiteDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "searchHistory.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "searchHistory";
    public static final String TABLE_COLUMN = "search_text";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + TABLE_COLUMN + " TEXT NOT NULL);";

    MySQLiteDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
