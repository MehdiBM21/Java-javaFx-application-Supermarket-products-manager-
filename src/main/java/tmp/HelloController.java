package tmp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToHello(ActionEvent event)throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/javafx/hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene2(ActionEvent event) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/example/javafx/scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /*@FXML
    //injects all the content in fxml file onto this controller
    private Arc myCircle;
    private double y;
    private double x;
    public void up(ActionEvent event){
        System.out.println("UP");
        myCircle.setCenterY(y-=1);
    }*/
}