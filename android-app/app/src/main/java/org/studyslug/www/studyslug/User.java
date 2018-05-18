package org.studyslug.www.studyslug;

import java.util.ArrayList;

public class User {

  private String name;
  private String email;
  private ArrayList<String> courseKeys;
  private ArrayList<String> groupKeys;

  public User() {

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

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }
}
