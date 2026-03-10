abstract class Room {

    private String roomType;
    private int numberOfBeds;
    private double size;
    private double price;

    public Room(String roomType, int numberOfBeds, double size, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price per night: " + price);
    }
}
class SingleRoom extends Room{

    SingleRoom() {
        super("Single Room", 1, 250, 1500);
    }
}
class DoubleRoom extends Room{

    DoubleRoom() {
        super("Double Room", 2, 400, 2500);
    }
}
class SuiteRoom extends Room{

    SuiteRoom() {
        super("Suite Room", 3, 700, 5000);
    }
}
class BookMyStayApp{

    public static void main(String[] args) {

        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;


        System.out.println("\nSingle Room :");
        singleRoom.displayRoomDetails();
        System.out.println("Available: " + singleRoomAvailable);

        System.out.println("\nDouble Room:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleRoomAvailable);

        System.out.println("\nSuite Room:");
        suiteRoom.displayRoomDetails();
        System.out.println("Available: " + suiteRoomAvailable);

    }
}