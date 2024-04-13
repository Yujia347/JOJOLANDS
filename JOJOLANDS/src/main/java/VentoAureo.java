import java.util.*;

public class VentoAureo {
    private HashMap<Integer, PriorityQueue<String>> distincts;

    public VentoAureo() {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> uncategorised = new ArrayList<>();
        uncategorised.addAll(JOJOLands.getMap().getLocationPoint());
        distincts = new HashMap<>();
        boolean exit = false;

        while (!exit) {
            try {
                System.out.print("Combine: ");
                String input = sc.nextLine();
                System.out.println("======================================================================");
                if (input.equalsIgnoreCase("exit")) {
                    exit = true;
                } else {
                    String[] combineLocation = input.split("&");
                    String location1 = combineLocation[0].trim();
                    String location2 = combineLocation[1].trim();
                    if (connected(location1, location2)) {
                        PriorityQueue<String> distinct1 = findDistinctContainingLocation(location1);
                        PriorityQueue<String> distinct2 = findDistinctContainingLocation(location2);
                        if (distinct1 != null && distinct2 != null && distinct1 != distinct2) {
                            int keyDistinct1 = 0;
                            int keyDistinct2 = 0;
                            for (Integer key : distincts.keySet()) {
                                if (distincts.get(key).containsAll(distinct1)) {
                                    keyDistinct1 = key;
                                } else if (distincts.get(key).containsAll(distinct2)) {
                                    keyDistinct2 = key;
                                }
                            }
                            if (keyDistinct1 < keyDistinct2) {
                                distinct1.addAll(distinct2);
                                distincts.remove(keyDistinct2);
                            } else {
                                distinct2.addAll(distinct1);
                                distincts.remove(keyDistinct1);
                            }
                        } else if (distinct1 != null) {
                            distinct1.add(location2);
                        } else if (distinct2 != null) {
                            distinct2.add(location1);
                        } else {
                            PriorityQueue<String> newDistinct = new PriorityQueue<>();
                            newDistinct.add(location1);
                            newDistinct.add(location2);
                            distincts.put(distincts.size() + 1, newDistinct);
                        }

                        uncategorised.remove(location1);
                        uncategorised.remove(location2);

                        for (int key : distincts.keySet()) {
                            System.out.println("Distinct " + key + ": " + distincts.get(key) + " (" + distincts.get(key).size() + " locations)");
                        }
                        System.out.println("Uncategorized: " + uncategorised + " (" + uncategorised.size() + " locations)");
                        System.out.println("======================================================================");

                    } else {
                        System.out.println(location1 + " and " + location2 + " are not connected by a road!");
                        System.out.println("======================================================================");

                    }
                    if (uncategorised.isEmpty() & distincts.size() == 1) {
                        System.out.println("Congratulation ! You have successfully categorized all the location into one distinct!");
                        break;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Please enter existing location properly!");
            }
        }
    }

    private PriorityQueue<String> findDistinctContainingLocation(String location) {
        for (PriorityQueue<String> distinct : distincts.values()) {
            if (distinct.contains(location)) {
                return distinct;
            }
        }
        return null;
    }

    private boolean connected(String location1, String location2) {
        LinkedList<Location> locationPoint = JOJOLands.getLocation();
        Location loc1 = null;
        for (Location location : locationPoint) {
            if (location1.equals(location.getName())) {
                loc1 = location;
            }
        }
        HashMap<Character, String> adjacentLocation = JOJOLands.getMap().getAdjacentPoint(loc1);
        for (Character key : adjacentLocation.keySet()) {
            if (adjacentLocation.get(key).equals(location2)) {
                return true;
            }
        }
        return false;
    }
}
