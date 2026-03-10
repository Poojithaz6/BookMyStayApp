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

    public String getRoomType() { return roomType; }
    public int getBeds() { return beds; }
    public double getSize() { return size; }
    public double getPrice() { return price; }

    // Display details in line-by-line format
    public void displayRoomDetailsLineByLine(int availableCount) {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
        System.out.println("Available: " + availableCount);
        System.out.println("------------------------");
    }
}

// Concrete Room types
class SingleRoom extends Room {
    SingleRoom() { super("Single Room", 1, 200, 80); }
}

class DoubleRoom extends Room {
    DoubleRoom() { super("Double Room", 2, 350, 120); }
}

class SuiteRoom extends Room {
    SuiteRoom() { super("Suite Room", 3, 600, 250); }
}

// Centralized inventory management
class RoomInventory {
    private Map<String, Integer> inventory;

    RoomInventory() { inventory = new HashMap<>(); }

    public void addRoomType(Room room, int availableCount) {
        inventory.put(room.getRoomType(), availableCount);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// Search service for guests – read-only access
class RoomSearchService {
    private RoomInventory inventory;

    RoomSearchService(RoomInventory inventory) { this.inventory = inventory; }

    // Display all rooms, even if availability is 0
    public void displayAvailableRooms(Room[] rooms) {
        System.out.println("\nAvailable Rooms:");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());
            room.displayRoomDetailsLineByLine(available);
        }
    }
}

// Main application class
class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System (Guest Search Demo)\n");

        // Create room objects
        Room single = new SingleRoom();
        Room doubleR = new DoubleRoom();
        Room suite = new SuiteRoom();
        Room[] rooms = { single, doubleR, suite };

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single, 5);
        inventory.addRoomType(doubleR, 3); // now double room has availability
        inventory.addRoomType(suite, 2);

        // Guest search
        RoomSearchService searchService = new RoomSearchService(inventory);
        searchService.displayAvailableRooms(rooms);

    }
}