package com.example.jaky_d.smartattendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Calendar;
@SuppressWarnings({"unused", "Convert2Lambda"})
public class AccountActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView txtLocationResult;
    private Button logOut;
    private static final int REQUEST_CODE = 1000;
    TextView txt_location,lotlgt;
    Button btn_start, btn_stop;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private String lt;
    private String lg;
    private Double classlot;
    private Double classlgt;
    private Double lot;
    private Double lgt;
    private String userClassName="";
    private Double userClassLat;
    private Double userClassLng;
    private Location mCurrentLocation;
    FirebaseAuth mAuth;
    private Boolean T;
    public FirebaseFirestore db;
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


    DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smartattendance-c896a.firebaseio.com/Classes/SE11/");
    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smartattendance-c896a.firebaseio.com/Attendance");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smartattendance-c896a.firebaseio.com/Users/");
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_account);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        statusCheck();
        getLocationPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAuth = FirebaseAuth.getInstance();

        logOut = (Button) findViewById(R.id.logoutbtn);

        txt_location = (TextView) findViewById(R.id.txt_location);
        txtLocationResult = (TextView)findViewById(R.id.location_result);
        this.db = FirebaseFirestore.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();
        String UID1 = user.getUid();

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            buildLocationRequest();
            buildLocationCallBack();





            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    GetClass();
                }
            }, 2000);





           /* ref.addValueEventListener(new ValueEventListener() {
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
            }); */



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
    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia,
        mMap = map;
        // and move the map's camera to the same location.
        //LatLng sydney = new LatLng(-33.852, 151.211);
        //googleMap.addMarker(new MarkerOptions().position(sydney)
          //      .title("Marker in Sydney"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                updateLocationUI();
                getDeviceLocation();
            }
        }, 4000);

    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d("a", "Current location is null. Using defaults.");
                            Log.e("b", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                       // dialog.cancel();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    public void GetClass() {
        FirebaseUser user = mAuth.getCurrentUser();
        String UID1 = user.getUid();

        DocumentReference userClassRef = db.collection("UserClass").document(UID1);
        userClassRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Data", "DocumentSnapshot data: " + document.getData());
                        List<String> classes = (List<String>) document.get("ClassName");
                        for (int i = 0; i < classes.size(); i++) {
                            //userClassName="";
                            getclasslatlong(classes.get(i));
                        /*if(userClassName.length()>0){
                            Log.d("Data", "Class found data: " + "Breaking Loop");
                           break;
                        }*/
                        }
                        //  GeoPoint geoPoint = document.getGeoPoint("SE22");
                        //    double clat = geoPoint.getLatitude();
                        //    double clng = geoPoint.getLongitude ();
                        //    Log.d("Data", "DocumentSnapshot data: " + clat);
                        //  Log.d("Data", "DocumentSnapshot data: " + clng);
                    } else {
                        Log.d("NOT", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }

            }

        });
    }
    public void getclasslatlong(final String className)
    {
        DocumentReference docRef = db.collection("Classes").document("ClassesList");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Data", "DocumentSnapshot data: " + document.getData());
                        GeoPoint geoPoint = document.getGeoPoint(className);
                        double clat = geoPoint.getLatitude();
                        double clng = geoPoint.getLongitude();
                        Log.d("Data", "DocumentSnapshot data: " + clat);
                        Log.d("Data", "DocumentSnapshot data: " + clng);
                        if(checkUserDistance(clat,clng)){
                            Log.d("Data", "You are in " + className);
                            userClassName=className;
                        }
                    } else {
                        Log.d("NOT", "No such document");
                    }
                } else {
                    Log.d("Failed", "get failed with ", task.getException());
                }
            }
        });
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

    public boolean checkUserDistance(double classLat,double classLgt){

        double clot = mCurrentLocation.getLatitude();
        double clgt = mCurrentLocation.getLongitude();
        double lott = classLat;
        double lgtt = classLgt;

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
        String d = Double.toString(distance2);
        Log.d("Distance",d);
        if (distance2 <= 15) {
            return true;
        }else{
            return false;
        }
    }

    public void markAttendance(View view) {
        /*double clot = lot;
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
        y2 = lgtt - coef / Math.cos(lott * 0.018);*/
      /*  if(((clot>=x2)&&(clot<=x1))&&((clgt>=y2)&&(clgt<=y1))){
            txtLocationResult.setText("attendance marked");
    } */
        // double distance = Math.sqrt(Math.pow((lott - clot), lgtt-clgt));
        // double distance2= mCurrentLocation.distanceTo(ol);
  /*  float[] distance3 = new float[1];
    Location.distanceBetween(lott, lgtt, clot, clgt, distance3);*/
        /*String s=String.valueOf(distance2);
        Log.i("distance",s);*/
        if (userClassName.length()>0) {
            T=true;
            FirebaseUser user = mAuth.getCurrentUser();
            String UID= user.getUid();
            Log.d(UID,"User ID");
            String cl_name = userClassName;
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            Log.d(strDate,"Current Date");
            DatabaseReference user1 = ref1.child(UID);
            user1.child(cl_name).child(strDate).setValue(T);
            txtLocationResult.setText("Attendance marked for "+userClassName);
        } else {
            txtLocationResult.setText("Attendance can't be marked for any Class");
            T=false;
        }

    }
}