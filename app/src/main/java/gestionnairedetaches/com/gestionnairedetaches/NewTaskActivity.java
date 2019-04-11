package gestionnairedetaches.com.gestionnairedetaches;

import android.support.annotation.NonNull;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import gestionnairedetaches.com.gestionnairedetaches.Model.TaskModel;

public class NewTaskActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        setTitle("Ajout de tâche");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setListener();
        storage = FirebaseStorage.getInstance();
    }

    private void setListener(){
        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskToUser();
            }
        });
    }

    private void addTaskToUser(){

        final EditText title = findViewById(R.id.editText_Title);
        final EditText description = findViewById(R.id.editText_description);

        if(!TextUtils.isEmpty(title.getText()) || !TextUtils.isEmpty(description.getText())){
            addTask(title.getText().toString(), description.getText().toString());
        }else{
            alertUserToFillTheEditText();
        }
    }

    private void alertUserToFillTheEditText(){
        Toast.makeText(getApplicationContext(),"Vous devez remplir les champs", Toast.LENGTH_LONG).show();
    }

    private void addTask(String title, String description){
        TaskModel task = new TaskModel(title, description);
        DocumentReference userDocument = db.collection("User").document(auth.getCurrentUser().getUid().toString());

        userDocument.collection("Tasks").add(task).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    onBackPressed();
                }else{
                    alertUserNoTaskWasAdded();
                }
            }
        });
    }
    private void alertUserNoTaskWasAdded(){
        Toast.makeText(getApplicationContext(),"Erreur lors de l'ajout de la tâche", Toast.LENGTH_LONG).show();
    }

    private void setListener() {
        findViewById(R.id.button_addImage).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
