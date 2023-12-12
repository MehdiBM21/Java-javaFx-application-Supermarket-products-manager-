package com.example.javafx;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Admin_Controller implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Button btn;

    @FXML
    private Scene scene;
    @FXML
    private AnchorPane topBar;
    @FXML
    private AnchorPane sideBar;
    @FXML
    private VBox sideBar_vbox;
    @FXML
    private AnchorPane content_pane;
    private double x,y;




    public void close(ActionEvent event) {

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("close");
        alert.setHeaderText("you are closing the application");
        alert.setContentText("Are you sure you want to close the application ??");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if(alert.showAndWait().get()== ButtonType.OK){
            stage.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topBar.setOnMousePressed(Event->{
            x=Event.getSceneX();
            y=Event.getSceneY();

        });
        topBar.setOnMouseDragged(event ->{
            main.primaryStage.setX(event.getScreenX()-x);
            main.primaryStage.setY(event.getScreenY()-y );

        });






    }
}
