package nl.tue.facetoface.Models;

/**
 * Created by s149453 on 20-3-2017.
 */

public class Contact {
    private String mName;

    public Contact(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name){
        mName = name;
    }
}