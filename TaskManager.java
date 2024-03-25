import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TaskManager extends Application {
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        ListView<String> taskListView = new ListView<>();
        TextField titleField = new TextField();
        TextField descriptionField = new TextField();
        Button addButton = new Button("Add Task");

        addButton.setOnAction(event -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            Task task = new Task(title, description);
            tasks.add(task);
            taskListView.getItems().add(task.getTitle());
            titleField.clear();
            descriptionField.clear();
        });

        root.getChildren().addAll(taskListView, titleField, descriptionField, addButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Task Manager");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}














