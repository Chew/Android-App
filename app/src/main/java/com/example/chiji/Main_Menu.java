package com.example.chiji;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class Main_Menu extends AppCompatActivity {
    Button bttn, bttn2;
    ImageView iv;
    Uri imageURi;
    EditText name;
    EditText phone;
    String Name,Phone, key,local,picture;
    Bitmap bm;
    private FusedLocationProviderClient client;
    private static final int PICK_IMAGE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        iv=findViewById(R.id.imageView2);
        bttn=findViewById(R.id.sendbttn);
        bttn2=findViewById(R.id.button2);
        requestPermission();
        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        bttn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                client= LocationServices.getFusedLocationProviderClient(Main_Menu.this);
                Name=name.getText().toString();
                Phone=phone.getText().toString();
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        ByteArrayOutputStream boas = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, boas);
                        byte[] b = boas.toByteArray();
                        picture =Base64.encodeToString(b, Base64.DEFAULT);
                        return picture;
                    }
                };
                if(ActivityCompat.checkSelfPermission(Main_Menu.this,ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(Main_Menu.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            local=location.toString();
                        }
                    }
                });

                doStuff();
            }
        });
    }
    private void openGallery(){
        Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int request, int results, Intent data){
        super.onActivityResult(request,results,data);
        if(results==RESULT_OK&& request==PICK_IMAGE){
            imageURi=data.getData();
            iv.setImageURI(imageURi);
        }
    }
    public void doStuff()
    {
        RequestBody formBody = new FormBody.Builder()
                .add("picture","picture")
                .add("phone",Phone)
                .add("reporter", Name)
                .add("location", local)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://ftt-api.chew.pro/fire/submit")
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
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }
}
