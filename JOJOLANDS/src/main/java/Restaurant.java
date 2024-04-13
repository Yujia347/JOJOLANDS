
import java.util.HashMap;

public class Restaurant extends Location {
    public Restaurant(String name) {
        super(name);
    }

    public void addRestaurantOption() {
        listOfFunction.put(listOfFunction.size() + 1, "View Waiting List and Order Processing List");
        listOfFunction.put(listOfFunction.size() + 1, "View Menu");
        listOfFunction.put(listOfFunction.size() + 1, "View Sales Information");
        listOfFunction.put(listOfFunction.size() + 1, "Modify Food Information");
        listOfFunction.put(listOfFunction.size() + 1, "Milagro Man");
    }

    @Override
    public void displayFunction() {
        listOfFunction = new HashMap<>();
        addMoveToOption();
        addRestaurantOption();
        if (JOJOLands.checkIsMoving) {
            addBackOption();
        }
        if (JOJOLands.checkIsBack) {
            addFowardOption();
        }
        addBackToTownHallOption();
    }

    @Override
    public void executeFunction(String selectFunction) {
        selectFunction = selectFunction.toUpperCase();
        int keyFunction = 0;
        char keyDestination = 0;
        keyFunction = Character.getNumericValue(selectFunction.charAt(0));
        if (selectFunction.length() > 1) {
            keyDestination = selectFunction.charAt(1);
            moveTo(keyDestination);
        } else {
            try {
                String function = listOfFunction.get(keyFunction);
                if (function.contains("Back (")) {
                    function = function.substring(0, 4);
                } else if (function.contains("Forward (")) {
                    function = function.substring(0, 7);
                }
                switch (function) {
                    case "View Waiting List and Order Processing List" -> {
                        PearlJam pearlJam = new PearlJam(this.getName());
                        pearlJam.viewWaitingList();
                        pearlJam.viewOrderProcessingList();
                    }
                    case "View Menu" -> JOJOLands.getFoodMenu().viewMenuList();
                    case "View Sales Information" -> {
                        MoodyBlues moodyBlues = new MoodyBlues();
                        moodyBlues.viewSalesHistory();
                    }
                    case "Modify Food Information" -> {
                        JOJOLands.getFoodMenu().modifyFoodInformation();
                    }
                    case "Milagro Man" -> {
                        MilagroMan milagroMan = new MilagroMan();
                    }
                    case "Back to Town Hall" -> backToTownHall();
                    case ("Back") -> back();
                    case "Forward" -> forward();
                }
            } catch (NullPointerException e) {
                System.out.println("No such function. Please enter selection again !");
            }
        }
    }

    public String toString() {
        String toString = super.toString();
        for (Integer key : listOfFunction.keySet()) {
            toString += "\n[" + key + "] " + listOfFunction.get(key);
        }
        toString += "\n\nSelect: ";
        return toString;
    }
}
