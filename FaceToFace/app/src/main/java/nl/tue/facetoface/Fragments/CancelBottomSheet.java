package nl.tue.facetoface.Fragments;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import nl.tue.facetoface.Activities.InboxActivity;
import nl.tue.facetoface.Adapters.CancelSendAdapter;
import nl.tue.facetoface.Adapters.InboxSendAdapter;
import nl.tue.facetoface.R;

/**
 * Created by s149453 on 27-3-2017.
 */

public class CancelBottomSheet extends BottomSheetDialogFragment {

    private String time;
    private String distance;
    private String topic;
    private int position;
    private ArrayList<String> interestList  = new ArrayList<>();

    TextView tvTime;
    TextView tvDistance;
    TextView tvTopic;

    FloatingActionButton fab;

    RecyclerView cancel_recyc;
    RecyclerView.Adapter cancel_adap;
    RecyclerView.LayoutManager cancel_manager;

    BottomSheetBehavior behavior;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_cancel_bottomsheet, null);
        dialog.setContentView(contentView);
        tvTime = (TextView) contentView.findViewById(R.id.timeCancel);
        tvDistance = (TextView) contentView.findViewById(R.id.distanceCancel);
        tvTime.setText(time);
        tvDistance.setText(distance);
        tvTopic = (TextView) contentView.findViewById(R.id.topicCancel);
        tvTopic.setText(topic);

        cancel_recyc = (RecyclerView) contentView.findViewById(R.id.rvCancel);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        cancel_recyc.getLayoutParams().height = size.y/4;
        cancel_manager = new LinearLayoutManager(getContext());
        cancel_recyc.setLayoutManager(cancel_manager);
        cancel_adap = new CancelSendAdapter(this.getActivity(), interestList);
        cancel_recyc.setAdapter(cancel_adap);
        behavior = BottomSheetBehavior.from((View)contentView.getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Log.e("onStateChanged", "onStateChanged:" + newState);
                } else {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        ((View) contentView.getParent()).setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

        fab = (FloatingActionButton) contentView.findViewById(R.id.cancel_fab);
        fab.setOnClickListener(new MyLovelyOnClickListener(position));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }

    public void setTopic(String topic){ this.topic = topic;}

    public void setInterestList(ArrayList interestList){ this.interestList = interestList; }

    public void setPosition(int position) {this.position = position;}


    public class MyLovelyOnClickListener implements View.OnClickListener
    {
        int position;
        View contentView;
        BottomSheetBehavior behavior;
        public MyLovelyOnClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v)
        {
            InboxSentListFragment fragment;
            InboxActivity.cancelSentRequest(position);
            fragment = InboxActivity.getFragment();
            fragment.notifyAdapter();

        }

    };
    //@Override
    //public void onClick(View view) {
    //    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
   // }
}
