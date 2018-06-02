package org.studyslug.www.studyslug;

import android.net.Uri;

import java.util.HashMap;

public class User {

  private String name;
  private String email;
  private String bio;
  private String userPhotoURI;
  private HashMap<String, String> courses;

  public User() { this.courses = new HashMap<>();}

  public User(String name, String email) {
    this.name = name;
    this.email = email;
    this.courses = new HashMap<>();
  }


  public User(String name, String email, String bio, Uri uri) {
    this.name = name;
    this.email = email;
    this.bio= bio;
    this.userPhotoURI = uri.toString();
    this.courses = new HashMap<>();
  }

  public User(String name, String email, String bio, String uri) {
    this.name = name;
    this.email = email;
    this.bio = bio;
    this.userPhotoURI = uri;
    this.courses = new HashMap<>();
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

  public void setCourses(HashMap<String,String> courses) {this.courses = courses;}

  public HashMap<String, String> getCourses() { return this.courses; }

  public void addCourse(String course) {
    this.courses.put(course, "0");
  }
}