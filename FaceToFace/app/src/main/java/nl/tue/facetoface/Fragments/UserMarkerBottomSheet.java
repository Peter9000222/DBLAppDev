package nl.tue.facetoface.Fragments;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import nl.tue.facetoface.Adapters.CancelSendAdapter;
import nl.tue.facetoface.Adapters.UserMarkerAdapter;
import nl.tue.facetoface.Models.ButtonOnClickListener;
import nl.tue.facetoface.R;

/**
 * Created by s149453 on 27-3-2017.
 */

public class UserMarkerBottomSheet extends BottomSheetDialogFragment {
    private String topic = "Topic";
    private String Interest = "Interests";
    private String idSheet;
    private TextView tvTopic;
    private ArrayList<String> interestList  = new ArrayList<>();
    BottomSheetBehavior bottomSheetBehavior;

    RecyclerView userMarker_recyc;
    RecyclerView.Adapter userMarker_adap;
    RecyclerView.LayoutManager userMarker_manager;

    Button sendRequest;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_user_marker_bottomsheet, null);
        dialog.setContentView(contentView);
        tvTopic = (TextView) contentView.findViewById(R.id.topicTitle);
        tvTopic.setText(topic);

        userMarker_recyc = (RecyclerView) contentView.findViewById(R.id.rvUserMarker);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        userMarker_recyc.getLayoutParams().height = size.y/4;
        userMarker_manager = new LinearLayoutManager(getContext());
        userMarker_recyc.setLayoutManager(userMarker_manager);
        userMarker_adap = new UserMarkerAdapter(this.getActivity(), interestList);
        userMarker_recyc.setAdapter(userMarker_adap);
        bottomSheetBehavior = BottomSheetBehavior.from((View)contentView.getParent());
        bottomSheetBehavior.setPeekHeight(size.y);
        sendRequest = (Button) contentView.findViewById(R.id.SendRequestButton);
        sendRequest.setOnClickListener(new ButtonOnClickListener(1, getIdSheet(), "Send Request"));
        ((View) contentView.getParent()).setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));


    }
    public void setTopic(String topic){ this.topic = topic;}

    public void setInterest(ArrayList interestList){ this.interestList = interestList; }

    public void setIdSheet(String idSheet){this.idSheet = idSheet;}

    public String getIdSheet(){ return this.idSheet;}


}


