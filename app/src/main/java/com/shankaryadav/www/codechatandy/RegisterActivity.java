package com.shankaryadav.www.codechatandy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_Pref = "ChatPref";
    public static final String DISPLAY_NAME = "UserName";

    private AutoCompleteTextView myUserName;
    private EditText myEmail;
    private EditText myPassword;
    private EditText myConfirmPassword;
    Button myButton;

    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get all the values

        myUserName = (AutoCompleteTextView) findViewById(R.id.register_username);
        myEmail = (EditText) findViewById(R.id.register_email);
        myPassword = (EditText) findViewById(R.id.register_password);
        myConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        myButton = (Button) findViewById(R.id.register_signup_button);

        // Get the instance of FireBase Authentication

        myAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view){
        registerUser();
    }

    //Actual Registration
    private void  registerUser(){
        //Resetting all the errors
        myEmail.setError(null);
        myPassword.setError(null);
//Grab the E-mail and Password
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();
        String confPass = myConfirmPassword.getText().toString();
        String userName = myUserName.getText().toString();

        //Variable to Cancel the registration under unnecessary condition
        boolean cancel = false;
        //To focus the User where goes something wrong
        View focusView = null;

        //E-mail Validation

        if(!checkemail(email)){
            myEmail.setError(getString(R.string.invalid_email));
            cancel = true;
            focusView = myEmail;
        }
        if(TextUtils.isEmpty(email)){
            showErrorbox("Please fill your E-mail");
            cancel = true;
            focusView = myEmail;
        }

        //Password Validation
        if(!checkPassword(password)){
            myPassword.setError(getString(R.string.invalid_password));
            cancel = true;
            focusView = myPassword;
        }
        if(TextUtils.isEmpty(password)){
            showErrorbox("Please fill your Password with minimum 4 characters");
            cancel = true;
            focusView = myPassword;
        }
        if(TextUtils.isEmpty(confPass) || !confPass.equals(password)){
            showErrorbox("Please Confirm Your Password Properly");
            cancel = true;
            focusView = myConfirmPassword;
        }
        if(TextUtils.isEmpty(userName)){
            showErrorbox("Please Fill your User name");
            cancel = true;
            focusView = myUserName;
        }
        if(cancel){
            focusView.requestFocus();
        }else{
            createUser();
            saveUserName();
        }
    }

    //Validation for E-mail
    private boolean checkemail(String email){
        return email.contains("@");
    }

    //Validation for password
    private boolean checkPassword(String password){
        String confPass = myConfirmPassword.getText().toString();
        return confPass.equals(password) && password.length() > 4;
    }

    //signUp a user at fireBase
    private void createUser(){
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        //Call the FireBase methods for Authenticating user
        myAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   // Deleting part before the production part
                        Log.i("FINDCODE","user creation is "+task.isSuccessful());

                        if(task.isSuccessful()){
                            saveUserName();
                            Toast.makeText(getApplicationContext(),DISPLAY_NAME + " is Authenticated",Toast.LENGTH_SHORT).show();

                            // Go into the new Activity, here LoginActivity
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            finish();
                            startActivity(intent);
                        }else {
                            showErrorbox("Heyy Try Again,Something went wrong");
                            Log.i("FINDCODE","user creation is "+task.isSuccessful());
                        }
                    }
                });
    }

    //use shares pref for errors
    private void saveUserName(){
        String userName = myUserName.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_Pref,0);
        prefs.edit().putString(DISPLAY_NAME,userName).apply();
    }

    //Create the errorBox function
    private void showErrorbox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Heyyy")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();

    }
}
