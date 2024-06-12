public class Principal {
    public static void main(String[] args) {
        Cluster cluster = new Cluster();

        Node node1 = new Node("Node1");
        Node node2 = new Node("Node2");
        Node node3 = new Node("Node3");

        cluster.addNode(node1);
        cluster.addNode(node2);
        cluster.addNode(node3);

        cluster.printClusterStatus();

        cluster.startTaskGeneration();

        try {
            // Simulación de operaciones en el clúster
            Thread.sleep(5000);
            cluster.deactivateNode("Node2");
            cluster.printClusterStatus();

            Thread.sleep(5000);
            cluster.activateNode("Node2");
            cluster.printClusterStatus();

            Thread.sleep(5000);
            cluster.removeNode("Node3");
            cluster.printClusterStatus();

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cluster.stopTaskGeneration();
        }
    }
}
