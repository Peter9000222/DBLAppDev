package nl.tue.facetoface.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.tue.facetoface.R;

/**
 * Created by s149453 on 27-3-2017.
 */

public class CancelBottomSheet extends BottomSheetDialogFragment{


    private int time;
    private String distance;
    TextView tvTime;
    TextView tvDistance;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_cancel_bottomsheet, null);
        dialog.setContentView(contentView);
        tvTime = (TextView) contentView.findViewById(R.id.timeCancel);
        tvDistance = (TextView) contentView.findViewById(R.id.distanceCancel);
        tvTime.setText("Time: " + String.valueOf(time));
        tvDistance.setText("Distance: " + distance);


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setTime(int position){
        time = position;
    }

    public void setDistance(String fragment){
        distance = fragment;
    }
}
