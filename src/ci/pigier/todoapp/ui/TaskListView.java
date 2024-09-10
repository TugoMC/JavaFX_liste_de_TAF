package ci.pigier.todoapp.ui;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import ci.pigier.todoapp.Task;

import java.util.function.Consumer;

public class TaskListView extends ListView<Task> {
    public TaskListView(Consumer<Task> onDetailsClicked, Consumer<Task> onEditClicked) {
        setCellFactory(param -> new TaskCell(onDetailsClicked, onEditClicked));
    }

    private static class TaskCell extends ListCell<Task> {
        private final Consumer<Task> onDetailsClicked;
        private final Consumer<Task> onEditClicked;

        public TaskCell(Consumer<Task> onDetailsClicked, Consumer<Task> onEditClicked) {
            this.onDetailsClicked = onDetailsClicked;
            this.onEditClicked = onEditClicked;
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox hbox = new HBox(10);
                Label titleLabel = new Label(task.getTitle() + " (" + task.getPriority() + ")");
                Button detailsButton = new Button("Détails");
                Button editButton = new Button("Modifier");

                detailsButton.setOnAction(event -> onDetailsClicked.accept(task));
                editButton.setOnAction(event -> onEditClicked.accept(task));

                hbox.getChildren().addAll(titleLabel, detailsButton, editButton);
                setGraphic(hbox);
            }
        }
    }
}