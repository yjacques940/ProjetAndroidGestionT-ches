package gestionnairedetaches.com.gestionnairedetaches;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gestionnairedetaches.com.gestionnairedetaches.Model.TaskModel;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {
    private ArrayList<TaskModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewTitre;
        public TextView textViewTitreDesc;
        public MyViewHolder(View v) {
            super(v);
            textViewTitre = v.findViewById(R.id.Title);
            textViewTitreDesc = v.findViewById(R.id.Description);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskListAdapter(ArrayList<TaskModel> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TaskListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_of_recycler, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewTitre.setText(mDataset.get(position).getTitle());
        holder.textViewTitreDesc.setText(mDataset.get(position).getDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}