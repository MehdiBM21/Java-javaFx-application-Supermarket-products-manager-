package com.example.javafx;
import Backend.Categorie.Categorie;
import Backend.Categorie.CategorieDaoImpl;
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
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable{
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Button users, home;
    @FXML
    private HBox DBCategories_container;

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
    TableColumn<Produit, Void> actions;
    @FXML
    Label categorieLabel, modifier_label, modifierUser_label;
    @FXML
    Label designation_rule, quantite_rule, prix_rule, date_rule, peremption_rule,
            modifierDesignation_rule, modifierQuantite_rule, modifierPrix_rule, modifierDate_rule, modifierPeremption_rule,
            username_rule, password_rule, type_rule,
            modifierUsername_rule, modifierPassword_rule, modifierType_rule,
            categoryName_rule;
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
    @FXML
    TableColumn<User, Void> actionsColumn;
    //DAO
    private UserDaoImpl userDao = new UserDaoImpl();
    private ProduitDaoImpl produitsDao = new ProduitDaoImpl();
    private CategorieDaoImpl categorieDao = new CategorieDaoImpl();

    private double x,y;//for dragging
    private int categorieNumber, productId_modifier, userId_modifier;
    //panes
    @FXML
    Pane users_pane, dashboard_pane, products_pane, addProduct_pane, modifierProduct_pane,
            addUser_pane, modifierUser_pane,
            addCategory_pane;
    @FXML
    TextField designation_field, quantite_field, prix_field, produitSearch_field,
              modifierDesignation_field, modifierQuantite_field, modifierPrix_field,
                username_field, password_field, type_field,
                modifierUsername_field, modifierPassword_field, modifierType_field,
                categoryName_field;
    @FXML
    DatePicker date_field, peremption_field,
               modifierDate_field, modifierPeremption_field;

    public void close(ActionEvent event) {

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("close");
        alert.setHeaderText("you are closing the application");
        alert.setContentText("Are you sure you want to close the application ??");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if(alert.showAndWait().get()== ButtonType.OK){
            dashboard_pane.toFront();
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
        initializeProduits();
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
    public void initializeProduits() {
        // Set up cell value factories for each column
        id.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("id"));
        designation.setCellValueFactory(new PropertyValueFactory<Produit, String>("designation"));
        quantite.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("qte"));
        prix.setCellValueFactory(new PropertyValueFactory<Produit, Double>("prix"));
        date.setCellValueFactory(new PropertyValueFactory<Produit, Date>("date"));
        dateDePeremption.setCellValueFactory(new PropertyValueFactory<Produit, Date>("peremption"));
        actions.setCellFactory(createButtonCellFactory());


        // Fetch data from DAO
        ObservableList<Produit> produitsList = FXCollections.observableList(produitsDao.getProduitByCategorie(categorieNumber));

        // Populate the TableView with data
        ProduitsTable.setItems(produitsList);
    }
    @FXML
    public void initializeProduitsSearch() {
        // Set up cell value factories for each column
        id.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("id"));
        this.designation.setCellValueFactory(new PropertyValueFactory<Produit, String>("designation"));
        quantite.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("qte"));
        prix.setCellValueFactory(new PropertyValueFactory<Produit, Double>("prix"));
        date.setCellValueFactory(new PropertyValueFactory<Produit, Date>("date"));
        dateDePeremption.setCellValueFactory(new PropertyValueFactory<Produit, Date>("peremption"));
        actions.setCellFactory(createButtonCellFactory());

        String designation = produitSearch_field.getText();
        // Fetch data from DAO
        ObservableList<Produit> produitsList = FXCollections.observableList(produitsDao.getProduitByKeyword(designation, categorieNumber));

        // Populate the TableView with data
        ProduitsTable.setItems(produitsList);
    }

    private Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> createButtonCellFactory() {
        return new Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>>() {
            @Override
            public TableCell<Produit, Void> call(final TableColumn<Produit, Void> param) {
                return new TableCell<Produit, Void>() {
                    private final Button modifierButton = new Button("Modifier");
                    private final Button supprimerButton = new Button("Supprimer");

                    {
                        modifierButton.setOnAction(event -> {
                            Produit produit = getTableView().getItems().get(getIndex());
                            productId_modifier = produit.getId();
                            System.out.println("ID produit " + produit.getId());
                            modifier(produit);
                            modifier_label.setText("Modifier le Produit "+produit.getId());
                            // Logique pour la modification ici
                            System.out.println("Modifier produit avec l'ID : " + produit.getId());
                        });

                        supprimerButton.setOnAction(event -> {
                            Produit produit = getTableView().getItems().get(getIndex());
                            supprimerProduit(produit);
                            System.out.println("Supprimer produit avec l'ID : " + produit.getId());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(createButtonPane());
                        }
                    }

                    private Pane createButtonPane() {
                        HBox buttonPane = new HBox(modifierButton, supprimerButton);
                        buttonPane.setSpacing(5);
                        return buttonPane;
                    }
                };
            }
        };
    }

    public void displayCategorie(String categorie){
        categorieLabel.setText("Catégorie: " + categorie);
    }

    //USER METHODS
    public void initializeUsers() {
        // Set up cell value factories for each column
        idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<User, String>("type"));

        // Fetch data from DAO
        ObservableList<User> usersList = FXCollections.observableList(userDao.getAll());

        // Populate the TableView with data
        usersTable.setItems(usersList);

        // Add event handlers to the existing "Actions" column buttons
        addActionsColumnEventHandlers();
    }

    private void addActionsColumnEventHandlers() {
        // Assuming you have a column named "actionsColumn" in your FXML
        TableColumn<User, Void> actionsColumn = (TableColumn<User, Void>) usersTable.getColumns().stream()
                .filter(column -> "actionsColumn".equals(column.getId()))
                .findFirst()
                .orElse(null);

        if (actionsColumn != null) {
            // Set up a cell factory for the actions column
            Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = (TableColumn<User, Void> param) -> {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {
                    private final Button editButton = new Button("Modifier");
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        // Add an event handler to the "Modifier" button
                        editButton.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            //Produit produit = getTableView().getItems().get(getIndex());
                            //productId_modifier = produit.getId();
                            //System.out.println("ID produit " + produit.getId());
                            userId_modifier = user.getId();
                            modifierUser(user);
                            modifierUser_label.setText("Modifier l'Utilisateur "+user.getId());
                            System.out.println("Modifier utilisateur avec l'ID : " + user.getId());
                        });

                        // Add an event handler to the "Supprimer" button
                        deleteButton.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            supprimerUser(user);
                            System.out.println("Supprimer utilisateur avec l'ID : " + user.getId());
                        });

                        // Set the buttons as the graphic for the cell
                        setGraphic(new HBox(editButton, deleteButton));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        // Make sure the cell is empty
                        if (empty) {
                            setGraphic(null);
                        }
                    }
                };

                return cell;
            };

            // Set the custom cell factory for the actions column
            actionsColumn.setCellFactory(cellFactory);
        }
    }


    public void SignOut(ActionEvent event) throws IOException {
        dashboard_pane.toFront();
        Parent Dashboard = FXMLLoader.load(getClass().getResource("/com/example/javafx/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(Dashboard);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }
    public void addProduct(){
        addProduct_pane.toFront();
    }
    public void addUser(){
        addUser_pane.toFront();
    }
    public void addCategory(){
        addCategory_pane.toFront();
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
    private Boolean userRules(){
        String username = username_field.getText();
        String password = password_field.getText();
        String type = type_field.getText();

        int flag=0;



        if (username.isEmpty()) {
            username_rule.setText("Vous devez fournir le username!");
            System.out.println("designation error");
            flag+=1;
        }


        if (password.isEmpty()) {
            password_rule.setText("Vous devez fournir le password");
            flag+=1;
        }
        if (type.isEmpty()) {
            type_rule.setText("Vous devez fournir le type ");
            flag+=1;
        }
        return flag == 0;
    }
    private Boolean modifierRules() {
        String designation = modifierDesignation_field.getText();
        String quantiteText = modifierQuantite_field.getText();
        String prixText = modifierPrix_field.getText();
        LocalDate date = modifierDate_field.getValue();
        int flag = 0;
        // Validate Designation
        if (designation.isEmpty()) {
            modifierDesignation_rule.setText("Vous devez fournir la designation du produit!");
            System.out.println("designation error");
            flag += 1;
        }

        // Validate Quantite
        if (quantiteText.isEmpty()) {
            modifierQuantite_rule.setText("Vous devez fournir la quantité du produit!");
            flag += 1;
        } else {
            int qte;
            try {
                qte = Integer.parseInt(quantiteText);
                if (qte <= 0) {
                    modifierQuantite_rule.setText("le nombre doit être positif!");
                    flag += 1;
                }
            } catch (NumberFormatException e) {
                // Handle error: Quantity should be a valid integer
                modifierQuantite_rule.setText("Vous devez taper un entier!");
                flag += 1;
            }
        }

        // Validate Prix
        if (prixText.isEmpty()) {
            modifierPrix_rule.setText("Vous devez fournir le prix du produit!");
            flag += 1;
        } else {
            float prix;
            try {
                prix = Float.parseFloat(prixText);
                if (prix <= 0) {
                    modifierPrix_rule.setText("le nombre doit être positif!");
                    flag += 1;
                }
            } catch (NumberFormatException e) {
                modifierPrix_rule.setText("Vous devez taper un nombre!");
                flag += 1;
            }
        }

        // Validate Date and Peremption
        if (date == null) {
            modifierDate_rule.setText("Vous devez fournir la date d'entrée!");
            flag += 1;
        }

        return flag == 0;
    }
    private Boolean modifierUserRules() {
        String username = modifierUsername_field.getText();
        String password = modifierPassword_field.getText();
        String type = modifierType_field.getText();

        int flag = 0;

        if (username.isEmpty()) {
            modifierUsername_rule.setText("Vous devez fournir le username!");
            System.out.println("username error");
            flag += 1;
        }

        if (password.isEmpty()) {
            modifierPassword_rule.setText("Vous devez fournir le password");
            flag += 1;
        }

        if (type.isEmpty()) {
            modifierType_rule.setText("Vous devez fournir le type ");
            flag += 1;
        }

        return flag == 0;
    }



    public void ValiderProduit() {
        if(rules()){
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
            initializeProduits();
        }
    }
    public void validerUser() {
        if(userRules()){
            String username = username_field.getText();
            String password = password_field.getText();
            String type = type_field.getText();
            User user = new User(username, password, type);
            userDao.add(user);
            users_pane.toFront();
            initializeUsers();
        }
    }
    public void modifier(Produit p){
        modifierDesignation_field.setText(p.getDesignation());
        modifierQuantite_field.setText(String.valueOf(p.getQte()));
        modifierPrix_field.setText(String.valueOf(p.getPrix()));
        modifierDate_field.setValue(p.getDate());
        modifierPeremption_field.setValue(p.getPeremption());
        modifierProduct_pane.toFront();
    }
    public void modifierUser(User user){
        modifierUsername_field.setText(user.getUsername());
        modifierPassword_field.setText(user.getPassword());
        modifierType_field.setText(user.getType());
        modifierUser_pane.toFront();
    }
    public void modifierValiderUser() {
        if(modifierUserRules()){
            String username = modifierUsername_field.getText();
            String password = modifierPassword_field.getText();
            String type = modifierType_field.getText();
            User user = new User(userId_modifier, username, password, type);
            userDao.update(user);
            users_pane.toFront();
            initializeUsers();
        }
    }
    public void modifierValiderProduit(){
        if (modifierRules()) {
            int categoryId = categorieNumber;
            String designation = modifierDesignation_field.getText();
            int qte = Integer.parseInt(modifierQuantite_field.getText());
            Float prix = Float.parseFloat(modifierPrix_field.getText());
            LocalDate date = modifierDate_field.getValue();
            LocalDate peremption = modifierPeremption_field.getValue(); // Adjust the field name as needed
            // TODO :: add the conditions for peremption = null
            Produit p = new Produit(productId_modifier, categoryId, designation, qte, prix, date, peremption);
            produitsDao.update(p);
            products_pane.toFront();
            initializeProduits();
        }

    }
    public void supprimerProduit(Produit produit) {
        produitsDao.delete(produit.getId());
        initializeProduits();
    }
    public void supprimerUser(User user) {
        userDao.delete(user.getId());
        initializeUsers();
    }
    public void cancelProduct(){
        products_pane.toFront();
    }
    public void cancelUser(){
        users_pane.toFront();
    }
    public void cancelCategory(){
        dashboard_pane.toFront();
    }

    public void initializeCategories(){
        List<Categorie> categories = categorieDao.getAll();
        for (Categorie category : categories) {
            if(category.getId() > 4){
            Button categoryButton = new Button(category.getNom());
            categoryButton.setId("categorie"+category.getId());
            categoryButton.setPrefHeight(127);
            categoryButton.setPrefWidth(177);
            categoryButton.setOnAction(event -> {
                try {
                    showProductsByCategorie(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
                // Logique à exécuter lorsque le bouton de catégorie est cliqué
                System.out.println("Catégorie sélectionnée : " + category);
                // Ajoutez ici la logique pour afficher les produits de la catégorie sélectionnée, par exemple
            });
            // Ajoutez le bouton à votre interface utilisateur, par exemple, à une VBox nommée "categoriesBox"
           DBCategories_container.getChildren().add(categoryButton);
        }
        }
    }
    public Boolean categoryRules(){
        String nom = categoryName_field.getText();
        int flag = 0;

        if (nom.isEmpty()) {
            categoryName_rule.setText("Vous devez fournir le nom de la catégorie!");
            flag += 1;
        }
        return flag == 0;
    }
    public void validerCategory(){
        if(categoryRules()){
                String nom = categoryName_field.getText();
                Categorie categorie = new Categorie(nom);
                categorieDao.add(categorie);
                dashboard_pane.toFront();
                initializeCategories();
        }
    }
    public void refreshCategory(){
        DBCategories_container.getChildren().clear();
        initializeCategories();
    }


}
