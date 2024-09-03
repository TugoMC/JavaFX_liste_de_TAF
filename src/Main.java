import controller.TaskController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.TaskView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestionnaire de Tâches Avancé");

        TaskController controller = new TaskController();
        TaskView view = new TaskView(controller);

        Scene scene = new Scene(view.createView(), 1200, 700);
        primaryStage.setScene(scene);

        view.setTaskSelectionListener();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}