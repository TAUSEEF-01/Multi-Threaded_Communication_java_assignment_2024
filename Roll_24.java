import java.util.*;

public class Roll_24 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        sc.nextLine();

        ArrayList<Server> allServers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] info = sc.nextLine().split(" ");
            String serverID = info[0];
            int serverRunTime = Integer.parseInt(info[1]);

            Server server = new Server(serverID, serverRunTime);
            allServers.add(server);
        }

        int q = sc.nextInt();

        for (int i = 0; i < q; i++) {
            int id = sc.nextInt();
            int time = sc.nextInt();

            Assigned_tasks task = new Assigned_tasks(id, time);

            int flag = 1;
            for (Server server : allServers) {
                if (server.time >= time) {
                    System.out.println("Task id: " + id + " is assigned to this server: " + server.id);
                    server.add_Task(task);
                    flag = 0;
                    break;
                }
            }

            if (flag == 1) {
                System.out.println("Task id: " + id + " couldn't assign to any server!");
            }
        }

        Thread serverStatus = new Thread(() -> {
            try {
                while (true) {
                    boolean flag = true;

                    for (Server server : allServers) {

                        System.out.print("Server " + server.id + ": ");
                        System.out.print("Last completed task = [ ");

                        if (server.prevTask != null) {
                            System.out.print("Task id: " + server.prevTask.id + " ]");
                        } else {
                            System.out.print("None ]");
                        }

                        System.out.print(", Ongoing tasks = [");
                        server.print_Ongoing_Tasks();
                        System.out.println("]");

                        if (!server.tasks.isEmpty())
                            flag = false;
                    }

                    if (flag) {
                        break;
                    }
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        serverStatus.start();

        for (Server server : allServers) {
            server.start();
        }

        for (Server server : allServers) {
            try {
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sc.close();
    }
}

class Server extends Thread {

    String id;
    int time;
    Stack<Assigned_tasks> tasks = new Stack<>();
    Assigned_tasks prevTask = null;

    Server(String id, int time) {
        this.id = id;
        this.time = time;
    }

    public void run() {
        while (true) {
            try {
                Assigned_tasks task = tasks.pop();
                System.out.println("Task id: " + task.id + " started!");
                try {
                    Thread.sleep(task.time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task id: " + task.id + " completed!");
                prevTask = task;
            } catch (Exception e) {
                break;
            }
        }
    }

    void add_Task(Assigned_tasks task) {
        this.tasks.push(task);
    }

    void print_Ongoing_Tasks() {
        System.out.print(" task id: ");
        if (tasks.empty()) {
            System.out.print("None ");
            return;
        }
        for (Assigned_tasks task : tasks) {
            System.out.print(task.id + " ");
        }
    }
}

class Assigned_tasks {

    int id, time;

    Assigned_tasks(int ID, int time) {
        this.id = ID;
        this.time = time;
    }
}
