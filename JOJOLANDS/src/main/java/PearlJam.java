import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PearlJam {
    private String restaurantName;
    private LinkedList<Customer> waitingList;
    private LinkedList<Customer> orderProcessingList;
    private static String headerFormat = "+----+-----------------------+-----+--------+-----------------------------------------+";
    private static String rowFormat1 = "| %-3s| %-22s| %-4s| %-7s| %-40s| %n";

    public PearlJam(String restaurantName) {
        this.restaurantName = restaurantName;
        waitingList = new LinkedList<>();
        try (Scanner reader = new Scanner(new FileInputStream("diningList.txt"))) {
            reader.nextLine();
            while (reader.hasNextLine()) {
                String waitingCustomer = reader.nextLine();
                String[] dataWaitingCustomer = waitingCustomer.split(",");
                if (dataWaitingCustomer[3].equalsIgnoreCase(restaurantName)) {
                    Customer customer = new Customer(dataWaitingCustomer[0], dataWaitingCustomer[1], dataWaitingCustomer[2], dataWaitingCustomer[4]);
                    waitingList.add(customer);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        orderProcessingList = new LinkedList<>();
    }

    public void viewWaitingList() {
        System.out.println("======================================================================");
        System.out.println("Restaurant: " + restaurantName);
        System.out.println("\nWaiting List");
        System.out.println(headerFormat);
        System.out.printf(rowFormat1, "No", "Name", "Age", "Gender", "Order");
        System.out.println(headerFormat);
        int orderNumber = 1;
        for (Customer customer : waitingList) {
            System.out.printf(rowFormat1, orderNumber, customer.getName(), customer.getAge(), customer.getGender(), customer.getOrder());
            orderNumber++;
        }
        System.out.println(headerFormat);
    }


    public void viewOrderProcessingList() {
        System.out.println("\nOrder Processing List");
        System.out.println(headerFormat);
        System.out.printf(rowFormat1, "No", "Name", "Age", "Gender", "Order");
        System.out.println(headerFormat);

        switch (restaurantName) {
            case "Jade Garden":
                while (!waitingList.isEmpty()) {
                    orderProcessingList.add(waitingList.pollFirst());
                    if (!waitingList.isEmpty()) {
                        orderProcessingList.add(waitingList.pollLast());
                    }
                }
                break;

            case "Cafe Deux Magots":
                LinkedList<Customer> NAOrderProcessList = new LinkedList<>();
                LinkedList<Customer> ageOrderProcessList = new LinkedList<>();

                while (!waitingList.isEmpty()) {
                    if (waitingList.getFirst().getAge().equals("N/A")) {
                        NAOrderProcessList.add(waitingList.poll());
                    } else {
                        ageOrderProcessList.offer(waitingList.poll());
                    }
                }

                Collections.sort(ageOrderProcessList, new AgeComparator());


                while (!ageOrderProcessList.isEmpty()) {
                    orderProcessingList.add(ageOrderProcessList.pollLast());
                    if (!ageOrderProcessList.isEmpty()) {
                        orderProcessingList.add(ageOrderProcessList.pollFirst());
                    }
                }
                orderProcessingList.addAll(NAOrderProcessList);
                break;


            case "Trattoria Trussardi":
                List<Customer> maleQueue = new LinkedList<>();
                List<Customer> femaleQueue = new LinkedList<>();
                List<Customer> zeroQueue = new LinkedList<>();
                for (Customer customer : waitingList) {
                    if (customer.getAge().equals("N/A")) {
                        zeroQueue.add(customer);
                    } else if (customer.getGender().equalsIgnoreCase("Male")) {
                        maleQueue.add(customer);
                    } else {
                        femaleQueue.add(customer);
                    }
                }
                Collections.sort(maleQueue, new AgeComparator());
                Collections.sort(femaleQueue, new AgeComparator());

                //Create a variable to keep track of the turn
                int turn = 0;

                //Loop until both queues are empty
                while (!maleQueue.isEmpty() || !femaleQueue.isEmpty()) {
                    //If it's an even turn, serve the youngest man and then the oldest woman
                    if (turn % 2 == 0) {
                        //Check if the male queue is not empty
                        if (!maleQueue.isEmpty()) {
                            //Remove and print the first element of the male queue
                            Customer maleOrder = maleQueue.remove(0);
                            orderProcessingList.add(maleOrder);
                        }
                        //Check if the female queue is not empty
                        if (!femaleQueue.isEmpty()) {
                            //Remove and print the last element of the female queue
                            Customer femaleOrder = femaleQueue.remove(femaleQueue.size() - 1);
                            orderProcessingList.add(femaleOrder);
                        }

                    } else { //If it's an odd turn, serve the oldest man and then the youngest woman
                        //Check if the male queue is not empty
                        if (!maleQueue.isEmpty()) {
                            //Remove and print the last element of the male queue
                            Customer maleOrder = maleQueue.remove(maleQueue.size() - 1);
                            orderProcessingList.add(maleOrder);
                        }

                        //Check if the female queue is not empty
                        if (!femaleQueue.isEmpty()) {
                            //Remove and print the first element of the female queue
                            Customer femaleOrder = femaleQueue.remove(0);
                            orderProcessingList.add(femaleOrder);
                        }
                    }
                    //Increment the turn by one
                    turn++;
                }
                while (!zeroQueue.isEmpty()) {
                    if (!zeroQueue.isEmpty()) {
                        Customer zeroOrder = zeroQueue.remove(0);
                        orderProcessingList.add(zeroOrder);
                    }
                }
                break;


            case "Libeccio":
                int orderNumber = 1;
                int currentDay = JOJOLands.getDay();
                Stack<Customer> sortedCustomerQueue = new Stack<>();
                for (Customer customer : waitingList) {
                    if (orderNumber % currentDay == 0) {
                        sortedCustomerQueue.push(customer);
                    } else {
                        orderProcessingList.add(customer);
                    }
                    orderNumber++;
                }

                while (!sortedCustomerQueue.isEmpty()) {
                    orderProcessingList.add(sortedCustomerQueue.pop());
                }
                break;

            case "Savage Garden":
                List<Customer> serveFirst = new ArrayList<>();
                Stack<Customer> reverseOrderWaitingList = new Stack<>();
                currentDay = JOJOLands.getDay();
                orderNumber = 1;
                for (int i = 0; i < waitingList.size(); i++) {
                    if (orderNumber == currentDay) {
                        serveFirst.add(waitingList.get(i));
                        orderNumber = 1;
                    } else {
                        reverseOrderWaitingList.push(waitingList.get(i));
                        orderNumber++;
                    }
                }

                for (Customer customer : serveFirst) {
                    orderProcessingList.add(customer);
                }
                while (!reverseOrderWaitingList.isEmpty()) {
                    orderProcessingList.add(reverseOrderWaitingList.pop());

                }

                break;

        }

        int orderNumber = 1;
        for (Customer customer : orderProcessingList) {
            System.out.printf(rowFormat1, orderNumber, customer.getName(), customer.getAge(), customer.getGender(), customer.getOrder());
            orderNumber++;
        }
        System.out.println(headerFormat);
    }
}

class Customer {
    private String name;
    private String gender;
    private String age;
    private String Order;

    public Customer(String name, String age, String gender, String order) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.Order = order;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getOrder() {
        return Order;
    }

}

class AgeComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer c1, Customer c2) {
        int age1 = 0;
        int age2 = 0;
        if (!c1.getAge().equals("N/A")) {
            age1 = Integer.parseInt(c1.getAge());
        }
        if (!c2.getAge().equals("N/A")) {
            age2 = Integer.parseInt(c2.getAge());
        }

        return Integer.compare(age1, age2);
    }
}

