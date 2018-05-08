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
import android.util.Log;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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

    private final static int RC_SIGN_IN = 2;

    // UI references.
    private EditText  mEmailView;
    private EditText mPasswordView;
    //private View mProgressView;
    //private View mLoginFormView;
    private Button buttonSignIn;
    private SignInButton googleSignIn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog signInProgress;

    private String userDataKey;


    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleSignIn= findViewById(R.id.g_sign_in_button);
        buttonSignIn= findViewById(R.id.email_sign_in_button);
        firebaseAuth= FirebaseAuth.getInstance();
        signInProgress = new ProgressDialog(this);

        // Set up the login form.
        mEmailView =  findViewById(R.id.email);
        mPasswordView=  findViewById(R.id.password);

        buttonSignIn.setOnClickListener(this);
        googleSignIn.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        System.out.println("Entered signin\n");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);

                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // I assume we should go to main here
                            if(firebaseAuth.getCurrentUser() != null) {
                                Intent mainIntent = new Intent(LoginActivity.this, addClasses.class);
                                startActivity(mainIntent);
                            } else {
                                return;
                            }

     //                       updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_form), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
     //                       updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private     void registerUser(){
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
                            if(task.getResult().getAdditionalUserInfo().isNewUser()) {
                                userDataKey = firebaseAuth.getUid();
                                databaseReference.child("users").child(userDataKey).push();
                            }
                            else{

                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Could not register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.g_sign_in_button:
                signIn();
                break;
            case R.id.email_sign_in_button:
                System.out.println("register\n");
                registerUser();
                break;
        }
    }

    /*public void onClick(View view){
        if(view == buttonSignIn){
            registerUser();
        }
        if(firebaseAuth.getCurrentUser() != null) {
            Intent mainIntent = new Intent(this, addClasses.class);
            startActivity(mainIntent);
        }
    }*/


}

