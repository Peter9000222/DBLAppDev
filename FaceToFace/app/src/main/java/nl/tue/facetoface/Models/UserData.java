package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
 */

public class UserData {

    protected int userID;
    protected String topic;
    protected ArrayList<String> interests;
    protected LatLng location;

    public void setUserID (int nID) {
        userID = nID;
    }

    public int getUserID () {
        return userID;
    }

    public String getTopic() {
        return topic;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public LatLng getLocation() { return location; }

}
