package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class models a nearbyUser (users who are not thisUser)
 */

public class NearbyUser extends UserData implements Serializable {

    public NearbyUser(String nID, String nTopic, ArrayList<String> nInterests, LatLng nLocation) {
        userID = nID;
        topic = nTopic;
        interests = nInterests;
        location = nLocation;
        marker = null;
    }

}