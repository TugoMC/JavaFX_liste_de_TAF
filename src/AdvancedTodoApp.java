import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AdvancedTodoApp extends Application {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private FilteredList<Task> filteredTasks = new FilteredList<>(tasks, p -> true);
    private ListView<Task> taskListView;
    private TextField titleInput;
    private TextArea descriptionInput;
    private DatePicker dueDatePicker;
    private ComboBox<String> listComboBox;
    private TextField tagsInput;
    private ComboBox<Priority> priorityComboBox;

    // Nouveaux contrôles pour le tri et le filtrage
    private ComboBox<String> filterListComboBox;
    private ComboBox<Priority> filterPriorityComboBox;
    private TextField filterTagInput;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestionnaire de Tâches Avancé");

        BorderPane borderPane = new BorderPane();
        
        // Panneau de gauche pour la liste des tâches et les contrôles de filtrage
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        
        // Ajout des contrôles de filtrage
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
        
        taskListView = new ListView<>(filteredTasks);
        taskListView.setCellFactory(createTaskCellFactory());
        leftPanel.getChildren().addAll(new Label("Filtres:"), filterControls, new Label("Tâches:"), taskListView);
        
        // Panneau de droite pour les détails et l'édition
        GridPane rightPanel = new GridPane();
        rightPanel.setPadding(new Insets(10));
        rightPanel.setVgap(5);
        rightPanel.setHgap(5);

        titleInput = new TextField();
        descriptionInput = new TextArea();
        dueDatePicker = new DatePicker();
        listComboBox = new ComboBox<>(FXCollections.observableArrayList("Travail", "Personnel", "Courses"));
        tagsInput = new TextField();
        priorityComboBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));

        rightPanel.addRow(0, new Label("Titre :"), titleInput);
        rightPanel.addRow(1, new Label("Description :"), descriptionInput);
        rightPanel.addRow(2, new Label("Date d'échéance :"), dueDatePicker);
        rightPanel.addRow(3, new Label("Liste :"), listComboBox);
        rightPanel.addRow(4, new Label("Tags :"), tagsInput);
        rightPanel.addRow(5, new Label("Priorité :"), priorityComboBox);

        Button addButton = new Button("Ajouter");
        addButton.setOnAction(e -> addTask());
        Button updateButton = new Button("Modifier");
        updateButton.setOnAction(e -> updateTask());
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(e -> deleteTask());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        rightPanel.add(buttonBox, 1, 6);

        borderPane.setLeft(leftPanel);
        borderPane.setRight(rightPanel);

        Scene scene = new Scene(borderPane, 1200, 700);
        primaryStage.setScene(scene);

        primaryStage.show();

        // Listener pour la sélection de tâche
        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titleInput.setText(newVal.getTitle());
                descriptionInput.setText(newVal.getDescription());
                dueDatePicker.setValue(newVal.getDueDate());
                listComboBox.setValue(newVal.getList());
                tagsInput.setText(String.join(", ", newVal.getTags()));
                priorityComboBox.setValue(newVal.getPriority());
            }
        });
    }

    private void addTask() {
        Task newTask = new Task(
            titleInput.getText(),
            descriptionInput.getText(),
            dueDatePicker.getValue(),
            listComboBox.getValue(),
            tagsInput.getText().split(",\\s*"),
            priorityComboBox.getValue()
        );
        tasks.add(newTask);
        clearInputs();
        applyFilter(); // Réappliquer le filtre après l'ajout
    }

    private void updateTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setTitle(titleInput.getText());
            selectedTask.setDescription(descriptionInput.getText());
            selectedTask.setDueDate(dueDatePicker.getValue());
            selectedTask.setList(listComboBox.getValue());
            selectedTask.setTags(tagsInput.getText().split(",\\s*"));
            selectedTask.setPriority(priorityComboBox.getValue());
            taskListView.refresh();
            clearInputs();
            applyFilter(); // Réappliquer le filtre après la modification
        }
    }

    private void deleteTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            clearInputs();
            applyFilter(); // Réappliquer le filtre après la suppression
        }
    }

    private void clearInputs() {
        titleInput.clear();
        descriptionInput.clear();
        dueDatePicker.setValue(null);
        listComboBox.getSelectionModel().clearSelection();
        tagsInput.clear();
        priorityComboBox.getSelectionModel().clearSelection();
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

    private Callback<ListView<Task>, ListCell<Task>> createTaskCellFactory() {
        return new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new ListCell<Task>() {
                    @Override
                    protected void updateItem(Task task, boolean empty) {
                        super.updateItem(task, empty);
                        if (empty || task == null) {
                            setText(null);
                        } else {
                            setText(task.getTitle() + " (" + task.getPriority() + ")");
                        }
                    }
                };
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Task {
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

enum Priority {
    HAUTE, MOYENNE, BASSE
}