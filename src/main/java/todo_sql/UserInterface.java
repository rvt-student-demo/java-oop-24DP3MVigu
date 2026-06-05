package todo_sql;
import java.util.Scanner;
public class UserInterface {
    private TodoList saraksts;
    private Scanner skeneris;
    public UserInterface(TodoList saraksts, Scanner skeneris) {
        this.saraksts = saraksts;
        this.skeneris = skeneris;
    }
    public void start() {
        System.out.println("Available commands: add, list, category, remove, stop");
        while (true) {
            System.out.print("Command: ");
            String komanda = skeneris.nextLine();
            if (komanda.equals("stop")) {
                break;
            }
            if (komanda.equals("add")) {
                System.out.print("To add: ");
                String darbs = skeneris.nextLine();
                System.out.print("Category: ");
                String category = skeneris.nextLine();
                saraksts.add(darbs, category);
            } else if (komanda.equals("list")) {
                saraksts.print();
            } else if (komanda.equals("category")) {
                System.out.print("Which category? ");
                String category = skeneris.nextLine();
                saraksts.listByCategory(category);
            } else if (komanda.equals("remove")) {
                System.out.print("Which one is removed? ");
                int id = Integer.valueOf(skeneris.nextLine());
                saraksts.remove(id);
            }
        }
    }

    public static void main(String[] args) {
        TodoList sarskts = new TodoList();
        Scanner skeneris = new Scanner(System.in);

        UserInterface ui = new UserInterface(sarskts, skeneris);
        ui.start();
    }
}