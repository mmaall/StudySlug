package org.studyslug.www.studyslug;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;



import java.util.ArrayList;


public class classRecycleAdapter extends RecyclerView.Adapter<classRecycleAdapter.ViewHolder>  {
    private static final String TAG = "classRecycleAdapter";  //debug tag
    private ArrayList<String> classNames = new ArrayList<>(); //array of classes, potentially fed by scraper (we'll work this out)
    private Context mContext;



    public classRecycleAdapter(ArrayList<String> classes, Context context)
    {
        classNames = classes;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_recycler_layout,parent, false); //inflate item layout
        ViewHolder holder = new ViewHolder(view); //insert view into a new viewholder, recycling
        return null;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int positon)
    {
        Log.d(TAG , "onBindViewHolder: called "); ///this will show in log everytime a new vew is added to list
        holder.className.setText(classNames.get(positon));
        holder.parent_layout.setOnClickListener(new View.OnClickListener()  /// click do-er
        {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + classNames.get(positon));

                /// Toast.makeText(mContext,classNames.get(positon), Toast.LENGTH_SHORT).show();
                //  potentially add some functionality here, such as if there's any growing study groups when
                //  this class is clicked on?



            }
        });

    }
    @Override
    public int getItemCount()
    {
        return classNames.size(); /// how many items for the adapter?
    }

    public class ViewHolder extends RecyclerView.ViewHolder{      //ViewHolder Widget Implemntation

        TextView className;
        RelativeLayout parent_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_Title);
            parent_layout = itemView.findViewById(R.id.parent_layout);

        }
    }


}
