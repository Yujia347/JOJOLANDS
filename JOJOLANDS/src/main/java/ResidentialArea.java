import java.util.*;

public class ResidentialArea extends Location {
    public ResidentialArea(String name) {
        super(name);
    }

    public void addViewResidentInfoOption() {
        listOfFunction.put(listOfFunction.size() + 1, "View Resident Information");
    }

    public void addDirtyDeedsDoneDirtCheap() {
        listOfFunction.put(listOfFunction.size() + 1, "Dirty Deeds Done Dirt Cheap");
    }

    public void addThusSpokeRohanKishibe() {
        listOfFunction.put(listOfFunction.size() + 1, "Thus Spoke Rohan Kishibe");
    }

    public void addVentoAureo() {
        listOfFunction.put(listOfFunction.size() + 1, "Vento Aureo");
    }

    public void addStayTheHellAwayFromMe() {
        listOfFunction.put(listOfFunction.size() + 1, "Stay the Hell Away from Me!");
    }

    public void addAnotherOneBiteTheDust() {
        listOfFunction.put(listOfFunction.size() + 1, "Another One Bites the Dust");
    }

    public void addRedHotChiliPepper() {
        listOfFunction.put(listOfFunction.size() + 1, "Red Hot Chili Pepper");
    }

    public void addTheHand() {
        listOfFunction.put(listOfFunction.size() + 1, "The Hand");
    }

    @Override
    public void displayFunction() {
        listOfFunction = new HashMap<>();
        addMoveToOption();
        addViewResidentInfoOption();
        if (this.getName().equals("Green Dolphin Street Prison")) {
            addDirtyDeedsDoneDirtCheap();
        }
        if (this.getName().equals("Morioh Grand Hotel")) {
            addTheHand();
            addThusSpokeRohanKishibe();

        }
        if (this.getName().equals("Vineyard")) {
            addVentoAureo();
        }
        if (this.getName().equals("San Giorgio Maggiore")) {
            addStayTheHellAwayFromMe();
        }
        if (this.getName().equals("Angelo Rock")) {
            addRedHotChiliPepper();
            addAnotherOneBiteTheDust();
        }
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
                    case "View Resident Information" -> {
                        HeavensDoor heavensDoor = new HeavensDoor();
                        heavensDoor.viewResidentInfo();
                    }
                    case "Back to Town Hall" -> backToTownHall();
                    case ("Back") -> back();
                    case "Forward" -> forward();
                    case "Dirty Deeds Done Dirt Cheap" -> {
                        DirtyDeedsDoneDirtCheap dirty = new DirtyDeedsDoneDirtCheap();
                    }
                    case "Vento Aureo" -> {
                        VentoAureo ventoAureo = new VentoAureo();
                    }
                    case "Thus Spoke Rohan Kishibe" -> {
                        ThusSpokeRohanKishibe tsrk = new ThusSpokeRohanKishibe();
                    }
                    case "Stay the Hell Away from Me!" -> {
                        StayTheHellAwayFromMe stayAway = new StayTheHellAwayFromMe();
                    }
                    case "Another One Bites the Dust" -> {
                        AnotherOneBitesTheDust anotherOneBitesTheDust = new AnotherOneBitesTheDust();
                    }
                    case "Red Hot Chili Pepper" -> {
                        SuperFly redHotChiliPepper = new SuperFly();
                        redHotChiliPepper.redHotChiliPepper();
                    }
                    case "The Hand" -> {
                        SuperFly theHand = new SuperFly();
                        theHand.theHand();
                    }
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
