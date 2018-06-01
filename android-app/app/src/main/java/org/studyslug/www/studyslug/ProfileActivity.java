package org.studyslug.www.studyslug;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
    private Button RemoveClass;
    private Spinner classSpinner;
    private String UserName,UserEmail;

    private void getLayout(){
        setContentView(R.layout.activity_profile);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayoutElements();
        buildDatabaseReferences();
        setCoursesMenu(classSpinner);
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
                                .getCurrentUser()
                                .getUid()
                )
                .child("classes");
    }
    private void configureLayoutElements() {
        setContentView(R.layout.activity_profile);
        classSpinner = findViewById(R.id.user_classes_spinner);
        RemoveClass = findViewById(R.id.drop_button);
        TextView UserName = (TextView) findViewById(R.id.user_name);
        UserName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        TextView UserEmail = (TextView) findViewById(R.id.user_email);
        UserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

}
