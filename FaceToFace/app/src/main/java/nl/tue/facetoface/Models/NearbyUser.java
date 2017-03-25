package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
 */

public class NearbyUser extends UserData {

    protected Boolean onlineStatus;

    public NearbyUser(int nID, String nTopic, ArrayList<String> nInterests, LatLng nLocation) {
        userID = nID;
        topic = nTopic;
        interests = nInterests;
        location = nLocation;
        onlineStatus = true;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }
}