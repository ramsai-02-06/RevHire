package com.revature.main;

import java.util.Scanner;

import com.revature.service.JobSeekerService;

public class JobSeekerMenu {

    private static Scanner sc = new Scanner(System.in);
    private static JobSeekerService service = new JobSeekerService();

    public static void show(int userId) {

    	if (!service.hasProfile(userId)) {
            System.out.println(
                "\n⚠ Job Seeker profile is required before proceeding."
            );
            service.createProfile(userId);

            if (!service.hasProfile(userId)) {
                System.out.println(
                    "Profile not created. Logging out."
                );
                return;
            }
        }
    	
    	while (true) {

            boolean hasResume =
                service.hasResume(userId);

            System.out.println("\n=== Job Seeker Menu ===");
            System.out.println("1. Create / Update Resume");

            if (hasResume) {
                System.out.println("2. View All Jobs");
                System.out.println("3. Apply for Job");
                System.out.println("4. View My Applications");
                System.out.println("5. Withdraw Application");
                System.out.println("6. View Notifications");
            } else {
                System.out.println("\n⚠ Complete resume to unlock other options");
            }

            System.out.println("7. Logout");
            System.out.print("Choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Invalid input");
                continue;
            }

            // BLOCK if resume missing
            if (!hasResume && choice > 1 && choice < 7) {
                System.out.println("Please complete resume first.");
                continue;
            }

            switch (choice) {
                case 1:
//                    service.setupProfileAndResume(userId);
                    service.createOrUpdateResume(userId);
                    break;
                case 2:
                    service.viewJobs();
                    break;
                case 3:
                    service.applyJob(userId);
                    break;
                case 4:
                    service.viewApplications(userId);
                    break;
                case 5:
                    service.withdrawApplication(userId);
                    break;
                case 6:
                    NotificationMenu.show(userId);
                    break;
                    
                case 7:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
