package ci.pigier.todoapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String list;
    private List<String> tags;
    private Priority priority;

    public Task(String title, String description, LocalDate dueDate, String list, String[] tags, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.list = list;
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.priority = priority;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getList() { return list; }
    public void setList(String list) { this.list = list; }

    public List<String> getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = new ArrayList<>(Arrays.asList(tags)); }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}