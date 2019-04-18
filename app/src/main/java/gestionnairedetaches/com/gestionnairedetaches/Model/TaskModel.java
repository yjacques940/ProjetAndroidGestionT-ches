package gestionnairedetaches.com.gestionnairedetaches.Model;

public class TaskModel {
    private String Title;
    private String Description;

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    private String DocumentId;

    public TaskModel() {
    }

    public TaskModel(String title, String description) {
        Title = title;
        Description = description;
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
}
