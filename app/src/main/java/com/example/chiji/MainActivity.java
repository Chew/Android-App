package com.example.chiji;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
//import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    String username, email, password, content;
    String key="";
    EditText username_input;
    EditText email_input;
    EditText pass_input;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username_input = findViewById(R.id.username);
        email_input = findViewById(R.id.email);
        pass_input = findViewById(R.id.password);

        signup = findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                username = username_input.getText().toString();
                email = email_input.getText().toString();
                password = pass_input.getText().toString();
                doStuff();

                showToast();
            }
        });
    }
    public void doStuff()
    {
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://ftt-api.chew.pro/register")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, IOException e) {
                System.out.println("FAILED!");
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {

                    System.out.println(responseBody);
                    // Figure out how to JSON Parse it, idk
                    try {
                        JSONObject json = new JSONObject(responseBody.string());
                        if(json.getString("status").equals("success")) {
                            key= json.getString("key");
                        } else {
                            key="";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
    private void showToast() {
        Intent jump = new Intent(this, Main3Activity.class);
        startActivity(jump);
    }

}
