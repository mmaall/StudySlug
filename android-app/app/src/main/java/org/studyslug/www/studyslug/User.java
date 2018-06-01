package org.studyslug.www.studyslug;

import android.net.Uri;

import java.util.ArrayList;
import java.net.URI;

public class User {

  private String name;
  private String email;
  private String bio;
  private URI userPhotoURL;
  private ArrayList<String> courseKeys;
  private ArrayList<String> groupKeys;

  public User() {}

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }


  public User(String name, String email, String bio, URI uri) {
    this.name = name;
    this.email = email;
    this.bio= bio;
    this.userPhotoURL = uri;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }

  public void setBio(String bio){
    this.bio=bio;
  }

  public String getBio(){
    return this.bio;
  }

  public void setURI(Uri uri) {this.userPhotoURL = uri;}

  public URI getURI() {return this.userPhotoURL;}
}