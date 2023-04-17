package org.aufgabe1;

import java.util.concurrent.locks.ReentrantLock;

public class Mensa {

    //
    final int anzahlKunden = 10;
    final int anzahlKassen = 3;
    //

    ReentrantLock warteschlangenLock = new ReentrantLock();


    public void startEinkauf(int anzahlKunden, int anzahlKassen){
        //Kassen anlegen
        Kasse[] kassen = new Kasse[anzahlKassen];

        for (int i = 0; i < kassen.length; i++) {
            kassen[i] = new Kasse("Kasse " + (i +1),0);
        }
        System.out.println("------------ START ------------");

        Student[] students = new Student[anzahlKunden];

        for (int i = 0; i < students.length; i++) {
            Student s = new Student("Kunde " + i, kassen, this);
            students[i] = s;
            s.start();
        }

        try {
            Thread.sleep(4000);
        }catch (InterruptedException e){

        }
        for (Student student : students) {
            student.interrupt();
        }

        for (Student student : students) {
            try {
                student.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < kassen.length; i++) {
            System.out.println(kassen[i].getCounter());
        }
        System.out.println("------------------------ ENDE ---------------------");

    }

    public static void main(String[] args) {
        Mensa mensa = new Mensa();
        mensa.startEinkauf(mensa.anzahlKunden, mensa.anzahlKassen);
    }
}
