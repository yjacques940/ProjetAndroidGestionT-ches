package gestionnairedetaches.com.gestionnairedetaches;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUpOrLogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_or_log_in);
        setTitle("Accueil");
        setListener();
    }

    private void setListener(){
        findViewById(R.id.button_home_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogInActivity();
            }
        });

        findViewById(R.id.button_home_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSignUpActivity();
            }
        });
    }

    private void sendToSignUpActivity(){
        Intent sendToSignUp = new Intent(this, SignUpActivity.class   );
        startActivity(sendToSignUp);
    }

    private void sendToLogInActivity(){
        Intent sendToLogIn = new Intent(this, LoginActivity.class   );
        startActivity(sendToLogIn);
    }
}
