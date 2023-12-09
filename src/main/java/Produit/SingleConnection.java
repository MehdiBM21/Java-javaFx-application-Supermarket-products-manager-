package Produit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnection {
    //1 connexion à la BD
    String db = "supermarche_db";
    String user = "root";
    String pwd = "";
    String url = "jdbc:mysql://localhost:3306/"+db;
    private static Connection connection = null;
    private SingleConnection(){
        try {
            connection = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connexion réussie!");

        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        if(connection == null){
            new SingleConnection();
            //System.out.println("instance cree");

        }
        return connection;
    }

}
