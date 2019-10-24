package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField nameField;
    @FXML
    private Button helloButton;
    @FXML
    private Button byeButton;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Label ourLabel;

    @FXML
    public void initialize() {
        helloButton.setDisable(true);
        byeButton.setDisable(true);
    }

    @FXML
    public void onButtonClicked(ActionEvent event) {
        if(event.getSource().equals(helloButton)) {
            System.out.println("Hello, " + nameField.getText());
        } else if(event.getSource().equals(byeButton)) {
            System.out.println("Bye, " + nameField.getText());
        }
        //test UI thread
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
//                    is.FxApplicationThread is very good way to check which tread the application is running right now.
                    System.out.println("Sleep on the " + s);
                    Thread.sleep(10000);
                    //after we finish another time consuming thread we need switch back to UI(FxApplicationThread) if you want to update the
                    // element on graph scene. Platform.runlater is one way to ensure the code runs on UI thread. Concurrency in JavaFX is
                    // package to creat multithreaded applications.
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
                            System.out.println("I's updating the label on the " + s);
                            ourLabel.setText("We did something");
                        }
                    });

//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
                } catch (InterruptedException e) {
                }
                }
            };
        new Thread(task).start();

        if(checkBox.isSelected()){
            nameField.clear();
            helloButton.setDisable(true);
            byeButton.setDisable(true);
        }
    }

    @FXML
    public void handleKeyReleased() {
        String text = nameField.getText();
        boolean disableButton = text.isEmpty() || text.trim().isEmpty(); //trim() to check if a space typed in
        helloButton.setDisable(disableButton);
        byeButton.setDisable(disableButton);
    }

    public void handleChange() {
        System.out.println("The CheckBox is " + (checkBox.isSelected() ? "checked" : "not checked"));
    }
}
