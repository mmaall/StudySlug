package org.studyslug.www.studyslug;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

public class Client {
  private FirebaseUser user;
  private String email;
  private String userName;
  private String displayName;
  private Uri photoUri;
  private String UID;
  private static final String TAG = "Client";

  public Client() {}

  public Client(FirebaseUser user) {
    this.user = user;
    this.email = user.getEmail();
    this.displayName = user.getDisplayName();
    this.userName = user.getEmail().split("@")[0];
    this.photoUri = user.getPhotoUrl();
    this.UID = user.getUid();
    Log.d(TAG, "Email: " + this.email);
    Log.d(TAG, "Display name: " + this.displayName);
    Log.d(TAG, "User name: " + userName);
    Log.d(TAG, "Photo URL: " + this.photoUri.toString());
    Log.d(TAG, "UID: " + this.UID);
  }

  public FirebaseUser getUser() {
    return user;
  }

  public void setUser(FirebaseUser user) {
    this.user = user;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Uri getPhotoUri() {
    return photoUri;
  }

  public void setPhotoUri(Uri photoUri) {
    this.photoUri = photoUri;
  }

  public void setUID(String UID) {
    this.UID = UID;
  }

  public String getUID() {
    return UID;
  }
}
