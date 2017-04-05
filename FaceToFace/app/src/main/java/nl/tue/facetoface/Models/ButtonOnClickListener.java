package nl.tue.facetoface.Models;

import android.view.View;

import nl.tue.facetoface.Activities.InboxActivity;
import nl.tue.facetoface.Fragments.InboxReceivedListFragment;
import nl.tue.facetoface.Fragments.InboxSentListFragment;

/**
 * Created by s149453 on 5-4-2017.
 */

public class ButtonOnClickListener implements View.OnClickListener {
        int position;
        String tag;
        InboxSentListFragment fragmentSent;
        InboxReceivedListFragment fragmentReceived;
        public ButtonOnClickListener(int position, String tag) {
            this.position = position;
            this.tag = tag;
        }
        @Override
        public void onClick(View v)
        {
            if (tag.matches("Cancel")){
                InboxActivity.cancelSentRequest(position);
                fragmentSent = InboxActivity.getSentFragment();
                fragmentSent.notifyAdapter();
            } else if (tag.matches("Accept")){
                InboxActivity.acceptRequest(position);
                fragmentReceived = InboxActivity.getReceivedFragment();
                fragmentReceived.notifyAdapter();
            }   else {
                InboxActivity.declineRequest(position);
                fragmentReceived = InboxActivity.getReceivedFragment();
                fragmentReceived.notifyAdapter();
            }

        }

}
