package Hotel;

import java.io.Serializable;

public class RoomType implements Serializable {
    private final String name; // Room name, e.g. Deluxe, Suite, Single
    private final String amenities;
    private final int maxOccupancy;
    private final double priceForNight;
    private final double cancellationFee;
    private final int  numberOfRooms;

    public RoomType(String name, String amenities, int maxOccupancy, double priceForNight, double cancellationFee, int numberOfRooms) {
        this.name = name;
        this.amenities = amenities;
        this.maxOccupancy = maxOccupancy;
        this.priceForNight = priceForNight;
        this.cancellationFee = cancellationFee;
        this.numberOfRooms = numberOfRooms;
    }

    public String getName() {
        return name;
    }

    public String getAmenities() {
        return amenities;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public double getPriceForNight() {
        return priceForNight;
    }

    public double getCancellationFee() {
        return cancellationFee;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }
}
