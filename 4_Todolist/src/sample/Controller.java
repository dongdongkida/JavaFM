package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import sample.datamodel.TodoItem;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {

        private List<TodoItem> todoItems;
        @FXML
        private ListView<TodoItem> todoListView;
        @FXML
        private TextArea detailView;

        public void initialize() {
            TodoItem item1 = new TodoItem("Mail birthday card", "Stop by USPS to mail the card to Cindy",
                    LocalDate.of(2019, 10, 15));
            TodoItem item2 = new TodoItem("Birthday Cake", "To order birthday cake for Daniel",
                    LocalDate.of(2020, 3, 11));
            TodoItem item3 = new TodoItem("Doctor appointment", "Dental appointment with Doctor Julie",
                    LocalDate.of(2019, Month.OCTOBER, 25));
            todoItems = new ArrayList<TodoItem>();
            todoItems.add(item1);
            todoItems.add(item2);
            todoItems.add(item3);

            todoListView.getItems().addAll(todoItems);
            todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
    @FXML
    public void handleClickListView() {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        System.out.println("The selected item is" + item);
        StringBuilder stringBuilder = new StringBuilder(item.getDetails());
        stringBuilder.append("\n\n\nDue: ");
        stringBuilder.append(item.getDeadLine().toString());
        detailView.setText(stringBuilder.toString());
    }

}
