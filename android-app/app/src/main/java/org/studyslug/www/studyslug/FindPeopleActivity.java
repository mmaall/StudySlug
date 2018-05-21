package org.studyslug.www.studyslug;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.Intent;

import java.util.List;
import java.util.ArrayList;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
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
  private ImageButton imageButton;

  private String text;
  private RecyclerView resultList;
  private List<String> areas;

  private DatabaseReference dbUserReference;
  private DatabaseReference dbCourseReference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_people);
    final Spinner areaSpinner = findViewById(R.id.spinner2);
    dbUserReference = FirebaseDatabase.getInstance()
                                      .getReference("users")
                                      .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                      .child("classes");
    searchButton = findViewById(R.id.imageButton4);
    imageButton = findViewById(R.id.imageButton);
    resultList = findViewById(R.id.result_list);
    resultList.setHasFixedSize(true);

    resultList.setLayoutManager(new LinearLayoutManager(this));

    dbUserReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        areas = new ArrayList<>();
        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
          String areaName = areaSnapshot.getValue(String.class);
          areas.add(areaName);
        }

        ArrayAdapter<String> areasAdapter =
            new ArrayAdapter<>(FindPeopleActivity.this,
                               android.R.layout.simple_spinner_item, areas);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areasAdapter);

        try {
          classKey = areaSpinner.getSelectedItem().toString();
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

    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String searchText = areaSpinner.getSelectedItem().toString();
        firebaseUserSearch(searchText);
      }
    });

    ImageButton addClasses = findViewById(R.id.imageButton2);

    addClasses.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(FindPeopleActivity.this, AddCoursesActivity.class));
      }
    });
  }

  private void firebaseUserSearch(String searchText) {


    Toast.makeText(FindPeopleActivity.this, "Finding Slugs!", Toast.LENGTH_LONG)
         .show();
    dbCourseReference = FirebaseDatabase.getInstance()
                                        .getReference("classes")
                                        .child(searchText)
                                        .child("students");
    Query firebaseSearchQuery = dbCourseReference.orderByChild("email");

    FirebaseRecyclerAdapter<String, UsersViewHolder> firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<String, UsersViewHolder>(

            String.class,
            R.layout.list_layout,
            UsersViewHolder.class,
            firebaseSearchQuery

        ) {
          @Override
          protected void populateViewHolder(UsersViewHolder viewHolder, String model,
                                            int position) {
            viewHolder.setDetails(getApplicationContext(), model);
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

    public void setDetails(Context ctx, String userName) {
      TextView user_name = (TextView) mView.findViewById(R.id.User1_name);
      user_name.setText(userName);
    }
  }
}
