package com.example.javafx;

import Produit.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ProduitsController{
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

    private ProduitDaoImpl produitsDao = new ProduitDaoImpl();

    @FXML
    public void initialize(int categorieId) {
        // Set up cell value factories for each column
        id.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("id"));
        designation.setCellValueFactory(new PropertyValueFactory<Produit, String>("designation"));
        quantite.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("qte"));
        prix.setCellValueFactory(new PropertyValueFactory<Produit, Double>("prix"));
        date.setCellValueFactory(new PropertyValueFactory<Produit, Date>("date"));
        dateDePeremption.setCellValueFactory(new PropertyValueFactory<Produit, Date>("peremption"));


        // Fetch data from DAO
        ObservableList<Produit> produitsList = FXCollections.observableList(produitsDao.getProduitByCategorie(categorieId));

        // Populate the TableView with data
        ProduitsTable.setItems(produitsList);
    }
    public void displayCategorie(String categorie){
        categorieLabel.setText("Catégorie: " + categorie);
    }
    public void switchToDashBoard(ActionEvent event) throws IOException {
        Parent Dashboard = FXMLLoader.load(getClass().getResource("/com/example/javafx/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(Dashboard);
        stage.setScene(scene);
        stage.show();
    }


}
