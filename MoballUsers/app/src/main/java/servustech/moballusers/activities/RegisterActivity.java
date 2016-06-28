package servustech.moballusers.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import servustech.moballusers.OkHttpManager;
import servustech.moballusers.R;
import servustech.moballusers.model.MyEnum;

/**
 * Created by Claudiu on 5/28/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    List<MyTask> tasks;
    Spinner mySpinner;
    ProgressBar pb;

    Button submit;

    EditText eName;
    EditText eSSN;
    EditText ePhone;
    EditText eEmail;

    EditText eUsername;
    EditText ePassword;

    String name;
    String ssn;
    String phone;
    String email;
    String gender;
    String username;
    String password;

    boolean accepted;

    protected void onCreate(Bundle stateIntance) {
        super.onCreate(stateIntance);
        setContentView(R.layout.register_activity);

        eName = (EditText) findViewById(R.id.etRegName);
        eSSN = (EditText) findViewById(R.id.etRegSSN);
        ePhone = (EditText) findViewById(R.id.etRegPhone);
        eEmail = (EditText) findViewById(R.id.etRegEmail);

        mySpinner = (Spinner) findViewById(R.id.spGender);
        mySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MyEnum.values()));

        eUsername = (EditText) findViewById(R.id.etRegUsername);
        ePassword = (EditText) findViewById(R.id.etRegPassword);

        tasks = new ArrayList<>();
        pb = (ProgressBar) findViewById(R.id.progressBarR);
        pb.setVisibility(View.INVISIBLE);

        submit = (Button) findViewById(R.id.bRegSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    requestData();
                } else {
                    Toast.makeText(getApplicationContext(), "Network isn't Available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isRegisterFormCompleted() {
        name = eName.getText().toString();
        ssn = eSSN.getText().toString();
        phone = ePhone.getText().toString();
        email = eEmail.getText().toString();
        gender = mySpinner.getSelectedItem().toString();
        username = eUsername.getText().toString();
        password = ePassword.getText().toString();

        if (name == null || name.isEmpty() ||
                ssn == null || ssn.isEmpty() ||
                phone == null || phone.isEmpty() ||
                email == null || email.isEmpty() ||
                gender == null || gender.isEmpty() ||
                username == null || username.isEmpty() ||
                password == null || password.isEmpty()
                ) {
            Toast.makeText(this, "Please Complete All Fields!!!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void requestData() {
        if (isRegisterFormCompleted()) {
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

            Toast.makeText(RegisterActivity.this, "Starting task!", Toast.LENGTH_SHORT).show();
            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = null;

            try {
              content = OkHttpManager.register(name,ssn,phone,email,gender,username,password);
                if (OkHttpManager.getRegisterError(content)){
                    Log.i("ErrorCaught",content);
                    Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
                    accepted = false;
                } else
                {
                    Log.i("Accepted",content);
                    accepted = true;
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
            Toast.makeText(RegisterActivity.this, "Finished task!", Toast.LENGTH_SHORT).show();
            if (accepted) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(RegisterActivity.this, "Working" + values[0], Toast.LENGTH_LONG).show();
        }
    }
}
