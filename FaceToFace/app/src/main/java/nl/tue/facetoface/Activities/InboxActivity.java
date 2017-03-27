package nl.tue.facetoface.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
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
    CancelBottomSheet bottomSheetCancelFragment;
    RequestBottomSheet bottomSheetRequestFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        // Action bar with icon
        tb = (Toolbar) findViewById(R.id.toolbar4);
        bottomSheetCancelFragment = new CancelBottomSheet();
        bottomSheetRequestFragment = new RequestBottomSheet();

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
    private void setupViewPager(ViewPager viewPager){
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new InboxReceivedListFragment(), "Received");
        adapter.addFragment(new InboxSentListFragment(), "Sent");
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
        public void onClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

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

    SlidingPaneLayout.PanelSlideListener panelListener = new SlidingPaneLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
        }

        @Override
        public void onPanelOpened(View panel) {
        }

        @Override
        public void onPanelClosed(View panel) {
        }
    };

    public void onItemClick(int position, String fragment){
        if (fragment == "Sent"){
            bottomSheetCancelFragment.setTime(position);
            bottomSheetCancelFragment.setDistance(fragment);
            bottomSheetCancelFragment.show(getSupportFragmentManager(), bottomSheetCancelFragment.getTag());
        } else {
            bottomSheetRequestFragment.show(getSupportFragmentManager(), bottomSheetRequestFragment.getTag());
        }
    }

}
