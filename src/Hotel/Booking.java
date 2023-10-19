package Hotel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {

    private String bookingNumber;
    private final int roomNumber;
    private final Date inDate;
    private final Date outDate;
    private final User user;

    public Booking(int roomNumber, Date inDate, Date outDate, User user) {
        this.roomNumber = roomNumber;
        this.inDate = inDate;
        this.outDate = outDate;
        this.user = user;
        //method for creating booking number
        this.bookingNumber = createBookingNumber();
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public Date getInDate() {
        return inDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public User getUser() {
        return user;
    }

    //creating unique booking number
    private String createBookingNumber() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMM");
        String inDateString = dateFormat.format(inDate);
        String outDateString = dateFormat.format(outDate);
        bookingNumber = "R" + roomNumber + inDateString + outDateString;
        return bookingNumber;
    }
}
