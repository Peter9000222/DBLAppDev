package nl.tue.facetoface.Models;

import java.util.ArrayList;

/**
 * Created by piano on 22-Mar-17.
 */

public class ThisUser extends UserData {

    public ThisUser(int nID, String nTopic, ArrayList<String> nInterests) {
        userID = nID;
        topic = nTopic;
        interests = nInterests;
    }

    public void setTopic(String nTopic){
        topic = nTopic;
    }

    public void addInterest(String interest) {
        interests.add(interest);
    }

    public void deleteInterest(String interest) {
        for (int i = 0; i < interests.size(); i++) {
            if (interests.get(i).equals(interest)) {
                interests.remove(i);
                break;
            }
        }
    }

}