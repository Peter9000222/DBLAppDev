package nl.tue.facetoface.Models;

import java.util.ArrayList;

/**
 * Created by s149453 on 20-3-2017.
 */

public class Contact extends UserData {

    private Boolean onlineStatus;

    public Contact(int nID, String nTopic, ArrayList<String> nInterests) {
        userID = nID;
        topic = nTopic;
        interests = nInterests;
        onlineStatus = true;
    }

}