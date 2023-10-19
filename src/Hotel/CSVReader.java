package Hotel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CSVReader {

    public static void readDataFromFile(String filePath, List<User> allUsers, List<Booking> allBookings, List<Booking> allCancelledBookings) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    // Users data: name, surname, email, pass
                    String firstName = data[0];
                    String lastName = data[1];
                    String email = data[2];
                    String password = data[3];
                    allUsers.add(new User(firstName, lastName, email, password));
                } else if (data.length == 5) {
                    // booking data
                    String bookingNumber = data[0];
                    int roomNumber = Integer.parseInt(data[1]);
                    Date inDate = dateFormat.parse(data[2]);
                    Date outDate = dateFormat.parse(data[3]);
                    String email = data[4];
                    User user = UserManager.findUser(email);
                    allBookings.add(new Booking(roomNumber, inDate, outDate, user));
                } else if (data.length == 6) {
                    // cancelled booking data
                    String bookingNumber = data[0];
                    int roomNumber = Integer.parseInt(data[1]);
                    Date inDate = dateFormat.parse(data[2]);
                    Date outDate = dateFormat.parse(data[3]);
                    String email = data[4];
                    User user = UserManager.findUser(email);
                    allCancelledBookings.add(new Booking(roomNumber, inDate, outDate, user));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}