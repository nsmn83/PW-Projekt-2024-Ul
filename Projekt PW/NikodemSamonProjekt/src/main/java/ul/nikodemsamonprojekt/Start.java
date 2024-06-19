package ul.nikodemsamonprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Scena1.fxml"));
            Scene scena1 = new Scene(root);
            stage.setScene(scena1);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
