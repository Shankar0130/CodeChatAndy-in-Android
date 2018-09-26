package com.shankaryadav.www.codechatandy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText myEmail;
    private EditText myPassword;
    Button loginButton;
    Button registerButton;
    private FirebaseAuth myAuth;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = myAuth.getCurrentUser();
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.login_signin_button);
        registerButton = (Button) findViewById(R.id.login_signup_button);
        myEmail = (EditText) findViewById(R.id.login_email);
        myPassword = (EditText) findViewById(R.id.login_password);

        myAuth = FirebaseAuth.getInstance();
    }

    //signed method on SIGN UP tap
    public void signIn(View view){
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();
        boolean cancel = false;
       View focus = null;
       if(TextUtils.isEmpty(email)){
           showErrorbox("Fill the E-mail");
           cancel = true;
           focus = myEmail;
       }
       if(TextUtils.isEmpty(password)){
           showErrorbox("Fill the Password");
           cancel = true;
           focus = myPassword;
       }
       if(cancel){
           focus.requestFocus();
       }else {
           logInUser();
       }
    }
    //Log in user with FireBase
public void logInUser(){
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                 Intent intent = new Intent(getApplicationContext(),MainChatActivity.class);
                 finish();
                 startActivity(intent);
                   }else{
                       Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();
                   }

                   if(!task.isSuccessful()){
                       Log.i("FINDLOGINCode","User sign in is " + task.isSuccessful());
                       showErrorbox("You are not successfully logged in");
                   }
                    }
                });
}
    //Take User to Register Page
    public void registerNewUser(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        finish();
        startActivity(intent);
    }
    private void showErrorbox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Heyyy")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();

    }
}
