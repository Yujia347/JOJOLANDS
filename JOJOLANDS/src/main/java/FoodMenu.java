import java.util.LinkedList;
import java.util.Scanner;

public class FoodMenu {
    private LinkedList<Food> menuList;

    public FoodMenu() {
        // Default Menu
        menuList = new LinkedList<>();
        Food braisedChickeninBlackBeanSauce = new Food("Jade Garden", "Braised Chicken in Black Bean Sauce", 15.00);
        Food braisedGooseWebwithVermicelli = new Food("Jade Garden", "Braised Goose Web with Vermicelli", 21.00);
        Food deepFriedHiroshimaOysters = new Food("Jade Garden", "Deep-fried Hiroshima Oysters", 17.00);
        Food poachedTofuwithDriedShrimps = new Food("Jade Garden", "Poached Tofu with Dried Shrimps", 12.00);
        Food scrambledEggWhitewithMilk = new Food("Jade Garden", "Scrambled Egg White with Milk", 10.00);
        Food samplingMaturedCheesePlatter = new Food("Cafe Deux Magots", "Sampling Matured Cheese Platter", 23.00);
        Food springLobsterSalad = new Food("Cafe Deux Magots", "Spring Lobster Salad", 35.00);
        Food springOrganicOmelett = new Food("Cafe Deux Magots", "Spring Organic Omelette", 23.00);
        Food truffleflavouredPoultrySupreme = new Food("Cafe Deux Magots", "Truffle-flavoured Poultry Supreme", 34.00);
        Food whiteAsparagus = new Food("Cafe Deux Magots", "White Asparagus", 26.00);
        Food capreseSalad = new Food("Trattoria Trussardi", "Caprese Salad", 10.00);
        Food cremeCaramel = new Food("Trattoria Trussardi", "Creme caramel", 6.50);
        Food lambChopswithAppleSauce = new Food("Trattoria Trussardi", "Lamb Chops with Apple Sauce", 25.00);
        Food spaghettiAllaPuttanesca = new Food("Trattoria Trussardi", "Spaghetti alla Puttanesca", 15.00);
        Food formaggio = new Food("Libeccio", "Formaggio", 12.50);
        Food ghiaccio = new Food("Libeccio", "Ghiaccio", 1.01);
        Food melone = new Food("Libeccio", "Melone", 5.20);
        Food prosciuttoandPesci = new Food("Libeccio", "Prosciutto and Pesci", 20.23);
        Food risotto = new Food("Libeccio", "Risotto", 13.14);
        Food zuccheroandSale = new Food("Libeccio", "Zucchero and Sale", 0.60);
        Food abbacchiosTea = new Food("Savage Garden", "Abbacchio’s Tea", 1.00);
        Food DIOsBread = new Food("Savage Garden", "DIO’s Bread", 36.14);
        Food giornosDonuts = new Food("Savage Garden", "Giorno’s Donuts", 6.66);
        Food josephsTequila = new Food("Savage Garden", "Joseph’s Tequila", 35.00);
        Food kakyoinsCherry = new Food("Savage Garden", "Kakyoin’s Cherry", 3.50);
        Food kakyoinsPorridge = new Food("Savage Garden", "Kakyoin’s Porridge", 4.44);

        menuList.add(braisedChickeninBlackBeanSauce);
        menuList.add(braisedGooseWebwithVermicelli);
        menuList.add(deepFriedHiroshimaOysters);
        menuList.add(poachedTofuwithDriedShrimps);
        menuList.add(scrambledEggWhitewithMilk);
        menuList.add(samplingMaturedCheesePlatter);
        menuList.add(springLobsterSalad);
        menuList.add(springOrganicOmelett);
        menuList.add(truffleflavouredPoultrySupreme);
        menuList.add(whiteAsparagus);
        menuList.add(capreseSalad);
        menuList.add(cremeCaramel);
        menuList.add(lambChopswithAppleSauce);
        menuList.add(spaghettiAllaPuttanesca);
        menuList.add(formaggio);
        menuList.add(ghiaccio);
        menuList.add(melone);
        menuList.add(prosciuttoandPesci);
        menuList.add(risotto);
        menuList.add(zuccheroandSale);
        menuList.add(abbacchiosTea);
        menuList.add(DIOsBread);
        menuList.add(giornosDonuts);
        menuList.add(josephsTequila);
        menuList.add(kakyoinsCherry);
        menuList.add(kakyoinsPorridge);
    }

    public void add(String foodName, double price) {
        Food newFood = new Food(JOJOLands.getCurrentLocation().getName(), foodName, price);
        menuList.add(newFood);
    }

    public boolean remove(String foodName) {
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getFoodName().equals(foodName)) {
                menuList.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean modify(String foodName, double newPrice) {
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getFoodName().equals(foodName)) {
                menuList.get(i).setPrice(newPrice);
                return true;
            }
        }
        return false;
    }

    public void modifyFoodInformation() {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("======================================================================");
            System.out.println("Modify Food Information");
            System.out.print("""
                    [1] Add New Food
                    [2] Remove Existing Food
                    [3] Modify Existing Food
                    [4] Exit

                    Select:  """);
            int selection = sc.nextInt();
            sc.nextLine();

            switch (selection) {
                case 1 -> {
                    System.out.print("Enter new food name: ");
                    String newFoodName = sc.nextLine();
                    System.out.print("Enter new price: $");
                    double newPrice = sc.nextDouble();
                    add(newFoodName, newPrice);
                    System.out.println(newFoodName + " successfully at to the menu!");

                }
                case 2 -> {
                    System.out.print("Enter existing food: ");
                    String removeFoodName = sc.nextLine();
                    if (remove(removeFoodName)) {
                        System.out.println(removeFoodName + " successfully removed from the menu ");
                    } else {
                        System.out.println("No such food to be removed from the menu!");

                    }
                }
                case 3 -> {
                    System.out.print("Enter existing food: ");
                    String modifyFoodName = sc.nextLine();
                    System.out.print("Enter modify price: ");
                    double modifyPrice = sc.nextDouble();
                    if (modify(modifyFoodName, modifyPrice)) {
                        System.out.println(modifyFoodName + " price modified to " + modifyPrice);
                    } else {
                        System.out.println("No such food existing to be modify. You can create a new food!");
                    }

                }
                case 4 -> exit = true;
            }
        }

    }

    public void viewMenuList() {
        System.out.println("======================================================================");
        System.out.println("Restaurant: " + JOJOLands.getCurrentLocation().getName());
        System.out.println("\nFood Menu");
        System.out.println("+-------------------------------------+-----------------+");
        String menuFormat = "| %-36s| %15s |\n";
        System.out.printf(menuFormat, "Menu", "Price ($)");
        System.out.println("+-------------------------------------+-----------------+");
        for (Food food : menuList) {
            if (food.getFoodLocation().equals(JOJOLands.getCurrentLocation().getName())) {
                System.out.printf(menuFormat, food.getFoodName(), String.format("%.2f", food.getPrice()));
            }
        }
        System.out.println("+-------------------------------------+-----------------+");

    }


    public LinkedList<Food> getMenuList() {
        return menuList;
    }

    public void setMenuList(LinkedList<Food> menuList) {
        this.menuList = menuList;
    }

}

class Food {
    private String foodName;
    private double price;
    private String foodLocation;

    public Food(String foodName, double price) {
        this.foodName = foodName;
        this.price = price;
    }

    public Food(String foodLocation, String foodName, double price) {
        this.foodName = foodName;
        this.price = price;
        this.foodLocation = foodLocation;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    public String getFoodLocation() {
        return foodLocation;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return this.getFoodLocation() + "," + this.getPrice() + "," + this.getFoodName();
    }

}

