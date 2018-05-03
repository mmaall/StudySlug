package org.studyslug.www.studyslug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class addClasses extends AppCompatActivity {

    private  static final String TAG = "addClasses";

    /// list components
    private ArrayList<String> listOfClasses;

    // Firebase stuff
    private DatabaseReference mDatabaseReference;
    private EditText user_department;
    private EditText user_course_number;
    private EditText user_course_section;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        Log.d(TAG, "onCreate: started");

        // Initialize database interactions
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Get references to input form
        user_department = findViewById(R.id.user_department);
        user_course_number = findViewById(R.id.user_course_number);
        user_course_section = findViewById(R.id.user_course_section);
        submit = findViewById(R.id.addClasses_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course newCourse = new Course();
                newCourse.addStudent("test this one");
                newCourse.setDepartment(user_department.getText().toString());
                newCourse.setNumber(user_course_number.getText().toString());
                newCourse.setSection(user_course_section.getText().toString());
                mDatabaseReference.child("classes").push().setValue(newCourse);
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
