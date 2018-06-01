package org.studyslug.www.studyslug;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

  // UI references.
  private SignInButton googleSignIn;
  private FirebaseAuth firebaseAuth;
  private DatabaseReference dbReference;
  private GoogleSignInClient mGoogleSignInClient;
  private static final String TAG = "SplashActivity: ";
  private final static int RCSignIn = 2;
  private final static String validDomain= "ucsc.edu";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "Started activity");
    setContentView(R.layout.activity_splash);
    googleSignIn = findViewById(R.id.g_sign_in_button);
    firebaseAuth = FirebaseAuth.getInstance();
    dbReference = FirebaseDatabase.getInstance().getReference("users");

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .setHostedDomain("ucsc.edu")
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    googleSignIn.setOnClickListener(this);

  }


  private void signIn() {
    Log.d(TAG, "Entered sign-in");
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RCSignIn);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RCSignIn) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
        //handleSignInResult(task);
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Log.d("TAG", "Google sign in failed", e);
        return;
        // TODO: Figure out something useful to do here
      }
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
    String emailAddress= acct.getEmail();
    final String currentDomain= emailAddress.substring(emailAddress.indexOf('@')+1,emailAddress.length());
    boolean isValidDomain = validDomain.equals(currentDomain);
    Log.d(TAG, "Email: "+emailAddress);
    Log.d(TAG, "Domain: "+currentDomain);


    if(isValidDomain) {
      //valid domain
      Log.d(TAG,"Valid domain given");
      AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
      Log.d("TAG", "Sign in method: " + credential.getProvider());

      firebaseAuth.signInWithCredential(credential)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Log.d("name",user.getDisplayName());
                    if (user != null) {
                      //Add user to datbase if not already there.
                      final String userID = user.getUid();
                      Log.d(TAG, "UID: "+ userID);

                      DatabaseReference currentUserReference= dbReference.child(userID);
                      Log.d(TAG, "DBRef: "+currentUserReference);
                      ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                          if(!dataSnapshot.exists()) {
                            DatabaseReference NewUserRef = FirebaseDatabase.getInstance().getReference("users");
                            User newUser = new User();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            newUser.setName(user.getDisplayName());
                            newUser.setEmail(user.getEmail());
                            NewUserRef.child(userID).setValue(newUser);
                            Log.d(TAG, "User not in database, must create new user");


                          } else{
                            Log.d(TAG, "User in database. Carry on");
                          }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                      };
                      currentUserReference.addListenerForSingleValueEvent(eventListener);



                      //Goto Find People
                      Log.d(TAG, "intent:goto FindPeople");

                      Intent findPeopleIntent =
                              new Intent(SplashActivity.this, FindPeopleActivity.class);
                      startActivity(findPeopleIntent);
                    } else {
                      /* TODO: Somehow the firlebase user is null - figure out if we need
                       * to throw an exception or something?
                       */

                      return;
                    }
                  } else {
                    // If sign in fails, display a message to the user.
                    // TODO: Figure out something useful to do if the sign-in fails
                    Log.d(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(SplashActivity.this,
                            "Authentication failed", Toast.LENGTH_LONG)
                            .show();
                  }
                }
              });
    }

    else{
      //invalid domain
      Toast.makeText(SplashActivity.this,
              "Invalid domain", Toast.LENGTH_LONG)
              .show();
      FirebaseAuth.getInstance().signOut();
      FirebaseUser user = firebaseAuth.getCurrentUser();

      try{
        Log.d(TAG, "Deleting illegal user");
        user.delete();
      }
      catch (Exception e){
        Log.d(TAG, "User unable to be deleted");
      }

      return;
    }
  }

  private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);
      Intent findPeopleIntent =
              new Intent(SplashActivity.this, FindPeopleActivity.class);
      startActivity(findPeopleIntent);
      // Signed in successfully, show authenticated UI.

    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
      Intent SplashPage =
              new Intent(SplashActivity.this, SplashActivity.class);
      startActivity(SplashPage);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  public void onClick(View v) {
    // TODO: Refactor this to have the button just call this onclick
    switch (v.getId()) {
      case R.id.g_sign_in_button:
        Log.d(TAG,"Sign In has been clicked");
        signIn();
        break;

    }
  }


}