package nl.tue.facetoface.Models;

import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
 */

public class NearbyUser extends UserData {

    private Boolean onlineStatus;

    public NearbyUser(int nID, String nTopic, ArrayList<String> nInterests) {
        userID = nID;
        topic = nTopic;
        interests = nInterests;
        onlineStatus = true;
    }

}