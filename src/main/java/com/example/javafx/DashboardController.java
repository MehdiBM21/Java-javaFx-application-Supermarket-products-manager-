package com.example.javafx;

import javafx.event.ActionEvent;
import Produit.*;
import javafx.scene.control.Button;

public class DashboardController {
    public void showProductsByCategorie(ActionEvent e){
        int categorieNumber = handleButtonClick(e);
        ProduitDaoImpl dao = new ProduitDaoImpl();
        System.out.println(dao.getProduitByCategorie(categorieNumber));
    }
    private int handleButtonClick(ActionEvent event) {
        // Retrieve the source (button) of the event
        Button clickedButton = (Button) event.getSource();

        // Retrieve and parse the ID of the clicked button
        String buttonId = clickedButton.getId();
        return parseCategoryNumber(buttonId);
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
