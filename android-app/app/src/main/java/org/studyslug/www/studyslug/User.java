package org.studyslug.www.studyslug;

import android.net.Uri;

import java.util.HashMap;

public class User {

  private String displayName;
  private String email;
  private String userPhotoURI;
  private String userName;
  private HashMap<String, String> courses;

  public User() { this.courses = new HashMap<>();}

  public User(Client client) {
    this.displayName = client.getDisplayName();
    this.userName = client.getUserName();
    this.userPhotoURI = client.getPhotoUri().toString();
    this.email = client.getEmail();
    this.courses = new HashMap<>();
  }

  public String getUserName() { return userName; }

  public void setUserName(String userName) { this.userName = userName; }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }

  public void setURI(String uri) {this.userPhotoURI = uri;}

  public String getURI() {return this.userPhotoURI;}

  public void setCourses(HashMap<String,String> courses) {this.courses = courses;}

  public HashMap<String, String> getCourses() { return this.courses; }

  public void addCourse(String course) {
    this.courses.put(course, "0");
  }
}