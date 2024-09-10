package ci.pigier.todoapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ci.pigier.todoapp.ui.TaskListView;
import ci.pigier.todoapp.ui.AddTaskDialog;
import ci.pigier.todoapp.ui.TaskDetailsDialog;
import ci.pigier.todoapp.ui.EditTaskDialog;

import java.util.function.Predicate;

public class AdvancedTodoApp extends Application {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private FilteredList<Task> filteredTasks = new FilteredList<>(tasks, p -> true);
    private TaskListView taskListView;

    private ComboBox<String> filterListComboBox;
    private ComboBox<Priority> filterPriorityComboBox;
    private TextField filterTagInput;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestionnaire de Tâches Avancé");

        BorderPane borderPane = new BorderPane();
        
        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        borderPane.setLeft(leftPanel);
        borderPane.setRight(rightPanel);

        Scene scene = new Scene(borderPane, 1200, 700);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));

        Button addTaskButton = new Button("Ajouter une tâche");
        addTaskButton.setOnAction(e -> showAddTaskDialog());

        leftPanel.getChildren().addAll(new Label("Actions:"), addTaskButton);
        
        return leftPanel;
    }

    private VBox createRightPanel() {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));

        HBox filterControls = createFilterControls();
        taskListView = new TaskListView(this::showTaskDetailsDialog, this::showEditTaskDialog);
        taskListView.setItems(filteredTasks);

        rightPanel.getChildren().addAll(new Label("Filtres:"), filterControls, new Label("Tâches:"), taskListView);
        
        return rightPanel;
    }

    private HBox createFilterControls() {
        HBox filterControls = new HBox(10);
        filterListComboBox = new ComboBox<>(FXCollections.observableArrayList("Toutes", "Travail", "Personnel", "Courses"));
        filterListComboBox.setValue("Toutes");
        filterPriorityComboBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));
        filterPriorityComboBox.getItems().add(0, null);
        filterPriorityComboBox.setValue(null);
        filterTagInput = new TextField();
        filterTagInput.setPromptText("Filtrer par tag");
        Button applyFilterButton = new Button("Appliquer le filtre");
        applyFilterButton.setOnAction(e -> applyFilter());

        filterControls.getChildren().addAll(new Label("Liste:"), filterListComboBox, 
                                            new Label("Priorité:"), filterPriorityComboBox, 
                                            filterTagInput, applyFilterButton);
        return filterControls;
    }

    private void showAddTaskDialog() {
        AddTaskDialog dialog = new AddTaskDialog();
        dialog.showAndWait().ifPresent(newTask -> {
            tasks.add(newTask);
            applyFilter();
        });
    }

    private void showTaskDetailsDialog(Task task) {
        TaskDetailsDialog dialog = new TaskDetailsDialog(task);
        dialog.showAndWait();
    }

    private void showEditTaskDialog(Task task) {
        EditTaskDialog dialog = new EditTaskDialog(task);
        dialog.showAndWait().ifPresent(editedTask -> {
            int index = tasks.indexOf(task);
            if (index != -1) {
                tasks.set(index, editedTask);
                applyFilter();
            }
        });
    }

    private void applyFilter() {
        String selectedList = filterListComboBox.getValue();
        Priority selectedPriority = filterPriorityComboBox.getValue();
        String tagFilter = filterTagInput.getText().toLowerCase().trim();

        Predicate<Task> filter = task -> {
            boolean listMatch = selectedList.equals("Toutes") || task.getList().equals(selectedList);
            boolean priorityMatch = selectedPriority == null || task.getPriority() == selectedPriority;
            boolean tagMatch = tagFilter.isEmpty() || task.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(tagFilter));
            return listMatch && priorityMatch && tagMatch;
        };

        filteredTasks.setPredicate(filter);
    }

    public static void main(String[] args) {
        launch(args);
    }
}