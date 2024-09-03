package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Task;
import model.Priority;
import java.time.LocalDate;
import java.util.function.Predicate;

public class TaskController {
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private FilteredList<Task> filteredTasks = new FilteredList<>(tasks, p -> true);

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public FilteredList<Task> getFilteredTasks() {
        return filteredTasks;
    }

    public void addTask(String title, String description, LocalDate dueDate, String list, String[] tags, Priority priority) {
        Task newTask = new Task(title, description, dueDate, list, tags, priority);
        tasks.add(newTask);
    }

    public void updateTask(Task task, String title, String description, LocalDate dueDate, String list, String[] tags, Priority priority) {
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setList(list);
        task.setTags(tags);
        task.setPriority(priority);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public void applyFilter(String selectedList, Priority selectedPriority, String tagFilter) {
        Predicate<Task> filter = task -> {
            boolean listMatch = selectedList.equals("Toutes") || task.getList().equals(selectedList);
            boolean priorityMatch = selectedPriority == null || task.getPriority() == selectedPriority;
            boolean tagMatch = tagFilter.isEmpty() || task.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(tagFilter.toLowerCase()));
            return listMatch && priorityMatch && tagMatch;
        };

        filteredTasks.setPredicate(filter);
    }
}