module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
    exports login;
    opens login to javafx.fxml;
    exports tmp;
    opens tmp to javafx.fxml;
}