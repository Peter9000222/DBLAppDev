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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import nl.tue.facetoface.Adapters.CancelSendAdapter;
import nl.tue.facetoface.Adapters.RequestAdapter;
import nl.tue.facetoface.Models.ButtonOnClickListener;
import nl.tue.facetoface.R;

/**
 * Created by s149453 on 27-3-2017.
 */

public class RequestBottomSheet extends BottomSheetDialogFragment {
    private String topic = "Topic";
    private String Interest = "Interests";
    private String userId;
    private TextView tvTopic;
    private ArrayList<String> interestList  = new ArrayList<>();

    BottomSheetBehavior bottomSheetBehavior;

    RecyclerView request_recyc;
    RecyclerView.Adapter request_adap;
    RecyclerView.LayoutManager request_manager;

    Button acceptButton;
    Button declineButton;

    private int position;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_request_bottomsheet, null);
        dialog.setContentView(contentView);
        tvTopic = (TextView) contentView.findViewById(R.id.topicTitle);
        tvTopic.setText(topic);

        request_recyc = (RecyclerView) contentView.findViewById(R.id.rvRequest);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        request_recyc.getLayoutParams().height = size.y/4;
        request_manager = new LinearLayoutManager(getContext());
        request_recyc.setLayoutManager(request_manager);
        request_adap = new RequestAdapter(this.getActivity(), interestList);
        request_recyc.setAdapter(request_adap);
        bottomSheetBehavior = BottomSheetBehavior.from((View)contentView.getParent());
        bottomSheetBehavior.setPeekHeight(size.y);

        acceptButton = (Button) contentView.findViewById(R.id.AcceptButton);
        acceptButton.setOnClickListener(new ButtonOnClickListener(position, userId, "Accept"));
        declineButton = (Button) contentView.findViewById(R.id.DeclineButton);
        declineButton.setOnClickListener(new ButtonOnClickListener(position, userId, "Decline"));
        ((View) contentView.getParent()).setBackgroundColor(ContextCompat.getColor(getContext(),
                android.R.color.transparent));


    }
    public void setTopic(String topic){ this.topic = topic;}

    public void setInterest(ArrayList interestList){ this.interestList = interestList; }

    public void setPosition(int position){ this.position = position;}

    public void setId(String userID){this.userId = userID;}
}


