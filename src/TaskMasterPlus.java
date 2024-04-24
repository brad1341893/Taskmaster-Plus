import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class TaskMasterPlus extends Application {

    private List<Task> tasks = new ArrayList<>();
    private Tab taskListTab; // Declare taskListTab as an instance variable
    private Tab upcomingDeadlinesTab;
    private Tab priorityTasksTab;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Create tabs
        TabPane tabPane = new TabPane();
        Tab createTasksTab = new Tab("Create Tasks");
        taskListTab = new Tab("Task List"); // Initialize taskListTab here
        Tab pastDueTab = new Tab("Past Due");
        Tab settingsTab = new Tab("Settings");

        // Create Upcoming Deadlines tab
        upcomingDeadlinesTab = new Tab("Upcoming Deadlines");
        VBox upcomingDeadlinesContent = new VBox(10);
        upcomingDeadlinesTab.setContent(upcomingDeadlinesContent);

        // Create Priority Tasks tab
        priorityTasksTab = new Tab("Priority Tasks");
        VBox priorityTasksContent = new VBox(10);
        priorityTasksTab.setContent(priorityTasksContent);

        // Home tab
        VBox homeContent = new VBox();
        homeContent.getChildren().addAll(new Label("Welcome to TaskMaster Plus."));
        Tab homeTab = new Tab("Home");
        homeTab.setContent(homeContent);

        // Create Tasks tab
        VBox createTasksContent = new VBox(10);
        TextField titleField = new TextField();
        TextField descriptionField = new TextField();
        DatePicker dueDateField = new DatePicker();
        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("High", "Medium", "Low");
        Button createTaskButton = new Button("Create Task");
        createTaskButton.setOnAction(event -> {
            // Create task
            String title = titleField.getText();
            String description = descriptionField.getText();
            LocalDate dueDate = dueDateField.getValue();
            String priority = priorityComboBox.getValue();
            Task task = new Task(title, description, dueDate, priority);
            tasks.add(task);
            
            // Create Task file
             
            try {
            
            PrintWriter writer = new PrintWriter(title + ".txt");
            
            } catch (IOException e) {
            
            System.out.println("An error occurred.");
            e.printStackTrace();
            
            }
            
            // Write task info into file
            
            try {
            
            FileWriter out = new FileWriter(title + ".txt");
            
            out.write(title + "\r\n");
            out.write(description + "\r\n");
            out.write(priority + "\r\n");
            out.write(dueDate.toString() + "\r\n");
            
            
            out.close();
            
            } catch (IOException e) {
            
            System.out.println("An error occurred.");
            e.printStackTrace();
            
            }
            
            // Clear fields
            titleField.clear();
            descriptionField.clear();
            dueDateField.getEditor().clear();
            priorityComboBox.getSelectionModel().clearSelection();
            // Update task list
            updateTaskList();
            // Check for upcoming deadlines
            updateUpcomingDeadlinesTab();
            if (priority.equals("High")) {
                updatePriorityTasksTab();
            }
        });
        createTasksContent.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Description:"), descriptionField,
                new Label("Due Date:"), dueDateField,
                new Label("Priority:"), priorityComboBox,
                createTaskButton);
        createTasksTab.setContent(createTasksContent);

        // Task List tab
        VBox taskListContent = new VBox(10);
        ListView<Task> taskListView = new ListView<>();
        taskListView.setOnMouseClicked(event -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                // Show dialog for editing or deleting task
                showEditDeleteDialog(selectedTask);
            }
        });
        taskListContent.getChildren().addAll(taskListView);
        taskListTab.setContent(taskListContent);

        // Past Due tab
        VBox pastDueContent = new VBox(10);
        Button clearPastTasksButton = new Button("Clear Past Tasks");
        clearPastTasksButton.setOnAction(event -> {
            tasks.removeIf(task -> task.getDueDate().isBefore(LocalDate.now()));
            updateTaskList();
        });
        pastDueContent.getChildren().addAll(clearPastTasksButton);
        pastDueTab.setContent(pastDueContent);

        // Settings tab
        VBox settingsContent = new VBox(10);
        ComboBox<String> themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll("Dark", "Light");
        themeComboBox.setOnAction(event -> {
            if (themeComboBox.getValue().equals("Dark")) {
                mainLayout.setStyle("-fx-background-color: grey;");
            } else {
                mainLayout.setStyle("-fx-background-color: white;");
            }
        });
        settingsContent.getChildren().addAll(
                new Label("Theme:"),
                themeComboBox);
        settingsTab.setContent(settingsContent);

        // Add tabs to tab pane
        tabPane.getTabs().addAll(homeTab, createTasksTab, taskListTab, pastDueTab, settingsTab);
        // Add Upcoming Deadlines and Priority Tasks tabs to tab pane
        tabPane.getTabs().addAll(upcomingDeadlinesTab, priorityTasksTab);

        mainLayout.setCenter(tabPane);

        // Set scene
        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TaskMaster Plus");
        primaryStage.show();
    }

    // Method to update task list
    private void updateTaskList() {
        VBox taskListContent = (VBox) taskListTab.getContent();
        ListView<Task> taskListView = (ListView<Task>) taskListContent.getChildren().get(0);
        taskListView.getItems().clear();
        List<Task> highPriorityTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority().equals("High")) {
                highPriorityTasks.add(task);
            }
        }
        tasks.sort((task1, task2) -> {
            if (task1.getPriority().equals("High")) {
                return -1;
            } else if (task2.getPriority().equals("High")) {
                return 1;
            } else if (task1.getPriority().equals("Medium")) {
                return task2.getPriority().equals("Low") ? -1 : 1;
            } else {
                return task2.getPriority().equals("Medium") ? 1 : -1;
            }
        });
        taskListView.getItems().addAll(tasks);
    }

    // Method to update "Priority Tasks" tab
    private void updatePriorityTasksTab() {
        VBox priorityTasksContent = (VBox) priorityTasksTab.getContent();
        ListView<Task> priorityTasksListView = new ListView<>();
        List<Task> highPriorityTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority().equals("High")) {
                highPriorityTasks.add(task);
            }
        }
        priorityTasksListView.getItems().addAll(highPriorityTasks);
        priorityTasksContent.getChildren().clear();
        priorityTasksContent.getChildren().addAll(priorityTasksListView);
    }

    // Method to update "Upcoming Deadlines" tab
    private void updateUpcomingDeadlinesTab() {
        VBox upcomingDeadlinesContent = (VBox) upcomingDeadlinesTab.getContent();
        ListView<Task> upcomingDeadlinesListView = new ListView<>();
        List<Task> upcomingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDueDate().isBefore(LocalDate.now().plusDays(4)) && task.getDueDate().isAfter(LocalDate.now())) {
                upcomingTasks.add(task);
            }
        }
        upcomingDeadlinesListView.getItems().addAll(upcomingTasks);
        upcomingDeadlinesContent.getChildren().clear();
        upcomingDeadlinesContent.getChildren().addAll(upcomingDeadlinesListView);
    }

    // Method to show edit/delete dialog
    private void showEditDeleteDialog(Task task) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit/Delete Task");
        dialog.setHeaderText("Edit or delete the task:");
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL, deleteButtonType);

        VBox content = new VBox(10);
        TextField titleField = new TextField(task.getTitle());
        TextField descriptionField = new TextField(task.getDescription());
        DatePicker dueDateField = new DatePicker(task.getDueDate());
        ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("High", "Medium", "Low");
        priorityComboBox.setValue(task.getPriority());
        content.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Description:"), descriptionField,
                new Label("Due Date:"), dueDateField,
                new Label("Priority:"), priorityComboBox);

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Update task
                task.setTitle(titleField.getText());
                task.setDescription(descriptionField.getText());
                task.setDueDate(dueDateField.getValue());
                task.setPriority(priorityComboBox.getValue());
                updateTaskList();
                updateUpcomingDeadlinesTab();
                if (priorityComboBox.getValue().equals("High")) {
                    updatePriorityTasksTab();
                }
            } else if (buttonType == deleteButtonType) {
                // Delete task
                tasks.remove(task);
                updateTaskList();
                updateUpcomingDeadlinesTab();
                updatePriorityTasksTab();
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Task class
    private static class Task {
        private String title;
        private String description;
        private LocalDate dueDate;
        private String priority;

        public Task(String title, String description, LocalDate dueDate, String priority) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
            this.priority = priority;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}