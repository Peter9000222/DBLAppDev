package nl.tue.facetoface.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.tue.facetoface.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InboxReceivedListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InboxReceivedListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxReceivedListFragment extends Fragment {

    public InboxReceivedListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_received_list, container, false);
    }

}
