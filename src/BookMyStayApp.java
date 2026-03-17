import java.util.*;

// Main Application
public class BookMyStayApp {

    // ------------------ CUSTOM EXCEPTION ------------------
    static class CancellationException extends Exception {
        public CancellationException(String message) {
            super(message);
        }
    }

    // ------------------ INVENTORY ------------------
    static class InventoryService {
        private Map<String, Integer> inventory = new HashMap<>();

        public void addRoomType(String type, int count) {
            inventory.put(type, count);
        }

        public boolean isAvailable(String type) {
            return inventory.getOrDefault(type, 0) > 0;
        }

        public void decrement(String type) {
            inventory.put(type, inventory.get(type) - 1);
        }

        public void increment(String type) {
            inventory.put(type, inventory.getOrDefault(type, 0) + 1);
        }

        public void printInventory() {
            System.out.println("Inventory: " + inventory);
        }
    }

    // ------------------ BOOKING SERVICE ------------------
    static class BookingService {

        private InventoryService inventory;

        // reservationId -> roomType
        private Map<String, String> reservations = new HashMap<>();

        public BookingService(InventoryService inventory) {
            this.inventory = inventory;
        }

        public String bookRoom(String roomType) {
            if (!inventory.isAvailable(roomType)) {
                System.out.println(" No rooms available");
                return null;
            }

            String reservationId = generateId(roomType);

            inventory.decrement(roomType);
            reservations.put(reservationId, roomType);

            System.out.println(" Booked: " + reservationId);
            return reservationId;
        }

        public boolean exists(String reservationId) {
            return reservations.containsKey(reservationId);
        }

        public String removeReservation(String reservationId) {
            return reservations.remove(reservationId);
        }

        private String generateId(String type) {
            return type + "-" + UUID.randomUUID().toString().substring(0, 5);
        }
    }

    // ------------------ CANCELLATION SERVICE ------------------
    static class CancellationService {

        private BookingService bookingService;
        private InventoryService inventory;

        // Stack for rollback tracking (LIFO)
        private Stack<String> rollbackStack = new Stack<>();

        public CancellationService(BookingService bookingService, InventoryService inventory) {
            this.bookingService = bookingService;
            this.inventory = inventory;
        }

        public void cancel(String reservationId) {

            try {

                if (reservationId == null || !bookingService.exists(reservationId)) {
                    throw new CancellationException("Invalid or already cancelled reservation");
                }

                String roomType = bookingService.removeReservation(reservationId);

                rollbackStack.push(reservationId);

                inventory.increment(roomType);

                System.out.println(" Cancelled: " + reservationId);

            } catch (CancellationException e) {
                System.out.println(" Cancellation failed: " + e.getMessage());
            }
        }

        public void printRollbackStack() {
            System.out.println("Rollback Stack: " + rollbackStack);
        }
    }

    // ------------------ MAIN ------------------
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("DELUXE", 1);

        BookingService bookingService = new BookingService(inventory);
        CancellationService cancellationService =
                new CancellationService(bookingService, inventory);

        // Book a room
        String res1 = bookingService.bookRoom("DELUXE");

        inventory.printInventory();

        // Cancel booking
        cancellationService.cancel(res1);

        inventory.printInventory();

        // Try cancelling again (should fail)
        cancellationService.cancel(res1);

        // Invalid ID
        cancellationService.cancel("INVALID-123");

        // Check rollback stack
        cancellationService.printRollbackStack();
    }
}