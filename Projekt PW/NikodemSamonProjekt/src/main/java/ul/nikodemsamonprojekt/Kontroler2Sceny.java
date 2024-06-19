package ul.nikodemsamonprojekt;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Kontroler2Sceny implements Initializable {

    @FXML
    private Text tekst;
    @FXML
    private Pane panelAnimacji; // Panel do dodawania animacji
    private int liczbaMiejsc;
    private int poczatkowaLiczbaPszczol;
    private int czasZycia;

    private Ul ul;
    private final Random random = new Random();
    private final int szerokosc = 900; // szerokość okna
    private final int wysokosc = 600; // wysokość okna

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void startSymulacji(int liczbaMiejsc, int poczatkowaLiczbaPszczol, int czasZycia) {
        this.liczbaMiejsc = liczbaMiejsc;
        this.poczatkowaLiczbaPszczol = poczatkowaLiczbaPszczol;
        this.czasZycia = czasZycia;

        double temp = liczbaMiejsc * 0.8;
        int ileJaj = (int) temp;

        ul = new Ul(liczbaMiejsc, ileJaj, poczatkowaLiczbaPszczol, this);

        for (int i = 0; i < poczatkowaLiczbaPszczol; ++i) {
            Pszczola pszczola = new Pszczola(0, czasZycia, ul, i, false);
            pszczola.start();
        }

        Krolowa krolowa = new Krolowa(ul);
        krolowa.start();
    }


    public void wejdz() {
        // Tworzenie kółka
        Circle circle = new Circle(20, Color.YELLOW);
        circle.setCenterX(-20);
        circle.setCenterY(random.nextInt(wysokosc - 100)+50);

        // Dodawanie kółka do panelu
        panelAnimacji.getChildren().add(circle);

        // Tworzenie i konfiguracja animacji
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), circle);
        transition.setFromX(-20);
        transition.setToX(szerokosc+20);
        transition.setOnFinished(event -> Platform.runLater(() -> panelAnimacji.getChildren().remove(circle)));

        // Uruchomienie animacji
        transition.play();
    }

    public void wyjdz() {
        // Tworzenie kółka
        Circle circle = new Circle(20, Color.YELLOW);
        circle.setCenterX(-20);
        circle.setCenterY(random.nextInt(wysokosc - 100)+50);

        // Dodawanie kółka do panelu
        panelAnimacji.getChildren().add(circle);

        // Tworzenie i konfiguracja animacji
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), circle);
        transition.setFromX(szerokosc+20);
        transition.setToX(-20);
        transition.setOnFinished(event -> Platform.runLater(() -> panelAnimacji.getChildren().remove(circle)));

        // Uruchomienie animacji
        transition.play();
    }

    public void zgin() {
        Circle circle = new Circle(20, Color.ORANGERED);
        circle.setCenterX(-20);
        circle.setCenterY(random.nextInt(wysokosc - 100)+50);

        // Dodawanie kółka do panelu
        panelAnimacji.getChildren().add(circle);

        // Tworzenie i konfiguracja animacji
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), circle);
        transition.setFromX(szerokosc+20);
        transition.setToX(-20);
        transition.setOnFinished(event -> Platform.runLater(() -> panelAnimacji.getChildren().remove(circle)));

        // Uruchomienie animacji
        transition.play();
    }

    public void wypiszIleNowych(int x){
        tekst.setText("Wyklucie " + x + " pszczół.\n");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> tekst.setText(""));
        pause.play();
    }
}
