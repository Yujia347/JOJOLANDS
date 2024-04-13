import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Order {
    private final Random random;
    private HashMap<String, LinkedList<Food>> orderHistory = new HashMap<>();
    private HashMap<Integer, HashMap<String, LinkedList<String>>> salesHistory = new HashMap<>();
    private LinkedList<Food> orderList;
    private LinkedList<Food> menuList;
    private final LinkedList<String> nameList;

    private final LinkedList<Food> currentRestaurantFood;
    private double budget;
    private int countNumVisitTrattoriaTrussardi;
    private String restaurantJotaroKujoVisitSat;

    public Order() {
        this.menuList = JOJOLands.getFoodMenu().getMenuList();
        this.random = new Random();
        this.orderHistory = new HashMap<>();
        this.nameList = new LinkedList<>();

        try {
            Scanner inputStream = new Scanner(new FileInputStream("residents.csv"));
            inputStream.nextLine();
            while (inputStream.hasNextLine()) {
                String[] input = inputStream.nextLine().split(",");
                String name = input[0] + "," + input[1] + "," + input[2];
                this.nameList.add(name);
                orderList = new LinkedList<>();
                orderHistory.put(input[0], orderList);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Check whether Jotaro try every dish in this restaurant before moving to another restaurant
        currentRestaurantFood = new LinkedList<>();

        //Check Josuke Higashikata budget
        budget = 0;

        //Count the number of Giorno Giovanna visit Trattoria Trussardi
        countNumVisitTrattoriaTrussardi = 0;

        //Common destination where Jolyne Cujoh and Jotaro Kujo to visit on sat
        restaurantJotaroKujoVisitSat = null;
    }

    public void orderFood() {
        this.menuList = JOJOLands.getFoodMenu().getMenuList();
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("diningList.txt"));
            out.append("Name, Age, Gender, Restaurant, Order, Price\n");

            for (int i = 0; i < nameList.size(); i++) {
                String[] data = nameList.get(i).split(",");
                String name = data[0];
                if (name.equals("Jotaro Kujo")) {
                    Food order = setOrder("Jotaro Kujo");
                    out.append(nameList.get(i) + "," + order.getFoodLocation() + "," + order.getFoodName() + "," + order.getPrice() + "\n");
                    orderHistory.get("Jotaro Kujo").add(order);
                }
            }

            for (int i = 0; i < nameList.size(); i++) {
                String[] data = nameList.get(i).split(",");
                String name = data[0];
                if (!name.equals("Jotaro Kujo")) {
                    Food order = setOrder(name);
                    out.append(nameList.get(i) + "," + order.getFoodLocation() + "," + order.getFoodName() + "," + order.getPrice() + "\n");
                    orderHistory.get(name).add(order);
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordSalesHistory() {
        HashMap<String, LinkedList<String>> sales = new HashMap<>();
        String[] restaurant = {"Jade Garden", "Cafe Deux Magots", "Trattoria Trussardi", "Libeccio", "Savage Garden"};
        for (String str : restaurant) {
            LinkedList<String> list = new LinkedList<>();
            sales.put(str, list);
        }
        try (Scanner sc = new Scanner(new FileInputStream("diningList.txt"))) {
            sc.nextLine();
            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                String[] dataDiningList = input.split(",");
                String restaurantName = dataDiningList[3];
                sales.get(restaurantName).add(dataDiningList[4] + "," + dataDiningList[5]);
            }
            this.salesHistory.put(JOJOLands.getDay(), sales);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public Food setOrder(String name) {
        int num = -1;
        switch (name) {
            case "Jonathan Joestar":
                HashMap<Food, Integer> frequencyOfEachFood = new HashMap<>();
                for (Food food : menuList) {
                    frequencyOfEachFood.put(food, 0); //Initialise all frequency of food as 0
                }
                for (Food food : orderHistory.get("Jonathan Joestar")) {
                    frequencyOfEachFood.put(food, frequencyOfEachFood.get(food) + 1);
                }

                boolean checkFrequency = false;
                while (!checkFrequency) {
                    num = random.nextInt(menuList.size());
                    Food newOrder = menuList.get(num);
                    frequencyOfEachFood.put(newOrder, frequencyOfEachFood.get(newOrder) + 1);

                    int maxFrequency = Integer.MIN_VALUE;
                    int minFrequency = Integer.MAX_VALUE;

                    for (int frequency : frequencyOfEachFood.values()) {
                        maxFrequency = Math.max(maxFrequency, frequency);
                        minFrequency = Math.min(minFrequency, frequency);
                    }
                    if (maxFrequency - minFrequency <= 1) {
                        checkFrequency = true;
                    } else {
                        //this food is eating too frequently, this new order is not acceptable and removed, continue the loop and generate a new food
                        frequencyOfEachFood.put(newOrder, frequencyOfEachFood.get(newOrder) - 1);
                    }

                }
                return menuList.get(num);

            case "Joseph Joestar":
                boolean[] sameFood = new boolean[menuList.size()];
                for (Food food : orderHistory.get("Joseph Joestar")) {
                    sameFood[menuList.indexOf(food)] = true;
                    // Check if he tried all food then it will reset
                    boolean allTrue = true;
                    for (boolean element : sameFood) {
                        if (!element) {
                            allTrue = false;
                            break;
                        }
                    }
                    if (allTrue) {
                        for (int i = 0; i < sameFood.length; i++) {
                            sameFood[i] = false;
                        }
                    }
                }

                //Initiallize status as false if the condition is pass the while loop will stop
                boolean status = false;
                while (!status) {
                    status = true;
                    num = random.nextInt(menuList.size());
                    if (sameFood[num]) {
                        status = false;
                    }
                    sameFood[num] = true;
                }
                return menuList.get(num);

            case "Jotaro Kujo":
                Food order;
                String[] restaurant = {"Jade Garden", "Cafe Deux Magots", "Trattoria Trussardi", "Libeccio", "Savage Garden"};
                //Check if he try every dish at his current restaurent, if yes it will move to another restaurant
                if (currentRestaurantFood.isEmpty()) {
                    String currentRestaurant = restaurant[random.nextInt(5)];
                    for (int i = 1; i < menuList.size(); i++) {
                        Food menu = menuList.get(i);
                        if (menu.getFoodLocation().equals(currentRestaurant)) {
                            currentRestaurantFood.add(menuList.get(i));
                        }
                    }
                    num = random.nextInt(currentRestaurantFood.size());
                    order = currentRestaurantFood.get(num);
                    currentRestaurantFood.remove(num);
                } else {
                    num = random.nextInt(currentRestaurantFood.size());
                    order = currentRestaurantFood.get(num);
                    currentRestaurantFood.remove(num);
                }
                if (JOJOLands.getDay() % 7 == 0) {
                    restaurantJotaroKujoVisitSat = order.getFoodLocation();
                }
                return order;


            case "Josuke Higashikata":
                if (JOJOLands.getDay() % 7 == 1)
                    budget = 0; // Reset budget on new week
                //Set min to 100 first
                double min = 100;
                //if over budget he will purchase the food with min price
                if (budget > 100) {
                    //find the food with the min price
                    for (int i = 1; i < menuList.size(); i++) {
                        Food menu = this.menuList.get(i);
                        double price = menu.getPrice();
                        if (price < min) {
                            min = price;
                        }
                    }
                    //Search for the food with min price
                    for (int i = 1; i < menuList.size(); i++) {
                        Food menu = menuList.get(i);
                        double price = menu.getPrice();
                        if (min == price) {
                            num = i;
                        }
                    }
                } else {
                    num = random.nextInt(menuList.size()); // Choose random food from menu
                    Food rest = menuList.get(num);
                    double cost = rest.getPrice(); // Convert price from string to integer
                    budget += cost;
                }
                return menuList.get(num);


            case "Giorno Giovanna":
                if (JOJOLands.getDay() % 7 == 1)
                    countNumVisitTrattoriaTrussardi = 0; // Reset week
                //if until the last second day of the week, GG still not visiting TT two times, GG must visit it
                Food newOrder = null;
                if ((JOJOLands.getDay() % 7 == 0 || JOJOLands.getDay() % 7 == 6) && countNumVisitTrattoriaTrussardi < 2) {
                    LinkedList<Food> menuListTrattoriaTrussardi = new LinkedList<>();
                    for (Food food : menuList) {
                        if (food.getFoodLocation().equals("Trattoria Trussardi")) {
                            menuListTrattoriaTrussardi.add(food);
                        }
                    }
                    //if only one food at TT, GG will take this dsih
                    if (menuListTrattoriaTrussardi.size() == 1) {
                        newOrder = menuListTrattoriaTrussardi.get(0);
                        countNumVisitTrattoriaTrussardi++;
                    } else {
                        //check whether the dish is different from his last visit
                        boolean differentDish = false;
                        while (!differentDish) {
                            int rand = random.nextInt(menuListTrattoriaTrussardi.size());
                            newOrder = menuListTrattoriaTrussardi.get(rand);
                            Food lastVisit = null;
                            for (int i = orderHistory.get("Giorno Giovanna").size() - 1; i >= 0; i--) {
                                if (orderHistory.get("Giorno Giovanna").get(i).getFoodLocation().equals("Trattoria Trussardi")) {
                                    lastVisit = orderHistory.get("Giorno Giovanna").get(i);
                                    break;
                                }
                            }
                            if (!newOrder.equals(lastVisit)) {
                                differentDish = true;
                            }
                        }
                        countNumVisitTrattoriaTrussardi++;
                    }
                } else {
                    boolean differentDish = false;
                    while (!differentDish) {
                        num = random.nextInt(menuList.size());
                        newOrder = menuList.get(num);
                        Food lastVisit = null;
                        for (int i = orderHistory.get("Giorno Giovanna").size() - 1; i >= 0; i--) {
                            if (orderHistory.get("Giorno Giovanna").get(i).getFoodLocation().equals(newOrder.getFoodLocation())) {
                                lastVisit = orderHistory.get("Giorno Giovanna").get(i);
                                break;
                            }
                        }
                        if (!newOrder.equals(lastVisit)) {
                            differentDish = true;
                        }
                    }
                    if (newOrder.getFoodLocation().equals("Trattoria Trussardi")) {
                        countNumVisitTrattoriaTrussardi++;
                    }
                }
                return newOrder;

            case "Jolyne Cujoh":
                if (JOJOLands.getDay() % 7 == 0) {
                    status = false;
                    while (!status) {
                        num = random.nextInt(menuList.size());
                        Food menu = menuList.get(num);
                        if (menu.getFoodLocation().equals(restaurantJotaroKujoVisitSat)) {
                            status = true;
                        }
                    }
                } else {
                    status = false;
                    while (!status) {
                        num = random.nextInt(menuList.size());
                        Food rest = menuList.get(num);
                        String currentRestaurant = rest.getFoodLocation();
                        String prevRestaurant = null;
                        //if he not visit any restaurant, prevRestaurant will remain null
                        try {
                            Food prev = orderHistory.get("Jolyne Cujoh").getLast();
                            prevRestaurant = prev.getFoodLocation();
                        } catch (NoSuchElementException e) {

                        }
                        if (!currentRestaurant.equals(prevRestaurant)) {
                            status = true;
                        }
                    }
                }
                return menuList.get(num);

            default:
                num = random.nextInt(menuList.size());
                return menuList.get(num);

        }

    }

    public void displayOrderHistory(String name) {
        LinkedList<Food> orderList = orderHistory.get(name);
        for (int i = 0; i < orderList.size(); i++) {
            System.out.printf("| %3d | %-40s | %-23s |\n", i + 1, orderList.get(i).getFoodName(), orderList.get(i).getFoodLocation());
        }
    }

    public HashMap<String, LinkedList<Food>> getOrderHistory() {
        return orderHistory;
    }

    public HashMap<Integer, HashMap<String, LinkedList<String>>> getSalesHistory() {
        return salesHistory;
    }

    public void setOrderHistory(HashMap<String, LinkedList<Food>> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void setSalesHistory(HashMap<Integer, HashMap<String, LinkedList<String>>> salesHistory) {
        this.salesHistory = salesHistory;
    }
}


