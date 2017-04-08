package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
 */

public class NearbyUser extends UserData implements Serializable {

    protected Boolean onlineStatus;

    public NearbyUser(String nID, String nTopic, ArrayList<String> nInterests, LatLng nLocation) {
        userID = nID;
        topic = nTopic;
        interests = nInterests;
        location = nLocation;
        marker = null;
        onlineStatus = true;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }
}