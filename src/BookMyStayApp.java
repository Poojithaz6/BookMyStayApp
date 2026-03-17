import java.util.*;

// Main Application
public class BookMyStayApp {

    // ------------------ MODEL ------------------
    static class BookingRequest {
        private String userId;
        private String roomType;

        public BookingRequest(String userId, String roomType) {
            this.userId = userId;
            this.roomType = roomType;
        }

        public String getUserId() { return userId; }
        public String getRoomType() { return roomType; }
    }

    // ------------------ ADD-ON SERVICE MODEL ------------------
    static class AddOnService {
        private String name;
        private double price;

        public AddOnService(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public String toString() {
            return name + " (₹" + price + ")";
        }
    }

    // ------------------ INVENTORY SERVICE ------------------
    static class InventoryService {
        private Map<String, Integer> roomInventory = new HashMap<>();

        public void addRoomType(String type, int count) {
            roomInventory.put(type, count);
        }

        public boolean isAvailable(String type) {
            return roomInventory.getOrDefault(type, 0) > 0;
        }

        public void decrement(String type) {
            roomInventory.put(type, roomInventory.get(type) - 1);
        }
    }

    // ------------------ BOOKING SERVICE ------------------
    static class BookingService {

        private Queue<BookingRequest> requestQueue = new LinkedList<>();
        private Map<String, Set<String>> allocatedRooms = new HashMap<>();
        private InventoryService inventoryService;

        // Store reservationId (roomId) for later use
        private List<String> confirmedReservations = new ArrayList<>();

        public BookingService(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
        }

        public void addRequest(BookingRequest request) {
            requestQueue.offer(request);
        }

        public String processNextBooking() {
            if (requestQueue.isEmpty()) {
                System.out.println("No booking requests.");
                return null;
            }

            BookingRequest request = requestQueue.poll();
            String roomType = request.getRoomType();

            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("❌ No rooms available for type: " + roomType);
                return null;
            }

            String roomId = generateRoomId(roomType);

            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            Set<String> rooms = allocatedRooms.get(roomType);

            if (rooms.contains(roomId)) {
                System.out.println("⚠ Duplicate room detected!");
                return null;
            }

            rooms.add(roomId);
            inventoryService.decrement(roomType);

            confirmedReservations.add(roomId);

            System.out.println("✅ Booking Confirmed");
            System.out.println("User: " + request.getUserId());
            System.out.println("Room ID (Reservation ID): " + roomId);
            System.out.println("----------------------------");

            return roomId; // important for add-ons
        }

        private String generateRoomId(String roomType) {
            return roomType + "-" + UUID.randomUUID().toString().substring(0, 6);
        }
    }

    // ------------------ ADD-ON SERVICE MANAGER ------------------
    static class AddOnServiceManager {

        // reservationId -> list of services
        private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

        // Add services to a reservation
        public void addService(String reservationId, AddOnService service) {
            serviceMap.putIfAbsent(reservationId, new ArrayList<>());
            serviceMap.get(reservationId).add(service);
        }

        // Get services for a reservation
        public List<AddOnService> getServices(String reservationId) {
            return serviceMap.getOrDefault(reservationId, new ArrayList<>());
        }

        // Calculate total cost
        public double calculateTotalCost(String reservationId) {
            List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
            double total = 0;
            for (AddOnService s : services) {
                total += s.getPrice();
            }
            return total;
        }

        // Print services
        public void printServices(String reservationId) {
            List<AddOnService> services = getServices(reservationId);

            if (services.isEmpty()) {
                System.out.println("No add-on services selected.");
                return;
            }

            System.out.println("Add-On Services for " + reservationId + ":");
            for (AddOnService s : services) {
                System.out.println("- " + s);
            }

            System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
            System.out.println("----------------------------");
        }
    }

    // ------------------ MAIN ------------------
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("DELUXE", 2);

        BookingService bookingService = new BookingService(inventory);
        AddOnServiceManager addOnManager = new AddOnServiceManager();

        // Add booking requests
        bookingService.addRequest(new BookingRequest("User1", "DELUXE"));
        bookingService.addRequest(new BookingRequest("User2", "DELUXE"));

        // Process bookings
        String res1 = bookingService.processNextBooking();
        String res2 = bookingService.processNextBooking();

        // ------------------ ADD-ON USAGE ------------------

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService spa = new AddOnService("Spa", 1500);
        AddOnService pickup = new AddOnService("Airport Pickup", 800);

        // Add services to reservation 1
        addOnManager.addService(res1, breakfast);
        addOnManager.addService(res1, spa);

        // Add services to reservation 2
        addOnManager.addService(res2, pickup);

        // Print details
        addOnManager.printServices(res1);
        addOnManager.printServices(res2);
    }
}