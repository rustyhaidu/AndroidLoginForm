package servustech.moballusers.activities;

/**
 * Created by Claudiu on 5/19/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import servustech.moballusers.ListDataAdapter;
import servustech.moballusers.MoballDbHelper;
import servustech.moballusers.R;
import servustech.moballusers.model.DataProvider;

/**
 * Created by claudiu.haidu on 7/24/2015.
 */
public class SelectInfoActivity extends AppCompatActivity {
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    MoballDbHelper moballDBHelper;
    Cursor cursor;
    ListDataAdapter listDataAdapter;

    protected void onCreate(Bundle stateIntance) {
        super.onCreate(stateIntance);
        setContentView(R.layout.select_info_activity);


        listView = (ListView) findViewById(R.id.lvListView);
        listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.rowlayout);
        listView.setAdapter(listDataAdapter);

        moballDBHelper = new MoballDbHelper(getApplicationContext());
        sqLiteDatabase = moballDBHelper.getReadableDatabase();

        cursor = moballDBHelper.selectInfo(sqLiteDatabase);

        if (cursor.moveToFirst()) {

            do {
                String moballName, moballEmail;
                moballName = cursor.getString(0);
                moballEmail = cursor.getString(1);
                DataProvider dataProvider = new DataProvider(moballName, moballEmail);
                listDataAdapter.add(dataProvider);
            }
            while (cursor.moveToNext());
        }
        //registerClickCallback();
        listDataAdapter.notifyDataSetChanged();
        cursor.close();
    }
}

