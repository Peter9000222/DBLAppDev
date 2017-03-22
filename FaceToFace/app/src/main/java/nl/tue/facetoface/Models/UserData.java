package nl.tue.facetoface.Models;

import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
 */

// TODO: implement location attribute

public class UserData {

    protected int userID;
    protected String topic;
    ArrayList<String> interests;

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

}
