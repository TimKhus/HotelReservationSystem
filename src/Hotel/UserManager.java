package Hotel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {

    // check if we have all needed info to register a user
    public static boolean registerUser(String firstName, String lastName, String email, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Checking for required fields. If any is empty, return false.
            return false;
        }

        User newUser = new User(firstName, lastName, email, password);
        RoomManager.getAllUsers().add(newUser);
        return true;
    }

    // check if the email is registered
    public static boolean emailRegistered(String email) {
        for (User user : RoomManager.getAllUsers()) {
            if (user.getEmail().equals(email)) {
                return true; // email already registered in our database
            }
        }
        return false; // email is not registered, user should register
    }

    //find user by email
    public static User findUser(String email) {
        for (User user : RoomManager.getAllUsers()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    //checking if the date
    public static boolean isDateValid(String userInput) {
        String datePattern = "\\d{2}.\\d{2}.\\d{4}";
        if (userInput.matches(datePattern)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            try {Date date = dateFormat.parse(userInput);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH) + 1; // adiing 1 because months are in range 1-12

                if (month >= 1 && month <= 12) {
                    return true; // month is valid
                } else {
                    return false; // wrong month number
                }
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    //checking if email is valid
    public static boolean isEmailValid(String userInput) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(userInput);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

}
