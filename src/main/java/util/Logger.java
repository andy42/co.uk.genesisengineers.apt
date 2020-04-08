package util;

public class Logger {

    public static boolean debugEnabled = true;

    // info
    public static void info (String message, Object... args) {
        log("Info", message, args);
    }

    // debug
    public static void debug (String message, Object... args) {
        if (debugEnabled == true) {
            log("Debug Message", message, args);
        }
    }

    // error
    public static void error (String message, Object... args) {
        error("Error", message, args);
    }

    // exception with stack trace
    public static void exception (Throwable throwable, String message, Object... args) {
        error("Exception", message, args);
        error("Exception Message", throwable.getMessage());
        throwable.printStackTrace();
    }


    // handle non error messages to console, with optional arguments
    private static void log (String type, String message, Object... args) {
        if (args.length > 0) {
            System.out.format(String.format("%s: %s\n", type, message), args);
            return;
        }

        System.out.print(String.format("%s: %s\n", type, message));
    }

    // handle error messages to console with optional arguments
    private static void error (String type, String message, Object... args) {
        if (args.length > 0) {
            System.err.format(String.format("%s: %s\n", type, message), args);
        }

        System.err.print(String.format("%s: %s\n", type, message));
    }

}
