package com.example.javafx;

import Backend.User.User;
import Backend.User.UserDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private Parent root;
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
    private double x,y;

    public void showProductsByCategorie(ActionEvent e) throws IOException {
        Button clickedButton = (Button) e.getSource();
        String buttonText = clickedButton.getText();
        int categorieNumber = parseCategoryNumber(clickedButton.getId());
        //ProduitDaoImpl dao = new ProduitDaoImpl();
        //System.out.println(dao.getProduitByCategorie(categorieNumber));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/produits.fxml"));
        root = loader.load();
        ProduitsController produitsController = loader.getController();
        produitsController.initialize(categorieNumber);
        produitsController.displayCategorie(buttonText);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // Method to parse the category number from the button ID
    private int parseCategoryNumber(String buttonId) {
        // Use regular expression to extract the number following "categorie"
        String regex = "categorie(\\d+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(buttonId);

        // Check if the pattern is found
        if (matcher.find()) {
            // Extract and parse the matched number
            String numberStr = matcher.group(1);
            return Integer.parseInt(numberStr);
        } else {
            // Return a default value or throw an exception based on your requirements
            return -1;
        }
    }
    public void switchToUsers(ActionEvent event) throws IOException {
        Parent Dashboard = FXMLLoader.load(getClass().getResource("/com/example/javafx/users.fxml"));
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
    //USER METHODS
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

}
