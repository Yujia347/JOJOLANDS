import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class HeavensDoor {
    private List<String> residentList;
    private List<String> standList;
    private List<Player> playerList;

    public HeavensDoor() {
        residentList = new ArrayList<>();
        standList = new ArrayList<>();
        playerList = new ArrayList<>();
        System.out.println("Resident Information in " + JOJOLands.getCurrentLocation().getName());
        try {
            // Read resident information from file
            Scanner readRes = new Scanner(new FileInputStream("residents.csv"));
            Scanner readStd = new Scanner(new FileInputStream("stands.csv"));
            readRes.nextLine();
            readStd.nextLine();
            while (readRes.hasNextLine()) {
                residentList.add(readRes.nextLine());
            }
            while (readStd.hasNextLine()) {
                standList.add(readStd.nextLine());
            }

            for (String resident : residentList) {
                String[] resInfo = resident.split(",");
                boolean checkIsStandUser = false;
                for (String stand : standList) {
                    String[] stdInfo = stand.split(",");
                    if (resInfo[0].equals(stdInfo[1])) {
                        List<String> parents = new ArrayList<>();
                        for (int i = 4; i < resInfo.length; i++) {
                            parents.add(resInfo[i]);
                        }
                        playerList.add(new Player(resInfo[0], resInfo[1], resInfo[2], resInfo[3], parents, stdInfo[0], stdInfo[2], stdInfo[3], stdInfo[4], stdInfo[5], stdInfo[6], stdInfo[7]));
                        checkIsStandUser = true;
                    }
                }
                if (!checkIsStandUser) {
                    List<String> parents = new ArrayList<>();
                    for (int i = 4; i < resInfo.length; i++) {
                        parents.add(resInfo[i]);
                    }
                    playerList.add(new Player(resInfo[0], resInfo[1], resInfo[2], resInfo[3], parents));
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }

    }

    public void viewResidentInfo() {
        ArrayList<Player> playerInCurrentLocation = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("+" + "-".repeat(4) + "+" + "-".repeat(23) + "+" + "-".repeat(5) + "+" + "-".repeat(8) + "+" + "-".repeat(25) + "+" + "-".repeat(20) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(11) + "+" + "-".repeat(24) + "+");
        System.out.printf("| %2s | %-21s | %3s | %6s | %-23s | %-18s | %-8s | %-8s | %-8s | %9s | %22s |\n", "No", "Name", "Age", "Gender", "Stand", "Destructive Power", "Speed", "Range", "Stamina", "Precision", "Development Potential");
        System.out.println("+" + "-".repeat(4) + "+" + "-".repeat(23) + "+" + "-".repeat(5) + "+" + "-".repeat(8) + "+" + "-".repeat(25) + "+" + "-".repeat(20) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(11) + "+" + "-".repeat(24) + "+");

        for (Player player : playerList) {
            if (player.getResidentialArea().equals(JOJOLands.getCurrentLocation().getName())) {
                playerInCurrentLocation.add(player);
            }
        }

        int index = 1;
        for (Player player : playerInCurrentLocation) {
            System.out.printf("| %2s | %-21s | %-3s | %-6s | %-23s | %-18s | %-8s | %-8s | %-8s | %-9s | %-22s |\n", index, player.getName(), player.getAge(), player.getGender(), player.getStand(), player.getDestructivePower(), player.getSpeed(), player.getRange(), player.getStamina(), player.getPrecision(), player.getDevelopmentPotential());
            index++;
        }
        System.out.println("+" + "-".repeat(4) + "+" + "-".repeat(23) + "+" + "-".repeat(5) + "+" + "-".repeat(8) + "+" + "-".repeat(25) + "+" + "-".repeat(20) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(11) + "+" + "-".repeat(24) + "+");

        boolean exit = false;
        while (!exit) {
            System.out.println("""
                    [1] View Resident's Profile
                    [2] Sort
                    [3] Exit
                    """);
            System.out.print("Select: ");
            int choice = 0;
            boolean validInput = false;
            while (!validInput) {
                try {
                    choice = sc.nextInt();
                    if (choice >= 1 && choice <= 3) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input. Please enter your selection again!");
                        System.out.print("Select (1-3): ");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter your selection again!");
                    System.out.print("Select (1-3): ");
                    sc.nextLine(); // Consume the invalid input
                }
            }

            switch (choice) {
                case 1 -> viewProfile();
                case 2 -> {
                    System.out.print("Enter the sorting order [column name (ASC/DESC)]:  ");
                    try {
                        sc.nextLine();
                        String input = sc.nextLine().replaceAll("\\s", "");
                        String[] sortingOrder = input.split(";");
                        selectionSort(playerInCurrentLocation, sortingOrder);
                        System.out.println("+" + "-".repeat(4) + "+" + "-".repeat(23) + "+" + "-".repeat(5) + "+" + "-".repeat(8) + "+" + "-".repeat(25) + "+" + "-".repeat(20) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(11) + "+" + "-".repeat(24) + "+");
                        System.out.printf("| %2s | %-21s | %3s | %6s | %-23s | %-18s | %-8s | %-8s | %-8s | %9s | %22s |\n", "No", "Name", "Age", "Gender", "Stand", "Destructive Power", "Speed", "Range", "Stamina", "Precision", "Development Potential");
                        System.out.println("+" + "-".repeat(4) + "+" + "-".repeat(23) + "+" + "-".repeat(5) + "+" + "-".repeat(8) + "+" + "-".repeat(25) + "+" + "-".repeat(20) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(11) + "+" + "-".repeat(24) + "+");
                        index = 1;
                        for (Player player : playerInCurrentLocation) {
                            System.out.printf("| %2s | %-21s | %-3s | %-6s | %-23s | %-18s | %-8s | %-8s | %-8s | %-9s | %-22s |\n", index, player.getName(), player.getAge(), player.getGender(), player.getStand(), player.getDestructivePower(), player.getSpeed(), player.getRange(), player.getStamina(), player.getPrecision(), player.getDevelopmentPotential());
                            index++;
                        }
                        System.out.println("+" + "-".repeat(4) + "+" + "-".repeat(23) + "+" + "-".repeat(5) + "+" + "-".repeat(8) + "+" + "-".repeat(25) + "+" + "-".repeat(20) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(10) + "+" + "-".repeat(11) + "+" + "-".repeat(24) + "+");
                    } catch (Exception e) {
                        System.out.println("Invalid format. Please enter your selection again!");
                    }
                }
                case 3 -> exit = true;
                default -> System.out.println("Invalid input. Please make your selection again !");
            }
        }
    }

    public static void selectionSort(ArrayList<Player> list, String[] sortingOrder) {
        for (int i = 0; i < list.size(); i++) {
            Player currentMin = list.get(i);
            int currentMinIndex = i;

            for (int j = i + 1; j < list.size(); j++) {
                if (currentMin.compareTo(list.get(j), sortingOrder) > 0) {
                    currentMin = list.get(j);
                    currentMinIndex = j;
                }
            }
            Player temp = list.get(currentMinIndex);
            list.set(currentMinIndex, list.get(i));
            list.set(i, temp);
        }
    }

    private void viewProfile() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the resident's name: ");
        String name = sc.nextLine().strip();
        for (Player player : playerList) {
            if (player.getName().equals(name)) {
                System.out.println("=".repeat(70));
                System.out.println(player.getName() + "'s Profile");
                System.out.printf("%-23s: %s\n", "Name", player.getName());
                System.out.printf("%-23s: %s\n", "Age", player.getAge());
                System.out.printf("%-23s: %s\n", "Gender", player.getGender());
                String parent = "";
                for (int i = 0; i < player.getParents().size(); i++) {
                    parent += player.getParents().get(i);
                    if (i != player.getParents().size() - 1) {
                        parent += ", ";
                    }
                }
                System.out.printf("%-23s: %s\n", "Parents", parent);
                System.out.printf("%-23s: %s\n", "Stand", player.getStand());
                System.out.printf("%-23s: %s\n", "Destructive Power", player.getDestructivePower());
                System.out.printf("%-23s: %s\n", "Speed", player.getSpeed());
                System.out.printf("%-23s: %s\n", "Range", player.getRange());
                System.out.printf("%-23s: %s\n", "Stamina", player.getStamina());
                System.out.printf("%-23s: %s\n", "Precision", player.getPrecision());
                System.out.printf("%-23s: %s\n", "Development Potential", player.getDevelopmentPotential());

                System.out.println("Order History");
                System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(42) + "+" + "-".repeat(25) + "+");
                System.out.printf("| %3s | %-40s | %-23s | \n", "Day", "Food", "Restaurant");
                System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(42) + "+" + "-".repeat(25) + "+");
                JOJOLands.getOrder().displayOrderHistory(name);
                System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(42) + "+" + "-".repeat(25) + "+");
            }

        }
    }

}

class Player {
    private String name;
    private String age;
    private String gender;
    private String residentialArea;
    private List<String> parents;
    private String stand;
    private String destructivePower;
    private String speed;
    private String range;
    private String stamina;
    private String precision;
    private String developmentPotential;

    //Player without stand user
    public Player(String name, String age, String gender, String residentialArea, List<String> parents) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.residentialArea = residentialArea;
        this.parents = parents;
        this.stand = "-";
        this.destructivePower = "-";
        this.speed = "-";
        this.range = "-";
        this.stamina = "-";
        this.precision = "-";
        this.developmentPotential = "-";
    }

    //Player with stand user
    public Player(String name, String age, String gender, String residentialArea, List<String> parents, String stand, String destructivePower, String speed, String range, String stamina, String precision, String developmentPotential) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.residentialArea = residentialArea;
        this.parents = parents;
        this.stand = stand;
        this.destructivePower = destructivePower;
        this.speed = speed;
        this.range = range;
        this.stamina = stamina;
        this.precision = precision;
        this.developmentPotential = developmentPotential;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getResidentialArea() {
        return residentialArea;
    }

    public List<String> getParents() {
        return parents;
    }

    public String getStand() {
        return stand;
    }

    public String getDestructivePower() {
        return destructivePower;
    }

    public String getSpeed() {
        return speed;
    }

    public String getRange() {
        return range;
    }

    public String getStamina() {
        return stamina;
    }

    public String getPrecision() {
        return precision;
    }

    public String getDevelopmentPotential() {
        return developmentPotential;
    }

    public int compareName(Player other) {
        return this.getName().compareTo(other.getName());
    }

    public int compareAge(Player other) {
        int age1 = this.getAge().equals("N/A") ? Integer.MAX_VALUE : Integer.parseInt(this.getAge());
        int age2 = other.getAge().equals("N/A") ? Integer.MAX_VALUE : Integer.parseInt(other.getAge());
        return Integer.compare(age1, age2);
    }

    public int compareGender(Player other) {
        return this.getGender().compareTo(other.getGender());
    }

    public int compareStand(Player other) {
        String stand1 = this.getStand();
        String stand2 = other.getStand();

        if (stand1.equals("-") && !stand2.equals("-")) {
            return 1; // Sort "-" after other values
        } else if (!stand1.equals("-") && stand2.equals("-")) {
            return -1; // Sort other values before "-"
        } else {
            return stand1.compareTo(stand2); // Compare normally for other cases
        }
    }

    public int compareDestructivePower(Player other) {
        return compareWithSortOrder(this.getDestructivePower(), other.getDestructivePower());
    }

    public int compareSpeed(Player other) {
        return compareWithSortOrder(this.getSpeed(), other.getSpeed());
    }

    public int compareRange(Player other) {
        return compareWithSortOrder(this.getRange(), other.getRange());
    }

    public int compareStamina(Player other) {
        return compareWithSortOrder(this.getStamina(), other.getStamina());
    }

    public int comparePrecision(Player other) {
        return compareWithSortOrder(this.getPrecision(), other.getPrecision());
    }

    public int compareDevelopmentPotental(Player other) {
        return compareWithSortOrder(this.getDevelopmentPotential(), other.getDevelopmentPotential());
    }

    private int compareWithSortOrder(String value1, String value2) {
        // Define the sorting order
        String[] sortingOrder = {"Infinity", "A", "B", "C", "D", "E", "?", "Null", "-"};

        int index1 = getIndex(value1, sortingOrder);
        int index2 = getIndex(value2, sortingOrder);

        return Integer.compare(index1, index2);
    }

    private int getIndex(String destructivePower, String[] sortingOrder) {
        for (int i = 0; i < sortingOrder.length; i++) {
            if (destructivePower.equals(sortingOrder[i])) {
                return i;
            }
        }
        return -1; // If the destructive power is not found in the sorting order
    }

    public int compareTo(Player other, String[] sortingOrder) {
        for (String str : sortingOrder) {
            int indexStart = str.indexOf("(");
            int indexEnd = str.indexOf(")");
            String field = str.substring(0, indexStart);
            String sortOrder = str.substring(indexStart + 1, indexEnd);
            int comparisonResult = compareFields(field.toLowerCase(), sortOrder, other);
            if (comparisonResult != 0) {
                return comparisonResult;
            }
        }

        // If all fields are equal, consider the players as equal
        return 0;
    }

    private int compareFields(String field, String sortOrder, Player other) {
        int result;
        switch (field.toLowerCase()) {
            case "name":
                result = compareName(other);
                break;
            case "age":
                result = compareAge(other);
                break;
            case "gender":
                result = compareGender(other);
                break;
            case "stand":
                result = compareStand(other);
                break;
            case "desstructive power":
                result = compareDestructivePower(other);
                break;
            case "speed":
                result = compareSpeed(other);
                break;
            case "range":
                result = compareRange(other);
                break;
            case "stamina":
                result = compareStamina(other);
                break;
            case "precision":
                result = comparePrecision(other);
                break;
            case "development potential":
                result = compareDevelopmentPotental(other);
                break;
            default:
                result = 0;
                break;
        }

        // Apply the sort order based on sortOrder
        if (sortOrder.equalsIgnoreCase("asc")) {
            return result;
        } else if (sortOrder.equalsIgnoreCase("desc")) {
            return -result;
        } else {
            return 0; // Default to no sorting if sortOrder is invalid
        }
    }
}


