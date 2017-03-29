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
import nl.tue.facetoface.Adapters.InboxSendAdapter;
import nl.tue.facetoface.R;


public class InboxSentListFragment extends Fragment {

    RecyclerView InboxSend_recyc;
    RecyclerView.Adapter InboxSend_adap;
    RecyclerView.LayoutManager InboxSend_manager;

    ArrayList<String> inboxSend = new ArrayList<>();

    public InboxSentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_sent_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inboxSend.add("hello");
        int i;
        for (i=0; i<30; i+=1){
            inboxSend.add("p");

        }


        InboxSend_recyc = (RecyclerView) getView().findViewById(R.id.inbox_send_recycler_view);
        InboxSend_manager = new LinearLayoutManager(getContext());
        InboxSend_recyc.setLayoutManager(InboxSend_manager);
        InboxSend_adap = new InboxSendAdapter(getContext(), inboxSend);
        InboxSend_recyc.setAdapter(InboxSend_adap);

        InboxSend_recyc.addOnItemTouchListener(new InboxActivity.RecyclerTouchListener(getActivity(),
                InboxSend_recyc, new InboxActivity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                ((InboxActivity)getActivity()).onItemClick(position, "Sent");
                Toast.makeText(getActivity(), "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
