package org.aufgabe1;

import java.util.concurrent.locks.ReentrantLock;



public class Student extends Thread {
    Kasse kasse;
    private String name;
    private Kasse[] kassen;
    Mensa mensa;

    public Student(String name, Kasse[] checkouts, Mensa mensa){
        this.name = name;
        this.kassen = checkouts;
        this.mensa = mensa;
    }

    @Override
    public void run() {
        try{
            while (!isInterrupted()){
                kasse = kasseWaehlen();
                kasse.bezahlen(name);
                kasseFreigeben(kasse);
                // Warten, da Student gerade am essen ist
                warten();
                // Student möchte sich wieder etwas kaufen
                warten();
            }
        }catch (InterruptedException e){
            //System.err.println(isInterrupted() + " ------------------");
            interrupt();
        }
        System.err.println(this.name + " wurde interrupted");
    }

    public void warten() throws InterruptedException {
        int wartezeit = (int) (Math.random() *100);
        Thread.sleep(wartezeit);
    }

    public Kasse kasseWaehlen(){
        Kasse ausgesuchteKasse = kassen[0];

        mensa.warteschlangenLock.lock();
        //kürzeste kasse wird ermittelt
        for (int i = 0; i < kassen.length; i++) {
            if(kassen[i].warteschlangeLaenge < ausgesuchteKasse.warteschlangeLaenge){
                ausgesuchteKasse = kassen[i];
            }
        }


        //Student stellt sich an die kürzeste Kasse, daher ++
        ausgesuchteKasse.warteschlangeLaenge++;

        System.out.println(this.name + " stellt sich an die Kasse " + ausgesuchteKasse.name);
        mensa.warteschlangenLock.unlock();
        return ausgesuchteKasse;
    }

    public void kasseFreigeben(Kasse checkout){
        mensa.warteschlangenLock.lock();
        checkout.warteschlangeLaenge--;
        mensa.warteschlangenLock.unlock();
    }

}
