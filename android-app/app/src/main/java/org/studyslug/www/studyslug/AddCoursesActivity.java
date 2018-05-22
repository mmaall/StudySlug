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
import android.widget.ListView;
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
  private DatabaseReference userReference;
  private FirebaseUser firebaseUser;
  private DatabaseReference coursesReference;
  private String dbCourseReference;
  private String userEmail;
  private String userKey;
  private String userDepartment;
  private String userCourseNumber;
  private String userCourseSection;


  // Spinner stuff
  private Spinner departmentSpinner;
  private GridView courseView;
  private String selectedDepartment;
  private String selectError = "Error choosing department.";
  ArrayAdapter<Course> departmentAdapter;
  ArrayList<Course> courseData;
  ArrayList<Course> filteredCourses;
  Query courseQuery;
  private String[] availableDepartments = {
      "ACEN", "AMST", "ANTH", "APLX", "AMS", "ARAB", "ARTG",
      "ASTR", "BIOC", "BIOL", "BIOE", "BME", "CHEM", "CHIN", "CLEI",
      "CLNI", "CLTE", "CMMU", "CMPM", "CMPE", "CMPS", "COWL", "LTCR",
      "CRES", "CRWN", "DANM", "EART", "ECON", "EDUC", "EE", "ENGR",
      "LTEL", "ENVS", "ETOX", "FMST", "FILM", "FREN", "LTFR", "GAME",
      "GERM", "LTGE", "GREE", "LTGR", "HEBR", "HNDI", "HIS", "HAVC",
      "HISC", "HUMN", "ISM", "ITAL", "LTIT", "JAPN", "JWST", "KRSG",
      "LAAD", "LATN", "LALS", "LTIN", "LGST", "LING", "LIT", "MATH",
      "MERR", "METX", "LTMO", "MUSC", "OAKS", "OCEA", "PHIL", "PHYE",
      "POLI", "PRTR", "PORT", "LTPR", "PSYC", "PUNJ", "RUSS", "SCIC",
      "SOCD", "SOCS", "SOCY", "SPAN", "SPHS", "SPSS", "LTSP", "STEV",
      "TIM", "THEA", "UCDC", "WMST", "LTWL", "WRIT", "YIDD"
  };


  private void initView() {
    // Initialize department spinner
    departmentSpinner = findViewById(R.id.departSpinner);
    departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
    departmentSpinner.setAdapter(
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                                 availableDepartments));
    courseView = findViewById(R.id.mainGrid);
    courseView.setAdapter(
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCourses()));


  }

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

  private void getCoursesBySelectedDepartment(String chosenDepartment) {
    filteredCourses = new ArrayList<>();
    /**
     if (chosenDepartment == " "){
     departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getCourses());
     }
     else{
     **/
    for (Course course : getCourses()) {
      if (course.getDepartment().equals(chosenDepartment)) {

        filteredCourses.add(course);
      } else {
        continue;
      }

    }//endfor

    departmentAdapter.addAll(filteredCourses);
    //}//endelse

    courseView.setAdapter(departmentAdapter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_classes);

    Log.d(TAG, "onCreate: started");

    getCourses();

    initView();

    /**
     * Track Click in Spinner
     */
    departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected");
        //String selectedDepartment = departmentSpinner.getSelectedItem().toString();
        if (position > 0 && position < availableDepartments.length) {
          Log.d(TAG, "Grabbed this item");
          selectedDepartment = availableDepartments[position];
          getCoursesBySelectedDepartment(selectedDepartment);
        } else {

        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    userReference = dbReference.child("users").child(userKey);

    /**
     * Track Click in GridView of Courses
     */
    courseView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Course selectedCourse = filteredCourses.get(position);
        // addCourse(selectedCourse);

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }
}
