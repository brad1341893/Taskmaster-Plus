import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TaskMaster extends Application {

    private ListView<String> taskListView;
    private TextArea taskDetailsArea;

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        taskListView = new ListView<>();
        taskDetailsArea = new TextArea();
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        // Set up layout for home screen
        VBox homeScreen = new VBox(10);
        homeScreen.setPadding(new Insets(10));
        homeScreen.getChildren().addAll(
                createClickableLabel("Pending Tasks Overview"),
                taskListView,
                createClickableLabel("Upcoming Deadlines"),
                createClickableLabel("Priority Tasks")
        );

        // Set up layout for task list screen
        VBox taskListScreen = new VBox(10);
        taskListScreen.setPadding(new Insets(10));
        taskListScreen.getChildren().addAll(
                new Label("Task List"),
                taskListView,
                new HBox(10, editButton, deleteButton)
        );

        // Set up layout for task details screen
        VBox taskDetailsScreen = new VBox(10);
        taskDetailsScreen.setPadding(new Insets(10));
        taskDetailsScreen.getChildren().addAll(
                new Label("Task Details"),
                taskDetailsArea,
                new HBox(10, editButton, deleteButton)
        );

        // Set up layout for task creation screen
        VBox taskCreationScreen = new VBox(10);
        taskCreationScreen.setPadding(new Insets(10));
        taskCreationScreen.getChildren().addAll(
                new Label("Create New Task"),
                new Label("Title:"),
                new TextField(),
                new Label("Description:"),
                new TextField(),
                new Label("Due Date:"),
                new TextField(),
                new Label("Priority:"),
                createPriorityComboBox(),
                createCreateTaskButton()
        );

        // Set up layout for settings screen
        VBox settingsScreen = new VBox(10);
        settingsScreen.setPadding(new Insets(10));
        settingsScreen.getChildren().addAll(
                new Label("Settings"),
                new Label("Notifications:"),
                new CheckBox("Enable Notifications"),
                createThemeComboBox(),
                createLanguageComboBox(),
                new Button("Save Settings")
        );

        // Set up tab pane
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                new Tab("Home", homeScreen),
                new Tab("Task List", taskListScreen),
                new Tab("Task Details", taskDetailsScreen),
                new Tab("Create Task", taskCreationScreen),
                new Tab("Settings", settingsScreen)
        );

        // Set up stage
        Scene scene = new Scene(tabPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TaskMaster");
        primaryStage.show();
    }

    private Label createClickableLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-cursor: hand; -fx-underline: true;");
        label.setOnMouseClicked(event -> {
            // Handle label click event
            System.out.println(text + " clicked");
        });
        return label;
    }

    private ComboBox<String> createPriorityComboBox() {
        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("High", "Medium", "Low");
        return priorityComboBox;
    }

    private Button createCreateTaskButton() {
        Button createTaskButton = new Button("Create Task");
        createTaskButton.setOnAction(event -> {
            // Add task to task list
            String title = ((TextField) ((VBox) taskListView.getParent().getParent()).getChildren().get(1)).getText();
            String description = ((TextField) ((VBox) taskListView.getParent().getParent()).getChildren().get(3)).getText();
            String dueDate = ((TextField) ((VBox) taskListView.getParent().getParent()).getChildren().get(5)).getText();
            String priority = ((ComboBox<String>) ((VBox) taskListView.getParent().getParent()).getChildren().get(7)).getValue();
            String taskDetails = title + " (Due: " + dueDate + ", Priority: " + priority + ")";
            taskListView.getItems().add(taskDetails);
        });
        return createTaskButton;
    }

    private ComboBox<String> createThemeComboBox() {
        ComboBox<String> themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll("Dark", "Light");
        return themeComboBox;
    }

    private ComboBox<String> createLanguageComboBox() {
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "Spanish");
        return languageComboBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
