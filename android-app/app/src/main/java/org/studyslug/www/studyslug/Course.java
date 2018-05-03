package org.studyslug.www.studyslug;

import java.util.ArrayList;

public class Course {
    private String department;
    private String number;
    private String section;
    private ArrayList<String> students;

    public Course() {

    }

    public Course(String department, String number, String section, ArrayList<String> students) {
        this.department = department;
        this.number = number;
        this.section = section;
        this.students = students;
    }
}
