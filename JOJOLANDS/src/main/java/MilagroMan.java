import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class MilagroMan {
    public MilagroMan() {
        HashMap<Integer, HashMap<String, LinkedList<String>>> salesHistory = JOJOLands.getOrder().getSalesHistory();
        HashMap<Integer, HashMap<String, LinkedList<String>>> originalSalesHistory = new HashMap<>();
        //Clone an Original Sales History
        for (Integer key : salesHistory.keySet()) {
            HashMap<String, LinkedList<String>> restaurantSalesHistory = salesHistory.get(key);
            HashMap<String, LinkedList<String>> clonedRestaurantSalesHistory = new HashMap<>();

            for (String location : restaurantSalesHistory.keySet()) {
                LinkedList<String> foodList = restaurantSalesHistory.get(location);
                LinkedList<String> clonedFoodList = new LinkedList<>(foodList); // Creates a new LinkedList
                clonedRestaurantSalesHistory.put(location, clonedFoodList);
            }
            originalSalesHistory.put(key, clonedRestaurantSalesHistory);
        }

        boolean exit = false;

        while (!exit) {
            System.out.println("======================================================================");
            System.out.println("Restaurant: " + JOJOLands.getCurrentLocation().getName() + " (Milagro Man Mode)");
            System.out.print("""
                    [1] Modify Food Prices
                    [2] View Sales Information
                    [3] Exit Milagro Man

                    Select:  """);
            Scanner sc = new Scanner(System.in);
            int input = sc.nextInt();
            sc.nextLine();
            switch (input) {
                case 1:
                    System.out.print("Enter food name: ");
                    String modifyFoodName = sc.nextLine();
                    System.out.print("Enter new price: $");
                    double newPrice = sc.nextDouble();
                    System.out.print("Enter Start Day: ");
                    int startDay = sc.nextInt();
                    System.out.print("Enter End Day: ");
                    int endDay = sc.nextInt();
                    for (int i = startDay; i <= endDay; i++) {
                        LinkedList<String> foodList = salesHistory.get(i).get(JOJOLands.getCurrentLocation().getName());
                        for (int j = 0; j < foodList.size(); j++) {
                            String[] foodInfo = foodList.get(j).split(",");
                            String foodName = foodInfo[0];
                            if (foodName.equals(modifyFoodName)) {
                                String newFoodInfo = foodName + "," + newPrice;
                                foodList.set(j, newFoodInfo);
                            }
                        }
                    }
                    break;
                case 2:
                    MoodyBlues moodyBlues = new MoodyBlues();
                    moodyBlues.viewSalesHistory();
                    break;
                case 3:
                    exit = true;
                    //restore sales record to original state
                    JOJOLands.getOrder().setSalesHistory(originalSalesHistory);
                    break;
            }
        }
    }
}
