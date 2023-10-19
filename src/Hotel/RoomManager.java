package Hotel;

import java.text.SimpleDateFormat;
import java.util.*;

public class RoomManager {
    private static List<Room> rooms;
    private static List<Booking> allBookings;
    private static List<Booking> allCancelledBookings;
    private static List<User> allUsers;

    public RoomManager() {
        rooms = new ArrayList<>();
        allBookings = new ArrayList<>();
        allCancelledBookings = new ArrayList<>();
        allUsers = new ArrayList<>();
    }

    public static Room getRoom(int number) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == number) {
                return room;
            }
        }
        return null; // no such room
    }

    // find booking by other data
    public static Booking getBooking(int roomNumber, Date inDate, Date outDate, String email) {
        for (Booking booking : allBookings) {
            if (booking.getRoomNumber() == roomNumber && isInDates(booking, inDate, outDate)
                    && booking.getUser().getEmail().equals(email)) {
                return booking;
            }
        }
        return null; // no such booking
    }

    public static List<Room> getRooms() {
        return rooms;
    }

    public static List<Booking> getAllBookings() {
        return allBookings;
    }

    public static List<Booking> getAllCancelledBookings() {
        return allCancelledBookings;
    }

    public static List<User> getAllUsers() {
        return allUsers;
    }

    // show all free rooms for the range of dates
    public List<Room> getFreeRoomsForDates(Date inDate, Date outDate) {
        List<Room> freeRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (isFree(room.getRoomNumber(), inDate, outDate)) {
                freeRooms.add(room);
            }
        }
        return freeRooms;
    }

    // show all bookings for exclusive email
    public List<Booking> myBookings(String email) {
        List<Booking> myBookings = new ArrayList<>();
        for (Booking booking : allBookings) {
            if (booking.getUser().getEmail().equals(email)) {
                myBookings.add(booking);
            }
        }
        return myBookings;
    }

    // return all cancelled bookings for a user
    public List<Booking> myCancelledBookings(String email) {
        List<Booking> myCancelledBookings = new ArrayList<>();
        for (Booking booking : allCancelledBookings) {
            if (booking.getUser().getEmail().equals(email)) {
                myCancelledBookings.add(booking);
            }
        }
        return myCancelledBookings;
    }

    // check if dates in required time period
    public static boolean isInDates(Booking booking, Date firstDate, Date secondDate) {
        return !(firstDate.after(booking.getOutDate()) || secondDate.before(booking.getInDate()));
    }

    // books room, create booking and return the process is finished
    public boolean bookRoom(int roomNumber, Date inDate, Date outDate, User user) {
        Room room = getRoom(roomNumber);
        if (room != null && isFree(roomNumber, inDate, outDate)) {
            Booking booking = new Booking(roomNumber, inDate, outDate, user);
            allBookings.add(booking);
            return true;
        }
        return false;
    }

    //void for searching and cancelling booking
    public void cancelBooking(String bookingNumber) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        for (Booking booking : allBookings) {
            if (booking.getBookingNumber().equals(bookingNumber)) {
                System.out.printf("%s, do you really want to cancel booking number %s, room %d, for the %s - %s?%n" +
                                "Cancellation fee is $%.2f. Please, input 1 to confirm booking cancelling:%n",
                        booking.getUser().getFirstName(), bookingNumber, booking.getRoomNumber(),
                        dateFormat.format(booking.getInDate()), dateFormat.format(booking.getOutDate()),
                        getRoom(booking.getRoomNumber()).getCancellationFee());
                Scanner sc = new Scanner(System.in);
                String cancelConfirmation = sc.nextLine().trim();
                if (cancelConfirmation.equals("1")) {
                    Room room = getRoom(booking.getRoomNumber());
                    if (room != null && !isFree(room.getRoomNumber(), booking.getInDate(), booking.getOutDate())) {
                        if (isInDates(booking, booking.getInDate(), booking.getOutDate())) {
                            System.out.printf("%s booking cancelled for the dates: %s - %s. Cancellation fee $%.2f%n",
                                    booking.getBookingNumber(), dateFormat.format(booking.getInDate()),
                                    dateFormat.format(booking.getOutDate()), getRoom(booking.getRoomNumber()).getCancellationFee());
                            System.out.println();
                            // remove booking from bookings to cancelled bookings
                            allCancelledBookings.add(booking);
                            allBookings.remove(booking);
                            break;
                        } else {
                            System.out.println("Wrong data, please check the booking number and try again");
                            System.out.println();
                        }

                    } else {
                        System.out.println("Wrong data, please check the booking number and try again");
                        System.out.println();
                    }
                } else {
                    System.out.println("Booking cancellation was revoked");
                    System.out.println();
                }
            } else {
                System.out.println("Wrong booking number. Please try again");
                System.out.println();
            }
        }


    }

    //calculate stay cost for 1 booking
    public double calculateStayCost(Booking booking) {
        long millisecondsPerDay = 24 * 60 * 60 * 1000;
        long stayDuration = booking.getOutDate().getTime() - booking.getInDate().getTime();
        int numberOfNights = (int) (stayDuration / millisecondsPerDay);

        return getRoom(booking.getRoomNumber()).getPriceForNight() * numberOfNights;
    }

    //calculate stay cost for a list of  bookings
    public static double calculateStayCost(List<Booking> Bookings) {
        double totalCost = 0;
        for (Booking booking : Bookings) {
            long millisecondsPerDay = 24 * 60 * 60 * 1000;
            long stayDuration = booking.getOutDate().getTime() - booking.getInDate().getTime();
            int numberOfNights = (int) (stayDuration / millisecondsPerDay);
            totalCost += getRoom(booking.getRoomNumber()).getPriceForNight() * numberOfNights;
        }
        return totalCost;
    }

    //calculate fee for 1 cancelled booking
    public static double calculateCancellationCost(Booking booking) {
        return getRoom(booking.getRoomNumber()).getCancellationFee();
    }

    //calculate fee for a list of cancelled  bookings
    public static double calculateCancellationCost(List<Booking> Bookings) {
        double totalCancellationCost = 0;
        for (Booking booking : Bookings) {
            totalCancellationCost += getRoom(booking.getRoomNumber()).getCancellationFee();
        }
        return totalCancellationCost;
    }

    // find all bookings for 1 exclusive room
    public static List<Booking> allBookingsForRoom(int roomNumber) {
        List<Booking> allBookingsForRoom = new ArrayList<>();
        for (Booking booking : allBookings) {
            if (booking.getRoomNumber() == roomNumber) {
                allBookingsForRoom.add(booking);
            }
        }
        return allBookingsForRoom;
    }

    // checks if the room is free for a range of dates
    public static boolean isFree(int roomNumber, Date inDate, Date outDate) {
        for (Booking booking : allBookingsForRoom(roomNumber)) {
            if (isInDates(booking, inDate, outDate)) {
                return false; // room is not free
            }
        }
        return true; // room is free
    }

}
