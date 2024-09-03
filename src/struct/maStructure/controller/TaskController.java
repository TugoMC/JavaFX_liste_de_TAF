package struct.maStructure.controller;

import com.yourcompany.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class TaskController {
    @FXML private ListView<Task> taskListView;
    @FXML private TextField taskInput;

    @FXML
    private void initialize() {
        // Initialisation du contrôleur
    }

    @FXML
    private void addTask() {
        String description = taskInput.getText();
        if (!description.isEmpty()) {
            Task newTask = new Task(taskListView.getItems().size() + 1, description);
            taskListView.getItems().add(newTask);
            taskInput.clear();
        }
    }

    // Autres méthodes pour gérer les tâches (supprimer, marquer comme terminée, etc.)
}