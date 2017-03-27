package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
 */

public class UserData {

    protected String userID;
    protected String topic;
    protected ArrayList<String> interests;
    protected LatLng location;

    public String getUserID () {
        return userID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public LatLng getLocation() { return location; }

}
