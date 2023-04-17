package org.aufgabe2.conque;

import org.aufgabe2.Schiedsrichter;
import org.aufgabe2.SpielErgebnisse;
import org.aufgabe2.Spieler;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Main2 {
    private final static int SPIELZEIT = 1000;

    private ConQue conQue = new ConQue();


    private void starteSpiel() {
        Schiedsrichter schiedsrichter = new Schiedsrichter(conQue);
        LinkedList<Spieler> players = new LinkedList<>();

        for (int i = 0; i < 2; i++) {
            Spieler player = new Spieler(conQue);
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

        int[] ergebnis = conQue.getErgebnis();
        Stream.of(SpielErgebnisse.values()).forEach(gameResult ->
                System.out.println(gameResult + ": " + ergebnis[gameResult.ordinal()]
                ));
        System.out.println("gesamte Spiele: " + conQue.getCounter());

    }

    public static void main(String[] args) {
        new Main2().starteSpiel();
    }
}
