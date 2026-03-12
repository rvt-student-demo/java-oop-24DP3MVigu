package rvt;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
public class TodoList {
    private ArrayList<String> darbi;
    public TodoList() {

        this.darbi = new ArrayList<>();
    }
    public void add(String task) {
        this.darbi.add(task);
    }
    public void print() {
        for (int i = 0; i < darbi.size(); i++) {
            System.out.println((i + 1) + ": " + darbi.get(i));
        }
    }
    public void remove(int number) {
        this.darbi.remove(number - 1);
    }
    
    public void loadFromFile(String filename) {
        try (Scanner skeners = new Scanner(new File(filename))) {
            if (skeners.hasNextLine()) {
                skeners.nextLine();
            }
            while (skeners.hasNextLine()) {
                String line = skeners.nextLine();
                String[] parts = line.split(",");
                String task = parts[1];
                darbi.add(task);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

