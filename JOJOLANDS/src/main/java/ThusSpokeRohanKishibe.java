import java.util.*;

public class ThusSpokeRohanKishibe {
    static ArrayList<String> locationPoint;

    public ThusSpokeRohanKishibe() {
        locationPoint = JOJOLands.getMap().getLocationPoint();
        System.out.print("Enter the location: ");
        Scanner sc = new Scanner(System.in);
        String[] locationToVisit = sc.nextLine().split(",");

        // Remove any space while input
        for (int i = 0; i < locationToVisit.length; i++) {
            locationToVisit[i] = locationToVisit[i].trim();
        }

        List<Integer> nodesToVisit = new ArrayList<>();
        for (int i = 0; i < locationToVisit.length; i++) {
            nodesToVisit.add(locationPoint.indexOf(locationToVisit[i]));
        }

        int startNode = locationPoint.indexOf("Morioh Grand Hotel");
        Map map = JOJOLands.getMap();
        int[][] adjacentMatrix = map.getAdjacentMatrix();
        try {
            System.out.println("======================================================================\nShortest Path:");
            findShortestPath(adjacentMatrix, startNode, nodesToVisit);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please enter existing location properly!");
        }

    }

    public static class Path implements Comparable<Path> {
        List<Integer> path;
        int distance;

        public Path(List<Integer> path, int distance) {
            this.path = path;
            this.distance = distance;
        }

        @Override
        public int compareTo(Path other) {
            return Integer.compare(this.distance, other.distance);
        }

    }

    public static void findShortestPath(int[][] graph, int source, List<Integer> nodesToVisit) {
        PriorityQueue<Path> queue = new PriorityQueue<>();
        List<Integer> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[graph.length];

        dfs(graph, source, nodesToVisit, visited, currentPath, queue);

        if (!queue.isEmpty()) {
            Path path = queue.poll();
            int distance = calculateDistance(graph, path.path);

            for (int i = 0; i < path.path.size(); i++) {
                System.out.print(locationPoint.get(path.path.get(i)) + " ");
                if (i != path.path.size() - 1)
                    System.out.print("\u2192" + " ");
            }

            System.out.println("(" + distance + "km)");
        }
    }

    private static void dfs(int[][] graph, int current, List<Integer> nodesToVisit, boolean[] visited,
                            List<Integer> currentPath, PriorityQueue<Path> queue) {
        visited[current] = true;
        currentPath.add(current);

        if (nodesToVisit.contains(current) && visitedAllNodes(nodesToVisit, visited)) {
            int distance = calculateDistance(graph, currentPath);
            queue.offer(new Path(new ArrayList<>(currentPath), distance));
        } else {
            for (int neighbor = 0; neighbor < graph.length; neighbor++) {
                if (!visited[neighbor] && graph[current][neighbor] != 0) {
                    dfs(graph, neighbor, nodesToVisit, visited, currentPath, queue);
                }
            }
        }

        visited[current] = false;
        currentPath.remove(currentPath.size() - 1);
    }

    private static boolean visitedAllNodes(List<Integer> nodesToVisit, boolean[] visited) {
        for (int node : nodesToVisit) {
            if (!visited[node]) {
                return false;
            }
        }
        return true;
    }

    private static int calculateDistance(int[][] graph, List<Integer> path) {
        int distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int node = path.get(i);
            int nextNode = path.get(i + 1);
            distance += graph[node][nextNode];
        }
        return distance;
    }
}
