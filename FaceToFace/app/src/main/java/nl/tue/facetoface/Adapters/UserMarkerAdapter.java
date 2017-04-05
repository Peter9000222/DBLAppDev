package nl.tue.facetoface.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nl.tue.facetoface.R;

/**
 * Created by s149453 on 29-3-2017.
 */

public class UserMarkerAdapter extends
        RecyclerView.Adapter<UserMarkerAdapter.ViewHolder>{
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvUserMarker;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView){
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            //cv = (CardView) itemView.findViewById(R.id.cv);
            tvUserMarker = (TextView) itemView.findViewById(R.id.tvUserMarker);
        }
    }

    // Store a member variable for the contacts
    private List<String> mInterests;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public UserMarkerAdapter(Context context, List<String> interests) {
        mInterests = interests;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public UserMarkerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.user_marker_list, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String interest = mInterests.get(position);
        // Set item views based on your views and data model
        TextView textView = viewHolder.tvUserMarker;
        textView.setText(interest);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mInterests.size();
    }
}