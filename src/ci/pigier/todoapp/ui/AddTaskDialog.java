package ci.pigier.todoapp.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ci.pigier.todoapp.Task;
import ci.pigier.todoapp.Priority;

import java.time.LocalDate;

public class AddTaskDialog extends Dialog<Task> {
    private TextField titleInput;
    private TextArea descriptionInput;
    private DatePicker dueDatePicker;
    private ComboBox<String> listComboBox;
    private TextField tagsInput;
    private ComboBox<Priority> priorityComboBox;

    public AddTaskDialog() {
        setTitle("Ajouter une tâche");
        setHeaderText("Entrez les détails de la nouvelle tâche");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createFormGrid();
        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Task(
                    titleInput.getText(),
                    descriptionInput.getText(),
                    dueDatePicker.getValue(),
                    listComboBox.getValue(),
                    tagsInput.getText().split(",\\s*"),
                    priorityComboBox.getValue()
                );
            }
            return null;
        });
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        titleInput = new TextField();
        descriptionInput = new TextArea();
        dueDatePicker = new DatePicker();
        listComboBox = new ComboBox<>(FXCollections.observableArrayList("Travail", "Personnel", "Courses"));
        tagsInput = new TextField();
        priorityComboBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));

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