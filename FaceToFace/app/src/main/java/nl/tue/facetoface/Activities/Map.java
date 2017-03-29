package nl.tue.facetoface.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import nl.tue.facetoface.Models.ThisUser;
import nl.tue.facetoface.R;

public class Map extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks,
        OnConnectionFailedListener {

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
    DatabaseReference userData = mRootRef.child("userData");

    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;

    String topic;
    ArrayList<String> interests = new ArrayList<>();
    String userID;

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

        // Move to location of the user when button clicked
        FloatingActionButton centerMap = (FloatingActionButton) findViewById(R.id.fab);
        centerMap.setImageResource(R.mipmap.ic_my_location_white_48dp);
        centerMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastLocation != null) {
                    LatLng locationUser = new LatLng(mLatitude, mLongitude);
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationUser));
                } else {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "No location known", Toast.LENGTH_LONG).show();
                }
            }
        });

        // make user and set the right values which are gotten from topic
        thisUser = new ThisUser();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
            topic = extras.getString("topic");
            interests = extras.getStringArrayList("interests");
        } else { topic = ""; }
        thisUser.setUserID(userID);
        thisUser.setTopic(topic);
        thisUser.setInterests(interests);

        // putt all info of the user on the database
        Button buttonListOfTopics = (Button) findViewById(R.id.buttonListOfTopics);
        buttonListOfTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userData = mRootRef.child("Users").child(thisUser.getUserID());
                userData.setValue("");
                userData.child("Topic").setValue(thisUser.getTopic());
                userData.child("Interests").setValue(thisUser.getInterests());
                userData.child("Lat").setValue(mLatitude);
                userData.child("Lng").setValue(mLongitude);
            }
        });

        // google thingy
        buildGoogleApiClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //This callback is triggered when the map is ready to be used.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.inboxIcon:
                Intent inboxIntent = new Intent(this, InboxActivity.class);
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

    // Build Google API client
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Android activity life cycle method
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        /*
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

    }

    // Android activity life cycle method
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();
                LatLng loc = new LatLng(mLatitude, mLongitude);

                //Set location when the Map Activity is visited
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                mUser = mMap.addMarker(new MarkerOptions().position(loc).title("You are here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

                LatLng locationUser = new LatLng(mLatitude, mLongitude);

                // Update user model location
                //thisUser.setLocation(locationUser);
            } else {
                Toast.makeText(this, "No_location_detected", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission check for fusedLocationAPI
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                        mLatitude = mLastLocation.getLatitude();
                        mLongitude = mLastLocation.getLongitude();
                        LatLng loc = new LatLng(mLatitude, mLongitude);

                        //Set location when the Map Activity is visited
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        mUser = mMap.addMarker(new MarkerOptions().position(loc).title("You are here"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

                        LatLng locationUser = new LatLng(mLatitude, mLongitude);

                        // Update user model location
                        thisUser.setLocation(locationUser);

                } else {
                    // permission denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
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
}
