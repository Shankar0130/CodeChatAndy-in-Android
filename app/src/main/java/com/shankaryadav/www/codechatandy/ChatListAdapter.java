package com.shankaryadav.www.codechatandy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity myActivity;
    private DatabaseReference mDatabasereference;
    private String myUserName;
    private ArrayList<DataSnapshot> mySnapshot;

    // Trying to avoid crashing
    private Context context;

    // Child event listener

    private ChildEventListener myListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
          mySnapshot.add(dataSnapshot);
          notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //Constructor for Object

    public ChatListAdapter(Activity activity,DatabaseReference databaseReference,String username){
          myActivity = activity;
          mDatabasereference = databaseReference.child("chats");
          myUserName = username;

          mySnapshot = new ArrayList<>();
 //  Add Listener
          mDatabasereference.addChildEventListener(myListener);

    }

 // static class

    static  class ViewHolder{
        TextView senderName;
        TextView chatBody;
        LinearLayout.LayoutParams layoutParams;
    }



    @Override
    public int getCount() {
        return mySnapshot.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot snapshot = mySnapshot.get(i);

        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row,viewGroup,false);
            Log.i("Error","this " + view);
            final ViewHolder holder = new ViewHolder();
            holder.senderName = (TextView) view.findViewById(R.id.author);
            holder.chatBody = (TextView) view.findViewById(R.id.message);
            holder.layoutParams = (LinearLayout.LayoutParams) holder.senderName.getLayoutParams();
            view.setTag(holder);
            Log.i("Error","this " + holder);
        }
        final InstantMessage instantMessage = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

        //styling
        boolean isMe = instantMessage.getAuthor().equals(myUserName);
        //Calling a function for styling
        chatRowStyling(isMe,holder);

        String author = instantMessage.getAuthor();
        holder.senderName.setText(author);

        String message = instantMessage.getMessage();
        holder.chatBody.setText(message);




        return view;
    }

    private void chatRowStyling(boolean isItMe, ViewHolder holder){
        if(isItMe){
            holder.layoutParams.gravity = Gravity.END;
            holder.senderName.setTextColor(Color.YELLOW);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_green);
        }else{
            holder.layoutParams.gravity = Gravity.START;
            holder.senderName.setTextColor(Color.BLACK);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_orange);
        }
        holder.senderName.setLayoutParams(holder.layoutParams);
        holder.chatBody.setLayoutParams(holder.layoutParams);
    }

    public void freeUpResources(){
       mDatabasereference.removeEventListener(myListener);
    }
}