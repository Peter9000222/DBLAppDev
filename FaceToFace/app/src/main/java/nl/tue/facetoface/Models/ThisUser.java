package nl.tue.facetoface.Models;

import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
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

}