package nl.tue.facetoface.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by piano on 22-Mar-17.
 */

public class ThisUser extends UserData {

    public ThisUser() {
        userID = "";//
        topic = "ik ben harry";
        interests = new ArrayList<>();
        location = null;
    }

    public void setUserID(String nID) {
        userID = nID;
    }

    public void setTopic(String nTopic){
        topic = nTopic;
    }

    public void addInterest(String nInterest) {
        interests.add(nInterest);
    }

    public void deleteInterest(String nInterest) {
        for (int i = 0; i < interests.size(); i++) {
            if (interests.get(i).equals(nInterest)) {
                interests.remove(i);
                break;
            }
        }
    }

    public void setLocation(LatLng nLocation) { location = nLocation; }
}