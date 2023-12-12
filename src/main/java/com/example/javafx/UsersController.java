package com.example.javafx;

import Backend.User.User;
import Backend.User.UserDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class UsersController {
    @FXML
    TableView<User> usersTable;
    @FXML
    TableColumn<User, Integer> idColumn;
    @FXML
    TableColumn<User, String> usernameColumn;
    @FXML
    TableColumn<User, String> passwordColumn;
    @FXML
    TableColumn<User, String> typeColumn;
    private UserDaoImpl userDao = new UserDaoImpl();
    public void initialize() {
        // Set up cell value factories for each column
        idColumn.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<User, String>("type"));
        // Fetch data from DAO
        ObservableList<User> produitsList = FXCollections.observableList(userDao.getAll());

        // Populate the TableView with data
        usersTable.setItems(produitsList);
    }
    public void switchToDashBoard(ActionEvent event) throws IOException {
        Parent Dashboard = FXMLLoader.load(getClass().getResource("/com/example/javafx/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(Dashboard);
        stage.setScene(scene);
        stage.show();
    }
    public void switchTo(ActionEvent event) throws IOException {
        Button sidebarBtn = (Button) event.getSource();
        Parent Dashboard = FXMLLoader.load(getClass().getResource("/com/example/javafx/" + sidebarBtn.getId() + ".fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(Dashboard);
        stage.setScene(scene);
        stage.show();
    }

}
