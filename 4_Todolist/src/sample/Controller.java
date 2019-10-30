package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import sample.datamodel.TodoData;
import sample.datamodel.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {

        private List<TodoItem> todoItems;
        @FXML
        private ListView<TodoItem> todoListView;
        @FXML
        private TextArea detailTextArea;
        @FXML
        private Label deadlineLabel;
        @FXML
        private BorderPane mainBorderPane;
        @FXML
        private ContextMenu listContextMenu;

        public void initialize() {
//            TodoItem item1 = new TodoItem("Mail birthday card", "Stop by USPS to mail the card to Cindy",
//                    LocalDate.of(2019, 10, 15));
//            TodoItem item2 = new TodoItem("Birthday Cake", "To order birthday cake for Daniel",
//                    LocalDate.of(2020, 3, 11));
//            TodoItem item3 = new TodoItem("Doctor appointment", "Dental appointment with Doctor Julie",
//                    LocalDate.of(2019, Month.OCTOBER, 25));
//            todoItems = new ArrayList<TodoItem>();
//            todoItems.add(item1);
//            todoItems.add(item2);
//            todoItems.add(item3);
//
//            TodoData.getInstance().setTodoItems(todoItems);

            listContextMenu = new ContextMenu();
            MenuItem deleteMenuItem = new MenuItem("Delete");
            deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    deleteItem(item);
                }
            });

            listContextMenu.getItems().addAll(deleteMenuItem);
            todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
                @Override
                public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem oldValue, TodoItem newValue) {
                    if(newValue != null) {
                        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                        detailTextArea.setText(item.getDetails());
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                        deadlineLabel.setText(dateTimeFormatter.format(item.getDeadLine()));
                    }
                }
            });

            todoListView.setItems(TodoData.getInstance().getTodoItems());  //Data Binding and Observablelist
            todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            todoListView.getSelectionModel().selectFirst();
// set the todoItem which is past due or due today to different color to alert user ***
            todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
                @Override
                public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                    ListCell<TodoItem> cell = new ListCell<TodoItem>() {

                        @Override
                        protected void updateItem(TodoItem todoItem, boolean b) {
                            super.updateItem(todoItem, b);
                            if(b) {
                                setText(null);
                            } else {
                                setText(todoItem.getShortDescription());
                                // all items past due or due today will show in red, the will due tomorrow will show in orange
                                if(todoItem.getDeadLine().isBefore(LocalDate.now().plusDays(1))) {
                                    setTextFill(Color.RED);
                                } else if(todoItem.getDeadLine().equals(LocalDate.now().plusDays(1))) {
                                    setTextFill(Color.ORANGE);
                                }
                            }
                        }
                    };
                    cell.emptyProperty().addListener(
                            (obs, wasEmpty, isNowEmpty) -> {
                                if(isNowEmpty) {
                                    cell.setContextMenu(null);
                                } else {
                                    cell.setContextMenu(listContextMenu);
                                }
                            }
                    );
                    return cell;
                }
            });
        }

        @FXML
        public void showNewItemDialog() {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            dialog.setTitle("Add New Todo Item"); // put title for windows pop up
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());

            } catch (IOException e) {
                System.out.println("Couldn't load the dialog");
                e.printStackTrace();
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                DialogController controller = fxmlLoader.getController();
                TodoItem newItem = controller.processResult();
//                todoListView.getItems().setAll(TodoData.getInstance().getTodoItems()); //refresh main windows' todoitem list.
                todoListView.getSelectionModel().select(newItem);
            }
        }
        @FXML
        public void handleClickListView() {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                detailTextArea.setText(item.getDetails());
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                deadlineLabel.setText(dateTimeFormatter.format(item.getDeadLine()));
    //        System.out.println("The selected item is" + item);
    //        StringBuilder stringBuilder = new StringBuilder(item.getDetails());
    //        stringBuilder.append("\n\n\nDue: ");
    //        stringBuilder.append(item.getDeadLine().toString());
    //        detailTextArea.setText(stringBuilder.toString()); //Only for test
        }

        public void deleteItem(TodoItem item) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Todo Item");
            alert.setHeaderText("Delete item: " + item.getShortDescription());
            alert.setContentText("Are you sure to delete? Press OK to confirm, or Cancel to back out.");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && (result.get() == ButtonType.OK)) {
                TodoData.getInstance().deleteTodoItem(item);
            }

        }

    }
