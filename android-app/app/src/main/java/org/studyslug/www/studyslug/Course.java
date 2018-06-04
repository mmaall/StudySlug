package org.studyslug.www.studyslug;

import java.util.HashMap;

public class Course {
  private String department;
  private String name;
  private String number;
  private String section;
  private String key;
  private HashMap<String, HashMap<String, String>> students;

  public Course() {
    this.students = new HashMap<>();
  }

  public Course(String department,
                String name,
                String number,
                String section,
                HashMap<String, HashMap<String, String>> students) {
    this.department = department;
    this.number = number;
    this.section = section;
    this.name = name;
    this.key=department + " " +
             number + " - " +section
             + " " + name;

    if (this.students == null) {
      this.students = new HashMap<>(students);
    } else {
      this.students = students;
    }
  }

  public String getKey() {
    if (key == null) {
      key = department + " " +
            number + " - " +
            section + " " +
            name;
    }
    return key;
  }

  public void setKey(String key) { this.key = key;}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getSection() {
    return section;
  }

  public void setSection(String section) {
    this.section = section;
  }

  public void addStudent(User student) {
    HashMap<String, String> newStudentEntry = new HashMap<>();
    newStudentEntry.put("displayName", student.getDisplayName());
    newStudentEntry.put("email", student.getEmail());
    newStudentEntry.put("uri", student.getURI());
    this.students.put(student.getUserName(), newStudentEntry);
  }

  public boolean equals(Course other) {
    return this.getKey().equals(other.getKey());
  }

  public HashMap<String, HashMap<String, String>> getStudents() {
    HashMap<String, HashMap<String, String>> student_list = new HashMap<>(this.students);
    return student_list;
  }

  public void setStudents(HashMap<String, HashMap<String, String>> students) {
    if (this.students == null) {
      this.students = new HashMap<>(students);
    } else {
      this.students.clear();
      this.students.putAll(students);
    }
  }

}