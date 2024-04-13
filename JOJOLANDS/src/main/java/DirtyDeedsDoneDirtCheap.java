
import java.util.*;

public class DirtyDeedsDoneDirtCheap {
    static ArrayList<String> locationPoint;

    public DirtyDeedsDoneDirtCheap() {
        locationPoint = JOJOLands.getMap().getLocationPoint();
        Scanner sc = new Scanner(System.in);
        System.out.print("Source: ");
        String source = sc.nextLine();

        System.out.print("Destination: ");
        String destination = sc.nextLine();

        int s = locationPoint.indexOf(source);
        int d = locationPoint.indexOf(destination);

        Map map = JOJOLands.getMap();
        int[][] adjacentMatrix = map.getAdjacentMatrix();
        System.out.println("======================================================================");

        try {
            findShortestPath(adjacentMatrix, s, d);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please enter existing location properly!");
        }


    }

    private static void findShortestPath(int[][] adjacentMatrix, int source, int destination) {
        PriorityQueue<List<Integer>> pq = new PriorityQueue<>(Comparator.comparingInt(
                        (List<Integer> path) -> getTotalDistance(adjacentMatrix, path))
                .thenComparingInt(List::size));

        List<Integer> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[adjacentMatrix.length];

        dfs(adjacentMatrix, source, destination, visited, currentPath, pq);

        System.out.println("Top Three Shortest Paths: ");
        int count = 0;
        while (!pq.isEmpty() && count < 3) {
            System.out.print(count + 1 + ". ");
            List<Integer> path = pq.poll();
            int distance = getTotalDistance(adjacentMatrix, path);
            for (int i = 0; i < path.size(); i++) {
                System.out.print(locationPoint.get(path.get(i)) + " ");
                if (i != path.size() - 1)
                    System.out.print("\u2192" + " ");
            }

            System.out.println("(" + distance + " km)");
            count++;
        }

    }

    private static void dfs(int[][] graph, int current, int destination, boolean[] visited,
                            List<Integer> currentPath, PriorityQueue<List<Integer>> pq) {
        visited[current] = true;
        currentPath.add(current);

        if (current == destination) {
            pq.offer(new ArrayList<>(currentPath));
        } else {
            for (int neighbor = 0; neighbor < graph.length; neighbor++) {
                if (!visited[neighbor] && graph[current][neighbor] != 0) {
                    dfs(graph, neighbor, destination, visited, currentPath, pq);
                }
            }
        }
        visited[current] = false;
        currentPath.remove(currentPath.size() - 1);
    }

    private static int getTotalDistance(int[][] graph, List<Integer> path) {
        int distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int node = path.get(i);
            int nextNode = path.get(i + 1);
            distance += graph[node][nextNode];
        }
        return distance;
    }
}
