import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Map extends Graph<String, Integer> {
    private final ArrayList<String> locationPoint;
    private HashMap<Character, String> possibleDestination;
    private final String filename;

    public Map(String filename) {
        super();
        this.filename = filename;
        locationPoint = new ArrayList<>();
    }

    public void addLocationPoint(String locationPointName) {
        locationPoint.add(locationPointName);
    }

    public void removeLocationPoint(String lacationPointName) {
        locationPoint.remove(lacationPointName);
    }

    public void createMap() {
        try {
            Scanner inputStream = new Scanner(new FileInputStream("Location.txt"));
            while (inputStream.hasNextLine()) {
                locationPoint.add(inputStream.nextLine());
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }

        for (String i : locationPoint) {
            this.addVertex(i);
        }

        try {
            Scanner sc = new Scanner(new File(filename));
            sc.nextLine();
            while (sc.hasNextLine()) {
                String connectedLocation = sc.nextLine();
                String[] connection = connectedLocation.split(",");
                this.addUndirectedEdge(connection[0], connection[1], Integer.parseInt(connection[2]));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }
    }

    public HashMap<Character, String> getAdjacentPoint(Location current) {
        possibleDestination = new HashMap<>();
        int ascii = 65;
        for (String i : locationPoint) {
            if (hasEdge(current.getName(), i)) {
                possibleDestination.put((char) ascii, i);
                ascii++;
            }
        }
        return possibleDestination;
    }

    public int[][] getAdjacentMatrix() {
        int[][] adjacentMatrix = new int[locationPoint.size()][locationPoint.size()];
        for (String i : locationPoint) {
            for (String j : locationPoint) {
                if (hasEdge(i, j)) {
                    adjacentMatrix[locationPoint.indexOf(i)][locationPoint.indexOf(j)] = getEdgeWeight(i, j);
                } else {
                    adjacentMatrix[locationPoint.indexOf(i)][locationPoint.indexOf(j)] = 0;
                }
            }
        }
        return adjacentMatrix;
    }

    public ArrayList<String> getLocationPoint() {
        return locationPoint;
    }

    public String getFilename() {
        return filename;
    }

    public String toString() {
        StringBuilder toString = new StringBuilder("Move to: \n");
        for (Character key : possibleDestination.keySet()) {
            toString.append("\t[").append(key).append("] ").append(possibleDestination.get(key));
        }
        return toString.toString();
    }
}

