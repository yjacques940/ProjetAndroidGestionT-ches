package gestionnairedetaches.com.gestionnairedetaches;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import gestionnairedetaches.com.gestionnairedetaches.Model.UserModel;

public class SignUpActivity extends AppCompatActivity {
FirebaseAuth auth;
FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Inscription");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setListener();
    }

    private void setListener() {

        findViewById(R.id.button_signUp).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                signUp();
            }
        });
    }

    private void signUp() {
        final ProgressBar progressBar = findViewById(R.id.progressBar_signup);
        progressBar.setVisibility(View.VISIBLE);
        final EditText userEmail = findViewById(R.id.editText_signUp_email);
        final EditText password = findViewById(R.id.editText_signUp_password);
        EditText passwordConfirm = findViewById(R.id.editText_signUp_confirm_password);
        final EditText userName = findViewById(R.id.editText_signUp_name);
        if(!password.getText().toString().equals( passwordConfirm.getText().toString())){
            Toast.makeText(getApplicationContext(),
                    "Les mots de passes ne sont pas identiques.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(userEmail.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    UserModel user = new UserModel(userName.getText().toString(), userEmail.getText().toString(), password.getText().toString());

                    db.collection("User").document(auth.getCurrentUser().getUid().toString()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            sendUserToMainActivity();
                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),
                            "Une erreur est survenue lors de l'inscription. Veuillez réessayer plus tard.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
