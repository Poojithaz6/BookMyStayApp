import java.io.*;
import java.util.*;

public class BookMyStayApp {

    static class SystemState implements Serializable {
        private static final long serialVersionUID = 1L;

        Map<String, Integer> inventory;
        List<String> bookings;

        public SystemState(Map<String, Integer> inventory, List<String> bookings) {
            this.inventory = inventory;
            this.bookings = bookings;
        }
    }

    static class InventoryService {
        Map<String, Integer> inventory = new HashMap<>();

        public void addRoomType(String type, int count) {
            inventory.put(type, count);
        }

        public boolean isAvailable(String type) {
            return inventory.getOrDefault(type, 0) > 0;
        }

        public void decrement(String type) {
            inventory.put(type, inventory.get(type) - 1);
        }

        public Map<String, Integer> getInventory() {
            return inventory;
        }

        public void setInventory(Map<String, Integer> data) {
            this.inventory = data;
        }
    }

    static class BookingService {
        List<String> bookings = new ArrayList<>();

        public void book(String roomType, InventoryService inventory) {
            if (!inventory.isAvailable(roomType)) {
                System.out.println("❌ No rooms available");
                return;
            }

            String id = roomType + "-" + UUID.randomUUID().toString().substring(0, 5);
            inventory.decrement(roomType);
            bookings.add(id);

            System.out.println("Booked: " + id);
        }

        public List<String> getBookings() {
            return bookings;
        }

        public void setBookings(List<String> data) {
            this.bookings = data;
        }
    }


    static class PersistenceService {

        private static final String FILE_NAME = "system_state.ser";

        public void save(SystemState state) {
            try (ObjectOutputStream out =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                out.writeObject(state);
                System.out.println("💾 State saved successfully.");

            } catch (IOException e) {
                System.out.println("❌ Failed to save state: " + e.getMessage());
            }
        }

        public SystemState load() {
            try (ObjectInputStream in =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                System.out.println("🔁 State loaded successfully.");
                return (SystemState) in.readObject();

            } catch (FileNotFoundException e) {
                System.out.println("⚠ No previous state found. Starting fresh.");
            } catch (Exception e) {
                System.out.println("⚠ Corrupted state. Starting fresh.");
            }

            return null;
        }
    }


    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService();
        PersistenceService persistence = new PersistenceService();


        SystemState loadedState = persistence.load();

        if (loadedState != null) {
            inventory.setInventory(loadedState.inventory);
            bookingService.setBookings(loadedState.bookings);
        } else {

            inventory.addRoomType("DELUXE", 2);
        }

        bookingService.book("DELUXE", inventory);
        bookingService.book("DELUXE", inventory);


        SystemState state = new SystemState(
                inventory.getInventory(),
                bookingService.getBookings()
        );

        persistence.save(state);


        System.out.println("📦 Inventory: " + inventory.getInventory());
        System.out.println("📜 Bookings: " + bookingService.getBookings());
    }
}