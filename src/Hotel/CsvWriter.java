package Hotel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CsvWriter {

    public static void writeUsersAndBookings(List<User> allUsers, List<Booking> allBookings, List<Booking> allCancelledBookings) {
        String dataStorage = "C:\\Users\\User\\IdeaProjects\\Sirma\\src\\Hotel\\Data.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataStorage))) {
            // write all users info
            for (User user : allUsers) {
                writer.write(user.getFirstName() + "," + user.getLastName() + "," + user.getEmail() + "," + user.getPassword());
                writer.newLine();
            }

            // write all bookings info
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            for (Booking booking : allBookings) {
                String inDate = dateFormat.format(booking.getInDate());
                String outDate = dateFormat.format(booking.getOutDate());
                writer.write(booking.getBookingNumber() + "," + booking.getRoomNumber() + "," + inDate + ","
                        + outDate + "," + booking.getUser().getEmail());
                writer.newLine();
            }

            // write all cancelled bookings info
            for (Booking booking : allCancelledBookings) {
                String inDate = dateFormat.format(booking.getInDate());
                String outDate = dateFormat.format(booking.getOutDate());
                writer.write(booking.getBookingNumber() + "," + booking.getRoomNumber() + "," + inDate + ","
                        + outDate + "," + booking.getUser().getEmail() + "," + RoomManager.calculateCancellationCost(booking));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method for delete all data in data file
    public static void clearData(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
