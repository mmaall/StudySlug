package org.studyslug.www.studyslug;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.Objects;

public class AddCoursesActivity extends AppCompatActivity {

  private static final String TAG = "AddCoursesActivity";

  // Firebase stuff
  private DatabaseReference dbReference;
  private DatabaseReference userReference;
  private FirebaseUser firebaseUser;
  private DatabaseReference coursesReference;
  private DatabaseReference departmentReference;


  /// Strings
  private String dbCourseReference;
  private String userEmail;
  private String userKey;


  // Spinner stuff
  private Spinner departmentSpinner;
  private Spinner courseSpinner;
  private RecyclerView courseRecycler;
  private RecyclerView.LayoutManager courseLayoutManager;
  private String selectedDepartment;
  String temp = " ";
  ArrayAdapter<Course> departmentAdapter;
  ArrayList<Course> courseData;
  ArrayList<String> filteredCourses;
  List<String> departments;


  private void findViews() {
    setContentView(R.layout.activity_add_courses);
    departmentSpinner = findViewById(R.id.departSpinner);
    courseSpinner = findViewById(R.id.course_spinner);
    courseRecycler = findViewById(R.id.course_recycler);
    courseRecycler.setHasFixedSize(true);
    courseLayoutManager = new LinearLayoutManager(this);
    courseRecycler.setLayoutManager(courseLayoutManager);
  }

  private void buildReferences() {
    dbReference = FirebaseDatabase.getInstance().getReference();
    departmentReference = FirebaseDatabase.getInstance()
                                          .getReference("classes")
                                          .child("department");
    coursesReference = FirebaseDatabase.getInstance()
                                       .getReference("classes");
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    try {
      userKey = firebaseUser.getUid();
    } catch (NullPointerException e) {
      Log.d(TAG,"Tried to get user ID, null pointer exception");
    }

    userReference = dbReference.child("users").child(userKey);


  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate: started");

    findViews();
    buildReferences();

    buildDropdownMenus();


    /**
     * insert code to track class add button
     * then
     * dbReference.child("classes").child(courseReference).setValue(courseName);
     *
     */

    /**
     *  Using SwitchActivity will looking something like this, I think.
       *  SwitchActivity setUpSwitch = new SwitchActivity(AddCoursesActivity.class);
       *  setUpSwitch.SwitchToAddCourses(setUpSwitch.sourceActivity);
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
    /**
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
     **/



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