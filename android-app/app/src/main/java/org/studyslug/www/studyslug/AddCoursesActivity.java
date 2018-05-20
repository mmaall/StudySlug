package org.studyslug.www.studyslug;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
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


public class AddCoursesActivity extends AppCompatActivity {

  private static final String TAG = "AddCoursesActivity";

  // Firebase stuff
  private DatabaseReference dbReference;
  private FirebaseUser firebaseUser;
  private DatabaseReference dbUserReference;
  private DatabaseReference coursesReference;
  private String dbCourseReference;


  // Spinner stuff
  private Spinner departmentSpinner;
  private GridView courseView;
  private String selectedDepartment;
  private String selectError = "Error choosing department.";
  ArrayAdapter<Course> departmentAdapter;
  Query courseQuery;
  private ArrayList<String> availableDepartments = new ArrayList<>();
  private ArrayList<Course> courseData;

  private void initView(){
    // Initialize department spinner
    departmentSpinner = (Spinner) findViewById(R.id.departSpinner);
    departmentSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,availableDepartments));
    courseView = (GridView) findViewById(R.id.mainGrid);
    courseView.setAdapter(new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_1,getCourses()));

    departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position >0 && position < availableDepartments.size()){

           selectedDepartment = availableDepartments.get(position);
           getCoursesBySelectedDepartment(selectedDepartment);
        }
        else{

        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

  }



  private ArrayList<Course> getCourses() {

    courseData = new ArrayList<>();
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
          String currentDepartment, currentNumber, currentSection, currentName;
          HashMap<String,String> currentStudents;
          currentDepartment = currentCourse.child("department").getValue().toString();
          currentName = currentCourse.child("name").getValue().toString();
          availableDepartments.add(currentDepartment);
          Log.d(TAG, "adding " + currentDepartment + " to availableDepartments");
          currentNumber = currentCourse.child("number").getValue().toString();
          currentSection = currentCourse.child("section").getValue().toString();

          Course courseInject = currentCourse.getValue(Course.class);
          courseData.add(courseInject);
          Log.d(TAG, "adding " + currentName + " to courseData");


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



  private void getCoursesBySelectedDepartment(String chosenDepartment) {
    ArrayList<Course> filteredCourses = new ArrayList<>();

    if (chosenDepartment == " "){
       departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getCourses());
    }
    else{
       for(Course course:getCourses() ){
         if(course.getDepartment()==chosenDepartment){

           filteredCourses.add(course);
          }

        }//endfor

      departmentAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,filteredCourses);
    }//endelse

    courseView.setAdapter(departmentAdapter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_classes);
    Log.d(TAG, "onCreate: started");

    initView();


   }
}
