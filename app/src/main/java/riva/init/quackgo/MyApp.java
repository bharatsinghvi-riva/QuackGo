package riva.init.quackgo;

import android.app.Application;

import riva.init.quackgo.history.MySQLiteDb;

/**
 * Created by bharat.s on 8/18/15.
 */
public class MyApp extends Application {

    private static MySQLiteDb mySQLiteDb;
    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mySQLiteDb = new MySQLiteDb(getApplicationContext());
        myApp = this;
    }

    public MySQLiteDb getMySQLiteDb() {
        return mySQLiteDb;
    }

    public static MyApp getInstance() {
        return myApp;
    }

}
