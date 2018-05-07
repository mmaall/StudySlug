package org.studyslug.www.studyslug;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.PACKAGE_USAGE_STATS;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 * TODO: Add an AuthListener that passes to this activity.
 * TODO: Remove like just about all of this code and replace with Firebase auth stuff
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText  mEmailView;
    private EditText mPasswordView;
    //private View mProgressView;
    //private View mLoginFormView;
    private Button buttonSignIn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog signInProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonSignIn= findViewById(R.id.email_sign_in_button);
        firebaseAuth= FirebaseAuth.getInstance();
        signInProgress = new ProgressDialog(this);

        // Set up the login form.
        mEmailView =  findViewById(R.id.email);
        mPasswordView=  findViewById(R.id.password);

        buttonSignIn.setOnClickListener(this);

    }

    private void registerUser(){
        String email= mEmailView.getText().toString().trim();
        String password= mPasswordView.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            return;//no email
        }
        if(TextUtils.isEmpty(password)){
            return;//no password
        }
        //Email and password has been entered
        //Register user

        signInProgress.setMessage("Logging in...");
        signInProgress.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is registerd succesfully and logged in
                            Toast.makeText(LoginActivity.this, "Registered Succesfully",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Could not register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void onClick(View view){
        if(view == buttonSignIn){
            registerUser();
        }
        if(firebaseAuth.getCurrentUser() != null) {
            Intent mainIntent = new Intent(this, findPeople.class);
            startActivity(mainIntent);
        }
    }


}

