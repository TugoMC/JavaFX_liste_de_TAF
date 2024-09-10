package ci.pigier.todoapp.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ci.pigier.todoapp.Task;
import ci.pigier.todoapp.Priority;

import java.time.LocalDate;

public class EditTaskDialog extends Dialog<Task> {
    private TextField titleInput;
    private TextArea descriptionInput;
    private DatePicker dueDatePicker;
    private ComboBox<String> listComboBox;
    private TextField tagsInput;
    private ComboBox<Priority> priorityComboBox;

    public EditTaskDialog(Task task) {
        setTitle("Modifier la tâche");
        setHeaderText("Modifiez les détails de la tâche");

        ButtonType editButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

        GridPane grid = createFormGrid(task);
        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == editButtonType) {
                task.setTitle(titleInput.getText());
                task.setDescription(descriptionInput.getText());
                task.setDueDate(dueDatePicker.getValue());
                task.setList(listComboBox.getValue());
                task.setTags(tagsInput.getText().split(",\\s*"));
                task.setPriority(priorityComboBox.getValue());
                return task;
            }
            return null;
        });
    }

    private GridPane createFormGrid(Task task) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        titleInput = new TextField(task.getTitle());
        descriptionInput = new TextArea(task.getDescription());
        dueDatePicker = new DatePicker(task.getDueDate());
        listComboBox = new ComboBox<>(FXCollections.observableArrayList("Travail", "Personnel", "Courses"));
        listComboBox.setValue(task.getList());
        tagsInput = new TextField(String.join(", ", task.getTags()));
        priorityComboBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));
        priorityComboBox.setValue(task.getPriority());

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titleInput, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionInput, 1, 1);
        grid.add(new Label("Date d'échéance:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Liste:"), 0, 3);
        grid.add(listComboBox, 1, 3);
        grid.add(new Label("Tags:"), 0, 4);
        grid.add(tagsInput, 1, 4);
        grid.add(new Label("Priorité:"), 0, 5);
        grid.add(priorityComboBox, 1, 5);

        return grid;
    }
}