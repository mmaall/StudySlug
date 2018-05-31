package org.studyslug.www.studyslug;

import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AddCoursesActivity extends AppCompatActivity {

    // Layout
    private String dropdownText = "";
    private String searchText="";
    private EditText searchField;
    private ImageButton searchButton;
    private ImageButton imageButton;
    private CheckBox classCheck;
    private Spinner departmentSpinner;

    // Output
    private RecyclerView resultList;
    private List<String> areas;
    private List<String> classes = new ArrayList<>();

    // Database
    private DatabaseReference dbUserReference;
    private DatabaseReference dbCourseReference;
    private DatabaseReference userReference;
    private FirebaseUser      currentUser;
    private String            currentUserKey;

    ArrayAdapter<Course> departmentAdapter;
    private String[] availableDepartments = { "FILTER BY SUBJECT",
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

    public void buildDatabaseReferences(){
        dbUserReference = FirebaseDatabase.getInstance()
                .getReference("classes");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserKey = currentUser.getUid();

    }

    public void getLayout(){
        setContentView(R.layout.activity_add_courses);
        departmentSpinner = findViewById(R.id.add_courses_department_spinner);

        searchButton = findViewById(R.id.add_courses_big_magnifying_glass);
        imageButton = findViewById(R.id.add_courses_icon_button);
        resultList = findViewById(R.id.result_list_courses);
        resultList.setHasFixedSize(true);
        classCheck = findViewById(R.id.checkBox);

        resultList.setLayoutManager(new LinearLayoutManager(this));


    }

    public void getDepartmentAdapter(){
        // Initialize department spinner
        ArrayAdapter<String> departmentAdapter =
                new ArrayAdapter<>(AddCoursesActivity.this,
                        android.R.layout.simple_spinner_item, availableDepartments);

        departmentSpinner.setAdapter(departmentAdapter);

    }

    public void chooseDepartment(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dropdownText = departmentSpinner.getSelectedItem().toString();
                dropdownText = dropdownText.trim();
                firebaseUserSearch(dropdownText, searchText);
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
        addCoursesToUser(classes);

        Log.d("SpinnerGot", dropdownText);
        ImageButton findPeople = findViewById(R.id.add_courses_little_magnifying_glass);

        findPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCoursesActivity.this, FindPeopleActivity.class));
            }
        });


    }

    private void firebaseUserSearch(String dropdownText, String searchText) {


        Toast.makeText(AddCoursesActivity.this, "Finding Slugs!", Toast.LENGTH_LONG)
                .show();

        dbCourseReference = FirebaseDatabase.getInstance()
                .getReference("classes");

        final Query firebaseSearchQuery = dbCourseReference.orderByKey().startAt(dropdownText).endAt(dropdownText + searchText + "\uf8ff");

        final FirebaseRecyclerAdapter<Course, UsersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Course, UsersViewHolder>(

                        Course.class,
                        R.layout.course_list_layout,
                        UsersViewHolder.class,
                        firebaseSearchQuery

                )


                {
                    @Override
                    protected void populateViewHolder(final UsersViewHolder viewHolder, final Course model,
                                                      final int position) {
                        viewHolder.setDetails(getApplicationContext(), model);
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                classes.add(firebaseRecyclerAdapter.getRef(position).toString());


                            }
                        });
                        /**
                        viewHolder.checkSelect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox box = (CheckBox) v;
                                String classTitle = box.getTag().toString();
                                classes.add(classTitle);

                            }
                        });**/



                    }





                };


        resultList.setAdapter(firebaseRecyclerAdapter);


    }


    // View Holder Class
    public class UsersViewHolder extends RecyclerView.ViewHolder  {
        View mView;
        CheckBox checkSelect;



        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }


        public void setDetails(Context ctx, Course temp) {
            TextView classname = (TextView) mView.findViewById(R.id.User1_name);
            TextView classnumber = (TextView) mView.findViewById(R.id.Class_number);
            TextView section = (TextView) mView.findViewById(R.id.Course_section);
            checkSelect = itemView.findViewById(R.id.checkBox);
            classname.setText(temp.getName());
            classnumber.setText(temp.getNumber());
            section.setText(temp.getSection());

        }




    }




    public void addCoursesToUser(List<String> classesChosenByUser){

        userReference = FirebaseDatabase.getInstance().getReference()
                                                      .child("users")
                                                      .child(currentUserKey);
        for (String currentCourse : classesChosenByUser) {

               userReference.child("classes").push().setValue(currentCourse);

        }
    }
}
