import java.util.*;
import java.util.*;

public class BookMyStayApp {

    static class BookingRequest {
        String userId;
        String roomType;

        public BookingRequest(String userId, String roomType) {
            this.userId = userId;
            this.roomType = roomType;
        }
    }

    static class InventoryService {
        private Map<String, Integer> inventory = new HashMap<>();

        public InventoryService() {
            inventory.put("DELUXE", 1);
        }

        public synchronized boolean allocateRoom(String roomType) {
            int available = inventory.getOrDefault(roomType, 0);

            if (available <= 0) {
                return false;
            }

            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(roomType, available - 1);
            return true;
        }

        public void printInventory() {
            System.out.println("Final Inventory: " + inventory);
        }
    }

    static class BookingProcessor {

        private Queue<BookingRequest> queue = new LinkedList<>();
        private InventoryService inventory;

        public BookingProcessor(InventoryService inventory) {
            this.inventory = inventory;
        }

        public synchronized void addRequest(BookingRequest request) {
            queue.offer(request);
        }

        public synchronized BookingRequest getNextRequest() {
            return queue.poll();
        }


        public void processBookings() {
            while (true) {
                BookingRequest req;


                synchronized (this) {
                    if (queue.isEmpty()) break;
                    req = queue.poll();
                }

                boolean success = inventory.allocateRoom(req.roomType);

                if (success) {
                    System.out.println( req.userId + " booked " + req.roomType);
                } else {
                    System.out.println( req.userId + " failed (no rooms)");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        InventoryService inventory = new InventoryService();
        BookingProcessor processor = new BookingProcessor(inventory);

        processor.addRequest(new BookingRequest("User1", "DELUXE"));
        processor.addRequest(new BookingRequest("User2", "DELUXE"));
        processor.addRequest(new BookingRequest("User3", "DELUXE"));


        Thread t1 = new Thread(processor::processBookings);
        Thread t2 = new Thread(processor::processBookings);
        Thread t3 = new Thread(processor::processBookings);


        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        inventory.printInventory();
    }
}}