import java.util.Stack;

public class Server extends Thread {

    String id;
    int time;
    Stack<Assigned_tasks> tasks = new Stack<>();

    Server(String id, int time) {
        this.id = id;
        this.time = time;
    }

    public void run() {
        while(!tasks.empty()){
            Assigned_tasks task = tasks.pop();
            System.out.println("Task id: " + task.id + " started!");
            try {
                Thread.sleep(task.time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task id: " + task.id + " completed!");
        }
    }

    void addTask(Assigned_tasks task) {
        this.tasks.push(task);
    }
}
