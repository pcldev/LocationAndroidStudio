package com.example.mylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Button btnUsingGPS;
    EditText etLat, etLong;
    LocationManager locationManager = null;
    Location lastLoc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        btnUsingGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUsingGPS();
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //cap nhat vi tri cua thiet bi khi location thay doi
        String locationInfo = String.format("Current location = (%f %f) @ (% meter up)",location.getLatitude(),location.getLongitude(),location.getAltitude());
        if(lastLoc != null){
            float distance = location.distanceTo(lastLoc);
            locationInfo += String.format("\n Distance from last = %f meters",distance);
        }
        lastLoc = location;
        Toast.makeText(this, "Location information: " + locationInfo, Toast.LENGTH_LONG).show();
    }

    private void getViews() {
        btnUsingGPS = findViewById(R.id.btnUsingGPS);
        etLat = findViewById(R.id.etLat);
        etLong = findViewById(R.id.etLong);
    }

    public void doUsingGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        //goi phuong thuc lay ra provider phu hop
        String bestProvider = locationManager.getBestProvider(criteria, true);
        //lay ra duoc vi tri location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location != null){
            //cap hat lai location
            locationManager.requestLocationUpdates(bestProvider,
                    1000,
                    0,
                    this::onLocationChanged);
        }else{
            double latitude = Double.parseDouble(etLat.getText().toString());
            double longtitude = Double.parseDouble(etLong.getText().toString());
            location.setLatitude(latitude);
            location.setLongitude(longtitude);
        }
    }
}