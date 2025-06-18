package org.example;

import java.util.*;

public class DijkstraAlgorithm {

    //Třída reprezentující hranu grafu
    static class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return destination + "(" + weight + ")";
        }
    }


    //Třída pro reprezentaci vrcholu
    static class Node implements Comparable<Node> {
        String vertex;
        int distance;

        Node(String vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    private Map<String, List<Edge>> graph;

    public DijkstraAlgorithm() {
        this.graph = new HashMap<>();
    }

    public void addEdge(String source, String destination, int weight) {
        graph.putIfAbsent(source, new ArrayList<>());
        graph.putIfAbsent(destination, new ArrayList<>());

        graph.get(source).add(new Edge(destination, weight));
    }

    public void loadGraphFromEdges(String[][] edges) {
        System.out.println("Načítám graf:");
        for (String[] edge : edges) {
            String source = edge[0];
            String destination = edge[1];
            int weight = Integer.parseInt(edge[2]);

            addEdge(source, destination, weight);
            System.out.println(source + " -> " + destination + " (váha: " + weight + ")");
        }
        System.out.println();
    }


    public Map<String, Integer> dijkstra(String startVertex) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (String vertex : graph.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, null);
        }
        distances.put(startVertex, 0);

        pq.offer(new Node(startVertex, 0));

        System.out.println("Spouštím Dijkstrův algoritmus z vrcholu: " + startVertex);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String currentVertex = current.vertex;

            if (current.distance > distances.get(currentVertex)) {
                continue;
            }

            if (visited.contains(currentVertex)) {
                continue;
            }

            visited.add(currentVertex);

            System.out.println("Zpracovávám vrchol: " + currentVertex +
                    " (vzdálenost: " + distances.get(currentVertex) + ")");

            if (graph.containsKey(currentVertex)) {
                for (Edge edge : graph.get(currentVertex)) {
                    String neighbor = edge.destination;
                    int weight = edge.weight;

                    if (visited.contains(neighbor)) {
                        continue;
                    }

                    int newDistance = distances.get(currentVertex) + weight;

                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        predecessors.put(neighbor, currentVertex);
                        pq.offer(new Node(neighbor, newDistance));

                        System.out.println("  Aktualizuji vzdálenost k " + neighbor +
                                ": " + newDistance + " (přes " + currentVertex + ")");
                    }
                }
            }
        }

        this.predecessors = predecessors;

        return distances;
    }

    private Map<String, String> predecessors;


    public String getShortestPath(String startVertex, String targetVertex, Map<String, Integer> distances) {
        if (distances.get(targetVertex) == Integer.MAX_VALUE) {
            return "nedosažitelný";
        }

        List<String> path = new ArrayList<>();
        String current = targetVertex;

        while (current != null) {
            path.add(current);
            current = predecessors.get(current);
        }

        Collections.reverse(path);

        return String.join("-->", path);
    }


    public void printAllDistances(String startVertex, Map<String, Integer> distances) {
        System.out.println("\nNejkratší vzdálenosti z vrcholu " + startVertex + ":");
        for (Map.Entry<String, Integer> entry : distances.entrySet()) {
            String vertex = entry.getKey();
            int distance = entry.getValue();

            if (distance == Integer.MAX_VALUE) {
                System.out.println(vertex + ": nedosažitelný");
            } else {
                System.out.println(vertex + ": " + distance);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();

        String[][] graph1 = {
                {"A", "B", "4"},
                {"A", "C", "2"},
                {"B", "A", "4"},   // zpětná hrana
                {"B", "C", "1"},
                {"B", "D", "5"},
                {"C", "A", "2"},   // zpětná hrana
                {"C", "B", "1"},   // zpětná hrana
                {"C", "D", "8"},
                {"C", "E", "10"},
                {"D", "B", "5"},   // zpětná hrana
                {"D", "C", "8"},   // zpětná hrana
                {"D", "E", "2"},
                {"E", "C", "10"},  // zpětná hrana
                {"E", "D", "2"}    // zpětná hrana
        };

        String[][] graph2 = {
                {"X", "Y", "3"},
                {"Y", "X", "3"},

                {"X", "Z", "7"},
                {"Z", "X", "7"},

                {"Y", "Z", "1"},
                {"Z", "Y", "1"},

                {"Y", "W", "5"},
                {"W", "Y", "5"},

                {"Z", "W", "2"},
                {"W", "Z", "2"},

                {"W", "V", "6"},
                {"V", "W", "6"},

                {"V", "Z", "3"},
                {"Z", "V", "3"}
        };

        String[][] graph3 = {
                {"1", "2", "2"},
                {"2", "1", "2"},

                {"1", "3", "4"},
                {"3", "1", "4"},

                {"2", "4", "7"},
                {"4", "2", "7"},

                {"2", "5", "1"},
                {"5", "2", "1"},

                {"3", "6", "3"},
                {"6", "3", "3"},

                {"5", "6", "1"},
                {"6", "5", "1"},

                {"6", "4", "2"},
                {"4", "6", "2"},

                {"4", "1", "5"},
                {"1", "4", "5"}
        };

        System.out.println("=== DIJKSTRŮV ALGORITMUS ===\n");

        dijkstra.loadGraphFromEdges(graph3); //Změnami mezi graph1, graph2 a graph3 pracujeme s různými předpřipravenými grafy

        System.out.print("Zadejte výchozí vrchol: ");
        String startVertex = scanner.nextLine().trim();

        System.out.println();
        Map<String, Integer> distances = dijkstra.dijkstra(startVertex);

        dijkstra.printAllDistances(startVertex, distances);

        while (true) {
            System.out.print("Zadejte cílový vrchol (nebo 'konec' pro ukončení): ");
            String targetVertex = scanner.nextLine().trim();

            if (targetVertex.equalsIgnoreCase("konec")) {
                break;
            }

            if (!dijkstra.graph.containsKey(targetVertex)) {
                System.out.println("Vrchol '" + targetVertex + "' neexistuje v grafu!");
                continue;
            }

            String path = dijkstra.getShortestPath(startVertex, targetVertex, distances);
            int distance = distances.get(targetVertex);

            System.out.print("Nejkratší cesta z " + startVertex + " do " + targetVertex + ": ");
            if (path.equals("nedosažitelný")) {
                System.out.println("nedosažitelný");
            } else {
                System.out.println(path + " (vzdálenost: " + distance + ")");
            }
            System.out.println();
        }

        scanner.close();
    }
}