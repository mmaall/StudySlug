package org.studyslug.www.studyslug;

import android.net.Uri;

import java.util.ArrayList;

public class User {

  private String name;
  private String email;
  private String bio;
  private String userPhotoURI;
  private ArrayList<String> courseKeys;
  private ArrayList<String> groupKeys;

  public User() {}

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }


  public User(String name, String email, String bio, Uri uri) {
    this.name = name;
    this.email = email;
    this.bio= bio;
    this.userPhotoURI = uri.toString();
  }

  public User(String name, String email, String bio, String uri) {
    this.name = name;
    this.email = email;
    this.bio = bio;
    this.userPhotoURI = uri;
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

  public void setURI(String uri) {this.userPhotoURI = uri;}

  public String getURI() {return this.userPhotoURI;}
}