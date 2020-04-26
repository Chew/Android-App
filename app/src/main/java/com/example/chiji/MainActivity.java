package com.example.chiji;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient client;
    String username, password, local;
    String key="";
    EditText username_input;
    EditText pass_input;
    Button signup;
    TextView bob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        setContentView(R.layout.activity_main);
        client= LocationServices.getFusedLocationProviderClient(this);
        username_input = findViewById(R.id.username);
        pass_input = findViewById(R.id.password);
        bob=findViewById(R.id.textView2);
        signup = findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                username = username_input.getText().toString();
                password = pass_input.getText().toString();
                if(ActivityCompat.checkSelfPermission(MainActivity.this,ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            local=location.toString();
                        }
                    }
                });
                username_input.setText(local);
                if (username.length() > 4 && password.length() > 4 && !username.equals(password)) {
                    doStuff();
                    showToast();
                }
                else {
                    openDialog();
                }


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
                            openDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
    private void showToast() {
        Intent jump = new Intent(this, Main_Menu.class);
         startActivity(jump);
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION},1);
    }
    public void openDialog() {
        ExampleDialog beep=new ExampleDialog();
        beep.show(getSupportFragmentManager(), "Error Message");
    }

}
