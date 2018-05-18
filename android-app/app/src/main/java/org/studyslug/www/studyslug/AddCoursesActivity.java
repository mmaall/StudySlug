package org.studyslug.www.studyslug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddCoursesActivity extends AppCompatActivity {

  private static final String TAG = "AddCoursesActivity";
  private DatabaseReference dbReference;
  private FirebaseUser firebaseUser;
  private DatabaseReference dbUserReference;
  private DatabaseReference coursesReference;
  private String dbCourseReference;
  private String userEmail;
  private String userKey;
  private EditText userDepartment;
  private EditText userCourseNumber;
  private EditText userCourseSection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_courses);
    Log.d(TAG, "onCreate: started");

    buildDatabaseReferences();
    findFormIDs();
  }

  private void findFormIDs() {
    userDepartment = findViewById(R.id.user_department);
    userCourseNumber = findViewById(R.id.user_course_number);
    userCourseSection = findViewById(R.id.user_course_section);
  }

  private void buildDatabaseReferences() {
    // Initialize database interactions and setup firebaseUser info
    Log.d(TAG, "Building db references");
    dbReference = FirebaseDatabase.getInstance().getReference();
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    try {
      userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    } catch (NullPointerException e) {
      Log.d(TAG,"Tried to get user ID, null pointer exception");
    }
    dbUserReference = dbReference.child("users").child(userKey);
    userEmail = firebaseUser.getEmail();
    coursesReference = dbReference.child("classes");
  }

  private void addCourse() {
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
    returnToFindPeople();
  }

  private void returnToFindPeople() {
    Intent returnIntent = new Intent(AddCoursesActivity.this,
                                     FindPeopleActivity.class);
    startActivity(returnIntent);
  }

  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.add_courses_submit_button:
        addCourse();
        break;
      case R.id.add_courses_cancel_button:
        returnToFindPeople();
        break;
    }
  }
}
