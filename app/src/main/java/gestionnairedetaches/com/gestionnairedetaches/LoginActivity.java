package gestionnairedetaches.com.gestionnairedetaches;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
FirebaseAuth auth;
AnimatorSet animatorSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Connexion");
        auth = FirebaseAuth.getInstance();
        animatorSet = new AnimatorSet();

        setListener();
    }

    private void setListener() {
        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                loginUser();
            }
        });
        EditText editText = findViewById(R.id.editText_login_email);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkIfBothAreFilled();
            }
        });
        editText = findViewById(R.id.editText_login_password);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkIfBothAreFilled();
            }
        });
    }

    private void checkIfBothAreFilled(){
        EditText editText = findViewById(R.id.editText_login_email);
        String email = editText.getText().toString();
        editText = findViewById(R.id.editText_login_password);
        String password = editText.getText().toString();
        if(email.length() != 0 && password.length() != 0){
            setAnimation(0.2f,1f);
        }else{
            setAnimation(1f,1f);
        }
    }

    public void setAnimation(float min, float max){
        Button button = findViewById(R.id.button_login);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(button, "alpha", min, max);
        fadeIn.setDuration(1500);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(button, "alpha", max, min);
        fadeIn.setDuration(1500);
        animatorSet.play(fadeIn).after(fadeOut);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });
        animatorSet.start();
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
        if(currentUser != null)
        {
            sendUserToMainActivity();
        }
    }

    private void loginUser() {
        EditText email = findViewById(R.id.editText_login_email);
        EditText password = findViewById(R.id.editText_login_password);

        auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    sendUserToMainActivity();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Une erreur est survenue, veuillez vérifier vos identifiants",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
