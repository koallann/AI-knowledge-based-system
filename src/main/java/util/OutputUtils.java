package util;

import java.util.Scanner;

public final class OutputUtils {

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void printf(String s, Object... args) {
        System.out.printf(s, args);
    }

    public static void hold(Scanner scanner) {
        print("\nPress enter to continue...");
        scanner.nextLine();
        print("\n");
    }

}
