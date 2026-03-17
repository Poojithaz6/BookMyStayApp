import java.util.*;

// Main Application
public class BookMyStayApp {

    // ------------------ MODELS ------------------

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

    // Reservation (NEW for history)
    static class Reservation {
        private String reservationId;
        private String userId;
        private String roomType;

        public Reservation(String reservationId, String userId, String roomType) {
            this.reservationId = reservationId;
            this.userId = userId;
            this.roomType = roomType;
        }

        public String getReservationId() { return reservationId; }
        public String getUserId() { return userId; }
        public String getRoomType() { return roomType; }

        @Override
        public String toString() {
            return "ReservationID=" + reservationId +
                    ", User=" + userId +
                    ", RoomType=" + roomType;
        }
    }

    // ------------------ ADD-ON ------------------

    static class AddOnService {
        private String name;
        private double price;

        public AddOnService(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public double getPrice() { return price; }

        @Override
        public String toString() {
            return name + "(₹" + price + ")";
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
    }

    // ------------------ BOOKING HISTORY ------------------

    static class BookingHistory {
        private List<Reservation> history = new ArrayList<>();

        public void addReservation(Reservation reservation) {
            history.add(reservation); // preserves order
        }

        public List<Reservation> getAllReservations() {
            return history;
        }
    }

    // ------------------ BOOKING SERVICE ------------------

    static class BookingService {

        private Queue<BookingRequest> queue = new LinkedList<>();
        private Map<String, Set<String>> allocatedRooms = new HashMap<>();

        private InventoryService inventory;
        private BookingHistory history;

        public BookingService(InventoryService inventory, BookingHistory history) {
            this.inventory = inventory;
            this.history = history;
        }

        public void addRequest(BookingRequest request) {
            queue.offer(request);
        }

        public String processNextBooking() {

            if (queue.isEmpty()) return null;

            BookingRequest req = queue.poll();

            if (!inventory.isAvailable(req.getRoomType())) {
                System.out.println("❌ No rooms available");
                return null;
            }

            String roomId = generateRoomId(req.getRoomType());

            allocatedRooms.putIfAbsent(req.getRoomType(), new HashSet<>());
            allocatedRooms.get(req.getRoomType()).add(roomId);

            inventory.decrement(req.getRoomType());

            // ✅ Create Reservation & store in history
            Reservation reservation = new Reservation(
                    roomId, req.getUserId(), req.getRoomType()
            );

            history.addReservation(reservation);

            System.out.println("✅ Booking Confirmed: " + reservation);

            return roomId;
        }

        private String generateRoomId(String type) {
            return type + "-" + UUID.randomUUID().toString().substring(0, 6);
        }
    }

    // ------------------ ADD-ON MANAGER ------------------

    static class AddOnServiceManager {

        private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

        public void addService(String reservationId, AddOnService service) {
            serviceMap.putIfAbsent(reservationId, new ArrayList<>());
            serviceMap.get(reservationId).add(service);
        }

        public double getTotalCost(String reservationId) {
            double total = 0;
            for (AddOnService s : serviceMap.getOrDefault(reservationId, new ArrayList<>())) {
                total += s.getPrice();
            }
            return total;
        }
    }

    // ------------------ REPORT SERVICE ------------------

    static class BookingReportService {

        public void printAllBookings(List<Reservation> reservations) {
            System.out.println("\n📜 Booking History:");
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }

        public void printSummary(List<Reservation> reservations) {
            System.out.println("\n📊 Summary Report:");

            Map<String, Integer> countByType = new HashMap<>();

            for (Reservation r : reservations) {
                countByType.put(
                        r.getRoomType(),
                        countByType.getOrDefault(r.getRoomType(), 0) + 1
                );
            }

            for (String type : countByType.keySet()) {
                System.out.println(type + " rooms booked: " + countByType.get(type));
            }
        }
    }

    // ------------------ MAIN ------------------

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("DELUXE", 2);
        inventory.addRoomType("STANDARD", 1);

        BookingHistory history = new BookingHistory();

        BookingService bookingService = new BookingService(inventory, history);
        AddOnServiceManager addOnManager = new AddOnServiceManager();
        BookingReportService reportService = new BookingReportService();

        // Add requests
        bookingService.addRequest(new BookingRequest("User1", "DELUXE"));
        bookingService.addRequest(new BookingRequest("User2", "STANDARD"));
        bookingService.addRequest(new BookingRequest("User3", "DELUXE"));

        // Process bookings
        String r1 = bookingService.processNextBooking();
        String r2 = bookingService.processNextBooking();
        String r3 = bookingService.processNextBooking();

        // Add services
        addOnManager.addService(r1, new AddOnService("Breakfast", 500));
        addOnManager.addService(r1, new AddOnService("Spa", 1500));

        // Reports
        reportService.printAllBookings(history.getAllReservations());
        reportService.printSummary(history.getAllReservations());
    }
}