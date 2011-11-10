package cz.vutbr.fit.tam.paint3d;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "paint3d";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE painting (" +
                       "painting_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                       "name VARCHAR NOT NULL COLLATE LOCALIZED)"
                  );
        db.execSQL("CREATE TABLE painting_point (" +
                       "painting_point_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                       "painting_id INTEGER NOT NULL," +
                       "x REAL NOT NULL," +
                       "y REAL NOT NULL," +
                       "z REAL NOT NULL)"
                  );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
