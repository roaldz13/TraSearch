package com.example.taquio.trasearch.Samok;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.taquio.trasearch.Models.Lokasyon;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.BottomNavigationViewHelper;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        LocationListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MapActivity";
    private ArrayList<String> mUsers;
    private ArrayList<String> businessUser;
    private DatabaseReference databaseReference;
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = MapActivity.this;
    //permissions
    private Boolean mLocationPermissionsGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //for the marker
    ArrayList<Location> myloc = new ArrayList<>();
    private String Name, Location, Contact;
    //for the current locatin
    private FusedLocationProviderClient mFusedLocationProviderClient;
    ArrayList<LatLng> listPoints;
    ArrayList<Location> latlong;
    GoogleMap mgoogleMap;
    GoogleApiClient mgoogleClient;
    Button button;
    String lattitude, longitude;
    LocationManager locationManager;
    private static final int LOCATION_REQUEST = 500;

    @Override//method to request permission
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d("MapActivity", "onRequestPermissionsResult: permission failed");
                            if (mgoogleClient == null) {
                                buildGoogleApiClient();
                            }
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mgoogleMap.setMyLocationEnabled(true);
                            return;
                        }
                    }
                    Log.d("MapActivity", "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }//end onRequestPermission

    protected synchronized void buildGoogleApiClient() {
        mgoogleClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mgoogleClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (googleServicesAvailable()) {
//            Toast.makeText(this, "Map is Good!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_map);

            if (mgoogleClient == null) {
                mgoogleClient = new GoogleApiClient.Builder(getApplicationContext())
                        .addApi(LocationServices.API)
                        .build();
                mgoogleClient.connect();

                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(30 * 1000);
                locationRequest.setFastestInterval(5 * 1000);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

// **************************

                builder.setAlwaysShow(true); // this is the key ingredient

// **************************

                PendingResult result = LocationServices.SettingsApi.checkLocationSettings(mgoogleClient, builder.build());

                result.setResultCallback(new ResultCallback() {
                    @Override
                    public void onResult(@NonNull Result result) {
                        final Status status = result.getStatus();
                        //final LocationSettingsStates state = result.getLocationSettingStates;

                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {

                                    status.startResolutionForResult(MapActivity.this, 1000);

                                } catch (IntentSender.SendIntentException e)

                                {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                break;
                        }
                    }
                });
            }

            mgoogleClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

            listPoints = new ArrayList<>();
            getLocationPermission();
            initMap();
            setupBottomNavigationView();


        }
    }

    //if from business profile
    private void setRoute(final android.location.Location loc) {
        Log.d("MapActivity", "setRoute: " + getDataFromBundle().getBsnBusinessName() +
                " LatLng: " + getDataFromBundle().getBsnLocation() +
                " Contact: " + getDataFromBundle().getBsnMobile() + "" + getDataFromBundle().getBsnPhone());
        final String name = getDataFromBundle().getBsnBusinessName();
        final String location = getDataFromBundle().getBsnLocation();
        final String contact = "" + getDataFromBundle().getBsnMobile() + "" + getDataFromBundle().getBsnPhone();

        Log.d("MapActivity", "setRoute: " + convertToLatLng(location));
        //addMarker(convertToLatLng(location), name, contact);

        mgoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLng destine = convertToLatLng(location);
                LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
                //Save first point select
                listPoints.add(ll);
                //Save first point select
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(ll);

                if (listPoints.size() == 1) {
                }
                listPoints.add(destine);
//               Toast.makeText(this, "Listpoints " + listPoints.add(destine), Toast.LENGTH_SHORT).show();

                if (listPoints.size() == 2) {
                    addMarker(convertToLatLng(location), name, contact);
                    //Create the URL to get request from first marker to second marker
                    String url1 = getRequestUrl(listPoints.get(0), listPoints.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url1);
                }
            }
        });

    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                mgoogleMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private Junkshop getDataFromBundle() {
        Log.d("MapActivity", "getDataFromBundle: arguments: ");
        Junkshop bundle = (Junkshop) getIntent().getParcelableExtra("BusinessDetail");

        if (bundle != null) {
            return bundle;
        } else {
            return null;
        }
    }

    //get permission Internet, GPs and etc...
    private void getLocationPermission() {
        Log.d("MapActivity", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void readDatabase() {

        init();
        Query query = databaseReference.child("Users")
                .orderByKey();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    mUsers.add(ds.getKey());

                }
                getBusinessUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void init() {
        mUsers = new ArrayList<>();
        businessUser = new ArrayList<>();
    }

    //if bottom navigation click
    private void getBusinessUser() {
//        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//        intent.putExtra("enabled", true);
//        sendBroadcast(intent);

        for (int i = 0; i < mUsers.size(); i++) {
            Query q = databaseReference.child("Users")
                    .child(mUsers.get(i))
                    .child("userType");
            final int o = i;
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue().equals("business")) {
                        businessUser.add(mUsers.get(o));


                        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(mUsers.get(o));
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Name = dataSnapshot.child("bsnBusinessName").getValue().toString();
                                Location = dataSnapshot.child("bsnLocation").getValue().toString();
//                                String Phone = dataSnapshot.child("bsnPhone").getValue().toString();
                                String Mobile = dataSnapshot.child("bsnMobile").getValue().toString();
                                Contact = " " + Location ;

                                addMarker(convertToLatLng(Location), Name, Contact);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    //add markers with name and contact
    private void addMarker(LatLng latLng, String name, String contact) {
        //putting marker
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(name)
                .snippet(contact);
        mgoogleMap.addMarker(options);
        //moving camera to marker
        CameraPosition newCamPos = new CameraPosition(latLng,
                10f,
                mgoogleMap.getCameraPosition().tilt, //use old tilt
                mgoogleMap.getCameraPosition().bearing); //use old bearing
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos));
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't Connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override//first load map
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
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
        mgoogleMap.setMyLocationEnabled(true);
        mgoogleMap.getUiSettings().setZoomControlsEnabled(true);
//        final Task loc = mFusedLocationProviderClient.getLastLocation();
//        if(loc==null) {
//            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//            intent.putExtra("enabled", true);
//            sendBroadcast(intent);
//        }else{
//            getDeviceLocation();
//        }
        //google map design
        try {
            latlong = new ArrayList<>();
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.d("MapActivity", "onMapReady: Error loading mapstyle");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapActivity", "Can't find style. Error: ", e);
        }

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //if from profile or from bottomnav
            Intent i = getIntent();
            String data = i.getStringExtra("CallFrom");
            switch (data) {
                case "fromprofile":


//                    final Task loc = mFusedLocationProviderClient.getLastLocation();
//                    if(loc==null) {
//                        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//                        intent.putExtra("enabled", true);
//                        sendBroadcast(intent);
//                    }else{
//                    getLocation();
//                    getDeviceLocation();
                    getLocation();

//                    setRoute(getLocation());
//                    }

                    break;
                case "frombottomnav":
//                    Toast.makeText(this, "From Buttom Nav: " + data, Toast.LENGTH_SHORT).show();
                    getDeviceLocation();
                    mgoogleMap.setMyLocationEnabled(true);
                    readDatabase();
                    break;
            }


        }
    }

    private Location getLocation() {

//        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//        } else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
//
//            if (location != null) {
//                double latti = location1.getLatitude();
//                double longi = location1.getLongitude();
//                lattitude = String.valueOf(latti);
//                longitude = String.valueOf(longi);
//            }else
//            {
//                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();
//            }
//            return location1;
//        }
//        return null;
        Location adrs = null;
        final android.location.Location currentLocation = null;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        final Lokasyon lokasyon = new Lokasyon();
        try{

            if (!mLocationPermissionsGranted) {
                getLocationPermission();
                getLocation();
            } else {
                final Task loc = mFusedLocationProviderClient.getLastLocation();

                loc.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            if(loc!=null){
//                                currentLocation = (Location) loc.getResult();
//                                lokasyon.setLoc((Location) loc.getResult());
                                myloc.add((Location) loc.getResult());
                                lokasyon.setLoc(myloc.get(0));
                                setRoute(myloc.get(0));
                                mgoogleMap.setMyLocationEnabled(true);
                                Log.d(TAG, "onCompleteloc: "+ myloc.get(0));
                            }else {
                                Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                                intent.putExtra("enabled", true);
                                sendBroadcast(intent);
                                getLocation();
                            }
                        }
                    }
                });

            }
        }catch (SecurityException e){
            Log.d(TAG, "getLocation: " + e.getMessage());
        }
//        Location adrs =lokasyon.getLoc();
//        for(int i=0; i<myloc.size(); i++){
//             adrs = myloc.get(0);
//        }

        Log.d(TAG, "getLocationloc: " + lokasyon.getLoc());
        return lokasyon.getLoc();
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(!mLocationPermissionsGranted){
                getLocationPermission();
                getDeviceLocation();
            }
            else{
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            if(location != null)
                            {
                                Log.d(TAG, "onComplete: found location!");
                                Location currentLocation = (Location) location.getResult();
                               // Log.d(TAG, "onCompleteloc: "+ currentLocation);
                                if (currentLocation != null) {
                                    mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 12));
                                }else
                                {
                                    Toast.makeText(MapActivity.this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
                                intent.putExtra("enabled", true);
                                sendBroadcast(intent);
                                getDeviceLocation();
                            }
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }

    }
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity2.class));
    }
    //convert string to (latitude, longitude)
    public LatLng convertToLatLng(String Location)
    {
        double lat = 0.00,lng = 0.00;

        Geocoder gc = new Geocoder(this);
        List<Address> list = null;
        try {
            list = gc.getFromLocationName(Location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            lat = address.getLatitude();
            lng = address.getLongitude();
        }

        return new LatLng(lat,lng);
    }

    LocationRequest mlocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = LocationRequest.create();
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleClient, mlocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Can't get current location!", Toast.LENGTH_LONG).show();
        }else{
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15f);
            mgoogleMap.animateCamera(update);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*
    * For Bottom Navigation
    */
    private void setupBottomNavigationView()
    {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, MapActivity.this ,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}