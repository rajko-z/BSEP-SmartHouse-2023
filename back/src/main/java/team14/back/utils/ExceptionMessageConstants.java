package team14.back.utils;

public final class ExceptionMessageConstants {
    public static final String INVALID_LOGIN = "Invalid username or password";

    public static final String INVALID_TWO_FACTOR = "Code is invalid or it has expired";
    public static final String NEW_PASSWORD_SAME_AS_PREVIOUS = "Password can't be same as previous";
    public static final String INVALID_CURRENT_PASSWORD = "Current password is invalid";

    public static final String FORMAT_FOR_PASSWORD_NOT_VALID = "Password format not valid. Requires at least 8 characters, one big letter, one small letter, one number and one character other than letter and number";
    public static final String PASSWORD_ON_LIST_OF_MOST_COMMON_PASSWORDS = "Password is on the list of most common passwords, please choose another one";

    private ExceptionMessageConstants() {
    }
}

