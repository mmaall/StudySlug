package org.studyslug.www.studyslug;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddCoursesActivity extends AppCompatActivity {

  // Layout
  private String dropdownText = "";
  private String searchText = "";
  private ImageButton searchButton;
  private ImageButton userPhoto;
  private Spinner departmentSpinner;
  // Output
  private RecyclerView resultList;

  // Database
  private FirebaseUser currentUser;
  private DatabaseReference dbReference;
  private DatabaseReference dbUserReference;
  private DatabaseReference checkClassesReference;
  private DatabaseReference dbCourseReference;
  private DatabaseReference userCourseReference;
  private User user;

  private static final Client CLIENT = new Client(FirebaseAuth.getInstance().getCurrentUser());
  private static final String CLIENT_KEY = CLIENT.getUserName();
  private static final String TAG = "AddCoursesActivity";


  ArrayAdapter<Course> departmentAdapter;
  private void setUserImage() {
     Uri userPhotoURL = currentUser.getPhotoUrl();
     Log.d(TAG, "userPhotoURL: " + userPhotoURL);
     if(userPhotoURL != null)
     {
//         userPhoto.setImageURI(null);
         new ASyncTaskLoadImage(userPhoto).execute(userPhotoURL.toString());
     }
     else
     {
         Toast.makeText(this,"Unable to retrieve URI", Toast.LENGTH_SHORT).show();
     }
}
  // TODO I thought we were going to make this read from the db?
  private String[] availableDepartments = {"FILTER BY SUBJECT",
      "ACEN", "AMST", "ANTH", "APLX", "AMS", "ARAB", "ARTG",
      "ASTR", "BIOC", "BIOL", "BIOE", "BME", "CHEM", "CHIN", "CLEI",
      "CLNI", "CLTE", "CMMU", "CMPM", "CMPE", "CMPS", "COWL", "LTCR",
      "CRES", "CRWN", "DANM", "EART", "ECON", "EDUC", "EE", "ENGR",
      "LTEL", "ENVS", "ETOX", "FMST", "FILM", "FREN", "LTFR", "GAME",
      "GERM", "LTGE", "GREE", "LTGR", "HEBR", "HNDI", "HIS", "HAVC",
      "HISC", "HUMN", "ISM", "ITAL", "LTIT", "JAPN", "JWST", "KRSG",
      "LAAD", "LATN", "LALS", "LTIN", "LGST", "LING", "LIT", "MATH",
      "MERR", "METX", "LTMO", "MUSC", "OAKS", "OCEA", "PHIL", "PHYE",
      "POLI", "PRTR", "PORT", "LTPR", "PSYC", "PUNJ", "RUSS", "SCIC",
      "SOCD", "SOCS", "SOCY", "SPAN", "SPHS", "SPSS", "LTSP", "STEV",
      "TIM", "THEA", "UCDC", "WMST", "LTWL", "WRIT", "YIDD"
  };

  public void buildDatabaseReferences() {
    // TODO add log messages in verbose log for all these refs

    dbReference = FirebaseDatabase.getInstance().getReference();

    dbUserReference = FirebaseDatabase.getInstance()
                                      .getReference("users")
                                      .child(CLIENT_KEY);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    user = new User(CLIENT);

    checkClassesReference = FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(CLIENT_KEY)
                                            .child("classes");

    dbCourseReference = FirebaseDatabase.getInstance()
                                        .getReference("classes");

    userCourseReference = dbUserReference.child("classes");
  }

  public void getLayout() {
    setContentView(R.layout.activity_add_courses);
    departmentSpinner = findViewById(R.id.add_courses_department_spinner);
    userPhoto = findViewById(R.id.add_courses_icon_button);
    searchButton = findViewById(R.id.add_courses_big_magnifying_glass);
    resultList = findViewById(R.id.result_list_courses);
    resultList.setHasFixedSize(true);

    resultList.setLayoutManager(new LinearLayoutManager(this));
  }

  public void getDepartmentAdapter() {
    // Initialize department spinner
    ArrayAdapter<String> departmentAdapter =
        new ArrayAdapter<>(AddCoursesActivity.this,
                           android.R.layout.simple_spinner_item, availableDepartments);

    departmentSpinner.setAdapter(departmentAdapter);
    try {
      Field popup = Spinner.class.getDeclaredField("mPopup");
      popup.setAccessible(true);

      android.widget.ListPopupWindow popupWindow =
          (android.widget.ListPopupWindow) popup.get(departmentSpinner);

      popupWindow.setHeight(600);
      popupWindow.setVerticalOffset(30);
    } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
      // silently fail.
    }
  }

  public void chooseDepartment() {
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String dropdownText = departmentSpinner.getSelectedItem().toString();
        dropdownText = dropdownText.trim();
        firebaseClassSearch(dropdownText, searchText);
      }
    });
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    buildDatabaseReferences();
    getLayout();
    getDepartmentAdapter();
    chooseDepartment();
      setUserImage();

    Log.d(TAG, "SpinnerGot " + dropdownText);
    ImageButton findPeople = findViewById(R.id.add_courses_little_magnifying_glass);

    findPeople.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(AddCoursesActivity.this, FindPeopleActivity.class));
      }
    });


    userPhoto.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) { startActivity(new Intent(AddCoursesActivity.this, ProfileActivity.class)); }
      });

  }

  private void firebaseClassSearch(String dropdownText, String searchText) {

    Toast.makeText(AddCoursesActivity.this, "Finding Classes!", Toast.LENGTH_SHORT)
         .show();

    final Query firebaseSearchQuery = dbCourseReference.orderByKey()
                                                       .startAt(dropdownText)
                                                       .endAt(dropdownText + searchText + "\uf8ff");

    FirebaseRecyclerAdapter<Course, UsersViewHolder> firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<Course, UsersViewHolder>(
            Course.class,
            R.layout.course_list_layout,
            UsersViewHolder.class,
            firebaseSearchQuery
        ) {
          @Override
          protected void populateViewHolder(UsersViewHolder viewHolder, final Course model,
                                            int position) {
            viewHolder.setDetails(getApplicationContext(), model);
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final String courseKey = model.getKey();
                Log.d(TAG, "courseKey: " + courseKey);

                userCourseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(courseKey).exists()) {

                      dbUserReference.child("classes")
                                     .child(courseKey)
                                     .setValue("0");

                      dbReference.child("StudySlugClasses")
                                 .child(courseKey)
                                 .child("students")
                                 .child(CLIENT_KEY)
                                 .child("displayName")
                                 .setValue(CLIENT.getDisplayName());


                      dbReference.child("StudySlugClasses")
                              .child(courseKey)
                              .child("students")
                              .child(CLIENT_KEY)
                              .child("email")
                              .setValue(CLIENT.getEmail());

                      dbReference.child("StudySlugClasses")
                              .child(courseKey)
                              .child("students")
                              .child(CLIENT_KEY)
                              .child("uri")
                              .setValue(CLIENT.getPhotoUri().toString());

                      Toast.makeText(
                              AddCoursesActivity.this,
                              "You are now enrolled in " + model.getName() + "!",
                              Toast.LENGTH_LONG)
                           .show();
                      Log.d(TAG, "Enrolled user in course " + courseKey);
                    } else {
                      Toast.makeText(
                              AddCoursesActivity.this,
                              "Already enrolled in " + model.getName() + "!",
                              Toast.LENGTH_LONG)
                           .show();
                      Log.d(TAG, "User already enrolled in course " + courseKey);
                    }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "Canceled?");
                  }
                });
              }

            });
          }
        };
    resultList.setAdapter(firebaseRecyclerAdapter);
  }


  // View Holder Class
  public static class UsersViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {
    View mView;

    public UsersViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
      itemView.setOnClickListener(this);  // listener for each row.
    }

    public void setDetails(Context ctx, Course temp) {
      TextView classname = mView.findViewById(R.id.User1_name);
      TextView classnumber = mView.findViewById(R.id.Class_number);
      TextView section = mView.findViewById(R.id.Course_section);
      classname.setText(temp.getName());
      classnumber.setText(temp.getNumber());
      section.setText(temp.getSection());
    }

    @Override
    public void onClick(View v) {
      int clickPosition = getAdapterPosition();
    }
  }
}