package all_classes;

import java.util.concurrent.*;

public class Server extends Thread {
    String name;
    int maxTime;
    BlockingQueue<Task> taskQueue;
    Task lastCompletedTask = null;

    Server(String name, int maxTime) {
        this.name = name;
        this.maxTime = maxTime;
        this.taskQueue = new LinkedBlockingQueue<>();
    }

    public void run() {
        while (true) {
            try {
                Task task = taskQueue.take();
                if (task.timeRequired > maxTime) {
                    System.out.println("Task " + task.id + " cannot be processed by " + name);
                    continue;
                }
                System.out.println("Server " + name + " processing task " + task.id);
                Thread.sleep(task.timeRequired);
                lastCompletedTask = task;
                System.out.println("Server " + name + " completed task " + task.id);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    void addTask(Task task) {
        taskQueue.add(task);
    }
}