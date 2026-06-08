package rvt;

import java.util.ArrayList;

public class Main {

    public static void printPersons(ArrayList<Person> persons) {
        for (Person p : persons) {
            System.out.println(p);
        }
    }

    public static void main(String[] args) {
        ArrayList<Person> list = new ArrayList<>();
        list.add(new Teacher("Ada Lovelace", "24 Maddox St. London W1S 2QN", 1200));
        list.add(new Student("Ollie", "6381 Hollywood Blvd. Los Angeles 90028"));
        printPersons(list);
    }
}
