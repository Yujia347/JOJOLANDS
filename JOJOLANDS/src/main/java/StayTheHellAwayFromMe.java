import java.util.*;

public class StayTheHellAwayFromMe {
    static ArrayList<String> locationPoint;

    public StayTheHellAwayFromMe() {
        locationPoint = JOJOLands.getMap().getLocationPoint();
        Scanner sc = new Scanner(System.in);

        System.out.print("Source: ");
        String source = sc.nextLine();

        System.out.print("Destination: ");
        String destination = sc.nextLine();

        System.out.print("Identified Locations: ");
        String[] input = sc.nextLine().split(",");

        //remove space for input
        for (int i = 0; i < input.length; i++) {
            input[i] = input[i].trim();
        }

        int startNode = locationPoint.indexOf(source);
        int endNode = locationPoint.indexOf(destination);
        ArrayList<Integer> identifiedLocation = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            identifiedLocation.add(locationPoint.indexOf(input[i]));
        }

        Map map = JOJOLands.getMap();
        int[][] adjacentMatrix = map.getAdjacentMatrix();
        try {
            System.out.println("======================================================================\nOptimal Path: ");
            findShortestPaths(adjacentMatrix, startNode, endNode, identifiedLocation);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please enter existing location properly!");
        }
    }

    public static void findShortestPaths(int[][] adjacentMatrix, int source, int destination, List<Integer> locationsToVisit) {
        PriorityQueue<List<Integer>> pq = new PriorityQueue<>((path1, path2) -> {
            int frequency1 = getFrequencyOfVisits(path1, locationsToVisit);
            int frequency2 = getFrequencyOfVisits(path2, locationsToVisit);
            int distance1 = getTotalDistance(adjacentMatrix, path1);
            int distance2 = getTotalDistance(adjacentMatrix, path2);

            if (frequency1 != frequency2) {
                return Integer.compare(frequency1, frequency2);
            } else {
                return Integer.compare(distance1, distance2);
            }
        });

        List<Integer> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[adjacentMatrix.length];

        dfs(adjacentMatrix, source, destination, visited, currentPath, pq);

        if (!pq.isEmpty()) {
            List<Integer> path = pq.poll();
            int distance = getTotalDistance(adjacentMatrix, path);

            for (int i = 0; i < path.size(); i++) {
                System.out.print(locationPoint.get(path.get(i)) + " ");
                if (i != path.size() - 1)
                    System.out.print("\u2192" + " ");
            }

            System.out.println("(" + distance + "km)");

        } else {
            System.out.println("No paths found.");
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

    private static int getFrequencyOfVisits(List<Integer> path, List<Integer> locationsToVisit) {
        int frequency = 0;
        for (int node : path) {
            if (locationsToVisit.contains(node)) {
                frequency++;
            }
        }
        return frequency;
    }
}
