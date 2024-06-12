import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {
    private String id;
    private boolean isActive;
    private BlockingQueue<String> taskQueue;

    public Node(String id) {
        this.id = id;
        this.isActive = true;
        this.taskQueue = new LinkedBlockingQueue<>();
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public void addTask(String task) {
        taskQueue.add(task);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String task = taskQueue.take();
                if (isActive) {
                    System.out.println("Node " + id + " is executing task: " + task);
                    Thread.sleep(1000);  // Simula la ejecución de la tarea
                } else {
                    System.out.println("Node " + id + " is inactive and cannot execute task: " + task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
