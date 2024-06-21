package ul.nikodemsamonprojekt;

import javafx.application.Platform;
import java.util.concurrent.Semaphore;

public class Ul {
    private int wyklucie = 0; //zmienna sluzaca do sprawdzenia czy ze wszystkich jajek wykluly sie pszczoly
    private final Semaphore[] wlotek;
    private final Semaphore chce_wejsc;
    private final Semaphore chce_wyjsc;
    private final Semaphore miejsca;
    private Kontroler2Sceny kontroler2Sceny;
    private int id_pszczoly;
    private int ileJaj;
    private int maksWizyt;
    private boolean zajetoWejscie = false;

    public Ul(int pojemnosc, int ileJaj, int ilePszczol, Kontroler2Sceny kontroler2Sceny, int maksWizyt){
        this.maksWizyt = maksWizyt;
        this.kontroler2Sceny = kontroler2Sceny;
        this.id_pszczoly = ilePszczol + 1;
        this.ileJaj = ileJaj;
        this.wlotek = new Semaphore[2];
        this.chce_wejsc = new Semaphore(2,true);
        this.chce_wyjsc = new Semaphore(2, true);
        this.miejsca = new Semaphore(pojemnosc);
        for (int i = 0; i < 2; i++) {
            this.wlotek[i] = new Semaphore(1);
        }
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
        zajetoWejscie = false;
        while(!zajetoWejscie){
            try {
                if (wlotek[0].tryAcquire()) {
                    zajetoWejscie = true;
                    Platform.runLater(() -> kontroler2Sceny.wlot(120, true, false));
                    System.out.println("Pszczółka " + nr+ " wchodzi do ula przez wejście 1 po raz: \n" + ileWizyt);
                    Thread.sleep(1500);
                    wlotek[0].release();
                } else if (wlotek[1].tryAcquire()) {
                    zajetoWejscie = true;
                    Platform.runLater(() -> kontroler2Sceny.wlot(320, true, false));
                    System.out.println("Pszczółka " + nr+ " wchodzi do ula przez wejście 2 po raz: \n" + ileWizyt);
                    Thread.sleep(1500);
                    wlotek[1].release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        chce_wejsc.release();
    }

    public void wyjdz(int ileWizyt, int nr, int maksWizyt){
        try {
            chce_wyjsc.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        zajetoWejscie = false;
        while(!zajetoWejscie){
            try {
                if (wlotek[1].tryAcquire()) {
                    zajetoWejscie = true;
                    wywolajAnimacjeWylotu(ileWizyt, maksWizyt, 320);
                    System.out.println("Pszczółka " + nr+ " wychodzi z ula przez wejście 2 po raz: \n" + ileWizyt);
                    Thread.sleep(1500);
                    wlotek[1].release();
                } else if (wlotek[0].tryAcquire()) {
                    zajetoWejscie = true;
                    wywolajAnimacjeWylotu(ileWizyt, maksWizyt, 120);
                    System.out.println("Pszczółka " + nr+ " wychodzi z ula przez wejście 1 po raz: \n" + ileWizyt);
                    Thread.sleep(1500);
                    wlotek[0].release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        chce_wyjsc.release();
        miejsca.release();
    }

    public void wywolajAnimacjeWylotu(int ileWizyt,int maksWizyt, int ktoreWejscie){
        if(ileWizyt == maksWizyt){
            Platform.runLater(() -> kontroler2Sceny.wlot(ktoreWejscie, false, true));
        }
        else{
            Platform.runLater(() -> kontroler2Sceny.wlot(ktoreWejscie, false, false));
        }
    }

    //nowo narodzona pszczola zajmuje miejsca zajete przez krolowa
    public void narodziny(){
        try {
            miejsca.acquire();
            System.out.println("Pszczółka się urodziła.\n");
            zwieksz();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void zlozJaja(){
        kontroler2Sceny.zgloscGotowosc();
        boolean zajeteMiejsce = false;
        while(!zajeteMiejsce){
            if(miejsca.tryAcquire(ileJaj)){
                zajeteMiejsce = true;
            }
        }
        System.out.println("Królowa zajęła miejsce na jajeczka\n");
        try {
            chce_wejsc.acquire(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        miejsca.release(ileJaj);
        for(int i = 0; i<ileJaj; i++){
            new Pszczola(0,maksWizyt,this, id_pszczoly, true).start();
            id_pszczoly++;
        }
        while(getWyklucie() < ileJaj) //czekanie dopoki wszystkie pszczoly sie nie wykluja
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        zeruj();
        kontroler2Sceny.wypiszZajecieMiejsca(ileJaj);
        System.out.println("Pszczółki się wykluły - odblokowanie możliwości wchodzenia do ula\n");
        chce_wejsc.release(2);
    }

}