package com.example.chiji;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    String username, email, password, content;
    String key="uEEDkisTQTnVnNWskEpQihgSEoXWZXVQluFRhTcYWtKYgjDwia";
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
                try {
                    //POST
                    GetText();
                } catch (Exception ex) {
                    content = (" url exeption! ");
                }

                    showToast();
            }
        });
    }

    private void showToast() {
        Intent jump = new Intent(this, Main3Activity.class);
        startActivity(jump);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GetText() throws IOException {
        System.setProperty("http.agent", "Chrome");
        URL url = new URL("https://ftt-api.chew.pro/register");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode("kittens", "UTF-8");
      data += "&" + URLEncoder.encode("password", "UTF-8")
                + "=" + URLEncoder.encode("kitter", "UTF-8");
        try {
            //conn.setRequestProperty(key,data);
            OutputStream dos = conn.getOutputStream();
            dos.write(("username="+"kitten"+"&password="+"kitten").getBytes());
            dos.flush();
            //conn.disconnect();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JSONObject jsonObject = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream()))) {
            jsonObject = new JSONObject(in.readLine());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conn.disconnect();

    }
}
