package org.aufgabe2.monitor;

import org.aufgabe2.SchereSteinPapier;
import org.aufgabe2.SpielErgebnisse;
import org.aufgabe2.Tisch;

public class TischMonitor implements Tisch {

    private int[] ergebnisse = new int[3];

    private SchereSteinPapier spieler1;

    int counter = 0;

    private SchereSteinPapier spieler2;

    private SpielErgebnisse[][] spielErgebnis = new SpielErgebnisse[3][3];

   public TischMonitor() {
        spielErgebnis[SchereSteinPapier.SCHERE.ordinal()][SchereSteinPapier.SCHERE.ordinal()] = SpielErgebnisse.UNENTSCHIEDEN;
        spielErgebnis[SchereSteinPapier.SCHERE.ordinal()][SchereSteinPapier.STEIN.ordinal()] = SpielErgebnisse.SPIELER2;
        spielErgebnis[SchereSteinPapier.SCHERE.ordinal()][SchereSteinPapier.PAPIER.ordinal()] = SpielErgebnisse.SPIELER1;

        spielErgebnis[SchereSteinPapier.STEIN.ordinal()][SchereSteinPapier.SCHERE.ordinal()] = SpielErgebnisse.SPIELER1;
        spielErgebnis[SchereSteinPapier.STEIN.ordinal()][SchereSteinPapier.STEIN.ordinal()] = SpielErgebnisse.UNENTSCHIEDEN;
        spielErgebnis[SchereSteinPapier.STEIN.ordinal()][SchereSteinPapier.PAPIER.ordinal()] = SpielErgebnisse.SPIELER2;

        spielErgebnis[SchereSteinPapier.PAPIER.ordinal()][SchereSteinPapier.SCHERE.ordinal()] = SpielErgebnisse.SPIELER2;
        spielErgebnis[SchereSteinPapier.PAPIER.ordinal()][SchereSteinPapier.STEIN.ordinal()] = SpielErgebnisse.SPIELER1;
        spielErgebnis[SchereSteinPapier.PAPIER.ordinal()][SchereSteinPapier.PAPIER.ordinal()] = SpielErgebnisse.UNENTSCHIEDEN;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public synchronized void spielen(SchereSteinPapier objekt) throws InterruptedException {
        if (Thread.currentThread().getName().contains("0")) {
            while (spieler1 != null) {
                this.wait();
            }
            spieler1 = objekt;
            notifyAll();
        } else if (Thread.currentThread().getName().contains("1")) {
            while (spieler2 != null) {
                this.wait();
            }
            spieler2 = objekt;
            notifyAll();
        } else {
            System.err.println("Spiel wurde abgebrochen");
        }

    }

    @Override
    public synchronized void auswerten() throws InterruptedException {
    while (spieler1 == null || spieler2 ==null){
        this.wait();
    }

    System.out.println("Spieler1: "+ spieler1 + " Spieler2: " + spieler2 + " Ergebnis " + (spielErgebnis[spieler1.ordinal()][spieler2.ordinal()]).name()) ;
    ergebnisse[(spielErgebnis[spieler1.ordinal()][spieler2.ordinal()]).ordinal()]++;

    spieler1 = null;
    spieler2 = null;

    counter++;
    notifyAll();
    }

    @Override
    public int[] getErgebnis() {
        return ergebnisse;
    }
}
