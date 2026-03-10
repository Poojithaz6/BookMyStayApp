import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    private String roomType;
    private int beds;
    private double size;
    private double price;

    Room(String roomType, int beds, double size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public void displayRoomDetails() {
        System.out.println(roomType + " | Beds: " + beds + " | Size: " + size + " sq ft | Price: $" + price);
    }
}

// Concrete Room types
class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 200, 80);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 350, 120);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 600, 250);
    }
}

// Centralized inventory management
class RoomInventory {

    private Map<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a room type with initial availability
    public void addRoomType(Room room, int availableCount) {
        inventory.put(room.getRoomType(), availableCount);
    }

    // Get current availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability by a delta (positive or negative)
    public void updateAvailability(String roomType, int delta) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + delta);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " available");
        }
    }
}

// Main application class
class HotelBookingApp {
    public static void main(String[] args) {

        System.out.println("Welcome to the Hotel Booking System (Inventory Demo)\n");

        // Create room objects
        Room single = new SingleRoom();
        Room doubleR = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Display room details
        System.out.println("Room Details:");
        single.displayRoomDetails();
        doubleR.displayRoomDetails();
        suite.displayRoomDetails();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single, 5);
        inventory.addRoomType(doubleR, 3);
        inventory.addRoomType(suite, 2);

        // Display initial inventory
        inventory.displayInventory();

        // Example updates
        System.out.println("\nBooking 1 Single Room and 1 Suite Room...");
        inventory.updateAvailability(single.getRoomType(), -1);
        inventory.updateAvailability(suite.getRoomType(), -1);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated.");
    }
}