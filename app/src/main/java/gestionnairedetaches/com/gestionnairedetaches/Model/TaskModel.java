package gestionnairedetaches.com.gestionnairedetaches.Model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskModel {

    private String Title;
    private String Description;
    private String PathToImage;
    private Boolean Completed;

    public TaskModel(String title, String description, Boolean completed) {
        Title = title;
        Description = description;
        Completed = completed;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    private String DocumentId;

    public TaskModel() {
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void deleteTask(){

        FirebaseAuth auth;
        FirebaseFirestore db;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Log.d("tag", "delete "+DocumentId);

    public String getPathToImage() {
        return PathToImage;
    }

    public void setPathToImage(String pathToImage) {
        PathToImage = pathToImage;
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            DocumentReference userDocument = db.collection("User").document(currentUser.getUid().toString());

            userDocument.collection("Tasks").document(DocumentId).delete();
        }
    }

    public void completeTask(){
        FirebaseAuth auth;
        FirebaseFirestore db;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Log.d("tag", "complete "+DocumentId);
        FirebaseUser currentUser = auth.getCurrentUser();
        Completed = true;
        if(currentUser != null) {
            DocumentReference userDocument = db.collection("User").document(currentUser.getUid().toString());

            userDocument.collection("Tasks").document(DocumentId).set(this);
        }
    }

    public Boolean getCompleted() {
        return Completed;
    }

    public void setCompleted(Boolean completed) {
        Completed = completed;
    }
}
