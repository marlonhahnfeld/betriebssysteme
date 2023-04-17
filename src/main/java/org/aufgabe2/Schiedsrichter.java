package org.aufgabe2;

public class Schiedsrichter extends Thread{

    private Tisch tisch;
    private int counter = 0;

    public Schiedsrichter(Tisch tisch){
        this.tisch = tisch;
    }



    @Override
    public void run() {
        try {
            while (!isInterrupted()){
                tisch.auswerten();
            }

        }catch (InterruptedException e){
            System.err.println("Schiedsrichter wurde erfolgreich interrupted!");

        }
    }
}
