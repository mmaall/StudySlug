package org.studyslug.www.studyslug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.RelativeLayout;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import android.content.Context;


public class addClasses extends AppCompatActivity {

    private  static final String TAG = "addClasses";

    /// list components
    private RecyclerView classList;
    private RecyclerView.Adapter classAdapter;
    private RecyclerView.LayoutManager classLayoutManager;
    private ArrayList<String> listOfClasses = new ArrayList<>();  /// populate this with classes
     





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);
        Log.d(TAG, "onCreate: started");
        sampleFillList();

    }

    private void sampleFillList()    /// testing RecyclerView
    {
        listOfClasses.add("CMPE 110");
        listOfClasses.add("CMPE 16");
        listOfClasses.add("CMPE 12");
        listOfClasses.add("CMPS 101");
        listOfClasses.add("CMPS 102");
        listOfClasses.add("CMPS 111");
        listOfClasses.add("CMPS 115");

        initRecycler();
    }
    private void initRecycler()
    {
        Log.d(TAG, "initRecycler:initRecyler ");
        RecyclerView recyclerView = findViewById(R.id.class_recycle);
        classRecycleAdapter adapter = new classRecycleAdapter(listOfClasses, this); // pass this activity context and dummy array
        recyclerView.setAdapter(adapter);   // set adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //format

    }
}
