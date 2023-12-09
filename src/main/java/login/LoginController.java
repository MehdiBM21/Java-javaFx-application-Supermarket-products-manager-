package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField username;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void login(ActionEvent event) throws IOException {
        String username = this.username.getText();
        System.out.println(username);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx/dashboard.fxml"));
        root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.displayName(username);
        //root = FXMLLoader.load(getClass().getResource("/login/dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
