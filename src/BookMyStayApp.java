import java.util.*;

// Main Application
public class BookMyStayApp {


    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }


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


    static class InventoryService {
        private Map<String, Integer> inventory = new HashMap<>();

        public void addRoomType(String type, int count) {
            inventory.put(type, count);
        }

        public boolean hasRoomType(String type) {
            return inventory.containsKey(type);
        }

        public boolean isAvailable(String type) {
            return inventory.getOrDefault(type, 0) > 0;
        }

        public void decrement(String type) throws InvalidBookingException {
            int current = inventory.getOrDefault(type, 0);

            if (current <= 0) {
                throw new InvalidBookingException("Inventory cannot go below zero for: " + type);
            }

            inventory.put(type, current - 1);
        }
    }


    static class BookingValidator {

        public static void validate(BookingRequest request, InventoryService inventory)
                throws InvalidBookingException {

            if (request == null) {
                throw new InvalidBookingException("Booking request cannot be null");
            }

            if (request.getUserId() == null || request.getUserId().isEmpty()) {
                throw new InvalidBookingException("Invalid user ID");
            }

            if (request.getRoomType() == null || request.getRoomType().isEmpty()) {
                throw new InvalidBookingException("Room type is required");
            }

            if (!inventory.hasRoomType(request.getRoomType())) {
                throw new InvalidBookingException("Room type does not exist: " + request.getRoomType());
            }

            if (!inventory.isAvailable(request.getRoomType())) {
                throw new InvalidBookingException("No rooms available for: " + request.getRoomType());
            }
        }
    }


    static class BookingService {

        private InventoryService inventory;

        public BookingService(InventoryService inventory) {
            this.inventory = inventory;
        }

        public void processBooking(BookingRequest request) {

            try {

                BookingValidator.validate(request, inventory);


                inventory.decrement(request.getRoomType());

                System.out.println(" Booking successful for user: " + request.getUserId());

            } catch (InvalidBookingException e) {

                System.out.println(" Booking failed: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("DELUXE", 1);

        BookingService bookingService = new BookingService(inventory);




        bookingService.processBooking(new BookingRequest("User1", "DELUXE"));


        bookingService.processBooking(new BookingRequest("User2", "SUITE"));


        bookingService.processBooking(new BookingRequest("User3", "DELUXE"));


        bookingService.processBooking(new BookingRequest(null, "DELUXE"));


        bookingService.processBooking(new BookingRequest("User4", ""));
    }
}