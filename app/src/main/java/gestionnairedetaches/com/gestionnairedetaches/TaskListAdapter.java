package gestionnairedetaches.com.gestionnairedetaches;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import gestionnairedetaches.com.gestionnairedetaches.Model.TaskModel;
import io.opencensus.tags.Tag;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {
    private ArrayList<TaskModel> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewTitre;
        public TextView textViewTitreDesc;
        public Button btnComplete;
        public Button btnDelete;
        public ImageView image;


        public MyViewHolder(View v) {
            super(v);
            textViewTitre = v.findViewById(R.id.Title);
            textViewTitreDesc = v.findViewById(R.id.Description);
            btnComplete = v.findViewById(R.id.button_complete);
            btnDelete = v.findViewById(R.id.button_delete);
            image = v.findViewById(R.id.imageView_taskImage);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskListAdapter(ArrayList<TaskModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TaskModel taskToDisplay = mDataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewTitre.setText(taskToDisplay.getTitle());
        holder.textViewTitreDesc.setText(taskToDisplay.getDescription());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.confirm_dialog);
                Button dialogButtonAccept = dialog.findViewById(R.id.btn_acceptCompletion);
                Button dialogButtonDeny = dialog.findViewById(R.id.btn_deniedCompletion);
                dialogButtonAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskToDisplay.deleteTask();
                        dialog.dismiss();
                    }
                });

                dialogButtonDeny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskToDisplay.completeTask();
            }
        });
        holder.image.setImageBitmap(null);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if(!TextUtils.isEmpty(taskToDisplay.getPathToImage())){

            storage.getReference().child(taskToDisplay.getPathToImage()).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.image.setImageBitmap(bitmap);
                }
            });
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setNewList(ArrayList<TaskModel> newTaskList){
        mDataset = newTaskList;
    }
}