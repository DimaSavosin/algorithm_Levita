import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ShortestPathUsingLevita {
    static class Edge {
        int source, destination, weight; // начальная вершина, конечная вершина,вес ребра

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
                            System.out.println("Затраченное время: " + (endTime - startTime)/1000000 + " миллисекунды");
                            System.out.println("Number of iterations: " + iterations);
                            return;
                        }
                    }
                }
                iterations++;
            }
        }

        long endTime = System.nanoTime();
        System.out.println("Затраченное время: " + (endTime - startTime)/1000000 + " миллисекунды");
        System.out.println("количество итераций: " + iterations);

        System.out.println("Кратчайшие расстояния от вершины " + start + ":");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Вершина " + i + ": " + distance[i]);
        }
    }

    public static void generateGraphDataToFile(String filename,  int minVertices, int maxVertices) {
        try {
            FileWriter writer = new FileWriter(filename);
            Random random = new Random();
            int numGraphs = random.nextInt(51) + 50; // Генерируем случайное число от 50 до 100
            System.out.println("Создано " + numGraphs + " графов.");
            for (int g = 0; g < numGraphs; g++) {
                vertices = random.nextInt(maxVertices - minVertices + 1) + minVertices;
                int edges = random.nextInt(vertices * (vertices - 1) / 2 + 1); // Случайное количество рёбер

                // Записываем количество вершин и рёбер в файл
                writer.write(vertices + " " + edges + "\n");

                // Записываем каждое ребро (случайные вершины и вес)
                for (int i = 0; i < edges; i++) {
                    int source = random.nextInt(vertices);
                    int destination = random.nextInt(vertices);
                    int weight = random.nextInt(100) + 1; // Принимаем случайные веса от 1 до 100
                    writer.write(source + " " + destination + " " + weight + "\n");
                }
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Ошибка при создании файла: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Путь к файлу с данными графа
        String filename = "graphs_data.txt";

        // Генерация данных графа и запись в файл
        generateGraphDataToFile(filename,  100, 10000);

        // Чтение графа из файла
        try {
            Scanner scanner = new Scanner(new File(filename));

            vertices = scanner.nextInt(); // Считываем количество вершин
            int edges = scanner.nextInt(); // Считываем количество рёбер
            graph = new ArrayList<>(vertices);

            // Инициализация списка смежности
            for (int i = 0; i < vertices; i++) {
                graph.add(new ArrayList<>());
            }

            // Считываем рёбра из файла
            for (int i = 0; i < edges; i++) {
                int source = scanner.nextInt();
                int destination = scanner.nextInt();
                int weight = scanner.nextInt();
                addEdge(source, destination, weight);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e.getMessage());
            return;
        }

        // Выполнение алгоритма Левита
        levitaAlgorithm(0);
    }
}
