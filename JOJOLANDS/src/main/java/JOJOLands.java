import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JOJOLands {
    private static Location currentLocation;
    private static Location forwardHistory;
    static int day;
    static Map map;
    static LinkedList<Location> pathway;
    static LinkedList<Location> location = new LinkedList<>();
    static boolean checkIsMoving;
    static boolean checkIsBack;
    static Order order;
    static boolean exit = false;
    static FoodMenu foodMenu;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] dayName = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        setUpLocation();
        startUpMenu();

        int key = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                key = sc.nextInt();
                if (key >= 1 && key <= 3) {
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

        switch (key) {
            case 1 -> {
                mapMenu();
                int keyMap = 0;
                validInput = false;
                while (!validInput) {
                    try {
                        keyMap = sc.nextInt();
                        if (keyMap >= 1 && keyMap <= 3) {
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
                switch (keyMap) {
                    case 1 -> {
                        map = new Map("Default Map.csv");
                        map.createMap();
                    }
                    case 2 -> {
                        map = new Map("Parallel Map.csv");
                        map.createMap();
                    }
                    case 3 -> {
                        map = new Map("Alternate Map.csv");
                        map.addLocationPoint("Passione Restaurant");
                        map.removeLocationPoint("Libeccio");
                        map.createMap();
                    }
                }
                //Player will start at Town Hall so set currentLocation to Town Hall
                Location townHall = new TownHall("Town Hall");
                currentLocation = townHall;
                pathway = new LinkedList<>();
                pathway.add(townHall);

                //The first day of JOJOLands
                day = 1;

                //Player has not chosen to move or back at this moment
                checkIsMoving = false;
                checkIsBack = false;

                //System set up the food menu
                foodMenu = new FoodMenu();

                //Set up the system of the game,Assign order every day for the player to dine in their specify restaurant
                order = new Order();
                order.orderFood();

                //Record the SalesHistory every day
                order.recordSalesHistory();
                sc.nextLine();
            }
            case 2 -> {
                System.out.print("Enter the path of your save file: ");
                sc.nextLine();
                String filepath = sc.nextLine();
                System.out.println("======================================================================");
                System.out.println("Welcome back to the fantastical realm of JOJOLands.");
                loadGame(filepath);
            }
            case 3 -> {
                exit = true;
            }
        }
        if (exit) {
            System.out.println("Thanks for playing JOJOLands!");
        } else {
            System.out.println("It’s Day " + day + " (" + dayName[day % 7] + ") of our journey in JOJOLands!");
        }
        while (!exit) {
            currentLocation.getAdjacentPoint();
            currentLocation.displayFunction();
            System.out.print(currentLocation);
            String selectFunction = sc.nextLine();
            currentLocation.executeFunction(selectFunction);
        }
        sc.close();
    }

    public static void startUpMenu() {
        System.out.print("""
                Welcome, to the fantastical realm of JOJOLands.
                [1] Start Game
                [2] Load Game
                [3] Exit

                Select:""");
    }

    public static void mapMenu() {
        System.out.print("""
                ======================================================================
                Select a map:
                [1] Default Map
                [2] Parallel Map
                [3] Alternate Map

                Select:""");
    }

    public static void loadGame(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {
            JSONTokener token = new JSONTokener(fileReader);
            JSONObject gameData = new JSONObject(token);

            // Retrieve and set the current location
            String currentLocationName = gameData.getString("Current Location");
            String forwardHistoryName = gameData.optString("Forward History", "");
            for (Location location : getLocation()) {
                if (currentLocationName.equals(location.getName())) {
                    setCurrentLocation(location);
                }
                if (forwardHistoryName.equals(location.getName())) {
                    setForwardHistory(location);
                }
            }

            // Retrieve and set the pathway
            JSONArray pathwayArray = gameData.getJSONArray("Pathway");
            LinkedList<Location> pathway = new LinkedList<>();
            for (int i = 0; i < pathwayArray.length(); i++) {
                String item = pathwayArray.getString(i);
                for (Location location : getLocation()) {
                    if (item.equals(location.getName())) {
                        pathway.add(location);
                    }
                }
            }
            setPathway(pathway);

            // Retrieve and set the day
            int day = gameData.getInt("Day");
            setDay(day);

            // Retrieve and set the map
            Map map = new Map((String) gameData.get("Map"));
            setMap(map);
            map.createMap();

            // Retrieve and set the checkIsMoving and checkIsBack variables
            boolean checkIsMoving = gameData.getBoolean("Check is Moving");
            boolean checkIsBack = gameData.getBoolean("Check is Back");
            setCheckIsMoving(checkIsMoving);
            setCheckIsBack(checkIsBack);

            LinkedList<Food> menuList = new LinkedList<>();

            // Retrieve the JSON object for the "Menu List"
            JSONObject foodMenuList = gameData.getJSONObject("Menu List");

            // Iterate over each entry in the JSON object
            for (String foodName : foodMenuList.keySet()) {
                // Retrieve the JSON object for the current food entry
                JSONObject foodData = foodMenuList.getJSONObject(foodName);
                // Extract the relevant information
                String foodLocation = foodData.getString("foodLocation");
                double price = foodData.getDouble("price");
                // Create a new Food object and add it to the menuList
                Food food = new Food(foodLocation, foodName, price);
                menuList.add(food);
            }
            foodMenu = new FoodMenu();
            foodMenu.setMenuList(menuList);

            // Retrieve order history data from the JSON object
            JSONObject orderHistoryData = gameData.getJSONObject("Order History");
            HashMap<String, LinkedList<Food>> orderHistory = new HashMap<>();

            // Iterate over the keys in the order history data
            for (String key : orderHistoryData.keySet()) {
                // Retrieve the list associated with the current key
                JSONArray foodListData = orderHistoryData.getJSONArray(key);

                // Create a new LinkedList to store the food list
                LinkedList<Food> foodList = new LinkedList<>();

                // Iterate over the items in the food list data and create Food objects
                for (int i = 0; i < foodListData.length(); i++) {
                    JSONObject foodData = foodListData.getJSONObject(i);
                    String foodLocation = foodData.getString("foodLocation");
                    double price = foodData.getDouble("price");
                    String foodName = foodData.getString("foodName");

                    Food food = new Food(foodLocation, foodName, price);
                    foodList.add(food);
                }

                // Add the food list to the orderHistory HashMap using the current key
                orderHistory.put(key, foodList);
            }

            order = new Order();
            // Set the orderHistory data in the Order object
            getOrder().setOrderHistory(orderHistory);

            // Retrieve and set the salesHistory
            HashMap<Integer, HashMap<String, LinkedList<String>>> salesHistory = new HashMap<>();
            JSONObject salesHistoryData = gameData.getJSONObject("Sales History");
            for (String key : salesHistoryData.keySet()) {
                int dayNumber = Integer.parseInt(key);
                HashMap<String, LinkedList<String>> locationSales = new HashMap<>();
                JSONObject locationSalesData = salesHistoryData.getJSONObject(key);
                for (String locationKey : locationSalesData.keySet()) {
                    JSONArray salesListData = locationSalesData.getJSONArray(locationKey);
                    LinkedList<String> salesList = new LinkedList<>();
                    for (int i = 0; i < salesListData.length(); i++) {
                        salesList.add(salesListData.getString(i));
                    }
                    locationSales.put(locationKey, salesList);
                }
                salesHistory.put(dayNumber, locationSales);
            }
            order.setSalesHistory(salesHistory);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(String filePath) {
        JSONObject gameData = new JSONObject();
        gameData.put("Current Location", getCurrentLocation().getName());
        if (getForwardHistory() != null) {
            gameData.put("Forward History", getForwardHistory().getName());
        } else {
            gameData.put("Forward History", JSONObject.NULL);

        }
        gameData.put("Day", getDay());
        gameData.put("Map", getMap().getFilename());

        JSONArray pathwayArray = new JSONArray();
        for (Location pathway : pathway) {
            pathwayArray.put(pathway.getName());
        }
        gameData.put("Pathway", pathwayArray);

        gameData.put("Check is Moving", getCheckIsMoving());
        gameData.put("Check is Back", getCheckIsBack());

        // Convert checkForModifyingPrice to JSON

        JSONObject menuList = new JSONObject();
        for (Food food : foodMenu.getMenuList()) {
            String foodLocation = food.getFoodLocation();
            double price = food.getPrice();
            String foodName = food.getFoodName();

            JSONObject foodData = new JSONObject();
            foodData.put("foodLocation", foodLocation);
            foodData.put("price", price);
            foodData.put("foodName", foodName);

            menuList.put(foodName, foodData);
        }

        gameData.put("Menu List", menuList);


        // Convert orderHistory to JSON
        JSONObject orderHistoryData = new JSONObject();
        for (String key : getOrder().getOrderHistory().keySet()) {
            LinkedList<Food> foodList = getOrder().getOrderHistory().get(key);
            JSONArray foodListData = new JSONArray();
            for (Food food : foodList) {
                JSONObject foodData = new JSONObject();
                foodData.put("foodLocation", food.getFoodLocation());
                foodData.put("price", food.getPrice());
                foodData.put("foodName", food.getFoodName());
                foodListData.put(foodData);
            }
            orderHistoryData.put(key, foodListData);
        }
        gameData.put("Order History", orderHistoryData);

        // Convert salesHistory to JSON
        JSONObject salesHistoryData = new JSONObject();
        for (Integer dayNumber : getOrder().getSalesHistory().keySet()) {
            HashMap<String, LinkedList<String>> locationSales = getOrder().getSalesHistory().get(dayNumber);
            JSONObject locationSalesData = new JSONObject();
            for (String locationKey : locationSales.keySet()) {
                LinkedList<String> salesList = locationSales.get(locationKey);
                JSONArray salesListData = new JSONArray(salesList);
                locationSalesData.put(locationKey, salesListData);
            }
            salesHistoryData.put(dayNumber.toString(), locationSalesData);
        }
        gameData.put("Sales History", salesHistoryData);

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(gameData.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUpLocation() {
        Location townHall = new TownHall("Town Hall");
        Location moriohGrandHotel = new ResidentialArea("Morioh Grand Hotel");
        Location trattoriaTrussardi = new Restaurant("Trattoria Trussardi");
        Location greenDolphinStreetPrison = new ResidentialArea("Green Dolphin Street Prison");
        Location jadeGarden = new Restaurant("Jade Garden");
        Location sanGiorgioMaggiore = new ResidentialArea("San Giorgio Maggiore");
        Location libeccio = new Restaurant("Libeccio");
        Location cafeDeuxMagots = new Restaurant("Cafe Deux Magots");
        Location polnareffLand = new ResidentialArea("Polnareff Land");
        Location savageGarden = new Restaurant("Savage Garden");
        Location joestarMansion = new ResidentialArea("Joestar Mansion");
        Location vineyard = new ResidentialArea("Vineyard");
        Location diosMansion = new ResidentialArea("DIO's Mansion");
        Location angeloRock = new ResidentialArea("Angelo Rock");
        Location passioneRestaurant = new Restaurant("Passione Restaurant");
        location.add(townHall);
        location.add(moriohGrandHotel);
        location.add(trattoriaTrussardi);
        location.add(greenDolphinStreetPrison);
        location.add(greenDolphinStreetPrison);
        location.add(jadeGarden);
        location.add(sanGiorgioMaggiore);
        location.add(libeccio);
        location.add(cafeDeuxMagots);
        location.add(polnareffLand);
        location.add(savageGarden);
        location.add(joestarMansion);
        location.add(vineyard);
        location.add(diosMansion);
        location.add(angeloRock);
        location.add((passioneRestaurant));
    }

    public static Location getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(Location currentLocation) {
        JOJOLands.currentLocation = currentLocation;
    }

    public static Location getForwardHistory() {
        return forwardHistory;
    }

    public static void setForwardHistory(Location history) {
        JOJOLands.forwardHistory = history;
    }

    public static int getDay() {
        return day;
    }

    public static void setDay(int day) {
        JOJOLands.day = day;
    }

    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        JOJOLands.map = map;
    }

    public static LinkedList<Location> getPathway() {
        return pathway;
    }

    public static void setPathway(LinkedList<Location> pathway) {
        JOJOLands.pathway = pathway;
    }

    public static LinkedList<Location> getLocation() {
        return location;
    }

    public static void setLocation(LinkedList<Location> location) {
        JOJOLands.location = location;
    }

    public static boolean getCheckIsMoving() {
        return checkIsMoving;
    }

    public static void setCheckIsMoving(boolean checkIsMoving) {
        JOJOLands.checkIsMoving = checkIsMoving;
    }

    public static boolean getCheckIsBack() {
        return checkIsBack;
    }

    public static void setCheckIsBack(boolean checkIsBack) {
        JOJOLands.checkIsBack = checkIsBack;
    }

    public static Order getOrder() {
        return order;
    }

    public static void setExit(boolean exit) {
        JOJOLands.exit = exit;
    }

    public static FoodMenu getFoodMenu() {
        return foodMenu;
    }

}
