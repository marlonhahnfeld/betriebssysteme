package org.aufgabe2.monitor;

import org.aufgabe2.Schiedsrichter;
import org.aufgabe2.SpielErgebnisse;
import org.aufgabe2.Spieler;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Main {

    private final static int SPIELZEIT = 4000;

    private TischMonitor tischMonitor = new TischMonitor();



    private void starteSpiel() {
        Schiedsrichter schiedsrichter = new Schiedsrichter(tischMonitor);
        LinkedList<Spieler> players = new LinkedList<>();

        for (int i = 0; i < 2; i++) {
            Spieler player = new Spieler(tischMonitor);
            player.setName("Spieler " + i);
            players.add(player);
            player.start();
        }


        schiedsrichter.start();

        try {
            Thread.sleep(SPIELZEIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       schiedsrichter.interrupt();

        for (Spieler current : players) {
            current.interrupt();
            if (current.isAlive()) {
                try {
                    current.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        try {
            schiedsrichter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int[] ergebnis = tischMonitor.getErgebnis();

        Stream.of(SpielErgebnisse.values())
                .forEach(spielErg -> System.out.println(spielErg + ": " + ergebnis[spielErg.ordinal()]));
        System.out.println("gesamte Spiele: " + tischMonitor.getCounter());
    }

    public static void main(String[] args) {
        new Main().starteSpiel();

    }


}
