package nl.tue.facetoface.Models;

import java.util.ArrayList;

/**
 * This class models the current user
 */

public class ThisUser extends UserData {

    public ThisUser() {
        userID = "";
        topic = "";
        interests = new ArrayList<>();
        location = null;
        marker = null;
    }

    public void setUserID(String nID) {
        userID = nID;
    }

    public void setTopic(String nTopic){
        topic = nTopic;
    }

}