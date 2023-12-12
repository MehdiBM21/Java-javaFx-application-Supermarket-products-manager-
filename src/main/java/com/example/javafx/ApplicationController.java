package com.example.javafx;
import Backend.Produit.Produit;
import Backend.Produit.ProduitDaoImpl;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable{
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Button users, home;

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
    //produits pane variables
    @FXML
    TableView<Produit> ProduitsTable;
    @FXML
    TableColumn<Produit, Integer> id;
    @FXML
    TableColumn<Produit, String> designation;
    @FXML
    TableColumn<Produit, Integer> quantite;
    @FXML
    TableColumn<Produit, Double> prix;
    @FXML
    TableColumn<Produit, Date> date;
    @FXML
    TableColumn<Produit, Date> dateDePeremption;
    @FXML
    Label categorieLabel;
    @FXML
    Label designation_rule, quantite_rule, prix_rule, date_rule, peremption_rule;
    //USERS PANE VARIABLES
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
    //DAO
    private UserDaoImpl userDao = new UserDaoImpl();
    private ProduitDaoImpl produitsDao = new ProduitDaoImpl();

    private double x,y;//for dragging
    private int categorieNumber;
    //panes
    @FXML
    Pane users_pane, dashboard_pane, products_pane, addProduct_pane;
    @FXML
    TextField designation_field, quantite_field, prix_field;
    @FXML
    DatePicker date_field, peremption_field;

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

    public void switchTo(ActionEvent event) throws IOException {
        //SIDEBAR BUTTONS
        //take the id of each sidebar button and show the corresponding pane
        Button sidebarBtn = (Button) event.getSource();
        if(sidebarBtn == home){
            dashboard_pane.toFront();
        }
        if(sidebarBtn == users){
            initializeUsers();
            users_pane.toFront();
        }
    }
    //DASHBOARD METHODS
    public void showProductsByCategorie(ActionEvent e) throws IOException {
        Button clickedButton = (Button) e.getSource();
        String buttonText = clickedButton.getText();
        categorieNumber = parseCategoryNumber(clickedButton.getId());
        //ProduitDaoImpl dao = new ProduitDaoImpl();
        //System.out.println(dao.getProduitByCategorie(categorieNumber));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/produits.fxml"));
        //root = loader.load();
        //ProduitsController produitsController = loader.getController();
        initializeProduits(categorieNumber);
        displayCategorie(buttonText);
        products_pane.toFront();
        //stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        //scene = new Scene(root);
        //stage.setScene(scene);
        //stage.show();
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topBar.setOnMousePressed(Event -> {
            x = Event.getSceneX();
            y = Event.getSceneY();

        });
        topBar.setOnMouseDragged(event -> {
            main.primaryStage.setX(event.getScreenX() - x);
            main.primaryStage.setY(event.getScreenY() - y);

        });
    }
    //PRODUIT METHODS
    @FXML
    public void initializeProduits(int categorieId) {
        // Set up cell value factories for each column
        id.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("id"));
        designation.setCellValueFactory(new PropertyValueFactory<Produit, String>("designation"));
        quantite.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("qte"));
        prix.setCellValueFactory(new PropertyValueFactory<Produit, Double>("prix"));
        date.setCellValueFactory(new PropertyValueFactory<Produit, Date>("date"));
        dateDePeremption.setCellValueFactory(new PropertyValueFactory<Produit, Date>("peremption"));


        // Fetch data from DAO
        ObservableList<Produit> produitsList = FXCollections.observableList(produitsDao.getAll());

        // Populate the TableView with data
        ProduitsTable.setItems(produitsList);
    }
    public void displayCategorie(String categorie){
        categorieLabel.setText("Catégorie: " + categorie);
    }

    //USER METHODS
    public void initializeUsers() {
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
    public void SignOut(ActionEvent event) throws IOException {
        Parent Dashboard = FXMLLoader.load(getClass().getResource("/com/example/javafx/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(Dashboard);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
    public void addProduct(){
        addProduct_pane.toFront();
    }
//    public void ValiderProduit(){
//        String designation = designation_field.getText();
//        int qte = Integer.parseInt(quantite_field.getText());
//        float prix = Float.parseFloat(prix_field.getText());
//        LocalDate date = date_field.getValue();
//        LocalDate peremption = peremption_field.getValue();
//        int categoryId = categorieNumber;
//        Produit p = new Produit(categoryId, designation, qte, prix, date, peremption);
//        produitsDao.add(p);
//        products_pane.toFront();
//        initializeProduits(categorieNumber);
//
//    }
    private Boolean rules(){
        String designation = designation_field.getText();
        String quantiteText = quantite_field.getText();
        String prixText = prix_field.getText();
        LocalDate date = date_field.getValue();
        int flag=0;
        //LocalDate peremption = peremption_field.getValue();

        // Validate Designation
        if (designation.isEmpty()) {
            designation_rule.setText("Vous devez fournir la designation du produit!");
            System.out.println("designation error");
            flag+=1;
        }

        // Validate Quantite
        if (quantiteText.isEmpty()) {
            quantite_rule.setText("Vous devez fournir la quantité du produit!");
            flag+=1;
        }else{
            int qte;
            try {
                qte = Integer.parseInt(quantiteText);
                if (qte <= 0) {
                    quantite_rule.setText("le nombre doit être positif!");
                    flag+=1;
                }
            } catch (NumberFormatException e) {
                // Handle error: Quantity should be a valid integer
                quantite_rule.setText("Vous devez taper un entier!");
                flag+=1;
            }
        }

        // Validate Prix
        if (prixText.isEmpty()) {
            prix_rule.setText("Vous devez fournir le prix du produit!");
            flag+=1;
        }else{
            float prix;
            try {
                prix = Float.parseFloat(prixText);
                if (prix <= 0) {
                    prix_rule.setText("le nombre doit être positif!");
                    flag+=1;
                }
            } catch (NumberFormatException e) {
                quantite_rule.setText("Vous devez taper un nombre!");
                flag+=1;
            }
        }

        // Validate Date and Peremption
        if (date == null ){
            date_rule.setText("Vous devez fournir la date d'entrée!");
            flag+=1;
        }
        if(flag != 0) return false;
        else return true;
    }

    public void ValiderProduit() {
        if(rules()){
            System.out.println("OUI");
            int categoryId = categorieNumber;
            String designation = designation_field.getText();
            int qte = Integer.parseInt(quantite_field.getText());
            Float prix = Float.parseFloat(prix_field.getText());
            LocalDate date = date_field.getValue();
            LocalDate peremption = peremption_field.getValue();
            //TODO :: add the conditions for peremption = null
            Produit p = new Produit(categoryId, designation, qte, prix, date, peremption);
            produitsDao.add(p);
            products_pane.toFront();
            initializeProduits(categorieNumber);
        }
    }

    public void cancelProduct(){
        products_pane.toFront();
    }



}
