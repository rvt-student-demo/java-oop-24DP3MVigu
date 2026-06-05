package todo_sql;

public class TodoList {
    private TodoDB db;

    public TodoList() {
        this.db = new TodoDB();
    }

    public void add(String task, String category) {
        db.add(task, category);
    }

    public void print() {
        db.findAll();
    }

    public void remove(int number) {
        db.removeById(number);
    }

    public void listByCategory(String category) {
        db.findByCategory(category);
    }
}

