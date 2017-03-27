package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by piano on 22-Mar-17.
 */

public class UserData {

    protected String userID = UUID.randomUUID().toString();
    protected String topic;
    protected ArrayList<String> interests;
    protected LatLng location;

    public String getUserID () {
        return userID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String ntopic) {
        topic = ntopic;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public LatLng getLocation() { return location; }

}
