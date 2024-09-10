package ci.pigier.todoapp.ui;

import javafx.scene.control.ListCell;
import ci.pigier.todoapp.Task;

public class TaskCell extends ListCell<Task> {
    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty || task == null) {
            setText(null);
        } else {
            setText(task.getTitle() + " (" + task.getPriority() + ")");
        }
    }
}