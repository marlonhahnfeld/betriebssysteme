package org.aufgabe2;

public interface Tisch {
    void spielen(SchereSteinPapier objekt) throws InterruptedException;

    void auswerten() throws InterruptedException;

    int[] getErgebnis();
}
