package com.revature.main;

import java.util.Scanner;

import com.revature.service.EmployerService;
import com.revature.util.ConsoleUtil;

public class EmployerMenu {

    private static Scanner sc = new Scanner(System.in);
    private static EmployerService service = new EmployerService();

    public static void show(int userId) {

    	 while (true) {

    	        boolean profileCompleted =
    	            service.isProfileCompleted(userId);

    	        System.out.println("\n=== Employer Menu ===");

    	        System.out.println("1. Complete Profile");

    	        if (profileCompleted) {
    	            System.out.println("2. Post Job");
    	            System.out.println("3. View My Jobs");
    	            System.out.println("4. View Applications");
    	            System.out.println("5. Shortlist / Reject Application");
    	        }

    	        System.out.println("6. Logout");
    	        System.out.print("Choice: ");

    	        int choice;

    	        try {
    	            choice = sc.nextInt();
    	            sc.nextLine();
    	        } catch (Exception e) {
    	            sc.nextLine();
    	            System.out.println("Invalid input. Enter a number.");
    	            continue;
    	        }

    	        // 🚫 BLOCK OPTIONS IF PROFILE NOT COMPLETED
    	        if (!profileCompleted && choice > 1 && choice < 6) {
    	            System.out.println(
    	                "Please complete your profile before accessing this option."
    	            );
    	            continue;
    	        }

            switch (choice) {
                case 1:
                    service.completeProfile(userId);
                    break;
                case 2:
                    service.postJob(userId);
                    break;
                case 3:
                    service.viewMyJobs(userId);
                    break;
                case 4:
                    service.viewApplications(userId);
                    break;
                case 5:
                    if (!service.hasApplications(userId)) {
                        System.out.println("No applications available to process.");
                        ConsoleUtil.pause(sc);
                        break;
                    }
                    service.updateApplicationStatus(userId);
                    break;

                case 6:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
