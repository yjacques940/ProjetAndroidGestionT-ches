package gestionnairedetaches.com.gestionnairedetaches.Model;

public class TaskModel {
    private String Title;
    private String Description;
    private String PathToImage;

    public TaskModel() {
    }

    public TaskModel(String title, String description, String pathToImage) {
        Title = title;
        Description = description;
        PathToImage = pathToImage;
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


    public String getPathToImage() {
        return PathToImage;
    }

    public void setPathToImage(String pathToImage) {
        PathToImage = pathToImage;
    }
}
