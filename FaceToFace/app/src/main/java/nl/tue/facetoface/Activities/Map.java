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
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import nl.tue.facetoface.Fragments.UserMarkerBottomSheet;
import nl.tue.facetoface.Models.NearbyUser;
import nl.tue.facetoface.Models.ThisUser;
import nl.tue.facetoface.R;

import static nl.tue.facetoface.R.id.cancelButton;
import static nl.tue.facetoface.R.id.map;

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
    private UserMarkerBottomSheet markerSheet;
    static Map mapInstance;
    public TextView textViewTopicMeeting;
    public Button cancelMeetingButton;

    /*
    ending with S: related to sent requests
    ending with R: related to received requests
     */
    private static ArrayList<ArrayList<String>> interestListSent;
    private static ArrayList<ArrayList<String>> interestListRequest;
    private static HashMap<String, ArrayList<String>> hashmapListRequest;
    private static ArrayList<String> topicListS;
    private static ArrayList<String> topicListR;
    private static ArrayList<String> timeListS;
    private static ArrayList<String> timeListR;
    private static ArrayList<String> distanceListS;
    private static ArrayList<String> distanceListR;
    private static ArrayList<String> interestListR;
    private static ArrayList<String> idListR;
    private static ArrayList<String> idListS;

    // Keep track of users who have sent thisUser a request
    ArrayList<String> requesterIDs = new ArrayList<String>();

    // Create database
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    // Users reference in database
    DatabaseReference userData = mRootRef.child("Users");
    DatabaseReference Users = mRootRef.child("Users");
    DatabaseReference DestroyUser = mRootRef.child("Users");

    // Requests reference in database
    DatabaseReference requestData = mRootRef.child("Requests");
    DatabaseReference Requests = mRootRef.child("Requests");
    DatabaseReference DestroyUserRequest = mRootRef.child("Requests");

    // Location handlers
    LocationManager mlocManager;
    LocationRequest mLocationRequest;
    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;

    // thisUser data variables
    String topic;
    ArrayList<String> interests = new ArrayList<>();
    String userID;
    LatLng locationUser;

    // ID of user matched with
    String matchID;

    // HashMap to store instances of NearbyUsers
    HashMap<String, NearbyUser> mapOfNearbyUsers = new HashMap<String, NearbyUser>();

    // OnCreate lifecycle method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Will be used when request button is clicked
        mapInstance = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain SupportMapFragment and get notified when map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        // Create action bar with icon
        tb = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Map");
        }
        tb.inflateMenu(R.menu.menu);

        // Move camera map view to user location when location button is clicked
        FloatingActionButton centerMap = (FloatingActionButton) findViewById(R.id.fab);
        centerMap.setImageResource(R.mipmap.ic_my_location_white_48dp);
        centerMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.remove();
                setLocation();
            }
        });

        // stop the map activity to be sure the user is deleted when closing the app
        Button buttonStopSearch = (Button) findViewById(R.id.stopButton);
        buttonStopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish the map activity call to ondestroy
                finish();
            }
        });

        // set cancel meeting button
        cancelMeetingButton = (Button) findViewById(cancelButton);
        cancelMeetingButton.setText("");
        cancelMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchID != null) {
                    //cancel the meeting if button is pressed
                    cancelMeeting(matchID);
                }
            }
        });

        textViewTopicMeeting = (TextView) findViewById(R.id.textViewTopicMeeting);
        textViewTopicMeeting.setVisibility(View.INVISIBLE);

        // Set user data (ID, topic, interest(s))
        setUser();

        // Build and load Google Maps
        buildGoogleApiClient();
        mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        createLocationRequest();

        // Send user data to database
        setUserToDatabase();

        // get the hasmap with all the nearby users back from topic only when topic is changed
        Bundle extras = getIntent().getExtras();
        if (extras.getSerializable("userHashMap") != null) {
            mapOfNearbyUsers = (HashMap<String, NearbyUser>) extras.getSerializable("userHashMap");
        }

        // Set new child in database for this user to receive requests
        openUserInboxDatabase();

        interestListSent = new ArrayList<>(); //each position has an list with interests of a user
        interestListRequest = new ArrayList<>();
        hashmapListRequest = new HashMap<>();
        topicListS = new ArrayList<>(); //each position has a topic of a user
        timeListS = new ArrayList<>(); //each position has a time of a user
        distanceListS = new ArrayList<>(); //each position has a distance of a user
        topicListR = new ArrayList<>();
        timeListR = new ArrayList<>();
        distanceListR = new ArrayList<>();
        idListR = new ArrayList<>();
        idListS = new ArrayList<>();
    }

    public static Map getMapInstance(){
        return mapInstance;
    }

    /*
     * Begin location update
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(7000);
        mLocationRequest.setFastestInterval(4000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest);

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mUser != null) {
            mUser.remove();
        }
        updateLocation(false);
        setUserLocationToDatabase();
    }
    /*
     * End location update
     */

    // Set user data, retrieved from TopicActivity
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
        thisUser.setLocation(locationUser);
    }

    // Set user data in HashMap and upload this HashMap to database
    private void setUserToDatabase() {
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("Topic", thisUser.getTopic());
        hashMapUser.put("Interests", thisUser.getInterests());
        hashMapUser.put("Lat", mLatitude);
        hashMapUser.put("Lng", mLongitude);

        userData.child(thisUser.getUserID()).setValue(hashMapUser);
    }

    // Set location data of this user to database
    private void setUserLocationToDatabase() {
        userData = mRootRef.child("Users").child(thisUser.getUserID());
        userData.child("Lat").setValue(mLatitude);
        userData.child("Lng").setValue(mLongitude);
    }

    // Send request to user with ID {@code receiverID}
    /* "incoming" indicates whether the received/updated request is an incoming or sent request
     * "received" indicates whether the received/updated request has been processed by the receiver
     * "status" indicates the response of the receiver (accept/deny request)
     * "canceled" indicates whether a request has been canceled
     */
    private void setRequestToDatabase(String receiverID) {
        HashMap<String, Object> requestToSend = new HashMap();
        requestToSend.put("incoming", true);
        requestToSend.put("received", false);
        requestToSend.put("timeStamp", (DateFormat.format("hh:mm", new java.util.Date()).toString()));
        requestToSend.put("status", false);
        requestToSend.put("canceled", false);
        requestData.child(receiverID).child(thisUser.getUserID()).setValue(requestToSend);

        // Wait for response from user with ID {@code receiverID}
        HashMap<String, Object> requestWaitForResponse = new HashMap();
        requestWaitForResponse.put("incoming", false);
        requestWaitForResponse.put("received", false);
        requestWaitForResponse.put("timeStamp", (DateFormat.format("hh:mm", new java.util.Date()).toString()));
        requestWaitForResponse.put("status", false);
        requestWaitForResponse.put("canceled", false);
        requestData.child(thisUser.getUserID()).child(receiverID).setValue(requestWaitForResponse);
    }

    // Accept {@code response == true} or deny {@code response == false} received request from
    // user with ID {@code requesterID}
    private void respondToRequestDatabase(String requesterID, Boolean response) {
        HashMap<String, Object> requestResponse = new HashMap();
        requestResponse.put("incoming", false);
        requestResponse.put("received", true);
        requestResponse.put("timeStamp", (DateFormat.format("hh:mm", new java.util.Date()).toString()));
        requestResponse.put("status", response);
        requestResponse.put("canceled", false);
        requestData.child(requesterID).child(thisUser.getUserID()).setValue(requestResponse);
        if (response == true) {
            matchID = requesterID;
            displayMatch(requesterID);
        }
    }

    // Cancel request sent to user with ID {@code receiverID)}
    private void cancelRequestDatabase(String receiverID) {
        requestData.child(receiverID).child(thisUser.getUserID()).removeValue();
    }

    // Cancel meeting with user with ID {@code userID}
    private void cancelMeetingDatabase(String userID) {
        HashMap<String, Object> cancelMeeting = new HashMap();
        cancelMeeting.put("incoming", false);
        cancelMeeting.put("received", true);
        cancelMeeting.put("timeStamp", (DateFormat.format("hh:mm", new java.util.Date()).toString()));
        cancelMeeting.put("status", false);
        cancelMeeting.put("canceled", true);
        requestData.child(userID).child(thisUser.getUserID()).setValue(cancelMeeting);
        matchID = null;
    }

    // Set new child in database for this user to receive requests
    private void openUserInboxDatabase() {
        requestData.child(thisUser.getUserID()).setValue("");
    }

    //Set two clickable symbols to go to topic and inbox activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*
    When clicking on an icon the related activity will be started, data needed by that activity
    will be passed with
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.inboxIcon:
                Intent inboxIntent = new Intent(this, InboxActivity.class);
                inboxIntent.putExtra("exUserID", thisUser.getUserID());
                hasID = false;
                inboxIntent.putExtra("hasID", hasID);
                inboxIntent.putExtra("userTopic", topicListR);
                inboxIntent.putExtra("userID", idListR);
                inboxIntent.putExtra("userDistanceR", distanceListR);
                inboxIntent.putExtra("hashMap", hashmapListRequest);
                inboxIntent.putExtra("userInterestList", interestListRequest);
                inboxIntent.putExtra("userTime", timeListR);
                inboxIntent.putExtra("userTopicS", topicListS);
                inboxIntent.putExtra("userInterestListS", interestListSent);
                inboxIntent.putExtra("userTimeS", timeListS);
                inboxIntent.putExtra("userDistanceS", distanceListS);
                inboxIntent.putExtra("userIDS", idListS);
                startActivity(inboxIntent);
                break;
            case R.id.topicIcon:
                Intent topicIntent = new Intent(this, TopicActivity.class);
                topicIntent.putExtra("exUserID", thisUser.getUserID());
                hasID = false;
                topicIntent.putExtra("hasID", hasID);
                topicIntent.putExtra("userTopic", thisUser.getTopic());
                topicIntent.putExtra("userInterestList", thisUser.getInterests());
                topicIntent.putExtra("userHashMap", mapOfNearbyUsers);
                startActivity(topicIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Android activity life cycle method
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        if (mUser != null) {
            mUser.remove();
        }

        // remove old markers
        for (String key : mapOfNearbyUsers.keySet()) {
            NearbyUser user = mapOfNearbyUsers.get(key);
            if (user.getMarker() != null) {
                removeMarker(key);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        // Add listener for changes in database for new users, user updates and user removals
        Users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Save the new user in map
                @SuppressWarnings("unchecked")
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                String dKey = dataSnapshot.getKey();
                String dTopic = (String) map.get("Topic");
                @SuppressWarnings("unchecked")
                ArrayList<String> dInterests = (ArrayList<String>) map.get("Interests");
                Double dLat = (Double) map.get("Lat");
                Double dLng = (Double) map.get("Lng");
                if (dLat == null || dLng == null) {
                    dLat = 0.0;
                    dLng = 0.0;
                }

                // Save the new user in mapOfNearbyUsers
                LatLng latLng = new LatLng(dLat, dLng);
                NearbyUser nearbyUser = new NearbyUser(dKey, dTopic, dInterests, latLng);
                mapOfNearbyUsers.put(dKey, nearbyUser);

                // Add marker for the new user
                if (!thisUser.getUserID().equals(dKey) && nearbyUser.getMarker() == null) {
                    addMarker(dKey, false);
                }

            }

            //Method called when data of another user is changed
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Save the updated user in a map
                @SuppressWarnings("unchecked")
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                String dKey = dataSnapshot.getKey();
                String dTopic = (String) map.get("Topic");
                @SuppressWarnings("unchecked")
                ArrayList<String> dInterests = (ArrayList<String>) map.get("Interests");
                Double dLat = (Double) map.get("Lat");
                Double dLng = (Double) map.get("Lng");
                if (dLat == null || dLng == null) {
                    dLat = 0.0;
                    dLng = 0.0;
                }

                LatLng latLng = new LatLng(dLat, dLng);
                NearbyUser user = mapOfNearbyUsers.get(dKey);
                if (user != null) {
                    user.setTopic(dTopic);
                    user.setInterests(dInterests);
                    user.setLocation(latLng);
                    // own location not null & not this user
                    if ((mLatitude != null) && (mLongitude != null) && dKey
                            != thisUser.getUserID()) {
                        // check if user in proximity (1km range) of this user
                        if (inProximity(mLatitude, mLongitude, dLat, dLng)) {
                            // if true, update position marker
                            if (user.getMarker() != null) {
                                removeMarker(dKey);
                            }
                            addMarker(dKey, false);
                        } else {
                            // else remove marker
                            if (user.getMarker() != null) {
                                removeMarker(dKey);
                            }
                        }
                    }
                }
            }

            // remove marker when a user is not online anymore
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String dKey = dataSnapshot.getKey();
                if (dKey != null) {
                    removeMarker(dKey);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Add listener for receiving requests
        Requests.child(thisUser.getUserID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Save request information in map
                HashMap<String, Object> request = (HashMap<String, Object>) dataSnapshot.getValue();
                String dKey = dataSnapshot.getKey();

                // Ignore request if that user has already sent an unresponded request
                if (requesterIDs.contains(dKey)) {
                    return;
                }

                // Add requester to list of users who have sent a request
                requesterIDs.add(dKey);

                // Check if request is a received request or a sent request
                Boolean incoming = (Boolean) request.get("incoming");
                if (incoming == false) {
                    return;
                }

                // Process the request
                String time = (String) request.get("timeStamp");

                // Delegate further requests processing to this method
                processNewIncomingRequest(dKey, time);

                // Display toast notification about the new request
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You have received a new request!", Toast.LENGTH_SHORT);
                toast.show();
            }

            // Add listener for responses to sent requests
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // Save request with response in map
                HashMap<String, Object> request = (HashMap<String, Object>)
                        dataSnapshot.getValue();
                String dKey = dataSnapshot.getKey();

                // Check whether the request is a meeting cancellation notifier
                Boolean canceled = (Boolean) request.get("canceled");
                if (canceled == true) {
                    // Delegate cancellation processing to this method
                    processMeetingCanceled(dKey);
                    return;
                }

                // Check whether the request is a sent request (incoming == false) and whether
                // the request has been received by the other user (received == true);
                Boolean incoming = (Boolean) request.get("incoming");
                Boolean received = (Boolean) request.get("received");
                if (incoming == true || received == false ) {
                    return;
                }

                // Process the response
                String time = (String) request.get("timeStamp");
                Boolean status = (Boolean) request.get("status");
                // Delegate response to a sent request to this method
                processResponseToSentRequest(dKey, time, status);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String dKey = dataSnapshot.getKey();
                //Delegate processing of a canceled request to this method
                processRequestCanceled(dKey);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //When Map activity is stopped
    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //When Map activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DestroyUser.child(thisUser.getUserID()).removeValue();
        DestroyUserRequest.child(thisUser.getUserID()).removeValue();
        finish();
        // Called when stop searching button is pressed
    }
    // End Android activity life cycle methods

    // Set initial location of the user
    private void setLocation() {
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                updateLocation(true);
            } else {
                Toast.makeText(this, "No location detected", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Enable location/GPS", Toast.LENGTH_LONG).show();
        }
    }

    // Update the location on the map
    private void updateLocation(boolean firstTimeOrManual) {
        mLatitude = mLastLocation.getLatitude();
        mLongitude = mLastLocation.getLongitude();
        locationUser = new LatLng(mLatitude, mLongitude);
        thisUser.setLocation(locationUser);
        // mUser.remove();
        mUser = mMap.addMarker(new MarkerOptions().position(locationUser)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mUser.setTag("mydevice");
        if (firstTimeOrManual)
        {
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationUser));
        }
        mMap.setOnMarkerClickListener(this);
    }

    // Display BottomSheet when clicking on a marker
    @Override
    public boolean onMarkerClick(Marker marker) {
        String idMarker = (String) marker.getTag();

        //marker is clicked that is your own marker
        if(idMarker != "mydevice") {
            NearbyUser user = mapOfNearbyUsers.get(idMarker);
            String topicMarker = user.getTopic();
            ArrayList<String> interestsMarker = user.getInterests();

            markerSheet = new UserMarkerBottomSheet();
            markerSheet.setTopic(topicMarker);
            markerSheet.setInterest(interestsMarker);
            markerSheet.setIdSheet(idMarker);
            markerSheet.show(getSupportFragmentManager(), "test");
            markerSheet.setIsOwnMarker(false);
        }
        // If own marker clicked
        else{
            markerSheet = new UserMarkerBottomSheet();
            Bundle extras = getIntent().getExtras();
            markerSheet.setTopic(extras.getString("topic"));
            markerSheet.setInterest(extras.getStringArrayList("interests"));
            markerSheet.setIdSheet(extras.getString("userID"));
            markerSheet.show(getSupportFragmentManager(), "test");
            markerSheet.setIsOwnMarker(true);
        }
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

    // This callback is triggered when the map is ready to be used
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // Google API client connection callback
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ask for permission dialog
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            setLocation();
            startLocationUpdates();
        }
    }

    //Receive results of permission request
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
        // Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " +
        // result.getErrorCode());
        Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
    }

    // Google API client connection callback
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        // Log.i(TAG, "Connection suspended");
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_LONG).show();
        mGoogleApiClient.connect();
    }

    // Calculates whether a new NearbyUser is in the proximity of ThisUser
    public boolean inProximity(double user1Lat, double user1Lng, double user2Lat,
                               double user2Lng) {

        double distance = calculateDistance(user1Lat, user1Lng, user2Lat, user2Lng);
        if (distance <= 1000) {
            return true; // User is within 1 km of this user
        } else {
            return false; // User is not within 1 km range of this user
        }
    }

    // Returns the distance between two gps locations in meters.
    public double calculateDistance(double user1Lat, double user1Lng, double user2Lat,
                                    double user2Lng) {
        final int R = 6371; // Radius of the earth
        // based on the two latitudes
        Double latDistance = Math.toRadians(user2Lat - user1Lat);
        // based on the two longitudes
        Double lonDistance = Math.toRadians(user2Lng - user1Lng);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(user1Lat)) * Math.cos(Math.toRadians(user2Lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // Convert to meters
        return R * c * 1000;
    }

    /*
     * Begin marker handling methods
     */
    // Adds new marker corresponding to user with ID {@code key} to map
    public void addMarker(String key, boolean match) {
        NearbyUser user = mapOfNearbyUsers.get(key);
        LatLng location = user.getLocation();
        double lat = location.latitude;
        double lng = location.longitude;
        float hue = 180;

        //set marker color to orange when there is a match (accepted request)
        if (match || key.equals(matchID)) {
            hue = 30;
        }

        if (user.getMarker() == null) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(hue)));
            // store marker in the NearbyUser user instance to be able to retrieve it later
            user.setMarker(marker);
            marker.setTag(key);
        }
    }

    // Removes the marker of user with ID {@code key} from map
    public void removeMarker(String key) {
        NearbyUser user = mapOfNearbyUsers.get(key);
        if (user != null) {
            Marker marker = user.getMarker();
            if (marker != null) {
                marker.remove();
                user.setMarker(null);
            }
        }
    }

    // Calls addMarker with the second parameter as true to indicate that there's a match
    public void displayMatch(String key) {
        NearbyUser user = mapOfNearbyUsers.get(key);
        if (user != null) {
            removeMarker(key);
            addMarker(key, true);
        }
    }
    /*
     * End marker handling methods
     */

    /*
     * Begin request handling methods
     */
    // Processes request from user with ID {@code nKey}
    public void processNewIncomingRequest(String nKey, String nTimeStamp) {
        NearbyUser requester = mapOfNearbyUsers.get(nKey);
        ArrayList<String> empty = new ArrayList<>();
        topicListR.add(requester.getTopic());
        if (requester.getInterests() == null){
            empty.add("");
            interestListRequest.add(empty);
        }
        else {
            interestListRequest.add(requester.getInterests());
        }

        LatLng locationUser1 = requester.getLocation();
        double user1Lat = locationUser1.latitude;
        double user1Lng = locationUser1.longitude;
        LatLng locationUser2 = thisUser.getLocation();
        double user2Lat = locationUser2.latitude;
        double user2Lng = locationUser2.longitude;
        double distance = calculateDistance(user1Lat, user1Lng, user2Lat, user2Lng);
        distanceListR.add(String.valueOf(Math.round(distance)) + "m");
        timeListR.add(nTimeStamp);
        idListR.add(requester.getUserID());
    }

    // Processes response to a sent request
    public void processResponseToSentRequest(String key, String timeStamp, Boolean status) {
        if (status) {
            // Request to user with ID 'key' was accepted
            displayMatch(key);
            matchID = key;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You have a connection - someone has accepted your request!",
                    Toast.LENGTH_SHORT);
            toast.show();
            textViewTopicMeeting.setText("Topic of Meeting: " +
                    mapOfNearbyUsers.get(key).getTopic().toString());
            textViewTopicMeeting.setVisibility(View.VISIBLE);
            cancelMeetingButton.setText("Cancel Meeting");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "A sent request has been denied.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Method called when the send request button is clicked
    public void sendRequest(String receiverKey) {
        // Process it in database
        setRequestToDatabase(receiverKey);

        NearbyUser requester = mapOfNearbyUsers.get(receiverKey);
        ArrayList<String> empty = new ArrayList<>();
        topicListS.add(requester.getTopic());
        if (requester.getInterests() == null){
            empty.add("");
            interestListSent.add(empty);
        }
        else {
            interestListSent.add(requester.getInterests());
        }
        LatLng locationUser1 = requester.getLocation();
        double user1Lat = locationUser1.latitude;
        double user1Lng = locationUser1.longitude;
        LatLng locationUser2 = thisUser.getLocation();
        double user2Lat = locationUser2.latitude;
        double user2Lng = locationUser2.longitude;
        double distance = calculateDistance(user1Lat, user1Lng, user2Lat, user2Lng);
        distanceListS.add(String.valueOf(Math.round(distance)) + "m");
        timeListS.add((DateFormat.format("hh:mm", new java.util.Date())).toString());
        idListS.add(requester.getUserID());

        Toast toast = Toast.makeText(getApplicationContext(), "The request has been sent.",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    // Method called when a user accepts or denies a received request
    public void sendResponse(String requesterID, boolean response, int position) {
        // Process it in database
        respondToRequestDatabase(requesterID, response);

        requesterIDs.remove(requesterIDs.indexOf(requesterID));
        requestData.child(thisUser.getUserID()).child(requesterID).removeValue();
        topicListR.remove(position);
        interestListRequest.remove(position);
        idListR.remove(position);
        timeListR.remove(position);
        distanceListR.remove(position);

        if (response) {
            Toast toast = Toast.makeText(getApplicationContext(), "The request has been accepted.",
                    Toast.LENGTH_SHORT);
            toast.show();
            matchID = requesterID;
            textViewTopicMeeting.setText("Meeting topic: " + topic);
            textViewTopicMeeting.setVisibility(View.VISIBLE);
            cancelMeetingButton.setText("Cancel meeting");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "The request has been denied.",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Method called when a user clicks on the cancel request button
    public void cancelRequest(String receiverID, int position) {
        // Process it in database
        cancelRequestDatabase(receiverID);

        topicListS.remove(position);
        interestListSent.remove(position);
        idListS.remove(position);
        timeListS.remove(position);
        distanceListS.remove(position);
        Toast toast = Toast.makeText(getApplicationContext(), "The request has been canceled.",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    // Method called when the user clicks the cancel meeting button
    public void cancelMeeting(String userID) {
        // Process it in database
        cancelMeetingDatabase(userID);

        // Update marker to remove indication of a match
        removeMarker(userID);
        addMarker(userID, false);

        Toast toast = Toast.makeText(getApplicationContext(), "The meeting has been canceled.",
                Toast.LENGTH_SHORT);
        toast.show();
        textViewTopicMeeting.setVisibility(View.INVISIBLE);
        cancelMeetingButton.setText("");
        matchID = null;
    }

    // Method called when another user cancels a request sent to this user
    public void processRequestCanceled(String requesterID) {
        // TODO: 21-4-2017 shouldn't this toast be uncommented ?? !!
        // TODO: answer: no. it causes a bug in which this toast is always shown when returning from the topic activity.

        //Toast toast = Toast.makeText(getApplicationContext(), "The request has been canceled.",
        // Toast.LENGTH_SHORT);
        //toast.show();

        //Retrieve the instance of "NearbyUser" for the cancellers ID
        //Then remove it from the list with IDs from requesters
        NearbyUser canceller = mapOfNearbyUsers.get(requesterID);
        requesterIDs.remove(requesterID);

        /*
        The if statements here determine the position of the requests' attributes in each of their
        corresponding lists if the requests' topic is present in the topicListR.
        Not that this position is the same in all lists as they have been set to the same position
        upon creation.
        Besides that it also determines wether a list is non-emtpy before it tries to delete
        something from it.
         */
        if (topicListR.contains(canceller.getTopic())) {
            int i = topicListR.indexOf(canceller.getTopic()); // set position i
            if (!timeListR.isEmpty()) {
                timeListR.remove(i); // removes timestamp of request
            }
            if (!distanceListR.isEmpty()) ; {
                distanceListR.remove(i); // removes distance of request
            }
        }

        topicListR.remove(canceller.getTopic()); // removes topic of request
        interestListRequest.remove(canceller.getInterests()); // removes interests of request
        idListR.remove(requesterID); // removes requester ID of request
    }

    // This method is called when another user cancels an ongoing meeting with this user
    public void processMeetingCanceled(String userID) {
        // Reset matchID
        matchID = null;

        // Remove indication of the match
        removeMarker(userID);
        addMarker(userID, false);

        Toast toast = Toast.makeText(getApplicationContext(), "The meeting has been canceled.",
                Toast.LENGTH_SHORT);
        toast.show();
        textViewTopicMeeting.setVisibility(View.INVISIBLE);
        cancelMeetingButton.setText("");
    }
    /*
     * End request handling methods
     */

}