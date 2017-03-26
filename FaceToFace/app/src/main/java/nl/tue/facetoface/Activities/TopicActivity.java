package nl.tue.facetoface.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

import nl.tue.facetoface.Models.Contact;
import nl.tue.facetoface.ContactsAdapter;
import nl.tue.facetoface.R;

public class TopicActivity extends AppCompatActivity {

    private RecyclerView Interests_recyc;
    private RecyclerView.Adapter Interest_adap;
    private RecyclerView.LayoutManager Interests_manager;
    private static final String TAG = "MyActivity";

    ArrayList<Contact> contacts = new ArrayList<>();
    EditText etInterest;
    Button bInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ImageView displaying whether a topic is filled in or not
        ImageView TopicCorrect = (ImageView) findViewById(R.id.TopicImage);
        TopicCorrect.setImageResource(R.mipmap.ic_launcher);
        Interests_recyc = (RecyclerView) findViewById(R.id.my_recycler_view);

        Interests_manager = new LinearLayoutManager(this);
        Interests_recyc.setLayoutManager(Interests_manager);
        Interest_adap = new ContactsAdapter(this, contacts);
        Interests_recyc.setAdapter(Interest_adap);

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

    private void saveInterest(){
        Contact newContact = new Contact(etInterest.getText().toString());
        contacts.add(newContact);
        Interest_adap.notifyDataSetChanged();
        etInterest.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic, menu);
        return true;
    }

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
