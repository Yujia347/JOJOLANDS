import java.util.HashMap;
import java.util.Scanner;

public class TownHall extends Location {
    public TownHall(String name) {
        super(name);
    }

    public void addAdvanceToNextDayOption() {
        listOfFunction.put(listOfFunction.size() + 1, "Advance to Next Day");
    }

    public void addExitOption() {
        listOfFunction.put(listOfFunction.size() + 1, "Exit");
    }

    public void addSaveOption() {
        listOfFunction.put(listOfFunction.size() + 1, "Save Game");
    }

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
                    case "Advance to Next Day" -> {
                        JOJOLands.setDay(JOJOLands.getDay() + 1);
                        String[] dayName = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                        System.out.println("It’s Day " + JOJOLands.getDay() + " (" + dayName[JOJOLands.getDay() % 7] + ") of our journey in JOJOLands!");
                        JOJOLands.setCheckIsMoving(false);
                        JOJOLands.setCheckIsBack(false);
                        JOJOLands.getOrder().orderFood();
                        JOJOLands.getOrder().recordSalesHistory();
                    }
                    case "Save Game" -> {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("Enter the path to save your file: ");
                        String filePath = sc.nextLine();
                        JOJOLands.saveGame(filePath);
                    }
                    case "Exit" -> JOJOLands.setExit(true);
                    case "Back to Town Hall" -> backToTownHall();
                    case ("Back") -> back();
                    case "Forward" -> forward();
                }
            } catch (NullPointerException e) {
                System.out.println("No such function. Please enter selection again !");
            }
        }
    }

    public void displayFunction() {
        listOfFunction = new HashMap<>();
        addMoveToOption();
        addAdvanceToNextDayOption();
        addSaveOption();
        if (JOJOLands.checkIsMoving) {
            addBackOption();
        }
        if (JOJOLands.checkIsBack) {
            addFowardOption();
        }
        addExitOption();
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
