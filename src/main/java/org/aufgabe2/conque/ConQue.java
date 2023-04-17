package org.aufgabe2.conque;

import org.aufgabe2.SchereSteinPapier;
import org.aufgabe2.SpielErgebnisse;
import org.aufgabe2.Tisch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConQue implements Tisch {

        int counter = 0;
    private int[] ergebnisse = new int[3];

    private SchereSteinPapier spieler1;

    private SchereSteinPapier spieler2;

    private SpielErgebnisse[][] spielErgebnis = new SpielErgebnisse[3][3];

    private final Lock tischLock = new ReentrantLock();
    private final Condition spielerFertig = tischLock.newCondition();
    private final Condition schiedsrichterFertig = tischLock.newCondition();

    public ConQue() {
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
    public void spielen(SchereSteinPapier objekt) throws InterruptedException {
        tischLock.lockInterruptibly();
        try {
            if (Thread.currentThread().getName().contains("0")) {
                while (spieler1 != null) {
                    schiedsrichterFertig.await();
                }
                spieler1 = objekt;
            } else if (Thread.currentThread().getName().contains("1")) {
                while (spieler2 != null) {
                    schiedsrichterFertig.await();
                }
                spieler2 = objekt;
            } else {
                System.err.println("Spiel wurde abgebrochen");
            }

            spielerFertig.signal();

        } finally {
            tischLock.unlock();
        }
    }

    @Override
    public void auswerten() throws InterruptedException {
        tischLock.lockInterruptibly();
        try {
            while (spieler1 == null || spieler2 == null) {
                spielerFertig.await();
            }

            System.out.println("Spieler1: " + spieler1 + " Spieler2: " + spieler2 + " Ergebnis "
                    + (spielErgebnis[spieler1.ordinal()][spieler2.ordinal()]).name());

            ergebnisse[(spielErgebnis[spieler1.ordinal()][spieler2.ordinal()]).ordinal()]++;
            spieler2 =null;
            spieler1 = null;
            counter++;
            schiedsrichterFertig.signalAll();
        } finally {
            tischLock.unlock();
        }
    }

    @Override
    public int[] getErgebnis() {
        return ergebnisse;
    }
}
