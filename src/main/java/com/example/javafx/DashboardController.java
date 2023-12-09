package com.example.javafx;

import javafx.event.ActionEvent;
import Produit.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController{
    private Scene scene;
    private Parent root;
    private Stage stage;

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

}
