package nl.tue.facetoface.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ActionMenuView;

import nl.tue.facetoface.R;

public class InboxActivity extends AppCompatActivity{

    private Toolbar tb;
    private ActionMenuView amv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        // Action bar with icon
        tb = (Toolbar) findViewById(R.id.toolbar4);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Inbox");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Received requests"));
        tabs.addTab(tabs.newTab().setText("Sent request"));
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
}
