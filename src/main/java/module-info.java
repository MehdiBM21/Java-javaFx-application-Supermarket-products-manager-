module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires java.base;
    requires com.jfoenix;
    requires AnimateFX;

    opens com.example.javafx to javafx.fxml, java.base;
    opens Backend.Produit;
    opens Backend.User;
    opens Backend.Dao;
    exports com.example.javafx;
    exports login;
    opens login to javafx.fxml;
    exports tmp;
    opens tmp to javafx.fxml;
    opens Backend;
}