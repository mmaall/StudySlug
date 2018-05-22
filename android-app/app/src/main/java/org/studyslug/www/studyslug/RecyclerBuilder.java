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
  private String childText;
  private RecyclerView.ViewHolder viewHolder;
  private DatabaseReference dbReference;
  private static final String TAG = "RecyclerBuilder";

  public RecyclerBuilder(String searchText,
                         String referenceText,
                         String childText,
                         String orderElement,
                         RecyclerView recyclerView,
                         Context context,
                         RecyclerView.ViewHolder viewHolder) {
    firebaseDatabase = FirebaseDatabase.getInstance();
    this.searchText = searchText;
    this.referenceText = referenceText;
    this.childText = childText;
    this.orderElement = orderElement;
    this.recyclerView = recyclerView;
    this.context = context;
    this.viewHolder = viewHolder;
    Log.d(TAG, "RecyclerBuilder constructed with fields: " + searchText + ", "
               + referenceText + ", " + childText + ", " + orderElement + ", " + recyclerView);

    dbReference = FirebaseDatabase.getInstance()
                                        .getReference(referenceText)
                                        .child(searchText)
                                        .child(childText);
    Query firebaseSearchQuery = dbReference.orderByChild(orderElement);

    FirebaseRecyclerAdapter<String, viewHolder> firebaseRecyclerAdapter =
        new FirebaseRecyclerAdapter<String, viewHolder>(

            String.class,
            R.layout.list_layout,
            viewHolder.class,
            firebaseSearchQuery

        ) {
          @Override
          protected void populateViewHolder(viewHolder, String model,
                                            int position) {
            viewHolder.setDetails(context(), model);
          }
        };

    recyclerView.setAdapter(firebaseRecyclerAdapter);

  }

}
