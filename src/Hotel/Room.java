package Hotel;

public class Room {
    private int roomNumber;
    private String roomType;
    private double priceForNight;
    private double cancellationFee;

    public Room(int roomNumber, String type, double priceForNight, double cancellationFee) {
        this.roomNumber = roomNumber;
        this.roomType = type;
        this.priceForNight = priceForNight;
        this.cancellationFee = cancellationFee;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPriceForNight() {
        return priceForNight;
    }

    public double getCancellationFee() {
        return cancellationFee;
    }



}
