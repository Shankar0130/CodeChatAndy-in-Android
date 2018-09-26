package com.shankaryadav.www.codechatandy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends AppCompatActivity {
  private String myUserName;
  private ListView myChatListView;
  private EditText myChatText;
  public ImageButton mySendChatButton;
  private DatabaseReference myDataBaseRef;

  private  ChatListAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setUpDisplayName();
        myDataBaseRef = FirebaseDatabase.getInstance().getReference();

// Get the reference of UI Elements
        myChatListView = (ListView) findViewById(R.id.chat_listView);
        myChatText = (EditText) findViewById(R.id.messageInput);
        mySendChatButton = (ImageButton) findViewById(R.id.sendButton);

        // push chat to FireBase button tab
        mySendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushChatToFirebase();
            }
        });

        //call the pushChatToFireBase on keyboard event

        myChatText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                pushChatToFirebase();
                return true;
            }
        });
    }

    //push chat to Firebase

    private void pushChatToFirebase(){
        String chatInput = myChatText.getText().toString();
        if(!chatInput.equals("")){
            InstantMessage chat = new InstantMessage(chatInput,myUserName);
            myDataBaseRef.child("chats").push().setValue(chat);
            myChatText.setText("");
        }
    }
 // set username for user

    private void setUpDisplayName(){
        SharedPreferences  prefs = getSharedPreferences(RegisterActivity.CHAT_Pref,MODE_PRIVATE);
        myUserName = prefs.getString(RegisterActivity.DISPLAY_NAME, null);

        if(myUserName == null){
            myUserName = "user";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter = new ChatListAdapter(MainChatActivity.this,myDataBaseRef,myUserName);
        myChatListView.setAdapter(myAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.freeUpResources();
    }
}