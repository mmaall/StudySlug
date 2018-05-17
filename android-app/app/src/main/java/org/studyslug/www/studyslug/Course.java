package org.studyslug.www.studyslug;

import java.util.HashMap;

public class Course {
    private String department;
    private String number;
    private String section;
    private HashMap<String, String> students;


    public Course() {
        this.students = new HashMap<>();
    }

    public Course(String department,
                  String number,
                  String section,
                  HashMap<String, String> students) {
        this.department = department;
        this.number = number;
        this.section = section;
        if (this.students == null) {
            this.students = new HashMap<>(students);
        } else {
            this.students = students;
        }
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

    public void addStudent(String student) {
        String next = "ID " + String.valueOf(students.size());
        this.students.put(next, student);
    }

    public boolean equals(Course other) {
        return this.department.equals(other.getDepartment()) &&
                this.number.equals(other.getNumber()) &&
                this.section.equals(other.getSection());
    }

    public HashMap<String, String> getStudents() {
        HashMap<String, String> student_list = new HashMap<>(this.students);
        return student_list;
    }

    public void setStudents(HashMap<String, String> students) {
        if (this.students == null) {
            this.students = new HashMap<>(students);
        } else {
            this.students.clear();
            this.students.putAll(students);
        }
    }

}