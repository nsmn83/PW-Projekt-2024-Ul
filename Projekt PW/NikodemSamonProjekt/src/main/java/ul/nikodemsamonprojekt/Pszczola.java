package ul.nikodemsamonprojekt;

public class Pszczola extends Thread{
    private final Ul ul;
    private int ileWizyt;
    private int maksWizyt;
    private boolean nowa;
    private int id;
    Pszczola(int ileWizyt, int maksWizyt, Ul ul, int id, boolean nowa){
        this.ileWizyt = ileWizyt;
        this.maksWizyt = maksWizyt;
        this.ul = ul;
        this.id = id;
        this.nowa = nowa;
    }

    @Override
    public void run() {
        while(ileWizyt < maksWizyt)
        {
            if(nowa){
                nowa = false;
                ul.narodziny();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ul.wyjdz(ileWizyt, id, maksWizyt);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ileWizyt++;
            ul.wejdz(ileWizyt, id);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ul.wyjdz(ileWizyt, id, maksWizyt);

        }
        System.out.println("Pszczolka " + id + " umiera :(\n");
    }
}