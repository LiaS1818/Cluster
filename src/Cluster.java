import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cluster {
    private List<Node> nodes;
    private BlockingQueue<String> taskQueue;
    private ScheduledExecutorService taskGenerator;

    public Cluster() {
        this.nodes = new ArrayList<>();
        this.taskQueue = new LinkedBlockingQueue<>();
        this.taskGenerator = Executors.newScheduledThreadPool(1);
    }

    public void startTaskGeneration() {
        taskGenerator.scheduleAtFixedRate(() -> {
            String task = "Task-" + System.currentTimeMillis();
            taskQueue.add(task);
            System.out.println("Generated " + task);
            distributeTasks();
        }, 0, 2, TimeUnit.SECONDS); // Genera una tarea cada 2 segundos
    }

    public void stopTaskGeneration() {
        taskGenerator.shutdown();
    }

    public void addNode(Node node) {
        nodes.add(node);
        new Thread(node).start();
        System.out.println("Node " + node.getId() + " added to the cluster.");
    }

    public void removeNode(String nodeId) {
        nodes.removeIf(node -> node.getId().equals(nodeId));
        System.out.println("Node " + nodeId + " removed from the cluster.");
    }

    private void distributeTasks() {
        for (Node node : nodes) {
            if (node.isActive()) {
                Optional<String> task = Optional.ofNullable(taskQueue.poll());
                task.ifPresent(node::addTask);
            }
        }
    }

    public void deactivateNode(String nodeId) {
        nodes.stream().filter(node -> node.getId().equals(nodeId)).forEach(Node::deactivate);
        System.out.println("Node " + nodeId + " deactivated.");
    }

    public void activateNode(String nodeId) {
        nodes.stream().filter(node -> node.getId().equals(nodeId)).forEach(Node::activate);
        System.out.println("Node " + nodeId + " activated.");
    }

    public void printClusterStatus() {
        System.out.println("Cluster Status:");
        for (Node node : nodes) {
            System.out.println("Node ID: " + node.getId() + ", Active: " + node.isActive());
        }
    }
}
