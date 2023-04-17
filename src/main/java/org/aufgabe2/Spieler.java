package org.aufgabe2;

public class Spieler extends Thread {
    private Tisch tisch;

    public Spieler(Tisch tisch){
        this.tisch = tisch;
    }

    @Override
    public void run() {
        try {
        while (!isInterrupted()) {
            //schere stein papier wird randomisiert
            int schereSteinPapier = (int) (3 * Math.random());
            tisch.spielen(SchereSteinPapier.values()[schereSteinPapier]);
        }
    }catch (InterruptedException ex) {
        System.err.println(this.getName() + " wurde erfolgreich interrupted!");
    }
    }
}
