import java.util.*;

public class ShortestPathUsingLevita {
    static class Edge {
        int source, destination, weight; // начальной вершине, конечной вершине,весе ребра

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    static int vertices;
    static ArrayList<ArrayList<Edge>> graph;

    public static void addEdge(int source, int destination, int weight) {
        graph.get(source).add(new Edge(source, destination, weight));
    }

    public static void levitaAlgorithm(int start) {
        int[] distance = new int[vertices];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[start] = 0;

        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[vertices];
        int[] count = new int[vertices];
        Arrays.fill(count, 0);
        queue.offer(start);
        inQueue[start] = true;

        int iterations = 0;

        long startTime = System.nanoTime();

        while (!queue.isEmpty()) {
            int current = queue.poll();
            inQueue[current] = false;

            for (Edge edge : graph.get(current)) {
                int newDistance = distance[current] + edge.weight;
                if (newDistance < distance[edge.destination]) {
                    distance[edge.destination] = newDistance;
                    if (!inQueue[edge.destination]) {
                        queue.offer(edge.destination);
                        inQueue[edge.destination] = true;
                        count[edge.destination]++;
                        if (count[edge.destination] > vertices) {
                            System.out.println("Graph contains negative cycle");
                            long endTime = System.nanoTime();
                            System.out.println("Elapsed Time: " + (endTime - startTime)/1000000 + " милисекунды");
                            System.out.println("Number of iterations: " + iterations);
                            return;
                        }
                    }
                }
                iterations++;
            }
        }

        long endTime = System.nanoTime();
        System.out.println("Elapsed Time: " + (endTime - startTime)/1000000 + " милисекунды");
        System.out.println("количество итераций: " + iterations);

        System.out.println("Кратчайшие расстояния от вершины " + start + ":");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Вершина " + i + ": " + distance[i]);
        }
    }

    public static void main(String[] args) {
        vertices = 5;
        graph = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            graph.add(new ArrayList<>());
        }

        addEdge(0, 1, 6);
        addEdge(0, 2, 7);
        addEdge(1, 2, 8);
        addEdge(1, 3, -4);
        addEdge(1, 4, 5);
        addEdge(2, 3, 9);
        addEdge(2, 4, -3);
        addEdge(3, 1, 7);
        addEdge(4, 0, 2);
        addEdge(4, 3, 7);

        levitaAlgorithm(0);
    }
}
