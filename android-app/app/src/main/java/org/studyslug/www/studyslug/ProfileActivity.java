package org.studyslug.www.studyslug;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private List<String> areas;
    private DatabaseReference dbUserReference;
    private DatabaseReference dbCourseReference;
    private FirebaseAuth auth;
    private Button removeCourse;
    private Button addCourse;
    private Button findPeople;
    private Button signOut;
    private ImageView userProfile;
    private Spinner classSpinner;
    private String UserName,UserEmail;
    String courseKey;
    private static final Client CLIENT = new Client(FirebaseAuth.getInstance().getCurrentUser());
    private static final User currentUser = new User(CLIENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayoutElements();
        buildDatabaseReferences();
        setCoursesMenu(classSpinner);

        setOnClickListeners();
    }
    private void setOnClickListeners() {
        removeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseKey = classSpinner.getSelectedItem().toString();
                firebaseDeleteCourse(courseKey);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SignOut();
                FirebaseAuth.getInstance().signOut();
                   Intent backToSplash =
                           new Intent(ProfileActivity.this,SplashActivity.class);
                   startActivity(backToSplash);


            }
        });
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCourses =
                        new Intent(ProfileActivity.this, AddCoursesActivity.class);
                startActivity(addCourses);
            }
        });
        findPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findPeople =
                        new Intent(ProfileActivity.this, FindPeopleActivity.class);
                startActivity(findPeople);

            }
        });
    }
    protected void firebaseDeleteCourse(String courseKey)
    {
        dbCourseReference.child(courseKey).child("students").child(currentUser.getUserName()).removeValue();
        dbUserReference.child(courseKey).removeValue();

    }
    private void setCoursesMenu(final Spinner classSpinner) {

        dbUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                areas = new ArrayList<>();
                areas.add("SELECT CLASS");
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.getKey().toString();
                    areas.add(areaName);
                }

                ArrayAdapter<String> areasAdapter =
                        new ArrayAdapter<>(ProfileActivity.this,
                                android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                classSpinner.setAdapter(areasAdapter);

                try {
                    String classKey = classSpinner.getSelectedItem().toString();
                    classKey = Normalizer.normalize(classKey, Normalizer.Form.NFD);
                } catch (Exception e) {
                    System.out.print("No classes in spinner");
                    Intent mainIntent =
                            new Intent(ProfileActivity.this, AddCoursesActivity.class);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { // TODO Something here?
            }
        });


    }
    private void buildDatabaseReferences() {
        dbUserReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(
                        FirebaseAuth.getInstance()
                                .getCurrentUser().getEmail().split("@")[0]

                )
                .child("classes");
        dbCourseReference = FirebaseDatabase.getInstance()
                .getReference("StudySlugClasses");
    }
    private void configureLayoutElements() {
        setContentView(R.layout.activity_profile);
        classSpinner = findViewById(R.id.user_classes_spinner);
        removeCourse = findViewById(R.id.drop_button);
        userProfile = findViewById(R.id.user_picture);
        findPeople = findViewById(R.id.find_people_button);
        addCourse = findViewById(R.id.add_courses_button);
        TextView UserName = (TextView) findViewById(R.id.user_name);
        UserName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        TextView UserEmail = (TextView) findViewById(R.id.user_email);
        UserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Uri userPhotoURL = Uri.parse(currentUser.getURI());
        if(userPhotoURL != null)
        {
//        userPhoto.setImageURI(null);
            new ASyncTaskLoadImage(userProfile).execute(userPhotoURL.toString());
        }
        else
        {
            Toast.makeText(this,"Unable to retrieve URI", Toast.LENGTH_SHORT).show();
        }
    }

}
