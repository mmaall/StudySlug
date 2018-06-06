package org.studyslug.www.studyslug;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
    private ImageView userProfile;
    private Spinner classSpinner;
    private String UserName,UserEmail;
    String courseKey;
    private static final Client CLIENT = new Client(FirebaseAuth.getInstance().getCurrentUser());
    private static final User currentUser = new User(CLIENT);
    private static final String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayoutElements();
        buildDatabaseReferences();
        setCoursesMenu(classSpinner);
    }
    public void onClickRemove(View view) {
      try {
        courseKey = classSpinner.getSelectedItem().toString();
        firebaseDeleteCourse(courseKey);
      } catch (Exception e) {
        Log.d(TAG, "Drop courses exception");
        e.printStackTrace();
      }
    }

    public void onClickSignOut(View view) {
      try {
        signOut(view);
      } catch (Exception e) {
        Log.d(TAG, "Sign out exception");
        e.printStackTrace();
      }
    }

    public void onClickFindPeople(View view) {
      try {
        Intent findPeople =
            new Intent(ProfileActivity.this, FindPeopleActivity.class);
        startActivity(findPeople);
      } catch (Exception e) {
        Log.d(TAG, "Find people exception" );
        e.printStackTrace();
      }
    }

    public void onClickAddCourse(View view) {
      try {
        Intent addCourses =
            new Intent(ProfileActivity.this, AddCoursesActivity.class);
        startActivity(addCourses);
      } catch (Exception e) {
        Log.d(TAG, "Add courses exception");
        e.printStackTrace();
      }
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
            public void onCancelled(DatabaseError databaseError) {
              Log.d(TAG, "Database error");
              databaseError.toException().printStackTrace();
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
        TextView UserName = findViewById(R.id.user_name);
        TextView UserEmail = findViewById(R.id.user_email);
        try {
          UserName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
          UserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        } catch (Exception e) {
          Log.d(TAG, "null user");
          e.printStackTrace();
        }
        Uri userPhotoURL = Uri.parse(currentUser.getURI());
        if(userPhotoURL != null)
        {
            new ASyncTaskLoadImage(userProfile).execute(userPhotoURL.toString());
        }
        else
        {
            Toast.makeText(this,"Unable to retrieve URI", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut(View itemView){
        if(itemView.getId() == R.id.sign_out_button) {
            FirebaseAuth.getInstance()
                    .signOut();
            Intent backToSplash =
                    new Intent(ProfileActivity.this, SplashActivity.class);
            backToSplash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(backToSplash);
        }

    }

}
