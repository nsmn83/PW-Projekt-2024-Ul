package ul.nikodemsamonprojekt;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Random;

public class Kontroler2Sceny {

    @FXML
    private Text tekst; // tekst do wyswietlania informacji
    @FXML
    private AnchorPane panelAnimacji;

    private int liczbaMiejsc;
    private int poczatkowaLiczbaPszczol;
    private int czasZycia;

    private Ul ul;
    private final Random random = new Random();
    private final int szerokosc = 900; // szerokosc okna
    private final int wysokosc = 600; // wysokosc okna

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


    public void wlot(int y, boolean wlot, boolean smierc) {
        Circle circle = new Circle(20, smierc ? Color.RED : Color.YELLOW); //tworzenie kolka
        circle.setCenterX(-20);
        circle.setCenterY(y);
        panelAnimacji.getChildren().add(circle);

        int poczatek = -20; //kofiguracja animacji
        int koniec = szerokosc+20;
        if(wlot == false){
            poczatek = szerokosc+20;
            koniec = -20;
        }
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), circle);
        transition.setFromX(poczatek);
        transition.setToX(koniec);
        transition.setOnFinished(event -> Platform.runLater(() -> panelAnimacji.getChildren().remove(circle)));

        transition.play();
    }

    public void wypiszZajecieMiejsca(int x){
        tekst.setText("Ilość nowych pszczół: " + x + "\n");
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> tekst.setText(""));
        pause.play();
    }
}