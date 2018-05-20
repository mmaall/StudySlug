package org.studyslug.www.studyslug;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

public class AddCoursesTest {

    private ArrayList<Course> listOfTestCourses;
    private ArrayList<Course> listofCourses;
    private  static  String testDepartment;
    private  static  String testNumber;
    private  static  String testSection;
    private  static  Course testCourse;

    // Firebase stuff
    private DatabaseReference dbReference;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbUserReference;
    private DatabaseReference coursesReference;
    Query courseQuery;

    @Before
    public void SetUp()
    {
        testCourse = new Course();
        testCourse.setDepartment("AMS");
        testCourse.setSection("01");
        testCourse.setNumber("10");



    }


    @Test
    public void testGetCourses()
    {
        listOfTestCourses.add(testCourse);

        public ArrayList<Course> getCourses(){
        final ArrayList<Course> courseData = new ArrayList<>();
        courseData.clear();
        dbReference = FirebaseDatabase.getInstance().getReference("classes");
        coursesReference = FirebaseDatabase.getInstance()
                .getReference("classes");
        courseQuery = coursesReference.orderByChild("department");

        ValueEventListener classes = dbReference.child("classes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> courseShot = dataSnapshot.getChildren().iterator();
                while (courseShot.hasNext()) {
                    DataSnapshot currentCourse = courseShot.next();
                    String currentDepartment, currentNumber, currentSection;
                    HashMap<String,String> currentStudents;
                    currentDepartment = currentCourse.child("department").getValue().toString();
                    currentNumber = currentCourse.child("number").getValue().toString();
                    currentSection = currentCourse.child("section").getValue().toString();

                    Course courseInject = currentCourse.getValue(Course.class);
                    courseData.add(courseInject);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //TODO:Fill courseData with courses
        // courseData.add(new Course(department, number, section, students));

        return courseData;

    }
    listofCourses = getCourses();




    }

}
