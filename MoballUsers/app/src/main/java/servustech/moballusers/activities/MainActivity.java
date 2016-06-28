package servustech.moballusers.activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import servustech.moballusers.OkHttpManager;
import servustech.moballusers.R;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb;
    Button login;
    List<MyTask> tasks;

    EditText etUser;
    EditText etPassword;
    boolean accepted;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etUser = (EditText)findViewById(R.id.et_id_username);
        etPassword = (EditText)findViewById(R.id.et_id_password);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        tasks = new ArrayList<>();

        Button register = (Button)findViewById(R.id.bRegister);

       login = (Button)findViewById(R.id.bLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    requestData();
                } else {
                    Toast.makeText(getApplicationContext(), "Network isn't Available", Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean isLoginFormCompleted(){
        username = etUser.getText().toString();
        password = etPassword.getText().toString();
        if (username == null || password ==null ||username.isEmpty()||password.isEmpty()){
            username = "";
            password = "";
            Toast.makeText(this, "Please Complete the Fields!!!", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
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
            if (isOnline()) {
                requestData();
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
        if (isLoginFormCompleted()) {
            MyTask task = new MyTask();
            task.execute();
        }
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

            Toast.makeText(MainActivity.this, "Starting task!", Toast.LENGTH_SHORT).show();
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = null;
            String token;
            try {
                content = OkHttpManager.authenticate(username,password);
                Log.i("ContentAsyncTask",content);
                if (OkHttpManager.getLoginError(content)){
                    Log.i("ErrorCaught",content);
                    accepted = false;
                } else
                {
                    Log.i("Accepted",content);
                    accepted = true;
                    token =OkHttpManager.getToken(content);
                    return token;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("OkHttpError", e.toString());
            }
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
            Toast.makeText(MainActivity.this, "Finished task!", Toast.LENGTH_SHORT).show();
            if (accepted) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SecondActivity.class);
                Log.i("Token", result);
                intent.putExtra("Token", result);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(MainActivity.this, "Working" + values[0], Toast.LENGTH_LONG).show();
        }
    }
}