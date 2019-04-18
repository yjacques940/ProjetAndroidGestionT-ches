package gestionnairedetaches.com.gestionnairedetaches;

import android.support.annotation.NonNull;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import gestionnairedetaches.com.gestionnairedetaches.Model.TaskModel;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import io.grpc.Context;

public class NewTaskActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
FirebaseStorage storage;
FirebaseStorage storageReference;
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
        storageReference = FirebaseStorage.getInstance();
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

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
            byte[] byteData =byteArray.toByteArray();

            Date currentTime = Calendar.getInstance().getTime();
            StorageReference storageReference = storage.getReference();
            StorageReference fileReference = storageReference.child("test");
            StorageReference imageReference =  storageReference.child("test/image" + currentTime + ".jpg");
            imageReference.getName().equals(imageReference.getName());
            imageReference.getPath().equals(imageReference.getPath());

            UploadTask uploadTask = imageReference.putBytes(byteData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                }
            });

            saveImage(imageBitmap);
        }
    }

    private void saveImage(Bitmap imageBitmap) {
        Date currentTime = Calendar.getInstance().getTime();
        StorageReference storageReference = storage.getReference();
        StorageReference imageReference = storageReference.child("images" + currentTime);



    }
}
