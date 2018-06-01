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
import android.util.Log;

import java.lang.reflect.Field;

import java.util.List;

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
  private EditText searchField;
  private ImageButton searchButton;
  private ImageButton imageButton;
  private Spinner departmentSpinner;

  // Output
  private RecyclerView resultList;
  private List<String> areas;
  private List<String> classes;

  // Database
  private DatabaseReference dbUserReference;
  private DatabaseReference checkClassesReference;
  private DatabaseReference dbCourseReference;
  private DatabaseReference userReference;
  private DatabaseReference userCourseReference;
  private DatabaseReference userToClass;
  private FirebaseUser currentUser;
  private String currentUserKey;

  ArrayAdapter<Course> departmentAdapter;

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
    dbUserReference = FirebaseDatabase.getInstance()
                                      .getReference("classes");

    currentUser = FirebaseAuth.getInstance()
                              .getCurrentUser();

    userToClass = FirebaseDatabase.getInstance()
                                  .getReference();

    currentUserKey = currentUser.getUid();

    checkClassesReference = FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(FirebaseAuth.getInstance()
                                                               .getCurrentUser()
                                                               .getUid())
                                            .child("classes");
  }

  public void getLayout() {
    setContentView(R.layout.activity_add_courses);
    departmentSpinner = findViewById(R.id.add_courses_department_spinner);

    searchButton = findViewById(R.id.add_courses_big_magnifying_glass);
    imageButton = findViewById(R.id.add_courses_icon_button);
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
    //addCoursesToUser(classes);

    Log.d("SpinnerGot", dropdownText);
    ImageButton findPeople = findViewById(R.id.add_courses_little_magnifying_glass);

    findPeople.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(AddCoursesActivity.this, FindPeopleActivity.class));
      }
    });


  }

  private void firebaseClassSearch(String dropdownText, String searchText) {

    Toast.makeText(AddCoursesActivity.this, "Finding Classes!", Toast.LENGTH_SHORT)
         .show();

    dbCourseReference = FirebaseDatabase.getInstance()
                                        .getReference("classes");
    userReference = FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(currentUserKey);
    userCourseReference = userReference.child("classes");
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
                final String classkey =
                    model.getDepartment() + " " +
                    model.getNumber() + " - " +
                    model.getSection() + " " +
                    model.getName();

                userCourseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(classkey).exists()) {

                      userReference.child("classes")
                                   .child(classkey)
                                   .setValue("0");

                      userToClass.child("StudySlugClasses")
                                 .child(classkey)
                                 .child("students")
                                 .child
                                     (
                                         FirebaseAuth.getInstance()
                                                     .getCurrentUser()
                                                     .getEmail()
                                                     .split("@")[0]
                                     )
                                 .child("name")
                                 .setValue
                                     (
                                         FirebaseAuth.getInstance()
                                                     .getCurrentUser()
                                                     .getDisplayName()
                                     );

                      userToClass.child("StudySlugClasses")
                                 .child(classkey)
                                 .child("students")
                                 .child
                                     (
                                         FirebaseAuth.getInstance()
                                                     .getCurrentUser()
                                                     .getEmail()
                                                     .split("@")[0]
                                     )
                                 .child("email")
                                 .setValue
                                     (
                                         FirebaseAuth.getInstance()
                                                     .getCurrentUser()
                                                     .getEmail()
                                     );

                      Toast.makeText(AddCoursesActivity.this,
                                     "You are now enrolled in " + model.getName() + "!",
                                     Toast.LENGTH_LONG)
                           .show();
                      // TODO add log message for successful enrollment

                    } else {
                      Toast.makeText(AddCoursesActivity.this,
                                     "Already enrolled in " + model.getName() + "!",
                                     Toast.LENGTH_LONG)
                           .show();
                      // TODO add log message for 'already enrolled'
                    }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                    // TODO add at least a log message here
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
      itemView.setOnClickListener((View.OnClickListener) this);  // listener for each row.
    }


    public void setDetails(Context ctx, Course temp) {
      TextView classname = (TextView) mView.findViewById(R.id.User1_name);
      TextView classnumber = (TextView) mView.findViewById(R.id.Class_number);
      TextView section = (TextView) mView.findViewById(R.id.Course_section);
      classname.setText(temp.getName());
      classnumber.setText(temp.getNumber());
      section.setText(temp.getSection());
    }


    @Override
    public void onClick(View v) {
      int clickPosition = getAdapterPosition();
    }
  }


 /*   public void addCoursesToUser(List<String> classesChosenByUser){

        userReference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserKey);
        for (String currentCourse : classesChosenByUser) {

            userReference.child("classes").push().setValue(currentCourse);

        }
    }
}
*/
}