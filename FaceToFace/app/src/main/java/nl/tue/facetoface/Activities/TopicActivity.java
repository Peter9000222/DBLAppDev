package nl.tue.facetoface.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import nl.tue.facetoface.Adapters.InterestsAdapter;
import nl.tue.facetoface.R;

public class TopicActivity extends AppCompatActivity {

    //objects for the interest recycler view
    RecyclerView Interests_recyc;
    RecyclerView.Adapter Interest_adap;
    RecyclerView.LayoutManager Interests_manager;

    //UI elements
    EditText etTopic;
    EditText etInterest;
    ImageButton bInterest;

    boolean topicFilledIn = false;

    //Userdata list
    ArrayList<String> interests;
    //UserData user = new UserData();
    //public ThisUser userMyself = new ThisUser();
    String topic;
    String userID;
    String exUserID;
    Boolean hasID;

    //Called upon creation of the topic activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        //Settings for the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Topic");

        etTopic = (EditText) findViewById(R.id.TopicEdit);
        etInterest = (EditText) findViewById(R.id.InterestsEdit);
        interests  = new ArrayList<>();

        //instancing the objects for the recycler view
        Interests_recyc = (RecyclerView) findViewById(R.id.my_recycler_view);
        Interests_manager = new LinearLayoutManager(this);
        Interests_recyc.setLayoutManager(Interests_manager);
        Interest_adap = new InterestsAdapter(this, interests);
        Interests_recyc.setAdapter(Interest_adap);

        hasID = true;


        // getting exciting id from map
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            exUserID = extras.getString("exUserID");
            hasID = extras.getBoolean("hasID");
            topic = extras.getString("userTopic");
            etTopic.setText(topic);
            if (extras.getStringArrayList("userInterestList") != null) {
                interests = extras.getStringArrayList("userInterestList");
            }
            if (exUserID == null) {
                hasID = true;
            }
        }

        //ImageView displaying whether a topic is filled in or not
        //MenuItem saveButton = (MenuItem) findViewById(R.id.save_button);


        final ImageView TopicCorrect = (ImageView) findViewById(R.id.TopicImage);
        checkTopic(TopicCorrect);

        etTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkTopic(TopicCorrect);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etTopic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handled = true;
                }
                return handled;
            }
        });

        //Listeners as to check whether the user wants to add an interest
        etInterest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    saveInterest();
                    handled = true;
                }
                return handled;
            }
        });
        bInterest = (ImageButton) findViewById(R.id.InterestButton);
        bInterest.setImageResource(R.mipmap.ic_send_white_48dp);
        bInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInterest();
            }
        });
    }

    //adding an interest to the interest list
    public void saveInterest(){
        if(etInterest.getText().toString().matches("")) {
            Toast.makeText(this, "Can't add empty interest", Toast.LENGTH_SHORT).show();
        }   else {
            interests.add(etInterest.getText().toString());
            Interest_adap.notifyDataSetChanged();
            etInterest.setText("");
        }
    }

    //removing an interest from the interest list (called from the recycleview adapter)
    public void deleteInterest(int position){
        interests.remove(position);
        Interest_adap.notifyDataSetChanged();
    }

    public void setUserData(){
        topic = etTopic.getText().toString();
        if (hasID == true) {
            userID = UUID.randomUUID().toString();
        } else {
            userID = exUserID;
        }
        //user.setTopic(topic);
        //userMyself.setTopic(topic);
        //user.setInterests(interests);
    }

    //creating the menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic, menu);
        menu.findItem(R.id.save_button).setEnabled(topicFilledIn);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (final Menu menu) {
        if (etTopic.getText().toString().matches("")) {
            topicFilledIn = false;
            menu.findItem(R.id.save_button).setEnabled(topicFilledIn);
        } else {
            topicFilledIn = true;
            menu.findItem(R.id.save_button).setEnabled(topicFilledIn);
        }
        etTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etTopic.getText().toString().matches("")) {
                    topicFilledIn = false;
                    menu.findItem(R.id.save_button).setEnabled(topicFilledIn);
                } else {
                    topicFilledIn = true;
                    menu.findItem(R.id.save_button).setEnabled(topicFilledIn);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return true;
    }

    //listener for the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_button:
                if (etTopic.getText().toString().matches("")){
                    Toast.makeText(this, "Topic must be filled in", Toast.LENGTH_SHORT).show();
                } else {
                    setUserData();
                    Toast.makeText(this, "Topic: " + topic, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Map.class);
                    // send userID to map
                    intent.putExtra("userID", userID);
                    // send topic to map
                    intent.putExtra("topic", topic);
                    //send interests to map
                    intent.putExtra("interests", interests);
                    startActivity(intent);
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkTopic(ImageView TopicCorrect){
        if (etTopic.getText().toString().matches("")) {
            TopicCorrect.setImageResource(R.mipmap.ic_clear_white_48dp);
            topicFilledIn = false;
        } else {
            TopicCorrect.setImageResource(R.mipmap.ic_done_white_128dp_2x);
            topicFilledIn = true;
        }
    }
}
