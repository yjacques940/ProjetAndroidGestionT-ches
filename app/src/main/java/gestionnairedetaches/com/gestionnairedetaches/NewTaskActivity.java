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
import android.widget.Button;
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
    Bitmap imageToSave;
    String pathToImage = "";
    byte[] byteData;

    public interface Callback{
        public void callback();
    }
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

        findViewById(R.id.button_addImage).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                dispatchTakePictureIntent();
            }
        });
    }

    private void addTaskToUser(){

        final EditText title = findViewById(R.id.editText_Title);
        final EditText description = findViewById(R.id.editText_description);

        setButtonEnable(false);
        if(!TextUtils.isEmpty(title.getText()) && !TextUtils.isEmpty(description.getText())){
            addTask(title.getText().toString(), description.getText().toString());
        }else{
            alertUserToFillTheEditText();
        }
    }

    private void alertUserToFillTheEditText(){

        setButtonEnable(true);
        Toast.makeText(getApplicationContext(),"Vous devez remplir les champs", Toast.LENGTH_LONG).show();
    }

    private void addTask(final String title, final String description){
        if(byteData != null){
            saveImage(new Callback() {
                @Override
                public void callback() {
                    TaskModel task = new TaskModel(title, description,pathToImage,false);
                    DocumentReference userDocument = db.collection("User").document(auth.getCurrentUser().getUid().toString());

                    userDocument.collection("Tasks").add(task).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Tâche enregistrée avec succès", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }else{
                                setButtonEnable(true);
                                alertUserNoTaskWasAdded();
                            }
                        }
                    });
                }
            });
        } else {
            setButtonEnable(true);
            Toast.makeText(getApplicationContext(), "Vous devez ajoutez une image", Toast.LENGTH_LONG).show();
        }

    }

    private void setButtonEnable(boolean isEnable){
        final Button btnAjouterImage = findViewById(R.id.button_addImage);
        final Button btnEnregistrer = findViewById(R.id.button_save);

        btnAjouterImage.setEnabled(isEnable);
        btnEnregistrer.setEnabled(isEnable);
    }
    private void alertUserNoTaskWasAdded(){
        Toast.makeText(getApplicationContext(),"Erreur lors de l'ajout de la tâche", Toast.LENGTH_LONG).show();
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
            imageToSave = (Bitmap) extras.get("data");

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            imageToSave.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
            byteData =byteArray.toByteArray();

            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(imageToSave);

            Date currentTime = Calendar.getInstance().getTime();
            pathToImage = "images/image" + currentTime + ".jpg";
        }
    }

    private void saveImage(final Callback callback) {
        StorageReference storageReference = storage.getReference();
        final StorageReference imageReference =  storageReference.child(pathToImage);

        UploadTask uploadTask = imageReference.putBytes(byteData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.callback();
            }
        });
    }
}
