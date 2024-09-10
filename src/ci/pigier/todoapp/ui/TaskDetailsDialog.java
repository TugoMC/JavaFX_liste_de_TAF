package ci.pigier.todoapp.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ci.pigier.todoapp.Task;

public class TaskDetailsDialog extends Dialog<Void> {
    public TaskDetailsDialog(Task task) {
        setTitle("Détails de la tâche");
        setHeaderText("Détails de la tâche : " + task.getTitle());

        ButtonType closeButtonType = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(closeButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(new Label(task.getTitle()), 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(new Label(task.getDescription()), 1, 1);
        grid.add(new Label("Date d'échéance:"), 0, 2);
        grid.add(new Label(task.getDueDate().toString()), 1, 2);
        grid.add(new Label("Liste:"), 0, 3);
        grid.add(new Label(task.getList()), 1, 3);
        grid.add(new Label("Tags:"), 0, 4);
        grid.add(new Label(String.join(", ", task.getTags())), 1, 4);
        grid.add(new Label("Priorité:"), 0, 5);
        grid.add(new Label(task.getPriority().toString()), 1, 5);

        getDialogPane().setContent(grid);
    }
}