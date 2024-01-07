package com.example.javafx;

import Backend.Categorie.Categorie;
import Backend.Categorie.CategorieDaoImpl;
import Backend.Produit.Produit;
import Backend.Produit.ProduitDaoImpl;
import Backend.User.User;
import Backend.User.UserDaoImpl;
import animatefx.animation.RotateIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Effect;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable{
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Button users, home ,refreshproduits_btn,refreshUsers_btn,refreshCategories_btn ,products;
    @FXML
    private Text refresh_icon,refresh_icon2,refresh_icon3;

    @FXML
    private FlowPane DBCategories_container;

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
    MFXTableView<Produit> ProduitsTable,AllProducts_table;
    @FXML
    MFXTableView<User> usersTable;

    @FXML
    Label categorieLabel, modifier_label, modifierUser_label;
    @FXML
    Label designation_rule, quantite_rule, prix_rule, date_rule, peremption_rule,
            modifierDesignation_rule, modifierQuantite_rule, modifierPrix_rule, modifierDate_rule, modifierPeremption_rule,
            username_rule, password_rule, type_rule,
            modifierUsername_rule, modifierPassword_rule, modifierType_rule,
            categoryName_rule;

    private UserDaoImpl userDao = new UserDaoImpl();
    private ProduitDaoImpl produitsDao = new ProduitDaoImpl();
    private CategorieDaoImpl categorieDao = new CategorieDaoImpl();

    private double x,y;//for dragging
    private int categorieNumber, productId_modifier, userId_modifier, qteAction;
    //panes
    @FXML
    Pane users_pane, dashboard_pane, products_pane, addProduct_pane, modifierProduct_pane,
            addUser_pane, modifierUser_pane,
            addCategory_pane,allProducts_Pane;
    @FXML
    TextField designation_field, prix_field,
              modifierDesignation_field, modifierQuantite_field, modifierPrix_field,
                username_field, password_field,
                modifierUsername_field, modifierPassword_field, modifierType_field,
                categoryName_field,quantite_field ;
    @FXML
    MFXDatePicker peremption_field,date_field, modifierDate_field, modifierPeremption_field;
    @FXML
    private MFXComboBox<String> type_field;

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
    public void minimize_window(ActionEvent event){
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    }

    public void switchTo(ActionEvent event) throws IOException {
        //SIDEBAR BUTTONS
        //take the id of each sidebar button and show the corresponding pane
        Button sidebarBtn = (Button) event.getSource();
        if(sidebarBtn == home){
            dashboard_pane.toFront();
        }
        if (sidebarBtn == products){
            initialize_AllProducts();
            allProducts_Pane.toFront();
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
//        System.out.println(buttonText +"  "+categorieNumber);
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
        refreshproduits_btn.setOnMousePressed(event ->{
            RotateIn rotateIn=new RotateIn(refresh_icon);
            rotateIn.setSpeed(0.7);
            rotateIn.play();
        });
        refreshUsers_btn.setOnMousePressed(event ->{
            RotateIn rotateIn=new RotateIn(refresh_icon2);
            rotateIn.setSpeed(0.7);
            rotateIn.play();
        });
        refreshCategories_btn.setOnMousePressed(event ->{
            RotateIn rotateIn=new RotateIn(refresh_icon3);
            rotateIn.setSpeed(0.7);
            rotateIn.play();
        });
     setup_table_produit();
     setup_table_users();
        setup_table_ALLProducts();
        refreshCategory();
    ObservableList<String> type_comboBox=FXCollections.observableArrayList("admin","caissier");
    type_field.setItems(type_comboBox);

    }
    //PRODUIT METHODS POURCHAQUE CATEGORIE
    public void setup_table_produit(){
        MFXTableColumn<Produit> idColumnProduit = new MFXTableColumn<>("Id", true, Comparator.comparing(Produit::getId));
        MFXTableColumn<Produit> designationColumn = new MFXTableColumn<>("Designation", true, Comparator.comparing(Produit::getDesignation));
        MFXTableColumn<Produit> quantiteColumn = new MFXTableColumn<>("Quantite", true, Comparator.comparing(Produit::getQte));
        MFXTableColumn<Produit> prixColumn = new MFXTableColumn<>("Prix", true, Comparator.comparing(Produit::getPrix));
        MFXTableColumn<Produit> dateColumn = new MFXTableColumn<>("Date", true, Comparator.comparing(Produit::getDate));
        MFXTableColumn<Produit> peremptionColumn = new MFXTableColumn<>("Peremption", true, Comparator.comparing(Produit::getPeremption));
        MFXTableColumn<Produit> supprimerColumn = new MFXTableColumn<>("Supprimer", true);
        MFXTableColumn<Produit> modifierColumn = new MFXTableColumn<>("Modifier", true);
        idColumnProduit.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getId));
        designationColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getDesignation));
        quantiteColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getQte));
        prixColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getPrix));
        dateColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getDate));
        peremptionColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getPeremption));
        supprimerColumn.setRowCellFactory(produit -> new MFXTableRowCell<>(data -> data.getId(), data -> {return null;}) {
            {
                setAlignment(Pos.CENTER);
                setMinWidth(60);

                MFXButton supprimerButton = new MFXButton("");
                FontAwesomeIcon icon= new FontAwesomeIcon();
                icon.setGlyphName("TRASH");
                icon.setSize("23");
                icon.setFill(Color.web("#bd2626"));
                supprimerButton.setGraphic(icon);
                supprimerButton.setId("delete_icon");
                supprimerButton.getStyleClass().add("produit.css");
                supprimerButton.setOnAction(event -> {
                    supprimerProduit(produit);
                    System.out.println("Supprimer produit avec l'ID : " + produit.getId());
                });
                setGraphic(supprimerButton);
            }
        });
        modifierColumn.setRowCellFactory(produit -> new MFXTableRowCell<>(data -> data.getId(), data -> {return null;}) {
            {
                setAlignment(Pos.CENTER);
                setMinWidth(60);
                MFXButton modifierButton = new MFXButton("");
                FontAwesomeIcon icon= new FontAwesomeIcon();
                icon.setGlyphName("EDIT");
                icon.setSize("23");
                icon.setFill(Color.web("#328D4CFF"));
                modifierButton.setGraphic(icon);
                modifierButton.setId("edit_icon");
                modifierButton.getStyleClass().add("produit.css");
                modifierButton.setOnAction(event -> {
                    productId_modifier = produit.getId();
                    System.out.println("ID produit " + produit.getId());
                    modifier(produit);
                    modifier_label.setText("Modifier le Produit "+produit.getId());
                    // Logique pour la modification ici
                    System.out.println("Modifier produit avec l'ID : " + produit.getId());
                });
                setGraphic(modifierButton);
            }
        });
        idColumnProduit.setAlignment(Pos.CENTER);
        idColumnProduit.setMinWidth(50);
        designationColumn.setAlignment(Pos.CENTER);
        designationColumn.setMinWidth(150);
        quantiteColumn.setAlignment(Pos.CENTER);
        quantiteColumn.setMinWidth(100);
        prixColumn.setAlignment(Pos.CENTER);
        prixColumn.setMinWidth(100);
        dateColumn.setAlignment(Pos.CENTER);
        dateColumn.setMinWidth(150);
        peremptionColumn.setAlignment(Pos.CENTER);
        prixColumn.setMinWidth(100);
        ProduitsTable.getTableColumns().addAll(idColumnProduit, designationColumn,quantiteColumn,prixColumn,dateColumn,peremptionColumn,supprimerColumn,modifierColumn);
        ProduitsTable.getFilters().addAll(
                new StringFilter<>("Designation", Produit::getDesignation),
                new IntegerFilter<>("Quantite", Produit::getQte),
                new DoubleFilter<>("Prix", Produit::getPrix)

        );
    };
    @FXML
        public void initializeProduits() {
        ObservableList<Produit> produitsList = FXCollections.observableArrayList(produitsDao.getProduitByCategorie(categorieNumber));
        ProduitsTable.setItems(produitsList);
    }



//    private Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> createButtonCellFactory() {
//        return new Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>>() {
//            @Override
//            public TableCell<Produit, Void> call(final TableColumn<Produit, Void> param) {
//                return new TableCell<Produit, Void>() {
//                    private final Button modifierButton = new Button("Modifier");
//                    private final Button supprimerButton = new Button("Supprimer");
//
//                    {
//                        modifierButton.setOnAction(event -> {
//                            Produit produit = getTableView().getItems().get(getIndex());
//                            productId_modifier = produit.getId();
//                            System.out.println("ID produit " + produit.getId());
//                            modifier(produit);
//                            modifier_label.setText("Modifier le Produit "+produit.getId());
//                            // Logique pour la modification ici
//                            System.out.println("Modifier produit avec l'ID : " + produit.getId());
//                        });
@FXML
public void setup_table_ALLProducts(){
    MFXTableColumn<Produit> idColumnProduit = new MFXTableColumn<>("Id Produit", true, Comparator.comparing(Produit::getId));
    MFXTableColumn<Produit> idCategorie_ColumnProduit = new MFXTableColumn<>("Categorie",true);
    MFXTableColumn<Produit> designationColumn = new MFXTableColumn<>("Designation", true, Comparator.comparing(Produit::getDesignation));
    MFXTableColumn<Produit> quantiteColumn = new MFXTableColumn<>("Quantite", true, Comparator.comparing(Produit::getQte));
    MFXTableColumn<Produit> prixColumn = new MFXTableColumn<>("Prix", true, Comparator.comparing(Produit::getPrix));
    MFXTableColumn<Produit> dateColumn = new MFXTableColumn<>("Date", true, Comparator.comparing(Produit::getDate));
    MFXTableColumn<Produit> peremptionColumn = new MFXTableColumn<>("Peremption", true, Comparator.comparing(Produit::getPeremption));

    idColumnProduit.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getId));
    idCategorie_ColumnProduit.setRowCellFactory(produit -> new MFXTableRowCell<>(data -> data.getId(), data -> {
        return  categorieDao.getNomCategorieFromId(produit.getIdCategorie());
    }));
    designationColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getDesignation));
    quantiteColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getQte));
    prixColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getPrix));
    dateColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getDate));
    peremptionColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getPeremption));

    idColumnProduit.setAlignment(Pos.CENTER);
    idColumnProduit.setMinWidth(100);
    idCategorie_ColumnProduit.setAlignment(Pos.CENTER);
    idCategorie_ColumnProduit.setMinWidth(180);
    designationColumn.setAlignment(Pos.CENTER);
    designationColumn.setMinWidth(150);
    quantiteColumn.setAlignment(Pos.CENTER);
    quantiteColumn.setMinWidth(100);
    prixColumn.setAlignment(Pos.CENTER);
    prixColumn.setMinWidth(100);
    dateColumn.setAlignment(Pos.CENTER);
    dateColumn.setMinWidth(125);

    peremptionColumn.setAlignment(Pos.CENTER);
    peremptionColumn.setMinWidth(125);
    peremptionColumn.setAlignment(Pos.CENTER);
    prixColumn.setMinWidth(100);
    AllProducts_table.getTableColumns().addAll(idColumnProduit,idCategorie_ColumnProduit, designationColumn,quantiteColumn,prixColumn,dateColumn,peremptionColumn);
    AllProducts_table.getFilters().addAll(
            new IntegerFilter<>("Quantite", Produit::getId),
            new StringFilter<>("Designation", Produit::getDesignation),
            new IntegerFilter<>("Quantite", Produit::getQte),
            new DoubleFilter<>("Prix", Produit::getPrix)
    );
}
    @FXML
    public void initialize_AllProducts() {
        ObservableList<Produit> produitsList = FXCollections.observableArrayList(produitsDao.getAll());
        AllProducts_table.setItems(produitsList);
    }

//                        supprimerButton.setOnAction(event -> {
//                            Produit produit = getTableView().getItems().get(getIndex());
//                            supprimerProduit(produit);
//                            System.out.println("Supprimer produit avec l'ID : " + produit.getId());
//                        });
//                    }
//
//                    @Override
//                    protected void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//
//                        if (empty) {
//                            setGraphic(null);
//                        } else {
//                            setGraphic(createButtonPane());
//                        }
//                    }
//
//                    private Pane createButtonPane() {
//                        HBox buttonPane = new HBox(modifierButton, supprimerButton);
//                        buttonPane.setSpacing(5);
//                        return buttonPane;
//                    }
//                };
//            }
//        };
//    }

    public void displayCategorie(String categorie){
        categorieLabel.setText("Catégorie: " + categorie);
    }

    //USER METHODS
    public void setup_table_users(){
        MFXTableColumn<User>  idColumn = new MFXTableColumn<>("Id", true);
        MFXTableColumn<User> usernameColumn = new MFXTableColumn<>("Username", true);
        MFXTableColumn<User> passwordColumn = new MFXTableColumn<>("Password", true);
        MFXTableColumn<User> typeColumn = new MFXTableColumn<>("Type", true);

        MFXTableColumn<User> supprimerColumn = new MFXTableColumn<>("Supprimer", true);
        MFXTableColumn<User> modifierColumn = new MFXTableColumn<>("Modifier", true);

        idColumn.setRowCellFactory(user -> new MFXTableRowCell<>(Backend.User.User::getId));
        usernameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(Backend.User.User::getUsername));
        passwordColumn.setRowCellFactory(user -> new MFXTableRowCell<>(Backend.User.User::getPassword));
        typeColumn.setRowCellFactory(user -> new MFXTableRowCell<>(Backend.User.User::getType));

        supprimerColumn.setRowCellFactory(user -> new MFXTableRowCell<>(data -> data.getId(), data -> {return null;}) {
            {
                setAlignment(Pos.CENTER);
                setMinWidth(60);

                MFXButton supprimerUserButton = new MFXButton("");
                FontAwesomeIcon icon= new FontAwesomeIcon();
                icon.setGlyphName("TRASH");
                icon.setSize("23");
                supprimerUserButton.setStyle("-fx-background-color: transparent;\n" +
                        "    -fx-padding: 0;");
                icon.setFill(Color.web("#bd2626"));
                supprimerUserButton.setGraphic(icon);
//                supprimerUserButton.setId("delete_icon");
//                supprimerUserButton.getStyleClass().add("produit.css");
                supprimerUserButton.setOnAction(event -> {
                    supprimerUser(user);
                    System.out.println("Supprimer utilisateur avec l'ID : " + user.getId());
                });
                setGraphic(supprimerUserButton);
            }
        });
        modifierColumn.setRowCellFactory(user -> new MFXTableRowCell<>(data -> data.getId(), data -> {return null;}) {
            {
                setAlignment(Pos.CENTER);
                setMinWidth(60);
                MFXButton modifierUserButton = new MFXButton("");
                FontAwesomeIcon icon= new FontAwesomeIcon();
                modifierUserButton.setStyle("-fx-background-color: transparent;\n" +
                        "    -fx-padding: 0;");
                icon.setGlyphName("EDIT");
                icon.setSize("23");
                icon.setFill(Color.web("#328D4CFF"));
                modifierUserButton.setGraphic(icon);
//                modifierUserButton.setId("edit_icon");
//                modifierUserButton.getStyleClass().add("produit.css");
                modifierUserButton.setOnAction(event -> {
                    userId_modifier = user.getId();
                    modifierUser(user);
                    modifierUser_label.setText("Modifier l'Utilisateur "+user.getId());
                    System.out.println("Modifier utilisateur avec l'ID : " + user.getId());
                });
                setGraphic(modifierUserButton);
            }
        });
        idColumn.setAlignment(Pos.CENTER);
        idColumn.setMinWidth(50);
        usernameColumn.setAlignment(Pos.CENTER);
        usernameColumn.setMinWidth(150);
        passwordColumn.setAlignment(Pos.CENTER);
        passwordColumn.setMinWidth(150);
        typeColumn.setAlignment(Pos.CENTER);
        typeColumn.setMinWidth(100);

        usersTable.getTableColumns().addAll(idColumn, usernameColumn,passwordColumn,typeColumn,supprimerColumn,modifierColumn);
        usersTable.getFilters().addAll(
                new IntegerFilter<>("Id", User::getId),
                new StringFilter<>("Username", User::getUsername),
                new StringFilter<>("Prix", User::getPassword),
                new StringFilter<>("Type",User::getType)

        );
    }
    public void initializeUsers() {


        // Fetch data from DAO
        ObservableList<User> usersList = FXCollections.observableList(userDao.getAll());

        // Populate the TableView with data
        usersTable.setItems(usersList);

    }

//    private void addActionsColumnEventHandlers() {
//        // Assuming you have a column named "actionsColumn" in your FXML
//        TableColumn<User, Void> actionsColumn = (TableColumn<User, Void>) usersTable.getColumns().stream()
//                .filter(column -> "actionsColumn".equals(column.getId()))
//                .findFirst()
//                .orElse(null);
//
//        if (actionsColumn != null) {
//            // Set up a cell factory for the actions column
//            Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = (TableColumn<User, Void> param) -> {
//                final TableCell<User, Void> cell = new TableCell<User, Void>() {
//                    private final Button editButton = new Button("Modifier");
//                    private final Button deleteButton = new Button("Supprimer");
//
//                    {
//                        // Add an event handler to the "Modifier" button
//                        editButton.setOnAction(event -> {
//                            User user = getTableView().getItems().get(getIndex());
//                            //Produit produit = getTableView().getItems().get(getIndex());
//                            //productId_modifier = produit.getId();
//                            //System.out.println("ID produit " + produit.getId());
//                            userId_modifier = user.getId();
//                            modifierUser(user);
//                            modifierUser_label.setText("Modifier l'Utilisateur "+user.getId());
//                            System.out.println("Modifier utilisateur avec l'ID : " + user.getId());
//                        });
//
//                        // Add an event handler to the "Supprimer" button
//                        deleteButton.setOnAction(event -> {
//                            User user = getTableView().getItems().get(getIndex());
//                            supprimerUser(user);
//                            System.out.println("Supprimer utilisateur avec l'ID : " + user.getId());
//                        });
//
//                        // Set the buttons as the graphic for the cell
//                        setGraphic(new HBox(editButton, deleteButton));
//                    }
//
//                    @Override
//                    protected void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//
//                        // Make sure the cell is empty
//                        if (empty) {
//                            setGraphic(null);
//                        }
//                    }
//                };
//
//                return cell;
//            };
//
//            // Set the custom cell factory for the actions column
//            actionsColumn.setCellFactory(cellFactory);
//        }
//    }


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
        designation_field.setText("");
        quantite_field.setText("");
        prix_field.setText("");
        date_field.setText("");
        peremption_field.setText("");
        quantite_rule.setText("");
        designation_rule.setText("");
        date_rule.setText("");
        peremption_rule.setText("");
        prix_rule.setText("");
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
        return flag == 0;
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
        System.out.println(rules());
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
            System.out.println("le produit est ajouter");
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
        qteAction = p.getQte();
        modifierPrix_field.setText(String.valueOf(p.getPrix()));
        modifierDate_field.setValue(p.getDate());
        modifierPeremption_field.setValue(p.getPeremption());
        modifierDesignation_rule.setText("");
        modifierQuantite_rule.setText("");
        modifierPrix_rule.setText("");
        modifierDate_rule.setText("");
        modifierPeremption_rule.setText("");
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
            int result = qte - qteAction;
            System.out.println(result);
            if(result < 0) {//on a retiré du produit
                produitsDao.update(p, -1, -(result));
            } else if (result > 0) {//on a ajouté du produit
                produitsDao.update(p, 1,result);
            }else{//on a rien fait (la table historique ne sera pas remplie
                produitsDao.update(p, 0, result);
            }

        }
            products_pane.toFront();
            initializeProduits();
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
            if(category.getId() > 5){
                System.out.println(category.getId());
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
//           DBCategories_container.getChildren().add(categoryButton);
           DBCategories_container.getChildren().addLast(categoryButton);
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
                refreshCategory();
        }
    }
    public void refreshCategory(){
        int size=DBCategories_container.getChildren().size();
        DBCategories_container.getChildren().remove(5,size);
        initializeCategories();
    }


}
