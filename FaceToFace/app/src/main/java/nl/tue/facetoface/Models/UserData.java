package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class models the users
 */

public class UserData {

    protected String userID = UUID.randomUUID().toString();
    protected String topic;
    protected ArrayList<String> interests;
    protected LatLng location;
    protected Marker marker;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String nID) {
        userID = nID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String nTopic) {
        topic = nTopic;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public LatLng getLocation() { return location; }

    public Marker getMarker() {
        return marker;
    }

    public void setLocation(LatLng nLocation) { location = nLocation; }

    public void setMarker(Marker nMarker) {
        marker = nMarker;
    }

}
