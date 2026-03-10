import java.util.LinkedList;
import java.util.Queue;

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

    public void displayRoomDetailsLineByLine() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
        System.out.println("------------------------");
    }
}

// Concrete Room types
class SingleRoom extends Room { SingleRoom() { super("Single Room", 1, 200, 80); } }
class DoubleRoom extends Room { DoubleRoom() { super("Double Room", 2, 350, 120); } }
class SuiteRoom extends Room { SuiteRoom() { super("Suite Room", 3, 600, 250); } }

// Represents a guest booking request
class Reservation {
    private String guestName;
    private String requestedRoomType;

    public Reservation(String guestName, String requestedRoomType) {
        this.guestName = guestName;
        this.requestedRoomType = requestedRoomType;
    }

    public String getGuestName() { return guestName; }
    public String getRequestedRoomType() { return requestedRoomType; }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + requestedRoomType);
    }
}

// Booking Request Queue – handles intake of reservations
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add reservation to the queue (FIFO)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Received booking request from " + reservation.getGuestName());
    }

    // Peek at the next reservation without removing
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Poll (remove) the next reservation – used by allocation system later
    public Reservation pollNextRequest() {
        return requestQueue.poll();
    }

    // Display all pending requests
    public void displayQueue() {
        System.out.println("\nPending Booking Requests (in order of arrival):");
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }
        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
        System.out.println("------------------------");
    }
}

// Main application class
class HotelBookingApp {
    public static void main(String[] args) {

        System.out.println("Welcome to the Hotel Booking System (Booking Queue Demo)\n");

        // Initialize rooms
        Room[] rooms = { new SingleRoom(), new DoubleRoom(), new SuiteRoom() };
        System.out.println("Available Room Types:");
        for (Room room : rooms) { room.displayRoomDetailsLineByLine(); }

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double Room"));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room"));

        // Display pending requests in order
        bookingQueue.displayQueue();

    }
}