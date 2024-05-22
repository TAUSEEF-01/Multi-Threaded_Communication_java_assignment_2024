package all_classes;

import java.util.*;
import java.util.concurrent.*;


public class Roll_x {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int K = sc.nextInt();
        sc.nextLine();

        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            String[] serverInfo = sc.nextLine().split(" ");
            String name = serverInfo[0];
            int maxTime = Integer.parseInt(serverInfo[1]);
            servers.add(new Server(name, maxTime));
        }

        int X = sc.nextInt();
        sc.nextLine();

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < X; i++) {
            String[] taskInfo = sc.nextLine().split(" ");
            String id = taskInfo[0];
            int timeRequired = Integer.parseInt(taskInfo[1]);
            tasks.add(new Task(id, timeRequired));
        }

        // Start all server threads
        for (Server server : servers) {
            server.start();
        }

        // Distribute tasks to servers
        for (Task task : tasks) {
            boolean taskAssigned = false;
            for (Server server : servers) {
                if (server.maxTime >= task.timeRequired) {
                    server.addTask(task);
                    taskAssigned = true;
                    break;
                }
            }
            if (!taskAssigned) {
                System.out.println("Task " + task.id + " cannot be assigned to any server");
            }
        }

        // Print status periodically
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (Server server : servers) {
                    System.out.print("Server " + server.name + ": ");
                    System.out.print("Last Completed Task = ");
                    System.out.print(server.lastCompletedTask != null ? server.lastCompletedTask.id : "None");
                    System.out.print(", Current Tasks = ");
                    System.out.println(server.taskQueue);
                }
            }
        }, 0, 5000); // Print every 5 seconds

        // Allow the main thread to wait until all tasks are completed
        for (Server server : servers) {
            try {
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sc.close();
    }
}
