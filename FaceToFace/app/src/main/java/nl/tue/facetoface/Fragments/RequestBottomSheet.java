package nl.tue.facetoface.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

import nl.tue.facetoface.R;

/**
 * Created by s149453 on 27-3-2017.
 */

public class RequestBottomSheet extends BottomSheetDialogFragment {
    private String topic = "Topic";
    private String Interest = "Interests";
    private TextView tvTopic;
    private TextView tvInsterst;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_request_bottomsheet, null);
        dialog.setContentView(contentView);
        tvTopic = (TextView) contentView.findViewById(R.id.topicTitle);
        tvInsterst = (TextView) contentView.findViewById(R.id.InterestTitle);
        tvTopic.setText(topic);
        tvInsterst.setText(Interest);


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}


