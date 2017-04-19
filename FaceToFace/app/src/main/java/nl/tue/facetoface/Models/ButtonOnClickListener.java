package nl.tue.facetoface.Models;

import android.view.View;

import nl.tue.facetoface.Activities.InboxActivity;
import nl.tue.facetoface.Activities.Map;
import nl.tue.facetoface.Fragments.InboxReceivedListFragment;
import nl.tue.facetoface.Fragments.InboxSentListFragment;
import nl.tue.facetoface.Fragments.UserMarkerBottomSheet;

/**
 * Created by s149453 on 5-4-2017.
 */

public class ButtonOnClickListener implements View.OnClickListener {
        int position;
        String idBottomSheet;
        String tag;
        InboxSentListFragment fragmentSent;
        InboxReceivedListFragment fragmentReceived;

        //instance of the Map activity
        Map x;
        UserMarkerBottomSheet userBottomSheetListener;

        public ButtonOnClickListener(int position, String idBottomSheet, String tag) {
            this.position = position;
            this.idBottomSheet = idBottomSheet;
            this.tag = tag;
        }


        //Events triggered by buttons from bottomsheets
        @Override
        public void onClick(View v)
        {
            if (tag.matches("Cancel")){
                InboxActivity.cancelSentRequest(position);
                x.getMapInstance().cancelRequest(idBottomSheet, position);
                fragmentSent = InboxActivity.getSentFragment();
                fragmentSent.notifyAdapter();
            } else if (tag.matches("Accept")){
                InboxActivity.acceptRequest(position);
                x.getMapInstance().sendResponse(idBottomSheet, true, position);
                fragmentReceived = InboxActivity.getReceivedFragment();
                fragmentReceived.notifyAdapter();
            } else if (tag.matches("Send Request")){
                /*String idBottomSheet = userBottomSheetListener.getIdSheet();*/
                //TODO - send request to database
                x.getMapInstance().sendRequest(idBottomSheet);

            } else if (tag.matches("Decline")){
                InboxActivity.declineRequest(position);
                x.getMapInstance().sendResponse(idBottomSheet, false, position);
                fragmentReceived = InboxActivity.getReceivedFragment();
                fragmentReceived.notifyAdapter();
            }

        }

}
