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
    private HashMap<Course, String> dict_of_classes;

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

        // Construct list of classes already in the db
        classes_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot db_class : dataSnapshot.getChildren()) {
                        Course temp = db_class.getValue(Course.class);
                        String temp_key = db_class.getKey();
                        dict_of_classes.put(temp, temp_key);
                        Toast.makeText(addClasses.this, "Found db_class: " + temp.getDepartment(),Toast.LENGTH_SHORT).show();

                        /*
                        if (db_class != null) {
                            Course temp_course = db_class.getValue(Course.class);
                            String temp_key = db_class.getKey();
                            if (temp_course != null && temp_key != null) {
                                dict_of_classes.put(temp_course, temp_key);
                            }
                        } else {
                            Log.d(TAG, "db_class not found");
                        }
                        */

                    }
                } else {
                    Log.d(TAG, "dataSnaphot not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct a class object using the info the user submitted
                Course newCourse = new Course();
                newCourse.addStudent(user_key);
                newCourse.setDepartment(user_department.getText().toString());
                newCourse.setNumber(user_course_number.getText().toString());
                newCourse.setSection(user_course_section.getText().toString());

                // Check to see if this course is listed in the db already
                // If it is, just add this student key, otherwise push a new course
                if (dict_of_classes != null && dict_of_classes.containsKey(newCourse)) {
                    // mDatabaseReference.child("classes").child("students").setValue(user_key);
                       String class_key = dict_of_classes.get(newCourse);
                       mDatabaseReference.child("classes").child(class_key).setValue(newCourse);

                } else {
                    mDatabaseReference.child("classes").push().setValue(newCourse);
                }
            }
        });

    }


}
