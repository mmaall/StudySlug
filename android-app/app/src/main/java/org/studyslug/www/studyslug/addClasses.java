package org.studyslug.www.studyslug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

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
    private ArrayList<Course> list_of_classes;

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
        if (user != null) {
            // user_name = user.getDisplayName();
            user_email = user.getEmail();
        } else {
            // TODO fix this so we go back to the login page
        }

        // Create reference to users tree
        DatabaseReference user_tree = mDatabaseReference.child("users");

        // Iterate through that tree to find reference to our user
        user_tree.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.getValue(Users.class).getEmail();
                    if (email.equals(user_email)) {
                        user_key = userSnapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // save some time by saving a reference to the user's entry in the db
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
                for (DataSnapshot db_class : dataSnapshot.getChildren()) {
                    list_of_classes.add(db_class.getValue(Course.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct a class using the info the user submitted
                Course newCourse = new Course();
                newCourse.addStudent(user_key);
                newCourse.setDepartment(user_department.getText().toString());
                newCourse.setNumber(user_course_number.getText().toString());
                newCourse.setSection(user_course_section.getText().toString());
                newCourse.addStudent(user_key);

                // Check to see if this course is listed in the db already
                // If it is, just add this student key, otherwise push a new course
                if (list_of_classes.contains(newCourse)) {

                } else {
                    mDatabaseReference.child("classes").push().setValue(newCourse);
                }
            }
        });

    }

    /*
    TODO: remove this whole method
    private void sampleFillList()    /// testing RecyclerView
    {
        listOfClasses.add("CMPE 110");
        listOfClasses.add("CMPE 16");
        listOfClasses.add("CMPE 12");
        listOfClasses.add("CMPS 101");
        listOfClasses.add("CMPS 102");
        listOfClasses.add("CMPS 111");
        listOfClasses.add("CMPS 115");

        initRecycler();
    }

    private void initRecycler()
    {
        Log.d(TAG, "initRecycler:initRecyler ");
        RecyclerView recyclerView = findViewById(R.id.class_recycle);
        classRecycleAdapter adapter = new classRecycleAdapter(listOfClasses, this); // pass this activity context and dummy array
        recyclerView.setAdapter(adapter);   // set adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //format

    }
    */
}
