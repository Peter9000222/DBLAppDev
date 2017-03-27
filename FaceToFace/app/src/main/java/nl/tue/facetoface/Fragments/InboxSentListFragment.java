package nl.tue.facetoface.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nl.tue.facetoface.R;


public class InboxSentListFragment extends Fragment {

    RecyclerView InboxReceived_recyc;
    RecyclerView.Adapter InboxReceived_adap;
    RecyclerView.LayoutManager InboxReceived_manager;

    ArrayList<String> inboxSent = new ArrayList<>();

    public InboxSentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_sent_list, container, false);
    }


}
