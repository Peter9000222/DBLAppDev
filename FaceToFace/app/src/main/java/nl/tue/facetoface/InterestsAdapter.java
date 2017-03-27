package nl.tue.facetoface;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import nl.tue.facetoface.Activities.TopicActivity;

/**
 * Created by s149453 on 20-3-2017.
 */

public class InterestsAdapter extends
        RecyclerView.Adapter<InterestsAdapter.ViewHolder>{

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button deleteButton;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView){
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);
            nameTextView = (TextView) itemView.findViewById(R.id.theTextView);
        }
    }

    // Store a member variable for the contacts
    private List<String> mInterests;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public InterestsAdapter(Context context, List<String> interests) {
        mInterests = interests;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public InterestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.my_text_view, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final InterestsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        String interest = mInterests.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(interest);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TopicActivity)mContext).deleteInterest(viewHolder.getAdapterPosition());

            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mInterests.size();
    }
}
