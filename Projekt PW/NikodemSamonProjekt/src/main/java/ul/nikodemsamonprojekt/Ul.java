package ul.nikodemsamonprojekt;

import javafx.application.Platform;

import java.util.concurrent.Semaphore;

public class Ul {
    private static int liczbaMiejsc;
    private int wyklucie = 0;
    private static int poczatkowaLiczbaPszczol;
    private final Semaphore wlotek;
    private final Semaphore chce_wejsc;
    private final Semaphore chce_wyjsc;
    private final Semaphore miejsca;
    private final Semaphore narodziny;
    private Kontroler2Sceny kontroler2Sceny;
    int id_pszczoly;
    int ileJaj;

    public Ul(int pojemnosc, int ileJaj, int ilePszczol, Kontroler2Sceny kontroler2Sceny){
        this.kontroler2Sceny = kontroler2Sceny;
        this.id_pszczoly = ilePszczol + 1;
        this.ileJaj = ileJaj;
        this.wlotek = new Semaphore(2);
        this.chce_wejsc = new Semaphore(2,true);
        this.chce_wyjsc = new Semaphore(2, true);
        this.narodziny = new Semaphore(1);
        this.miejsca = new Semaphore(pojemnosc);
    }


    public synchronized void zwieksz(){
        wyklucie++;
    }

    public synchronized void zeruj(){
        wyklucie = 0;
    }

    public synchronized int getWyklucie(){
        return wyklucie;
    }
    public void wejdz(int ileWizyt, int nr){
        try {
            chce_wejsc.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            miejsca.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            wlotek.acquire();
            if (kontroler2Sceny != null) {
                Platform.runLater(() -> kontroler2Sceny.wejdz());
            }
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("--> Pszczolka " + nr + " wchodzi do ula " + ileWizyt + " raz. Miejsc w ulu: " + miejsca.availablePermits());
        wlotek.release();
        chce_wejsc.release();
    }

    public void wyjdz(int ileWizyt, int nr, int maksWizyt){
        try {
            chce_wyjsc.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            wlotek.acquire();
            if (kontroler2Sceny != null && ileWizyt == maksWizyt) {
                Platform.runLater(() -> kontroler2Sceny.zgin());
            }
            else if(kontroler2Sceny != null){
                Platform.runLater(()-> kontroler2Sceny.wyjdz());
            }
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("<-- Pszczolka " + nr + " wychodzi z ula " + ileWizyt + " raz\n");
        wlotek.release();
        miejsca.release();
        chce_wyjsc.release();
    }

    public void narodziny(){
        //procedura dla swiezo urodzonej pszczoly
        try {
            miejsca.acquire();
            System.out.println("Pszczolka urodzila sie :)\n");
            //jakas animacja?
            narodziny.acquire();
            zwieksz();
            narodziny.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void zlozJaja(){
        try {
            miejsca.acquire(ileJaj);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Krolowa zajela miejsce na jaja\n");
        try {
            chce_wejsc.acquire(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        miejsca.release(ileJaj);
        for(int i = 0; i<ileJaj; i++){
            new Pszczola(0,3,this, id_pszczoly, true).start();
            id_pszczoly++;
        }
        while(getWyklucie() < ileJaj)
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            narodziny.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        kontroler2Sceny.wypiszIleNowych(ileJaj);
        zeruj();
        narodziny.release();
        chce_wejsc.release(2);
    }

}