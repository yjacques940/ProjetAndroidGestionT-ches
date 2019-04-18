package gestionnairedetaches.com.gestionnairedetaches;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import gestionnairedetaches.com.gestionnairedetaches.Model.TaskModel;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<TaskModel> listOfTasks = new ArrayList<TaskModel>();

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Si vous voulez vous déconnecter, clicker de nouveau sur retour", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_log_out, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaskListAdapter(listOfTasks);
        recyclerView.setAdapter(mAdapter);

        setTitle("Tâches");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setListener();

        initList();
    }

    private void setListener() {
        findViewById(R.id.btn_addTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAddTask();
            }
        });
    }

    private void initList(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            DocumentReference userDocument = db.collection("User").document(auth.getCurrentUser().getUid().toString());

            userDocument.collection("Tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("Listen failed.", e);
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        TaskModel task = doc.toObject(TaskModel.class);
                        task.setDocumentId(doc.getId());
                        listOfTasks.add(task);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void moveToAddTask(){
        Intent sendToNewTaskIntent = new Intent (this,NewTaskActivity.class);
        startActivity(sendToNewTaskIntent);
    }


    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void logOut() {
        auth.signOut();
        updateUI(auth.getCurrentUser());
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser == null)
        {
            sendUserToSignUpOrLoginActivity();
        }
    }

    private void sendUserToSignUpOrLoginActivity()
    {
        Intent intent = new Intent(this,SignUpOrLogInActivity.class);
        startActivity(intent);
    }

}
