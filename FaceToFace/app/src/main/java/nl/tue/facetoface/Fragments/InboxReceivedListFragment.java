package nl.tue.facetoface.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import nl.tue.facetoface.Activities.InboxActivity;
import nl.tue.facetoface.Adapters.InboxReceivedAdapter;
import nl.tue.facetoface.R;


public class InboxReceivedListFragment extends Fragment {

    RecyclerView InboxReceived_recyc;
    RecyclerView.Adapter InboxReceived_adap;
    RecyclerView.LayoutManager InboxReceived_manager;

    ArrayList<String> topic = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> distance = new ArrayList<>();

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

        topic = ((InboxActivity)getActivity()).getTopic("Received");
        time = ((InboxActivity)getActivity()).getTime("Received");
        distance = ((InboxActivity)getActivity()).getDistance("Received");

        InboxReceived_recyc = (RecyclerView) getView().findViewById(
                R.id.inbox_received_recycler_view);
        InboxReceived_manager = new LinearLayoutManager(getContext());
        InboxReceived_recyc.setLayoutManager(InboxReceived_manager);
        InboxReceived_adap = new InboxReceivedAdapter(getContext(), topic, time, distance);
        InboxReceived_recyc.setAdapter(InboxReceived_adap);

        InboxReceived_recyc.addOnItemTouchListener(new InboxActivity.RecyclerTouchListener(
                getActivity(),
                InboxReceived_recyc, new InboxActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                ((InboxActivity)getActivity()).onItemClick("Received", position);
                Toast.makeText(getActivity(), "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void notifyAdapter(){
        InboxReceived_adap.notifyDataSetChanged();
    }



}
