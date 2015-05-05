package com.nexchanges.hailyo.model;


        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

 /*
public class DatabaseHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, PoemContract.DB_NAME, null, PoemContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PoemContract.DATABASE_CREATE_POEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Crashlytics.log(
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + PoemContract.TABLE);
        this.onCreate(db);
    }
}
*/


