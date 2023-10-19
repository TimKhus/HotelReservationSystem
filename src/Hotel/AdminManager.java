package Hotel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class AdminManager {
    private RoomManager roomManager;
    private Scanner sc;

    public AdminManager(RoomManager roomManager) {
        this.roomManager = roomManager;
        this.sc = new Scanner(System.in);
    }

    public void startAdminMode() {
        boolean adminMode = false;
        System.out.println("Enter 'admin' to access admin mode: ");
        String input = sc.nextLine().trim();

        if (input.equals("admin")) {
            System.out.println("Enter the admin password:");
            String adminPassword = sc.nextLine();
            if (adminPassword.equals(admin.getPassword())) {
                adminMode = true;
                System.out.println("Admin mode activated");
            } else {
                System.out.println("Invalid admin password. Continue as a regular user");
                System.out.println();
            }
        } else {
            System.out.println("Wrong admin name. Please try again");
            System.out.println();
        }

        if (adminMode) {
            System.out.println("Welcome to admin mode");

            boolean adminModeActive = true;

            while (adminModeActive) {
                System.out.println("1 - Show all bookings");
                System.out.println("2 - Show total income");
                System.out.println("3 - Show total cancellation income");
                System.out.println("4 - Show all users");
//            System.out.println("5 - Delete room");
//            System.out.println("6 - Change room details");
                System.out.println("0 - Exit");
                System.out.println("Select an option: ");

                String commandString = sc.nextLine().trim();

                if (Character.isDigit(commandString.toCharArray()[0]) && commandString.length() == 1) {
                    switch (Integer.parseInt(commandString)) {
                        case 1 -> {
                            // Show all bookings
                            List<Booking> hotelBookings = RoomManager.getAllBookings();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            for (Booking booking : hotelBookings) {
                                System.out.printf("Room number: %d, Date in: %s, Date out: %s%n", booking.getRoomNumber(),
                                        dateFormat.format(booking.getInDate()), dateFormat.format(booking.getOutDate()));
                                System.out.printf("Booking number: %s. Cost of accommodation is $%.2f%n", booking.getBookingNumber(),
                                        roomManager.calculateStayCost(booking));
                            }
                            System.out.println();
                        }
                        case 2 -> {
                            // Show total bookings income
                            List<Booking> hotelBookingsForCalc = RoomManager.getAllBookings();
                            double totalIncome = RoomManager.calculateStayCost(hotelBookingsForCalc);
                            System.out.printf("Total booking income for the hotel is $%.2f%n", totalIncome);
                            System.out.println();
                        }
                        case 3 -> {
                            // Show total cancellation income
                            List<Booking> hotelCancelledBookings = RoomManager.getAllCancelledBookings();
                            double totalCancellationIncome = RoomManager.calculateCancellationCost(hotelCancelledBookings);
                            System.out.printf("Total cancellation income for the hotel is $%.2f%n", totalCancellationIncome);
                            System.out.println();
                        }
                        case 4 -> {
                            // show all registered users
                            List<User> registeredUsers = RoomManager.getAllUsers();
                            for (User user : registeredUsers) {
                                System.out.printf("Name: %s %s, email %s%n", user.getFirstName(), user.getLastName(), user.getEmail());
                            }
                            System.out.println();
                        }
                        case 0 -> {
                            // Exit
                            adminModeActive = false;
                            System.out.println("You exit admin mode");
                            System.out.println();
                        }
                        default -> {
                            System.out.println("Wrong command");
                            System.out.println();
                        }
                    }
                } else {
                    System.out.println("Wrong command, please try again");
                    System.out.println();
                }
            }
        }
    }
    private static Admin admin = new Admin("admin");
}
