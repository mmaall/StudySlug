package org.studyslug.www.studyslug;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RecyclerBuilder {
  private FirebaseDatabase firebaseDatabase;
  private RecyclerView recyclerView;
  private Context context;
  private String searchText;
  private String referenceText;
  private String orderElement;

  final private RecyclerView.ViewHolder viewHolder;
  private DatabaseReference dbReference;
  private static final String TAG = "RecyclerBuilder";

  public RecyclerBuilder(String searchText,
                         String referenceText,
                         String orderElement,
                         RecyclerView recyclerView,
                         Context context,
                         final RecyclerView.ViewHolder viewHolder) {
    firebaseDatabase = FirebaseDatabase.getInstance();
    this.searchText = searchText;
    this.referenceText = referenceText;
    this.orderElement = orderElement;
    this.recyclerView = recyclerView;
    this.context = context;
    this.viewHolder = viewHolder;
    Log.d(TAG, "RecyclerBuilder constructed with fields: " + searchText + ", "
               + referenceText + ", " +", " + orderElement + ", " + recyclerView);

    dbReference = FirebaseDatabase.getInstance()
                                        .getReference(referenceText)
                                        .child(searchText);
    Query firebaseSearchQuery = dbReference.orderByChild(orderElement);

    FirebaseRecyclerAdapter<String, FindPeopleActivity.UsersViewHolder> firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<String, viewHolder>(

            String.class,
            R.layout.list_layout,
            viewHolder.class,
            firebaseSearchQuery

        ) {
          @Override
          protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, String model,
                                            int position) {
             // TODO: Fix this


          }
        };

    recyclerView.setAdapter(firebaseRecyclerAdapter);

  }

}
