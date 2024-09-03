package struct.maStructure.model;

public class Task {
    private int id;
    private String description;
    private boolean isDone;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.isDone = false;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }

    @Override
    public String toString() {
        return description + (isDone ? " (Terminée)" : "");
    }

}