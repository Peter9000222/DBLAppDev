package nl.tue.facetoface.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.tue.facetoface.R;

public class InboxReceivedAdapter extends
        RecyclerView.Adapter<InboxReceivedAdapter.ViewHolder>{

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView inboxReceivedTopicTextView;
        public TextView inboxReceivedTimeTextView;
        public TextView inboxReceivedDistanceTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView){
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            inboxReceivedTopicTextView = (TextView) itemView.findViewById(
                    R.id.inboxReceivedTopicTextView);
            inboxReceivedTimeTextView = (TextView) itemView.findViewById(
                    R.id.inboxReceivedTimeTextView);
            inboxReceivedDistanceTextView = (TextView) itemView.findViewById(
                    R.id.inboxReceivedDistanceTextView);
        }
    }

    // Store a member variable for the contacts
    private List<String> topicList;
    private List<String> timeList;
    private List<String> distanceList;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public InboxReceivedAdapter(Context context, List<String> topic, List<String> time,
                                List<String> distance) {
        topicList = topic;
        timeList = time;
        distanceList = distance;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public InboxReceivedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.inbox_received_cards, parent, false);

        // Return a new holder instance
        InboxReceivedAdapter.ViewHolder viewHolder = new InboxReceivedAdapter.ViewHolder(
                contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final InboxReceivedAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String topic = topicList.get(position);
        String time = timeList.get(position);
        String distance = distanceList.get(position);
        if (distance == null) {
            System.out.print("null");
        }
        // Set item views based on your views and data model
        TextView tvTopic = viewHolder.inboxReceivedTopicTextView;
        tvTopic.setText("Topic: "+ topic);
        TextView tvTime = viewHolder.inboxReceivedTimeTextView;
        tvTime.setText("Request received at: " + time);
        TextView tvDistance = viewHolder.inboxReceivedDistanceTextView;
        tvDistance.setText("Distance: " + distance);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return topicList.size();
    }
}
