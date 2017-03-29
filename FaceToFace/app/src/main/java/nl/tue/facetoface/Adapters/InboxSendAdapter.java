package nl.tue.facetoface.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.tue.facetoface.R;

public class InboxSendAdapter extends
        RecyclerView.Adapter<InboxSendAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView inboxSendTopicTextView;
        public TextView inboxSendTimeTextView;
        public TextView inboxSendDistanceTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView){
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            inboxSendTopicTextView = (TextView) itemView.findViewById(R.id.inboxsendTopicTextView);
            inboxSendTimeTextView = (TextView) itemView.findViewById(R.id.inboxsendTimeTextView);
            inboxSendDistanceTextView = (TextView) itemView.findViewById(R.id.inboxsendDistanceTextView);
        }
    }

    // Store a member variable for the contacts
    private List<String> mInterests;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public InboxSendAdapter(Context context, List<String> interests) {
        mInterests = interests;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public InboxSendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.inbox_sent_cards, parent, false);

        // Return a new holder instance
        InboxSendAdapter.ViewHolder viewHolder = new InboxSendAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final InboxSendAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String interest = mInterests.get(position);

        // Set item views based on your views and data model
        TextView tvTopic = viewHolder.inboxSendTopicTextView;
        tvTopic.setText("Topic: "+ interest);
        TextView tvTime = viewHolder.inboxSendTimeTextView;
        tvTime.setText("Time: " + interest);
        TextView tvDistance = viewHolder.inboxSendDistanceTextView;
        tvDistance.setText("Distance: " + interest);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mInterests.size();
    }
}
