package view;
import model.Priority;
import controller.TaskController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import model.Task;

public class TaskView {
    private TaskController controller;
    
    private ListView<Task> taskListView;
    private TextField titleInput;
    private TextArea descriptionInput;
    private DatePicker dueDatePicker;
    private ComboBox<String> listComboBox;
    private TextField tagsInput;
    private ComboBox<Priority> priorityComboBox;
    private ComboBox<String> filterListComboBox;
    private ComboBox<Priority> filterPriorityComboBox;
    private TextField filterTagInput;

    public TaskView(TaskController controller) {
        this.controller = controller;
    }

    public BorderPane createView() {
        BorderPane borderPane = new BorderPane();
        
        VBox leftPanel = createLeftPanel();
        GridPane rightPanel = createRightPanel();

        borderPane.setLeft(leftPanel);
        borderPane.setRight(rightPanel);

        return borderPane;
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        
        HBox filterControls = createFilterControls();
        
        taskListView = new ListView<>(controller.getFilteredTasks());
        taskListView.setCellFactory(createTaskCellFactory());
        leftPanel.getChildren().addAll(new Label("Filtres:"), filterControls, new Label("Tâches:"), taskListView);
        
        return leftPanel;
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
        applyFilterButton.setOnAction(e -> controller.applyFilter(filterListComboBox.getValue(), filterPriorityComboBox.getValue(), filterTagInput.getText()));
        
        filterControls.getChildren().addAll(new Label("Liste:"), filterListComboBox, 
                                            new Label("Priorité:"), filterPriorityComboBox, 
                                            filterTagInput, applyFilterButton);
        return filterControls;
    }

    private GridPane createRightPanel() {
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

        return rightPanel;
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

    private void addTask() {
        controller.addTask(
            titleInput.getText(),
            descriptionInput.getText(),
            dueDatePicker.getValue(),
            listComboBox.getValue(),
            tagsInput.getText().split(",\\s*"),
            priorityComboBox.getValue()
        );
        clearInputs();
    }

    private void updateTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            controller.updateTask(
                selectedTask,
                titleInput.getText(),
                descriptionInput.getText(),
                dueDatePicker.getValue(),
                listComboBox.getValue(),
                tagsInput.getText().split(",\\s*"),
                priorityComboBox.getValue()
            );
            taskListView.refresh();
            clearInputs();
        }
    }

    private void deleteTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            controller.deleteTask(selectedTask);
            clearInputs();
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

    public void setTaskSelectionListener() {
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
}