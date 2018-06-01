package org.studyslug.www.studyslug;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

    private void getLayout(){
        setContentView(R.layout.activity_profile);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setCoursesMenu(final Spinner peopleSpinner) {

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

                peopleSpinner.setAdapter(areasAdapter);

                try {
                    String classKey = peopleSpinner.getSelectedItem().toString();
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
                                .getCurrentUser()
                                .getUid()
                )
                .child("classes");
    }
    private void configureLayoutElements() {
    }

}
