package ul.nikodemsamonprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Kontroler1Sceny {
    int miejsca; //ilosc miejsc w ulu
    int pszczoly; //ilosc pszczol na poczatku programu
    int czasZycia; //czas zycia pszczoly liczony iloscia odwiedzin
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField inputMiejsca;
    @FXML
    private TextField inputPszczoly;
    @FXML
    private TextField inputZycia;
    @FXML
    void login(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scena2.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Kontroler2Sceny scene2Controller = loader.getController();

        //zaladowanie poczatkowych wartosci danych wejsciowych
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try{
            miejsca = Integer.parseInt(inputMiejsca.getText());
            pszczoly = Integer.parseInt(inputPszczoly.getText());
            czasZycia = Integer.parseInt(inputZycia.getText());
        }
        catch(NumberFormatException e){
            miejsca = Integer.parseInt(properties.getProperty("liczbaMiejsc"));
            pszczoly = Integer.parseInt(properties.getProperty("liczbaPszczol"));
            czasZycia = Integer.parseInt(properties.getProperty("czasZycia"));
        }

        scene2Controller.startSymulacji(miejsca, pszczoly, czasZycia);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}


