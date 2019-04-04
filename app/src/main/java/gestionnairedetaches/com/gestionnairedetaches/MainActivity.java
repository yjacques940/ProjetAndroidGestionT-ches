package gestionnairedetaches.com.gestionnairedetaches;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Tâches");
        auth = FirebaseAuth.getInstance();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.button_logOut).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                logOut();
            }
        });
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

    private void logOut() {
        auth.signOut();
        updateUI(auth.getCurrentUser());
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
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
