package nl.tue.facetoface.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nl.tue.facetoface.Fragments.CancelBottomSheet;
import nl.tue.facetoface.Fragments.InboxReceivedListFragment;
import nl.tue.facetoface.Fragments.InboxSentListFragment;
import nl.tue.facetoface.Fragments.RequestBottomSheet;
import nl.tue.facetoface.R;

public class InboxActivity extends AppCompatActivity{

    private Toolbar tb;
    static CancelBottomSheet bottomSheetCancelFragment;
    static RequestBottomSheet bottomSheetRequestFragment;
    private static ArrayList<ArrayList<String>> interestListSent;
    private static ArrayList<ArrayList<String>> interestListRequest;
    private ArrayList<String> interestListS;
    private ArrayList<String> interestListR;
    private static ArrayList<String> topicListS;
    private static ArrayList<String> topicListR;
    private static ArrayList<String> timeListS;
    private static ArrayList<String> timeListR;
    private static ArrayList<String> distanceListS;
    private static ArrayList<String> distanceListR;
    public static InboxSentListFragment inboxSentListFragment;
    public static InboxReceivedListFragment inboxReceivedListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        // Action bar with icon
        tb = (Toolbar) findViewById(R.id.toolbar4);
        bottomSheetCancelFragment = new CancelBottomSheet();
        bottomSheetRequestFragment = new RequestBottomSheet();
        //These arraylists should be filled with data from server
        interestListSent = new ArrayList<>(); //each position has an list with interests of a user
        interestListRequest = new ArrayList<>();
        topicListS = new ArrayList<>(); //each position has a topic of a user
        timeListS = new ArrayList<>(); //each position has a time of a user
        distanceListS = new ArrayList<>(); //each postion has a distance of a user
        interestListS = new ArrayList<>(); //each position has an interest of a user
        topicListR = new ArrayList<>();
        timeListR = new ArrayList<>();
        distanceListR = new ArrayList<>();
        interestListR = new ArrayList<>();
        inboxSentListFragment = new InboxSentListFragment();
        inboxReceivedListFragment = new InboxReceivedListFragment();

        int i;
        for (i=0; i<30; i+=1){
            interestListS.add(String.valueOf(i) + " Sent");
            interestListSent.add(interestListS);
            interestListR.add(String.valueOf(i) + "Request");
            interestListRequest.add(interestListR);
            topicListS.add(String.valueOf(i )+ " Sent");
            timeListS.add(String.valueOf(i) + " Sent");
            distanceListS.add(String.valueOf(i) + " Sent");
            topicListR.add(String.valueOf(i) + "Request");
            timeListR.add(String.valueOf(i) + "Request");
            distanceListR.add(String.valueOf(i) + "Request");
        }

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Inbox");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
    // adds fragments to tabs
    public void setupViewPager(ViewPager viewPager){
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(inboxReceivedListFragment, "Received");
        adapter.addFragment(inboxSentListFragment, "Sent");
        viewPager.setAdapter(adapter);
    }



    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
        // When an icon in the toolbar is clicked
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch(id) {
                case android.R.id.home :
                    onBackPressed();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    public interface ClickListener{
         void onClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context,final RecyclerView recyclerView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public void onItemClick(String tag, int position){
        if (tag == "Sent"){
            bottomSheetCancelFragment.setTime(timeListS.get(position));
            bottomSheetCancelFragment.setDistance(distanceListS.get(position));
            bottomSheetCancelFragment.setTopic(topicListS.get(position));
            bottomSheetCancelFragment.setInterestList(interestListSent.get(position));
            bottomSheetCancelFragment.show(getSupportFragmentManager(), bottomSheetCancelFragment.getTag());
            bottomSheetCancelFragment.setPosition(position);
        } else {
            bottomSheetRequestFragment.setTopic(topicListR.get(position));
            bottomSheetRequestFragment.setInterest(interestListRequest.get(position));
            bottomSheetRequestFragment.show(getSupportFragmentManager(), bottomSheetRequestFragment.getTag());
            bottomSheetRequestFragment.setPosition(position);
        }
    }

    public ArrayList<String> getTopic(String tag){
        if (tag == "Sent"){
            return topicListS;
        } else {
            return  topicListR;
        }
    }
    public ArrayList<String> getTime(String tag){
        if (tag == "Sent"){
            return timeListS;
        } else {
            return  timeListR;
        }
    }
    public ArrayList<String> getDistance(String tag){
        if (tag == "Sent"){
            return distanceListS;
        } else {
            return  distanceListR;
        }
    }
    public ArrayList<ArrayList<String>> getInterests(String tag){
        if (tag == "Sent"){
            return interestListSent;
        } else {
            return  interestListRequest;
        }
    }

    public static void cancelSentRequest(int position){
        topicListS.remove(position);
        timeListS.remove(position);
        distanceListS.remove(position);
        interestListSent.remove(position);
        bottomSheetCancelFragment.dismiss();
    }

    public static void acceptRequest(int position){
        topicListR.remove(position);
        timeListR.remove(position);
        distanceListR.remove(position);
        interestListRequest.remove(position);
        bottomSheetRequestFragment.dismiss();
    }

    public static void declineRequest(int position){
        topicListR.remove(position);
        timeListR.remove(position);
        distanceListR.remove(position);
        interestListRequest.remove(position);
        bottomSheetRequestFragment.dismiss();
    }

    public static InboxSentListFragment getSentFragment(){return inboxSentListFragment;}
    public static InboxReceivedListFragment getReceivedFragment(){return inboxReceivedListFragment;}
}
