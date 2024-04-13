import java.util.HashMap;

public abstract class Location {
    private String name;
    protected HashMap<Integer, String> listOfFunction;

    public Location(String name) {
        this.name = name;
        listOfFunction = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void getAdjacentPoint() {
        JOJOLands.getMap().getAdjacentPoint(this);
    }

    //common function
    public void moveTo(char keyDestination) {
        HashMap<Character, String> adjacentPoint = JOJOLands.getMap().getAdjacentPoint(JOJOLands.getCurrentLocation());
        try {
            for (Location i : JOJOLands.getLocation()) {
                if (adjacentPoint.get(keyDestination).equals(i.getName())) {
                    JOJOLands.setCurrentLocation(i);
                    JOJOLands.getPathway().add(i);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("No such location to move on. Please enter your selection again !");
        }

        JOJOLands.setCheckIsMoving(true);
        JOJOLands.setCheckIsBack(false);
    }

    public void forward() {
        JOJOLands.setCurrentLocation(JOJOLands.getForwardHistory());
        JOJOLands.pathway.add(JOJOLands.getForwardHistory());
        JOJOLands.setForwardHistory(null);
        JOJOLands.setCheckIsMoving(true);
        JOJOLands.setCheckIsBack(false);
    }

    public void back() {
        Location destination = JOJOLands.getPathway().get(JOJOLands.getPathway().size() - 2);
        JOJOLands.setCurrentLocation(destination);
        JOJOLands.setForwardHistory(JOJOLands.getPathway().get(JOJOLands.getPathway().size() - 1));
        JOJOLands.pathway.removeLast();
        JOJOLands.setCheckIsMoving(true);
        JOJOLands.setCheckIsBack(true);
    }

    public void backToTownHall() {
        Location destination = JOJOLands.getPathway().getFirst();
        JOJOLands.setCurrentLocation(destination);
        JOJOLands.getPathway().add(destination);
    }

    //common option
    public void addMoveToOption() {
        listOfFunction.put(listOfFunction.size() + 1, JOJOLands.getMap().toString());
    }

    public void addBackOption() {
        if (JOJOLands.pathway.size() > 1)
            listOfFunction.put(listOfFunction.size() + 1, "Back (" + JOJOLands.getPathway().get(JOJOLands.getPathway().size() - 2).getName() + ")");
    }

    public void addFowardOption() {
        listOfFunction.put(listOfFunction.size() + 1, "Forward (" + JOJOLands.getForwardHistory().getName() + ")");
    }

    public void addBackToTownHallOption() {
        listOfFunction.put(listOfFunction.size() + 1, "Back to Town Hall");
    }

    public abstract void displayFunction();

    public abstract void executeFunction(String key);


    public String toString() {
        return "======================================================================\nCurrent Location :"
                + this.name;
    }
}

