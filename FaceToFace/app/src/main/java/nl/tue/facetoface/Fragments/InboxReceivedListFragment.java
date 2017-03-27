package nl.tue.facetoface.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import nl.tue.facetoface.Activities.InboxActivity;
import nl.tue.facetoface.InterestsAdapter;
import nl.tue.facetoface.Models.InboxReceivedAdapter;
import nl.tue.facetoface.Models.UserData;
import nl.tue.facetoface.R;


public class InboxReceivedListFragment extends Fragment {

    RecyclerView InboxReceived_recyc;
    RecyclerView.Adapter InboxReceived_adap;
    RecyclerView.LayoutManager InboxReceived_manager;

    ArrayList<String> inboxReceived = new ArrayList<>();

    public InboxReceivedListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_received_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inboxReceived.add("hello");
        int i;
        for (i=0; i<30; i+=1){
            inboxReceived.add("olla");

        }

        InboxReceived_recyc = (RecyclerView) getView().findViewById(R.id.inbox_received_recycler_view);
        InboxReceived_manager = new LinearLayoutManager(getContext());
        InboxReceived_recyc.setLayoutManager(InboxReceived_manager);
        InboxReceived_adap = new InboxReceivedAdapter(getContext(), inboxReceived);
        InboxReceived_recyc.setAdapter(InboxReceived_adap);

        InboxReceived_recyc.addOnItemTouchListener(new InboxActivity.RecyclerTouchListener(getActivity(),
                InboxReceived_recyc, new InboxActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                ((InboxActivity)getActivity()).onItemClick(position, "Received");
                Toast.makeText(getActivity(), "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();
            }
        }));
    }




}
