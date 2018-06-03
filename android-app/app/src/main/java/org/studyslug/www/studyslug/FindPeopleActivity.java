package org.studyslug.www.studyslug;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.Intent;

import java.io.InputStream;
import java.net.URL;
import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;
import java.net.URI;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FindPeopleActivity extends AppCompatActivity {

  private String classKey;
  private EditText searchField;
  private ImageButton searchButton;
  private ImageButton addClasses;
  private ImageButton emailButton;
  private String text;
  private RecyclerView resultList;
  private List<String> areas;
  private DatabaseReference dbUserReference;
  private DatabaseReference dbCourseReference;
  private Spinner areaSpinner;
  private ImageButton userPhoto;
  private Uri userPhotoURL;
  private FirebaseUser currentUser;
  private static final String TAG = "FindPeopleActivity";

  private void configureLayoutElements() {
    setContentView(R.layout.activity_find_people);
    areaSpinner = findViewById(R.id.find_people_spinner);
    searchButton = findViewById(R.id.find_people_magnifying_glass);
    userPhoto = findViewById(R.id.find_people_picture_button);
    addClasses = findViewById(R.id.find_people_plus_button);
    resultList = findViewById(R.id.result_list);
    resultList.setHasFixedSize(true);
    resultList.setLayoutManager(new LinearLayoutManager(this));
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
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
  }

  private void setUserImage() {
    userPhotoURL = currentUser.getPhotoUrl();
    Log.d(TAG, "userPhotoURL: " + userPhotoURL);
    if(userPhotoURL != null)
    {
//        userPhoto.setImageURI(null);
      new ASyncTaskLoadImage(userPhoto).execute(userPhotoURL.toString());
    }
    else
    {
      Toast.makeText(this,"Unable to retrieve URI", Toast.LENGTH_SHORT).show();
    }
  }

  private void setOnClickListeners() {
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String searchText = areaSpinner.getSelectedItem().toString();
        firebaseUserSearch(searchText);
      }
    });

    addClasses.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(FindPeopleActivity.this, AddCoursesActivity.class));
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    configureLayoutElements();
    buildDatabaseReferences();
    setOnClickListeners();
    setUserImage();
    setCoursesMenu(areaSpinner);
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
            new ArrayAdapter<>(FindPeopleActivity.this,
                               android.R.layout.simple_spinner_item, areas);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        peopleSpinner.setAdapter(areasAdapter);

        try {
          classKey = peopleSpinner.getSelectedItem().toString();
          classKey = Normalizer.normalize(classKey, Normalizer.Form.NFD);
        } catch (Exception e) {
          System.out.print("No classes in spinner");
          Intent mainIntent =
              new Intent(FindPeopleActivity.this, AddCoursesActivity.class);
          startActivity(mainIntent);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) { // TODO Something here?
      }
    });


  }

  private void firebaseUserSearch(final String searchText) {
    Toast.makeText(FindPeopleActivity.this, "Finding Slugs!", Toast.LENGTH_SHORT)
         .show();

    dbCourseReference = FirebaseDatabase.getInstance()
                                        .getReference("StudySlugClasses")
                                        .child(searchText)
                                        .child("students");

    Query firebaseSearchQuery = dbCourseReference.orderByKey();

    FirebaseRecyclerAdapter<User, UsersViewHolder> firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<User, UsersViewHolder>(

            User.class,
            R.layout.list_layout,
            UsersViewHolder.class,
            firebaseSearchQuery

        ) {
          @Override
          protected void populateViewHolder(UsersViewHolder viewHolder, final User model,
                                            final int position) {

            viewHolder.setDetails(getApplicationContext(), model);
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", model.getEmail(), null));

                intent.putExtra(Intent.EXTRA_SUBJECT,
                                "You've been invited to study through StudySlug!");

                intent.putExtra(Intent.EXTRA_TEXT,
                                "Hi " + model.getName() + ",\nI saw you were looking to study for "
                                + searchText + ". We should study together!  \n\n -" + FirebaseAuth
                                    .getInstance().getCurrentUser().getDisplayName());
                startActivity(Intent.createChooser(intent, "Choose application:"));
              }
            });
          }
        };
    resultList.setAdapter(firebaseRecyclerAdapter);

  }


  // View Holder Class
  public static class UsersViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public UsersViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
    }

    public void setDetails(Context ctx, User model) {
      TextView user_name = (TextView) mView.findViewById(R.id.User1_name);

      user_name.setText(
          model.getName().split(" ")[0] +
          model.getName().split(" ")[1].charAt(0)
      );
    }
  }
}
