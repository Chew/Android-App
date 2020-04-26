package com.example.chiji;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Login_Page extends AppCompatActivity {
    Button hello;
    Button login;
    EditText name_input;
    EditText pass_input;
    String name, pass;
    String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        pass_input=findViewById(R.id.user1);
        name_input=findViewById(R.id.pass1);
        hello=findViewById(R.id.buttn6);
        login=findViewById(R.id.loginbttn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_input.getText().toString();
                pass = pass_input.getText().toString();
                if (name.length() > 4 && pass.length() > 4 && !name.equals(pass)) {
                  //  Logins();
                    Intent yeet=new Intent(Login_Page.this, Main_Menu.class);
                    startActivity(yeet);
                }
                else
                  openDialog();
            }
        });

        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bob=new Intent(Login_Page.this,MainActivity.class);
                startActivity(bob);
            }
        });

    }
    public void Logins() {
        RequestBody formBody = new FormBody.Builder()
                .add("username", name)
                .add("password", pass)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://ftt-api.chew.pro/login")
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
                            openDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
    public void openDialog() {
        ExampleDialog beep=new ExampleDialog();
        beep.show(getSupportFragmentManager(), "Error Message");
    }

    }
