package org.studyslug.www.studyslug;

import java.util.ArrayList;

public class Course {
    private String department;
    private String number;
    private String section;
    private ArrayList<String> students;

    public Course() {
        this.students = new ArrayList<>();
    }

    public Course(String department, String number, String section, ArrayList<String> students) {
        this.department = department;
        this.number = number;
        this.section = section;
        if (this.students == null) {
            this.students = new ArrayList<>();
        }
        this.students = students;
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
        this.students.add(student);
    }

    public ArrayList<String> getStudents() {
        return this.students;
    }
}
