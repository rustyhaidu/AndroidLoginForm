package servustech.moballusers;

/**
 * Created by Claudiu on 5/19/2016.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import servustech.moballusers.model.ErrorResult;
import servustech.moballusers.model.ResultAutentification;

/**
 * Created by Claudiu on 10/11/2015.
 */
public class OkHttpManager {


    public static String run(String user, String password) throws IOException {
        String token;
        String content;
        Log.i("Text","text");
        token = authenticate(user,password);
        //content = getUsers(token);
        Log.i("Text","text");
        Log.i("ContentAsyncTask",token);
        return token;
    }
    public static String getToken(String content) throws IOException {
        String token;
        Gson gson = new GsonBuilder().create();
        ResultAutentification ra = gson.fromJson(content, ResultAutentification.class);
        Log.i("GSONToken", ra.getToken().getValue());
        token = ra.getToken().getValue();

        return token;
    }
    public static boolean getLoginError(String message){
        String code;
        Log.i("Text","text3");
        Gson gson = new GsonBuilder().create();
        ErrorResult er = gson.fromJson(message, ErrorResult.class);

        if (er.getError()!=null){
            Log.i("ErrorNotNull","notnull");
            code = er.getError().getCode();
            Log.i("TextTrue",code);
            if (code.equals(new String("user.not.found"))){
                return true;
            }
            else {
                Log.i("TextFalse","text4");
                return false;
            }
        }  else{
            Log.i("TextFalse","Else");
            return false;
        }
    }
    public static boolean getRegisterError(String message){
        String code;
        Gson gson = new GsonBuilder().create();
        ErrorResult er = gson.fromJson(message, ErrorResult.class);

        if (er.getError()!=null){
            Log.i("ErrorNotNull","notnull");
            code = er.getError().getCode();
            Log.i("TextTrue",code);
            if (code.equals(new String("user.email.invalid.pattern"))){
                Log.i("EmailPatter",code);
            }
            return true;
        }  else{
            Log.i("TextFalse","Else");
            return false;
        }
    }
    public static String run2(String user, String password) throws IOException {
        String token;
        String content;
        token = authenticate(user,password);
        content = getUsers(token);
        return content;
    }
    public static String authenticate(String user, String password) throws IOException {
        Log.i("Text","text2");
        String token;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,
                "{\"username\" :"+"\""+user+"\" "+", \"password\" :"+"\""+password+"\""+"}");
        Request request = new Request.Builder()
                .url("Enter URL")
                .post(body)
                .build();

        Response responseToken = client.newCall(request).execute();
        token = responseToken.body().string();
        Log.i("UserToken", token);
        return token;
    }
    public static String register(String user, String ssn,String phone, String email, String gender, String username, String password) throws IOException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,
                "{" +
                        "\"user\" :"+"\""+user+"\" "+"," +
                        "\"ssn\" :"+"\""+ssn+"\" "+"," +
                        "\"phone\" :"+"\""+phone+"\" "+"," +
                        "\"email\" :"+"\""+email+"\" "+"," +
                        "\"gender\" :"+"\""+gender+"\" "+"," +
                        "\"username\" :"+"\""+username+"\" "+"," +
                        " \"password\" :"+"\""+password+"\""+"}"
        );
        Request request = new Request.Builder()
                .url("Enter URL")
                .post(body)
                .build();

        Response responseRegister = client.newCall(request).execute();
        String registerResponse = responseRegister.body().string();
        Log.i("CreateUser",  registerResponse);
        return registerResponse;

    }
    public static String getUsers(String token) throws IOException {
        Log.i("GetUsersResponse", "Start");
        String returnResponse;
        OkHttpClient client = new OkHttpClient();

        Request request2 = new Request.Builder()
                .url("Enter URL")
                .addHeader("authorization", token)
                .get()
                .build();

        Response response = client.newCall(request2).execute();
        returnResponse = response.body().string();
        Log.i("GetUsersResponse", returnResponse);
        return returnResponse;
    }
}