package org.studyslug.www.studyslug;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;

public class AddCoursesActivity extends AppCompatActivity {

  private static final String TAG = "AddCoursesActivity";
  private DatabaseReference dbReference;
  private DatabaseReference userReference;
  private FirebaseUser firebaseUser;
  private DatabaseReference coursesReference;
  private DatabaseReference departmentReference;
  private String dbCourseReference;
  private String userEmail;
  private String userKey;
  private String userDepartment;
  private String userCourseNumber;
  private String userCourseSection;



  // Spinner stuff
  private Spinner departmentSpinner;
  private Spinner courseSpinner;
  private RecyclerView courseRecycler;
  private String selectedDepartment;
  private String selectError = "Error choosing department.";
  String temp = " ";
  ArrayAdapter<Course> departmentAdapter;
  ArrayList<Course> courseData;
  ArrayList<String> filteredCourses;
  List<String> departments;
  Query courseQuery;




/** Let's bench this method for now
  private ArrayList<Course> getCourses() {

    courseData = new ArrayList<>();
    courseData.clear();
    dbReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference coursesReference = FirebaseDatabase.getInstance()
                                                         .getReference("classes");
    courseQuery = coursesReference.orderByChild("department");

    dbReference.child("classes").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> courseCheck = dataSnapshot.getChildren();
        for (DataSnapshot child : courseCheck) {
          Course placeCourse = child.getValue(Course.class);
          courseData.add(placeCourse);
        }
        //Iterator<DataSnapshot> courseShot = dataSnapshot.getChildren().iterator();
        //while (courseShot.hasNext()) {
        //DataSnapshot currentCourse = courseShot.next();
        //String currentName;
        //HashMap<String,String> currentStudents;
        //currentName = currentCourse.child("name").getValue().toString();
        //Course courseInject = currentCourse.getValue(Course.class);
        //courseData.add(courseInject);
        //Log.d(TAG, "adding " + currentName + " to courseData");


      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    return courseData;

  }
 **/







  private void getCoursesBySelectedDepartment(String chosenDepartment) {
    filteredCourses = new ArrayList<>();
    /**
     if (chosenDepartment == " "){
     departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getCourses());
     }
     else{
     **/
    /**for (Course course : getCourses()) {
     if (course.getDepartment().equals(chosenDepartment)) {

     filteredCourses.add(course);
     } else {
     continue;
     }

     }//endfor
     **/

    // departmentAdapter.addAll(filteredCourses);
    //}//endelse

    // courseView.setAdapter(departmentAdapter);
  }

  private void findViews() {
    setContentView(R.layout.activity_add_classes);
    departmentSpinner = findViewById(R.id.departSpinner);
    courseSpinner = findViewById(R.id.course_spinner);
  }

  private void buildReferences() {
    departmentReference = FirebaseDatabase.getInstance()
                                          .getReference("classes")
                                          .child("department");
    coursesReference = FirebaseDatabase.getInstance()
                                       .getReference("classes");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate: started");

    findViews();
    buildReferences();

    buildDropdownMenus();



    /**
     * Fill the first spinner.
     */
  }

  private void buildDropdownMenus() {

    departmentReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        departments = new ArrayList<>();
        for (DataSnapshot depSnapshot : dataSnapshot.getChildren()) {
          String depName = depSnapshot.getValue(String.class);

          if (!depName.equals(temp)) {
            departments.add(depName);
            temp = depName;
          }

          ArrayAdapter<String> areasAdapter =
              new ArrayAdapter<>(AddCoursesActivity.this,
                                 android.R.layout.simple_spinner_item, departments);

          areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

          departmentSpinner.setAdapter(areasAdapter);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });
    if (dbCourseReference == null) {
      // This is a new course
      DatabaseReference newCourseRef = dbReference.child("classes").push();
      newCourseRef.setValue(newCourse);
      dbUserReference.child("classes").push().setValue(newCourseRef.getKey());
    }
    returnToFindPeople();
  }

  private void returnToFindPeople() {
    Intent returnIntent = new Intent(AddCoursesActivity.this,
                                     FindPeopleActivity.class);
    startActivity(returnIntent);
  }



    Object selectedItem = departmentSpinner.getSelectedItem();

    if (selectedItem != null) {
      selectedDepartment = departmentSpinner.getSelectedItem().toString();

      coursesReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          for (DataSnapshot classShot : dataSnapshot.getChildren()) {
            if (classShot.child("department").getValue(String.class).equals(selectedDepartment)) {
              filteredCourses.add(classShot.getKey());

            }

            ArrayAdapter<String> courseAdapter =
                new ArrayAdapter<>(AddCoursesActivity.this,
                                   android.R.layout.simple_spinner_item, filteredCourses);

            courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            courseSpinner.setAdapter(courseAdapter);
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
      });

    } else {
      // TODO show all classes
    }

  }
}