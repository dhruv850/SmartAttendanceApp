package com.example.jaky_d.smartattendance;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

public class AccountActivity extends AppCompatActivity {
private TextView txtLocationResult;
    private Button logOut;
    private static final int REQUEST_CODE = 1000;
    TextView txt_location,lotlgt;
    Button btn_start, btn_stop;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private String lt;
    private String lg;
    private Double classlot;
    private Double classlgt;
    private Double lot;
    private Double lgt;
    private Location mCurrentLocation;
    FirebaseAuth mAuth;
private Boolean T;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smartattendance-c896a.firebaseio.com/Classes/SE11/");
    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smartattendance-c896a.firebaseio.com/Attendance");

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);



        mAuth = FirebaseAuth.getInstance();

        logOut = (Button) findViewById(R.id.logoutbtn);
lotlgt = (TextView) findViewById(R.id.clotlgt);
        txt_location = (TextView) findViewById(R.id.txt_location);
        btn_start = (Button) findViewById(R.id.btn_start_updates);
        btn_stop = (Button) findViewById(R.id.btn_stop_updates);

txtLocationResult = (TextView)findViewById(R.id.location_result);
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            buildLocationRequest();
            buildLocationCallBack();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator );
                    String Latitude = map.get ("latitude");
                    String Longitude = map.get ("longitude");
                    Log.v("E_Value","latitude :"+ Latitude);
                    Log.v("E_Value","longitude :"+ Longitude);
                    classlot = Double.parseDouble(Latitude);
                    classlgt = Double.parseDouble(Longitude);
                    lotlgt.setText(classlot+"/"+classlgt);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    btn_start.setEnabled(!btn_start.isEnabled());
                    btn_stop.setEnabled(!btn_stop.isEnabled());
                }
            });
            btn_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        return;
                    }
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    btn_start.setEnabled(!btn_start.isEnabled());
                    btn_stop.setEnabled(!btn_stop.isEnabled());
                }
            });

            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AccountActivity.this, MainActivity.class));
                    Toast.makeText(AccountActivity.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location:locationResult.getLocations())
                    txt_location.setText(String.valueOf(location.getLatitude())+"/"+String.valueOf(location.getLongitude()));
                mCurrentLocation = locationResult.getLastLocation();
                lot = mCurrentLocation.getLatitude();
                lgt = mCurrentLocation.getLongitude();
                lg = Double.toString(lgt);
                lt = Double.toString(lot);

            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    public void markAttendance(View view) {
        double clot = lot;
        double clgt = lgt;
        double lott;
        double lgtt;
         lott = classlot;
        lgtt = classlgt;
        Location ol=new Location(mCurrentLocation);
        ol.setLatitude(lott);
        ol.setLongitude(lgtt);

        double x1, x2, y1, y2;
        double meters = 20;
        double coef = meters * 0.0000089;
        x1 = lott + coef;
        y1 = lgtt + coef / Math.cos(lott * 0.018);
        x2 = lott - coef;
        y2 = lgtt - coef / Math.cos(lott * 0.018);
      /*  if(((clot>=x2)&&(clot<=x1))&&((clgt>=y2)&&(clgt<=y1))){


            txtLocationResult.setText("attendance marked");

    } */
        double distance = Math.sqrt(Math.pow((lott - clot), 2));
        double distance2= mCurrentLocation.distanceTo(ol);
  /*  float[] distance3 = new float[1];
    Location.distanceBetween(lott, lgtt, clot, clgt, distance3);*/
        String s=String.valueOf(distance2);
        Log.i("distance",s);
        if (distance2 <= 10) {
            T=true;
            txtLocationResult.setText("attendance marked");
        } else {
            txtLocationResult.setText("attendance cant be marked");
            T=false;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        String UID= user.getUid();
        Log.d(UID,"User ID");
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        Log.d(strDate,"Current Date");
        DatabaseReference user1 = ref1.child(UID);
        user1.child(strDate).setValue(T);
    }
}