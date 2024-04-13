import java.util.*;

public class MoodyBlues {
    private HashMap<Integer, HashMap<String, LinkedList<String>>> salesHistory;
    private HashMap<Integer, Double> totalSalesMap;

    public MoodyBlues() {
        salesHistory = JOJOLands.getOrder().getSalesHistory();
        totalSalesMap = new HashMap<>();
        //Calculate total Sales every day
        for (int i = 1; i <= JOJOLands.getDay(); i++) {
            LinkedList<String> salesInCurrentLocation = salesHistory.get(i).get(JOJOLands.getCurrentLocation().getName());
            LinkedList<String> foodList = new LinkedList<>();
            HashMap<String, Integer> foodQuantity = new HashMap<>();
            HashMap<String, Double> foodPrice = new HashMap<>();
            double totalSales = 0;
            for (String str : salesInCurrentLocation) {
                String[] strings = str.split(",");
                String foodName = strings[0];
                double price = Double.parseDouble(strings[1]);
                foodQuantity.put(foodName, foodQuantity.getOrDefault(foodName, 0) + 1);
                foodPrice.put(foodName, foodPrice.getOrDefault(foodName, 0.0) + price);
                if (!foodList.contains(foodName)) {
                    foodList.add(foodName);
                }
            }

            for (Double price : foodPrice.values()) {
                totalSales += price;
            }

            totalSalesMap.put(i, totalSales);
        }
    }

    public void viewSalesHistory() {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("======================================================================");
            System.out.println("Restaurant: " + JOJOLands.getCurrentLocation().getName());
            System.out.println("Sales Information");
            System.out.print("""
                    [1] View Sales
                    [2] View Aggregated Information
                        [A] Minimum Sales
                        [B] Maximum Sales
                        [C] Top k highest Sales
                        [D] Total and Average Sales
                    [3] Exit

                    Select:  """);

            String input = sc.nextLine();
            switch (input.toLowerCase()) {
                case "1" -> {
                    System.out.print("Enter Day: ");
                    int day = sc.nextInt();
                    viewSales(day);
                    sc.nextLine();
                }
                case "2a" -> viewMinimumSales();
                case "2b" -> viewMaximumSales();
                case "2c" -> {
                    System.out.print("Enter k: ");
                    int k = sc.nextInt();
                    viewTopKHighestSales(k);
                    sc.nextLine();
                }
                case "2d" -> {
                    System.out.print("Enter Start Day: ");
                    int startDay = sc.nextInt();
                    System.out.print("Enter End Day: ");
                    int endDay = sc.nextInt();
                    viewTotalAndAverageSales(startDay, endDay);
                    sc.nextLine();
                }
                case "3" -> exit = true;
            }

        }
    }

    public void viewSales(int day) {
        System.out.println("======================================================================");
        System.out.println("Restaurant: " + JOJOLands.getCurrentLocation().getName());
        System.out.println("Day " + day + " Sales");
        LinkedList<String> salesInCurrentLocation = salesHistory.get(day).get(JOJOLands.getCurrentLocation().getName());
        LinkedList<String> foodList = new LinkedList<>();
        HashMap<String, Integer> foodQuantity = new HashMap<>();
        HashMap<String, Double> foodPrice = new HashMap<>();
        for (String str : salesInCurrentLocation) {
            String[] strings = str.split(",");
            String foodName = strings[0];
            double price = Double.parseDouble(strings[1]);
            foodQuantity.put(foodName, foodQuantity.getOrDefault(foodName, 0) + 1);
            foodPrice.put(foodName, foodPrice.getOrDefault(foodName, 0.0) + price);
            if (!foodList.contains(foodName)) {
                foodList.add(foodName);
            }
        }
        Collections.sort(foodList);

        System.out.println("+------------------------------------------+------------+--------------+");
        System.out.printf("| %-40s | %10s | %12s |\n", "Food Name", "Quantity", "Total Price");
        System.out.println("+------------------------------------------+------------+--------------+");

        for (String foodname : foodList) {
            System.out.printf("| %-40s | %10d | %12s |\n", foodname, foodQuantity.get(foodname), String.format("$%.2f", foodPrice.get(foodname)));
        }

        System.out.println("+------------------------------------------+------------+--------------+");

        double totalSales = foodPrice.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.printf("|%55s| %13s|\n", "Total Sales ", String.format("$%.2f", totalSales));
        System.out.println("+------------------------------------------+------------+--------------+");
    }

    public void viewTotalAndAverageSales(int startDay, int endDay) {
        System.out.println("======================================================================");
        System.out.println("Restaurant: " + JOJOLands.getCurrentLocation().getName());
        System.out.println("Total and Average Sales (" + startDay + "-" + endDay + ") Sales");
        LinkedList<String> foodList = new LinkedList<>();
        HashMap<String, Integer> foodQuantity = new HashMap<>();
        HashMap<String, Double> foodPrice = new HashMap<>();
        double totalSales = 0;
        for (int i = startDay; i <= endDay; i++) {
            LinkedList<String> salesInCurrentLocation = salesHistory.get(i).get(JOJOLands.getCurrentLocation().getName());
            for (String str : salesInCurrentLocation) {
                String[] strings = str.split(",");
                String foodName = strings[0];
                double price = Double.parseDouble(strings[1]);
                foodQuantity.put(foodName, foodQuantity.getOrDefault(foodName, 0) + 1);
                foodPrice.put(foodName, foodPrice.getOrDefault(foodName, 0.0) + price);
                if (!foodList.contains(foodName)) {
                    foodList.add(foodName);
                }
            }
        }
        Collections.sort(foodList);
        for (Double price : foodPrice.values()) {
            totalSales += price;
        }
        System.out.println("+------------------------------------------+------------+--------------+");
        System.out.printf("| %-40s | %10s | %12s |\n", "Food Name", "Quantity", "Total Price");
        System.out.println("+------------------------------------------+------------+--------------+");
        for (String foodname : foodList) {
            System.out.printf("| %-40s | %10d | %12s |\n", foodname, foodQuantity.get(foodname), String.format("$%.2f", foodPrice.get(foodname)));
        }

        System.out.println("+------------------------------------------+------------+--------------+");

        double averageSales = totalSales / (endDay - startDay + 1);
        System.out.printf("|%55s| %13s|\n", "Total Sales ", String.format("$%.2f", totalSales));
        System.out.printf("|%55s| %13s|\n", "Average Sales ", String.format("$%.2f", averageSales));
        System.out.println("+------------------------------------------+------------+--------------+");
    }

    public void viewMinimumSales() {
        double minSales = Collections.min(totalSalesMap.values());
        System.out.print("Minimum Sales: " + String.format("$%.2f", minSales) + " (Day ");
        for (int day : totalSalesMap.keySet()) {
            if (totalSalesMap.get(day) == minSales) {
                System.out.print(day);
            }
        }
        System.out.print(" Sales)");
        System.out.println();
    }

    public void viewMaximumSales() {
        double maxSales = Collections.max(totalSalesMap.values());
        System.out.print("Maximum Sales: " + String.format("$%.2f", maxSales) + " (Day ");
        for (int day : totalSalesMap.keySet()) {
            if (totalSalesMap.get(day) == maxSales) {
                System.out.print(day);
            }
        }
        System.out.print(" Sales)");
        System.out.println();
    }

    public void viewTopKHighestSales(int k) {
        List<Double> topKHighestSales = new ArrayList<>(totalSalesMap.values());
        topKHighestSales.sort(Collections.reverseOrder());
        for (int i = 0; i < k && i < topKHighestSales.size(); i++) {
            System.out.print("Top " + (i + 1) + " sales: " + String.format("$%.2f", topKHighestSales.get(i)) + " (Day ");
            for (int day : totalSalesMap.keySet()) {
                if (totalSalesMap.get(day) == topKHighestSales.get(i)) {
                    System.out.print(day);
                }
            }
            System.out.print(" Sales)");
            System.out.println();
        }
    }

    public HashMap<Integer, HashMap<String, LinkedList<String>>> getSalesHistory() {
        return salesHistory;
    }

    public void setSalesHistory(HashMap<Integer, HashMap<String, LinkedList<String>>> salesHistory) {
        this.salesHistory = salesHistory;
    }

}
