import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class SuperFly {
    static ArrayList<String> locationPoint;
    private static int[][] graph = JOJOLands.getMap().getAdjacentMatrix();
    private static int numVertices = graph.length;

    public SuperFly() {
        locationPoint = JOJOLands.getMap().getLocationPoint();
    }

    public void redHotChiliPepper() {
        List<Edge> edges = getSortedEdgesAsc();
        List<Edge> mst = kruskal(edges);
        printMST(mst);
    }

    public void theHand() {
        int startNode = 11; // Specify the start node here
        List<Edge> edges = getSortedEdgesDesc();
        List<Edge> longestTree = kruskal(edges);
        printRemainingEdges(edges, longestTree);
    }

    private static List<Edge> getSortedEdgesAsc() {
        List<Edge> edges = new ArrayList<>();
        for (int src = 0; src < numVertices; src++) {
            for (int dest = src + 1; dest < numVertices; dest++) {
                int weight = graph[src][dest];
                if (weight != 0) {
                    edges.add(new Edge(src, dest, weight));
                }
            }
        }
        Collections.sort(edges);
        return edges;
    }

    private static List<Edge> getSortedEdgesDesc() {
        List<Edge> edges = new ArrayList<>();
        for (int src = 0; src < numVertices; src++) {
            for (int dest = src + 1; dest < numVertices; dest++) {
                int weight = graph[src][dest];
                if (weight != 0) {
                    edges.add(new Edge(src, dest, weight));
                }
            }
        }
        Collections.sort(edges, Collections.reverseOrder());
        return edges;
    }

    private static List<Edge> kruskal(List<Edge> edges) {
        List<Edge> mst = new ArrayList<>();
        int[] parent = new int[numVertices];
        int[] rank = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

        for (Edge edge : edges) {
            int srcParent = find(parent, edge.src);
            int destParent = find(parent, edge.dest);

            if (srcParent != destParent) {
                mst.add(edge);
                union(parent, rank, srcParent, destParent);
            }
        }

        return mst;
    }

    private static int find(int[] parent, int vertex) {
        if (parent[vertex] != vertex) {
            parent[vertex] = find(parent, parent[vertex]);
        }
        return parent[vertex];
    }

    private static void union(int[] parent, int[] rank, int x, int y) {
        int xRoot = find(parent, x);
        int yRoot = find(parent, y);

        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot;
        } else if (rank[xRoot] > rank[yRoot]) {
            parent[yRoot] = xRoot;
        } else {
            parent[yRoot] = xRoot;
            rank[xRoot]++;
        }
    }

    private static void printMST(List<Edge> mst) {
        System.out.println("======================================================================");
        System.out.println("Necessary Power Cables to be Upgraded: ");
        int totalWeight = 0;

        // Sort the edges based on smaller node and then larger node
        mst.sort((a, b) -> {
            if (a.src != b.src) {
                return Integer.compare(a.src, b.src);
            } else {
                return Integer.compare(a.dest, b.dest);
            }
        });

        int index = 1;
        for (Edge edge : mst) {
            int src = edge.src;
            int dest = edge.dest;
            int weight = edge.weight;
            System.out.println(index + ". " + locationPoint.get(src) + " --- " + locationPoint.get(dest) + " (" + weight + " km)");
            totalWeight += weight;
            index++;
        }
        System.out.println("\nTotal Length: " + totalWeight + " km");
    }

    private static void printRemainingEdges(List<Edge> edges, List<Edge> longestTree) {
        System.out.println("======================================================================");
        System.out.println("Unnecessary Water Connection to be removed:");
        List<Edge> remainingEdges = new ArrayList<>();
        int totalLength = 0;
        for (Edge edge : edges) {
            if (!longestTree.contains(edge)) {
                remainingEdges.add(edge);
                totalLength += edge.weight;
            }
        }
        Collections.sort(remainingEdges, new Comparator<Edge>() {
            @Override
            public int compare(Edge edge1, Edge edge2) {
                if (edge1.src != edge2.src) {
                    return Integer.compare(edge1.src, edge2.src);
                } else {
                    return Integer.compare(edge1.dest, edge2.dest);
                }
            }
        });

        int index = 1;
        for (Edge edge : remainingEdges) {
            System.out.println(index + ". " + locationPoint.get(Math.min(edge.src, edge.dest)) + " - " + locationPoint.get(Math.max(edge.src, edge.dest)) + " (" + edge.weight + " km)");
            index++;
        }

        System.out.println("\nTotal Length: " + totalLength + " km");
    }

    private static class Edge implements Comparable<Edge> {
        int src, dest, weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }
}


