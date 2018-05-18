package org.studyslug.www.studyslug;

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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddCoursesActivity extends AppCompatActivity {

  private static final String TAG = "AddCoursesActivity";

  // Firebase stuff
  private DatabaseReference dbReference;
  private FirebaseUser firebaseUser;
  private DatabaseReference dbUserReference;
  private DatabaseReference coursesReference;
  private String dbCourseReference;

  // User info
  // private String user_name;
  private String userEmail;
  private String userKey;

  // Form info
  private EditText userDepartment;
  private EditText userCourseNumber;
  private EditText userCourseSection;
  private Button submitButton;
  private Button cancelButton;

  // Spinner stuff
  private Spinner departmentSpinner;
  private GridView courseView;
  ArrayAdapter<Course> departmentAdapter;
  String[] departments = {"ACEN", "AMST", "ANTH", "APLX", "AMS", "ARAB", "ART",
          "ASTR", "BIOC", "BIOL", "BIOE", "BME", "CLNI", "CLTE", "CMMU", "CMPM", "CMPE",
          "CMPS", "COWL", "LTCR",};

  private ArrayList<Course> getCourses() {
    ArrayList<Course> courseData = new ArrayList<>();
    courseData.clear();

    //TODO:Fill courseData with courses by Department (ID)
    // courseData.add(new Course(department, number, section, students));


    return courseData;
  }

  private void getCoursesBySelectedDepartment(String chosenDepartment) {
    ArrayList<Course> filteredCourses = new ArrayList<>();
    if (chosenDepartment == " "){

        departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getCourses());
    }
    else{
        for(Course course: getCourses()){

          if(course.getDepartment()==chosenDepartment){
             filteredCourses.add(course);
          }//endif

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

    // Initialize database interactions and setup firebaseUser info
    dbReference = FirebaseDatabase.getInstance().getReference();
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    dbUserReference = dbReference.child("users").child(userKey);
    userEmail = firebaseUser.getEmail();

    // Get references to input form - for now I don't care about error checking
    userDepartment = findViewById(R.id.user_department);
    userCourseNumber = findViewById(R.id.user_course_number);
    userCourseSection = findViewById(R.id.user_course_section);
    submitButton = findViewById(R.id.addClasses_button);
    cancelButton = findViewById(R.id.cancel_button);

    // Get reference to the classes tree
    coursesReference = dbReference.child("classes");

    // Initialize department spinner
    departmentSpinner = (Spinner) findViewById(R.id.departSpinner);
    departmentSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,departments));
    courseView = (GridView) findViewById(R.id.mainGrid);
    courseView.setAdapter(new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_1,getCourses()));

    departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         if(position >= 0 && position < departments.length){
              
         }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });




    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Construct a class object using the info the firebaseUser submitted
        final Course newCourse = new Course();
        newCourse.addStudent(userEmail);
        newCourse.setDepartment(userDepartment.getText().toString().toUpperCase());
        newCourse.setNumber(userCourseNumber.getText().toString().toUpperCase());
        newCourse.setSection(userCourseSection.getText().toString().toUpperCase());

        // Scan db to see if this class already exists
        coursesReference.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
              for (DataSnapshot db_class : dataSnapshot.getChildren()) {
                Course temp = db_class.getValue(Course.class);
                if (temp.equals(newCourse)) {
                  dbCourseReference = db_class.getKey();
                  temp.addStudent(userEmail);
                  dbUserReference.child("classes").push().setValue(dbCourseReference);
                  dbReference.child("classes").child(dbCourseReference).setValue(temp);
                  break;
                }
              }
            } else {
              Log.d(TAG, "dataSnaphot not found");
            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
            // TODO: Something here?
          }
        });
        if (dbCourseReference == null) {
          // This is a new course
          DatabaseReference newCourseRef = dbReference.child("classes").push();
          newCourseRef.setValue(newCourse);
          dbUserReference.child("classes").push().setValue(newCourseRef.getKey());
        }
        Intent returnIntent = new Intent(AddCoursesActivity.this,
                                         FindPeopleActivity.class);
        startActivity(returnIntent);
      }
    });
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent returnIntent = new Intent(AddCoursesActivity.this,
                                         FindPeopleActivity.class);
        startActivity(returnIntent);
      }
    });

  }
}
