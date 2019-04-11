package gestionnairedetaches.com.gestionnairedetaches;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
FirebaseAuth auth;
FirebaseFirestore db;
boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            logOut();
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
        setTitle("Tâches");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.btn_addTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAddTask();
            }
        });
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
