package nl.tue.facetoface.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nl.tue.facetoface.Models.Contact;
import nl.tue.facetoface.ContactsAdapter;
import nl.tue.facetoface.R;

public class TopicActivity extends AppCompatActivity {

    //objects for the interest recycler view
    RecyclerView Interests_recyc;
    RecyclerView.Adapter Interest_adap;
    RecyclerView.LayoutManager Interests_manager;

    //UI elements
    EditText etTopic;
    EditText etInterest;
    Button bInterest;

    //Userdata list
    ArrayList<Contact> contacts = new ArrayList<>();

    //Called upon creation of the topic activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        //Settings for the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Topic");

        //ImageView displaying whether a topic is filled in or not
        etTopic = (EditText) findViewById(R.id.TopicEdit);
        final ImageView TopicCorrect = (ImageView) findViewById(R.id.TopicImage);
        TopicCorrect.setImageResource(R.mipmap.ic_launcher);
        etTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etTopic.getText().toString().matches("")) {
                    TopicCorrect.setImageResource(R.mipmap.ic_launcher);
                } else {
                    TopicCorrect.setImageResource(R.mipmap.ic_mail_white_48dp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //instancing the objects for the recycler view
        Interests_recyc = (RecyclerView) findViewById(R.id.my_recycler_view);
        Interests_manager = new LinearLayoutManager(this);
        Interests_recyc.setLayoutManager(Interests_manager);
        Interest_adap = new ContactsAdapter(this, contacts);
        Interests_recyc.setAdapter(Interest_adap);

        //Listeners as to check whether the user wants to add an interest
        etInterest = (EditText) findViewById(R.id.InterestsEdit);
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
        bInterest = (Button) findViewById(R.id.InterestButton);
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
            Toast.makeText(this, "CAN'T BE EMPTY", Toast.LENGTH_SHORT).show();
        }   else {
            Contact newContact = new Contact(etInterest.getText().toString());
            contacts.add(newContact);
            Interest_adap.notifyDataSetChanged();
            etInterest.setText("");
        }
    }

    //removing an interest from the interest list (called from the recycleview adapter)
    public void deleteInterest(int position){
        contacts.remove(position);
        Interest_adap.notifyDataSetChanged();
    }

    //creating the menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic, menu);
        return true;
    }

    //listener for the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_button:
                Intent intent = new Intent(this, Map.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
