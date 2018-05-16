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

public class findPeople extends AppCompatActivity {

    private String class_key;
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private ImageButton imageButton;

    private String text;
    private RecyclerView mResultList;
    private List<String> areas;

    private DatabaseReference mUserDatabase;
    private DatabaseReference mClassDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);
        final Spinner areaSpinner = (Spinner) findViewById(R.id.spinner2);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("classes");

        mSearchBtn = (ImageButton) findViewById(R.id.imageButton4);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                areas = new ArrayList<String>();
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.getValue(String.class);
                    areas.add(areaName);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(findPeople.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);

                try {
                    class_key = areaSpinner.getSelectedItem().toString();
                }
                catch(Exception e){
                    System.out.printf("No classes in spinner");
                    Intent mainIntent = new Intent(findPeople.this, addCoursesActivity.class);
                    startActivity(mainIntent);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = areaSpinner.getSelectedItem().toString();
                firebaseUserSearch(searchText);
            }
        });

        ImageButton addClasses = (ImageButton) findViewById(R.id.imageButton2);

        addClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(findPeople.this, addCoursesActivity.class));
            }
        });
    }
    private void firebaseUserSearch(String searchText) {


        Toast.makeText(findPeople.this, "Started Slug Search!", Toast.LENGTH_LONG).show();
        mClassDatabase = FirebaseDatabase.getInstance().getReference("classes").child(searchText).child("students");
        Query firebaseSearchQuery = mClassDatabase.orderByChild("email");

        FirebaseRecyclerAdapter<String, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String, UsersViewHolder>(

                String.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, String model, int position) {


                viewHolder.setDetails(getApplicationContext(), model);

            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    // View Holder Class

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String userName){

            TextView user_name = (TextView) mView.findViewById(R.id.User1_name);

            user_name.setText(userName);



        }

    }

}
