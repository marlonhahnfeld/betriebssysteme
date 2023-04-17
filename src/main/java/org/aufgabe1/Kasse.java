package org.aufgabe1;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Kasse {
    int warteschlangeLaenge;

    private int counter;
    private ReentrantLock bezahlenMutex = new ReentrantLock();

    String name;

    public Kasse(String name, int counter) {
        this.name = name;
        this.counter = 0;
    }

    public int getCounter() {
        return this.counter;
    }

    public void bezahlen(String student) throws InterruptedException {
        //Muss gelockt werden, da bei dem Bezahlvorgang immer nur eine Person bezahlen kann
        bezahlenMutex.lock();
        try {
            this.counter++;
            System.out.println(this.name + " hat noch " + this.warteschlangeLaenge + " Kunden in der Warteschlage");
            System.out.println(student + " bezahlt an der Kasse " + this.name);
            Random random = new Random();

            Thread.sleep(random.nextInt(300));//kann Random sein


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            bezahlenMutex.unlock();
        }
    }
}
