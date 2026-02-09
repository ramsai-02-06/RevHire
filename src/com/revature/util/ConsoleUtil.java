package com.revature.util;

import java.util.Scanner;

public class ConsoleUtil {

    private ConsoleUtil() {
        // prevent object creation
    }

    public static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void pause(Scanner sc) {
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
}
