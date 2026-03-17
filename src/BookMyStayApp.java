import java.util.*;

// Main Application Class
public class HotelBookingApp {

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

        public void printInventory() {
            System.out.println("Remaining Inventory: " + roomInventory);
        }
    }

    // ------------------ BOOKING SERVICE ------------------
    static class BookingService {

        private Queue<BookingRequest> requestQueue = new LinkedList<>();
        private Map<String, Set<String>> allocatedRooms = new HashMap<>();
        private InventoryService inventoryService;

        public BookingService(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
        }

        public void addRequest(BookingRequest request) {
            requestQueue.offer(request);
        }

        public void processNextBooking() {
            if (requestQueue.isEmpty()) {
                System.out.println("No booking requests.");
                return;
            }

            BookingRequest request = requestQueue.poll();
            String roomType = request.getRoomType();

            // Step 1: Check availability
            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("❌ No rooms available for type: " + roomType);
                return;
            }

            // Step 2: Generate unique room ID
            String roomId = generateRoomId(roomType);

            // Step 3: Ensure uniqueness using Set
            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            Set<String> rooms = allocatedRooms.get(roomType);

            if (rooms.contains(roomId)) {
                System.out.println("⚠ Duplicate room detected! (should not happen)");
                return;
            }

            rooms.add(roomId);

            // Step 4: Update inventory immediately
            inventoryService.decrement(roomType);

            // Step 5: Confirm booking
            System.out.println("✅ Booking Confirmed");
            System.out.println("User: " + request.getUserId());
            System.out.println("Room Type: " + roomType);
            System.out.println("Room ID: " + roomId);
            System.out.println("----------------------------");
        }

        // Unique ID generator
        private String generateRoomId(String roomType) {
            return roomType + "-" + UUID.randomUUID().toString().substring(0, 6);
        }
    }

    // ------------------ MAIN METHOD ------------------
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("DELUXE", 2);
        inventory.addRoomType("STANDARD", 1);

        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO order)
        bookingService.addRequest(new BookingRequest("User1", "DELUXE"));
        bookingService.addRequest(new BookingRequest("User2", "DELUXE"));
        bookingService.addRequest(new BookingRequest("User3", "DELUXE")); // should fail
        bookingService.addRequest(new BookingRequest("User4", "STANDARD"));

        // Process requests
        bookingService.processNextBooking();
        bookingService.processNextBooking();
        bookingService.processNextBooking();
        bookingService.processNextBooking();

        // Final inventory
        inventory.printInventory();
    }
}