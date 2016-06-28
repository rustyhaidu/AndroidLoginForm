package servustech.moballusers;

/**
 * Created by Claudiu on 5/19/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import servustech.moballusers.model.MoballDbContract;

/**
 * Created by claudiu.haidu on 7/16/2015.
 */
public class MoballDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Moball.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_QUERY = "CREATE TABLE " + MoballDbContract.TABLE_NAME +
            " (" + MoballDbContract.NAME + " TEXT, "
                 + MoballDbContract.PHONE + " TEXT, "
                 + MoballDbContract.EMAIL + " TEXT, "
                 + MoballDbContract.USERNAME + " TEXT, "
                 + MoballDbContract.PASSWORD + " TEXT);";

    public MoballDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("Database opened", "database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_QUERY);
        Log.e("Database", "Table created!");
    }

    public void insertInfo(String name, String phone, String email, String username, String password, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoballDbContract.NAME, name);
        contentValues.put(MoballDbContract.PHONE, phone);
        contentValues.put(MoballDbContract.EMAIL, email);
        contentValues.put(MoballDbContract.USERNAME, username);
        contentValues.put(MoballDbContract.PASSWORD, password);
        db.insert(MoballDbContract.TABLE_NAME, null, contentValues);
        Log.i("Database operation", "one row inserted");
    }

    public Cursor selectInfo(SQLiteDatabase db) {
        Cursor cursor;
        String[] projections = {MoballDbContract.NAME,
                                //MoballDbContract.PHONE,
                                MoballDbContract.EMAIL,
                              //  MoballDbContract.USERNAME,
                            //    MoballDbContract.PASSWORD,
        };
        cursor = db.query(MoballDbContract.TABLE_NAME, projections, null, null, null, null, null);
        return cursor;
    }
    public void deleteTable(){
        String query = "DELETE FROM "+ MoballDbContract.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MoballDbContract.TABLE_NAME);
        onCreate(db);
    }

}