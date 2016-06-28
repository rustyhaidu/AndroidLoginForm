package servustech.moballusers.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import servustech.moballusers.MoballDbHelper;
import servustech.moballusers.OkHttpManager;
import servustech.moballusers.R;
import servustech.moballusers.model.Item;
import servustech.moballusers.model.ResultGson;

public class SecondActivity extends AppCompatActivity {

    ProgressBar pb;
    List<MyTask> tasks;
    ListView list;
    List<String> finalList = new ArrayList<>();
    MoballDbHelper moballDbHelper;
    SQLiteDatabase sqLiteDatabase;
    Context context = this;

    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Bundle extras = getIntent().getExtras();
        newString= extras.getString("Token");
        Log.i("GotToken", newString);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.progressBarS);
        pb.setVisibility(View.INVISIBLE);

        list = (ListView) findViewById(R.id.listView);
        tasks = new ArrayList<>();
    }

    public void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item, finalList);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (isOnline() && finalList.size() < 1) {
                requestData();
            } else if (finalList.size() > 0) {
                Toast.makeText(this, "List Already Loaded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Network isn't Available", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.action_db) {
            Intent intent = new Intent();
            intent.setClass(this, SelectInfoActivity.class);
            startActivity(intent);
        }

        return false;
    }

    private void requestData() {
            MyTask task = new MyTask();
            task.execute();
    }

    protected void updateDisplay(String message) {

        moballDbHelper = new MoballDbHelper(context);
        moballDbHelper.deleteTable();
        String strAppend = "";
        InputStream is = new ByteArrayInputStream(message.getBytes());

        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        String str;
        String concatenate;

        try {
            while ((str = in.readLine()) != null) {
                strAppend = strAppend + str;
                Log.i("lineString", strAppend);
            }

            Gson gson = new GsonBuilder().create();
            ResultGson rg = gson.fromJson(strAppend, ResultGson.class);

            ArrayList<Item> itemList;
            itemList = rg.getItems();
            for (int i = 0; i < itemList.size(); i++) {
                concatenate = itemList.get(i).getName() + itemList.get(i).getPhone() + itemList.get(i).getEmail() + itemList.get(i).getUsername() + itemList.get(i).getPassword();
                insertToDB(itemList.get(i).getName(), itemList.get(i).getPhone(), itemList.get(i).getEmail(), itemList.get(i).getUsername(), itemList.get(i).getPassword(), moballDbHelper);
                finalList.add(concatenate);
            }

        } catch (Exception e) {
            Toast.makeText(SecondActivity.this, "Error" + e, Toast.LENGTH_LONG).show();
            finalList.add(message);
        }

    }

    private void insertToDB(String name, String phone, String email, String username, String password, MoballDbHelper moballDbHelper) {

        sqLiteDatabase = moballDbHelper.getWritableDatabase();
        moballDbHelper.insertInfo(name, phone, email, username, password, sqLiteDatabase);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            Toast.makeText(SecondActivity.this, "Starting task!", Toast.LENGTH_SHORT).show();
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = null;
            try {
                content = OkHttpManager.getUsers(newString);
                Log.i("ContentAsyncTask",content);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("OkHttpError", e.toString());
            }
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ResultPost", result);
            updateDisplay(result);
           populateListView();

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
            Toast.makeText(SecondActivity.this, "Finished task!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(SecondActivity.this, "Working" + values[0], Toast.LENGTH_LONG).show();
        }
    }
}