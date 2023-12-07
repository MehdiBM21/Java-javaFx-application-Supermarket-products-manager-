package login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML
    Label nameLabel;
    @FXML
    Label name;

    public void displayName(String username){
        nameLabel.setText("Hello: " + username);
        name.setText(username);
        System.out.println(username);
    }
}
