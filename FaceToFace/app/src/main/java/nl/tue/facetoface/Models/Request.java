package nl.tue.facetoface.Models;


//Created to model requests as according to the class diagram, left unused in the implementation.

public class Request {

    public int senderID;
    public int receiverId;
    public String timeStamp;
    public boolean status;

    public void setSenderID(int ID) {
        this.senderID = ID;
    }
}