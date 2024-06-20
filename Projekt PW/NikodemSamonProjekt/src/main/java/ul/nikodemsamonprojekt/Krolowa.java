package ul.nikodemsamonprojekt;

public class Krolowa extends Thread{
    private final Ul ul;
    Krolowa(Ul ul){
        this.ul = ul;
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ul.zlozJaja();
        }
    }
}