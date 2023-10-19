package Hotel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReservationSystem {
    public static void main(String[] args) throws ParseException {
        RoomManager roomManager = new RoomManager();
        // load data from Hotel file - room types, amenities, prices, number of rooms in hotel
        List<RoomType> roomTypes = loadRoomTypesFromFile("src/Hotel/Hotel.csv");
        createHotel(roomTypes, roomManager);
        // read data from data file (users, bookings, cancelled bookings)
        String dataStorage = "src/Hotel/Data.csv";
        CSVReader.readDataFromFile(dataStorage, RoomManager.getAllUsers(), RoomManager.getAllBookings(),
                RoomManager.getAllCancelledBookings());

        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        boolean loggedIn = false; // so only registered user can use our system
        String loginEmail = null;

        while (!exit) {
            System.out.println("0 - Enter the admin mode");
            System.out.println("1 - Log in / register a new user");
            System.out.println("2 - View room types");
            System.out.println("3 - Book a room");
            System.out.println("4 - Cancel booking");
            System.out.println("5 - Show my bookings");
            System.out.println("6 - Log out");
            System.out.println("7 - Exit");
            System.out.println("Select an option: ");
            String choice = sc.nextLine().trim();

            if (Character.isDigit(choice.toCharArray()[0]) && choice.length() == 1) {
                switch (Integer.parseInt(choice)) {
                    // enter admin mode
                    case 0 -> {
                        // if we go to admin mode from user registered mode this helps to log out anyway
                        if (loggedIn) {
                            loginEmail = null;
                            loggedIn = false;
                            System.out.println("Goodbye, we look forward to see you again");
                        }
                        AdminManager adminManager = new AdminManager(roomManager);
                        adminManager.startAdminMode();
                    }
                    // user registration or logging in
                    case 1 -> {
                        if (loggedIn) {
                            System.out.println("You already logged in");
                            System.out.println();
                            break;
                        }
                        System.out.println("1 - Register a new user");
                        System.out.println("2 - Log in with your email and password");
                        String regReply = sc.nextLine().trim();
                        if (Character.isDigit(regReply.toCharArray()[0]) && regReply.length() == 1) {
                            switch (Integer.parseInt(regReply)) {
                                // users registration
                                case 1 -> {
                                    System.out.println("Input your first name : ");
                                    String newFirstName = sc.nextLine().trim().toUpperCase();
                                    System.out.println("Input your last name: ");
                                    String newLastName = sc.nextLine().trim().toUpperCase();
                                    System.out.println("Input your email: ");
                                    String newEmail = sc.nextLine().trim().toLowerCase();
                                    // checking email is real
                                    if (!UserManager.isEmailValid(newEmail)) {
                                        System.out.println("Email does not exist. Please try again");
                                        System.out.println();
                                        break;
                                    }
                                    // if email already registered we ask user to log in with it
                                    if (UserManager.emailRegistered(newEmail)) {
                                        System.out.println("This email already registered. Please log in");
                                        System.out.println();
                                        break;
                                    }
                                    System.out.println("Input your password, without spaces: ");
                                    String newPassword = sc.nextLine().trim();
                                    // checking we have all required data
                                    if (UserManager.registerUser(newFirstName, newLastName, newEmail, newPassword)) {
                                        System.out.printf("New user %s registered%n", newEmail);
                                        System.out.printf("Hi %s! You are logged in using email %s%n", newFirstName, newEmail);
                                        System.out.println();
                                        loggedIn = true;
                                        loginEmail = newEmail;
                                    } else {
                                        System.out.println("Please fill in all required fields to register.");
                                        System.out.println();
                                    }
                                }
                                // log in if user already registered
                                case 2 -> {
                                    User user = null;
                                    System.out.println("Input your email: ");
                                    String email = sc.nextLine().trim().toLowerCase();
                                    // checking email is real
                                    if (!UserManager.isEmailValid(email)) {
                                        System.out.println("Email does not exist. Please try again");
                                        System.out.println();
                                        break;
                                    }
                                    //checking users email is in our base
                                    if (UserManager.emailRegistered(email)) {
                                        user = UserManager.findUser(email);
                                    } else {
                                        System.out.println("User with this email does not exist");
                                        System.out.println();
                                        break;
                                    }
                                    System.out.println("Input your password: ");
                                    String password = sc.nextLine().trim();
                                    assert user != null;
                                    if (user.getPassword().equals(password)) {
                                        System.out.printf("Hi %s! You are logged in using email %s%n", user.getFirstName(), email);
                                        System.out.println();
                                        loggedIn = true;
                                        loginEmail = email;
                                        break;
                                    } else {
                                        System.out.println("Wrong password! Please try again");
                                    }
                                    System.out.println();
                                }
                                default -> {
                                    System.out.println("Wrong command, please try again");
                                    System.out.println();
                                }
                            }
                        }
                    }
                    // mode to see room types and their details
                    case 2 -> {
                        if (loggedIn) {
                            viewRooms(roomTypes);
                            System.out.println();
                        } else {
                            System.out.println("Please login or register to view room types and work with reservations");
                            System.out.println();
                        }
                    }
                    // booking a room
                    case 3 -> {
                        if (loggedIn) {
                            System.out.println("Input the date of arrival at the hotel in the format DD.MM.YYYY: ");
                            String arrivalDate = sc.nextLine().trim();
                            System.out.println("Input the date of departure from the hotel in the format DD.MM.YYYY: ");
                            String departureDate = sc.nextLine().trim();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            // checking dates are valid
                            if (UserManager.isDateValid(arrivalDate) && UserManager.isDateValid(departureDate)) {
                                Date inDate = dateFormat.parse(arrivalDate);
                                Date outDate = dateFormat.parse(departureDate);

                                //checking dates goes in required order and are not same
                                if (outDate.after(inDate)) {
                                    List<Room> availableRooms = roomManager.getFreeRoomsForDates(inDate, outDate);
                                    if (availableRooms.isEmpty()) {
                                        System.out.println("No free rooms for the selected dates.");
                                    } else {
                                        System.out.println("Free rooms for the selected dates:");
                                        for (Room room : availableRooms) {
                                            System.out.printf("Room %s: ", room.getRoomNumber());
                                            System.out.printf("%s, ", room.getRoomType());
                                            System.out.printf("price for 1 night: $%.2f, ", room.getPriceForNight());
                                            System.out.printf("cancellation fee: $%.2f%n", room.getCancellationFee());
                                        }
                                    }
                                } else {
                                    System.out.println("Please check the dates and try again");
                                    break;
                                }
                                System.out.println("Input room number for a booking: ");
                                try {
                                    int roomNumber = Integer.parseInt(sc.nextLine().trim());
                                    //creating the booking and booking a room
                                    roomManager.bookRoom(roomNumber, inDate, outDate, UserManager.findUser(loginEmail));
                                    System.out.printf("%s %s successfully booked for the dates: %s - %s%n",
                                            roomManager.getRoom(roomNumber).getRoomType(),
                                            roomNumber, dateFormat.format(inDate), dateFormat.format(outDate));
                                    Booking booking = RoomManager.getBooking(roomNumber, inDate, outDate, loginEmail);
                                    System.out.printf("Your booking number is: %s%n", booking.getBookingNumber());
                                    System.out.printf("Cost of hotel accommodation is $%.2f%n", roomManager.calculateStayCost(booking));
                                    System.out.println();
                                } catch (NullPointerException | NumberFormatException e) {
                                    System.out.println("Wrong room number. PLease try again");
                                }
                            } else {
                                System.out.println("Wrong date format. Please try again");
                            }
                        } else {
                            System.out.println("Please login or register to view room types and work with reservations");
                            System.out.println();
                        }
                    }
                    // cancelling the booking
                    case 4 -> {
                        if (loggedIn) {
                            System.out.println("Input the booking number for cancelling it: ");
                            try {
                                String bookingNumber = sc.nextLine().trim();
                                roomManager.cancelBooking(bookingNumber);
                            } catch (NullPointerException | NumberFormatException e) {
                                System.out.println("Wrong room number. PLease try again");
                            }

                        } else {
                            System.out.println("Please login or register to view room types and work with reservations");
                            System.out.println();
                        }
                    }
                    // see all users bookings and cancelled bookings and fees
                    case 5 -> {
                        if (loggedIn) {
                            List<Booking> myBookings = roomManager.myBookings(loginEmail);
                            List<Booking> myCancelledBookings = roomManager.myCancelledBookings(loginEmail);
                            if (myBookings.size() == 0 && myCancelledBookings.size() == 0) {
                                System.out.println("You do not have any bookings");
                                System.out.println();
                                break;
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            if (myBookings.size() > 0) {
                                System.out.println("Your active bookings: ");
                                for (Booking booking : myBookings) {
                                    System.out.printf("Room number: %d, Date in: %s, Date out: %s%n", booking.getRoomNumber(),
                                            dateFormat.format(booking.getInDate()), dateFormat.format(booking.getOutDate()));
                                    System.out.printf("Booking number: %s. Cost of accommodation is $%.2f%n", booking.getBookingNumber(),
                                            roomManager.calculateStayCost(booking));
                                }
                                System.out.printf("Total accommodation fee: %.2f%n", roomManager.calculateStayCost(myBookings));
                            }

                            if (myCancelledBookings.size() > 0) {
                                System.out.println("Your cancelled bookings: ");
                                for (Booking booking : myCancelledBookings) {
                                    System.out.printf("Room number: %d, Date in: %s, Date out: %s%n", booking.getRoomNumber(),
                                            dateFormat.format(booking.getInDate()), dateFormat.format(booking.getOutDate()));
                                    System.out.printf("Booking number: %s. Cancellation cost is $%.2f%n", booking.getBookingNumber(),
                                            roomManager.calculateCancellationCost(booking));
                                }
                                System.out.printf("Total cancellation fee: %.2f%n", roomManager.calculateCancellationCost(myCancelledBookings));
                            }
                            System.out.println("---------------------------");
                            System.out.printf("Total fee: %.2f%n", roomManager.calculateStayCost(myBookings) +
                                    roomManager.calculateCancellationCost(myCancelledBookings));
                            System.out.println();
                        } else {
                            System.out.println("Please login or register to view room types and work with reservations");
                            System.out.println();
                        }
                    }
                    // user logs out, new user can log in
                    case 6 -> {
                        if (loggedIn) {
                            System.out.printf("Goodbye %s, we look forward to see you again%n", UserManager.findUser(loginEmail));
                            System.out.println();
                            loginEmail = null;
                            loggedIn = false;
                        } else {
                            System.out.println("Please login or register to view room types and work with reservations");
                            System.out.println();
                        }
                    }
                    // save data (users, bookings, cancelled bookings) and exit the program
                    case 7 -> {
                        exit = true;
                        CsvWriter.clearData(dataStorage);
                        CsvWriter.writeUsersAndBookings(RoomManager.getAllUsers(), RoomManager.getAllBookings(),
                                RoomManager.getAllCancelledBookings());
                    }
                    default -> {
                        System.out.println("Wrong command, please try again");
                        System.out.println();
                    }
                }
            } else {
                System.out.println("Wrong command, please try again");
            }
        }

    }


    private static boolean loggedIn = false;

    // method to load data (room types and details) from our Hotel file
    private static List<RoomType> loadRoomTypesFromFile(String filePath) {
        String line = "";
        String separator = ",";
        List<RoomType> roomTypes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(separator);
                RoomType roomType;

                if (data.length == 6) { // Checking we have all needed fields
                    String name = data[0];
                    String amenities = data[1];
                    int maxOccupancy = Integer.parseInt(data[2]);
                    double priceForNight = Double.parseDouble(data[3]);
                    double cancellationFee = Double.parseDouble(data[4]);
                    int numberOfRooms = Integer.parseInt(data[5]);
                    roomType = new RoomType(name, amenities, maxOccupancy, priceForNight, cancellationFee, numberOfRooms);
                    roomTypes.add(roomType);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return roomTypes;
    }

    public static void createHotel(List<RoomType> roomTypes, Hotel.RoomManager roomManager) {
        int counter = 0;
        for (RoomType roomType : roomTypes) {
            for (int i = 1; i <= roomType.getNumberOfRooms(); i++) {
                counter++;
                int roomNumber = counter;
                String type = roomType.getName();
                double priceForNight = roomType.getPriceForNight();
                double cancellationFee = roomType.getCancellationFee();
                Room room = new Room(roomNumber, type, priceForNight, cancellationFee);
                Hotel.RoomManager.getRooms().add(room);
            }
        }
    }

    // shows all rooms and their details in our hotel
    public static void viewRooms(List<RoomType> roomTypes) {
        for (RoomType roomType : roomTypes) {
            System.out.printf("Type: %s. Amenities: %s. Maximal Occupancy: %d%nPrice For Night: $%.2f. " +
                            "Cancellation Fee: $%.2f. Number of rooms in the hotel: %d%n", roomType.getName(), roomType.getAmenities(),
                    roomType.getMaxOccupancy(), roomType.getPriceForNight(), roomType.getCancellationFee(), roomType.getNumberOfRooms());
            System.out.println("-------------------------------");
        }
    }

    private static Admin admin = new Admin("admin");
}
