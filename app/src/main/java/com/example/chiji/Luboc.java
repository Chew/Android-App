package com.example.chiji;

import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Luboc extends AppCompatActivity {
  private FusedLocationProviderClient client;
  Button bttn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luboc);
        client= LocationServices.getFusedLocationProviderClient(this);
        bttn=findViewById(R.id.button);
        requestPermission();
        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(Luboc.this,ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(Luboc.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            TextView tv=findViewById(R.id.textView3);
                            tv.setText(location.toString());
                        }
                    }
                });
            }
        });


    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }
  }