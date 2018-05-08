package org.studyslug.www.studyslug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class addClasses extends AppCompatActivity {

    private  static final String TAG = "addClasses";

    // Firebase stuff
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user;
    private DatabaseReference user_ref;
    private DatabaseReference classes_ref;
    private String db_course_ref;

    // User info
    // private String user_name;
    private String user_email;
    private String user_key;

    // Form info
    private EditText user_department;
    private EditText user_course_number;
    private EditText user_course_section;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        Log.d(TAG, "onCreate: started");

        // Initialize database interactions and setup user info
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user_ref = mDatabaseReference.child("users").child(user_key);

        // Get references to input form - for now I don't care about error checking
        user_department = findViewById(R.id.user_department);
        user_course_number = findViewById(R.id.user_course_number);
        user_course_section = findViewById(R.id.user_course_section);
        submit = findViewById(R.id.addClasses_button);

        // Get reference to the classes tree
        classes_ref = mDatabaseReference.child("classes");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct a class object using the info the user submitted
                final Course newCourse = new Course();
                newCourse.addStudent(user_key);
                newCourse.setDepartment(user_department.getText().toString().toUpperCase());
                newCourse.setNumber(user_course_number.getText().toString().toUpperCase());
                newCourse.setSection(user_course_section.getText().toString().toUpperCase());

                // Scan db to see if this class already exists
                classes_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot db_class : dataSnapshot.getChildren()) {
                                Course temp = db_class.getValue(Course.class);
                                if (temp.equals(newCourse)) {
                                    db_course_ref = db_class.getKey();
                                    temp.addStudent(user_key);
                                    user_ref.child("classes").push().setValue(db_course_ref);
                                    mDatabaseReference.child("classes").child(db_course_ref).setValue(temp);
                                    break;
                                }
                            }
                        } else {
                            Log.d(TAG, "dataSnaphot not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if (db_course_ref == null) {
                    // This is a new course
                    DatabaseReference newCourseRef = mDatabaseReference.child("classes").push();
                    newCourseRef.setValue(newCourse);
                    user_ref.child("classes").push().setValue(newCourseRef.getKey());
                }
            }
        });
    }
}
