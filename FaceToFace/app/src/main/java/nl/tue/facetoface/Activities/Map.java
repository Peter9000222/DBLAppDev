package nl.tue.facetoface.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import nl.tue.facetoface.Fragments.UserMarkerBottomSheet;
import nl.tue.facetoface.Models.NearbyUser;
import nl.tue.facetoface.Models.ThisUser;
import nl.tue.facetoface.Models.UserData;
import nl.tue.facetoface.R;

import static android.R.attr.key;

public class Map extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private Toolbar tb;

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected Double mLatitude;
    protected Double mLongitude;
    protected Marker mUser;
    public ThisUser thisUser;
    public Boolean hasID;

    // Create database
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userData = mRootRef.child("Users");
    DatabaseReference Users = mRootRef.child("Users");

    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;

    String topic;
    ArrayList<String> interests = new ArrayList<>();
    String userID;

    LatLng locationUser;
    LocationManager mlocManager;
    LocationRequest mLocationRequest;

    HashMap<String, NearbyUser> mapOfNearbyUsers = new HashMap<String, NearbyUser>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create action bar with icon
        tb = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Map");
        }
        tb.inflateMenu(R.menu.menu);

        // Move to location of the user when own location button clicked
        FloatingActionButton centerMap = (FloatingActionButton) findViewById(R.id.fab);
        centerMap.setImageResource(R.mipmap.ic_my_location_white_48dp);
        centerMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
            }
        });

        // Set user data
        setUser();

        Button buttonListOfTopics = (Button) findViewById(R.id.buttonListOfTopics);
        buttonListOfTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show list
            }
        });

        // build and load google maps
        buildGoogleApiClient();
        mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        createLocationRequest();

        // putt all info of the user on the database
        setUserToDatabase();
    }

    // Begin location update
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(7000);
        mLocationRequest.setFastestInterval(4000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest);

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateLocation();
        setUserLocationToDatabase();
    }
    // End location update

    // make user and set the right values which are gotten from topic
    private void setUser() {
        thisUser = new ThisUser();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
            topic = extras.getString("topic");
            interests = extras.getStringArrayList("interests");
        } else {
            topic = "";
        }
        thisUser.setUserID(userID);
        thisUser.setTopic(topic);
        thisUser.setInterests(interests);
    }

    private void setUserToDatabase() {

        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("Topic", thisUser.getTopic());
        hashMapUser.put("Interests", thisUser.getInterests());
        hashMapUser.put("Lat", mLatitude);
        hashMapUser.put("Lng", mLongitude);

        userData.child(thisUser.getUserID()).setValue(hashMapUser);

      /*  userData = mRootRef.child("Users").child(thisUser.getUserID());
        userData.setValue(thisUser.getUserID());

        //userData.child("Lat").setValue(mLatitude);
        //userData.child("Lng").setValue(mLongitude);

        userData.child("Topic").setValue(thisUser.getTopic());
        userData.child("Interests").setValue(thisUser.getInterests());
       // setUserLocationToDatabase();*/

    }

    private void setUserLocationToDatabase() {
        userData = mRootRef.child("Users").child(thisUser.getUserID());
        userData.child("Lat").setValue(mLatitude);
        userData.child("Lng").setValue(mLongitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.inboxIcon:
                Intent inboxIntent = new Intent(this, InboxActivity.class);
                inboxIntent.putExtra("exUserID", thisUser.getUserID());
                hasID = false;
                inboxIntent.putExtra("hasID", hasID);
                inboxIntent.putExtra("userTopic", thisUser.getTopic());
                inboxIntent.putExtra("userInterestList", thisUser.getInterests());
                startActivity(inboxIntent);
                break;
            case R.id.topicIcon:
                Intent topicIntent = new Intent(this, TopicActivity.class);
                topicIntent.putExtra("exUserID", thisUser.getUserID());
                hasID = false;
                topicIntent.putExtra("hasID", hasID);
                topicIntent.putExtra("userTopic", thisUser.getTopic());
                topicIntent.putExtra("userInterestList", thisUser.getInterests());
                startActivity(topicIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Begin Android activity life cycle methods
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        Users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked")
                HashMap<String, HashMap<String, Object>> map = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                for (String key : map.keySet()) {
                    String dTopic = (String) map.get(key).get("Topic");
                    ArrayList<String> dInterests = (ArrayList<String>) map.get(key).get("Interests");
                    Double dLat = (Double) map.get(key).get("Lat");
                    Double dLng = (Double) map.get(key).get("Lng");
                    if (dLat == null || dLng == null) {
                        dLat = 0.0;
                        dLng = 0.0;
                    }

                   /* if (inProximity(mLatitude, mLongitude,dLat, dLng))
                    {
                    LatLng latLng = new LatLng(dLat, dLng);
                    NearbyUser nearbyUser = new NearbyUser(key, dTopic, dInterests, latLng);
                    mapOfNearbyUsers.put(key, nearbyUser);
                    }*/
                    // Still need to test whether this function still triggers when a new user is registered or changes data

                    // Print test to check correctness of data base retrieval and adding to local map
                    //System.out.println("wtff" + key + " " + dTopic + " " + dInterests + " " + latLng + " " + dLat + " " + dLng);
                }

                // Print test to check content of mapOfNearbyUsers after retrieving data
                for (String key: mapOfNearbyUsers.keySet()) {
                    System.out.println("wtffFinalTest" + mapOfNearbyUsers.get(key).getUserID() + mapOfNearbyUsers.get(key).getTopic() + mapOfNearbyUsers.get(key).getInterests() + mapOfNearbyUsers.get(key).getLocation());
                }

                // OLD CODE
                /*HashMap<String, Object> newUser = map.get("6731cc1c-9f3b-4644-b98a-4f0b1541173c");
                String dTopic = (String) newUser.get("Topic");
                Double dLat = (Double) newUser.get("Lat");
                Double dLng = (Double) newUser.get("Lng");
                ArrayList<String> dInterests = (ArrayList<String>) newUser.get("Interests");
                System.out.println("wtf" + newUser);
                System.out.println("wtf" + dTopic + dLat + dLng + dInterests); */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Delete user from database when app is closed
        Users.child(thisUser.getUserID()).removeValue();
    }
    // End Android activity life cycle methods

    // Set the initial location of the user
    private void setLocation() {
        //mMap.clear();
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                updateLocation();
            } else {
                Toast.makeText(this, "No location detected", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Enable location/GPS", Toast.LENGTH_LONG).show();
        }
    }

    // Update the location on the map
    private void updateLocation() {
        mLatitude = mLastLocation.getLatitude();
        mLongitude = mLastLocation.getLongitude();
        locationUser = new LatLng(mLatitude, mLongitude);

        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mUser = mMap.addMarker(new MarkerOptions().position(locationUser).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationUser));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        UserMarkerBottomSheet markerSheet = new UserMarkerBottomSheet();
        markerSheet.show(getSupportFragmentManager(), "test");
        return true;
    }


    // Build Google API client
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //This callback is triggered when the map is ready to be used.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // Google API client connection callback
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ask for permission dialog
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            setLocation();
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setLocation();
            return;
        }
    }

    // Google API client connection callback
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
    }

    // Google API client connection callback
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        //Log.i(TAG, "Connection suspended");
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_LONG).show();
        mGoogleApiClient.connect();
    }

    public boolean inProximity(double user1Lat, double user1Lng, double user2Lat, double user2Lng) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(user2Lat - user1Lat);
        Double lonDistance = Math.toRadians(user2Lng - user1Lng);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(user1Lat)) * Math.cos(Math.toRadians(user2Lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance <= 1000;
    }

    // Adds markers on the map
    public void addMarkersMap(HashMap<String, HashMap<String, Object>> userDataMarkerCollection){

        for(String key: userDataMarkerCollection.keySet()){
            HashMap<String, Object> userDataMarker = userDataMarkerCollection.get(key);
            //NOT CORRECT LOCATION: THIS LOCATION SHOULD BE RETRIEVED FROM userDataMarker object
            LatLng locOtherUser = new LatLng(mLatitude+100, mLongitude+100);
            //add marker on map with color blue
            Marker markerOtherUser = mMap.addMarker(new MarkerOptions().position(locOtherUser)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            //adds the ID of userDataMarker as identifier, when click on Marker use getTag() and
            //use it to get object in UserDataMarkerCollection which could be a global HashMap
            markerOtherUser.setTag(key);
        }
    }

}
