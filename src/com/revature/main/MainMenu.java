package com.revature.main;

import java.util.Scanner;

import com.revature.model.User;
import com.revature.service.AuthService;
import com.revature.service.NotificationService;
import com.revature.util.ConsoleUtil;
import com.revature.util.InputValidator;

public class MainMenu {

    private static Scanner sc = new Scanner(System.in);
    private static AuthService authService = new AuthService();

    public static void main(String[] args) {

    	while (true) {

    	   

    	    System.out.println("\n=== RevHire ===");
    	    System.out.println("1. Register");
    	    System.out.println("2. Login");
    	    System.out.println("3. Forgot Password");
    	    System.out.println("4. Exit");
    	    System.out.print("Choice: ");
    	    
    	    int choice;

    	    try {
    	        choice = sc.nextInt();
    	        sc.nextLine(); // clear buffer
    	    } catch (Exception e) {
    	        sc.nextLine(); // clear invalid input
    	        System.out.println("\nInvalid input! Please enter a number.");
    	        ConsoleUtil.pause(sc);
    	        ConsoleUtil.clearConsole();
    	        continue; // 🔁 back to menu
    	        
    	    }
    	    
    	    switch (choice) {
    	    
    	        case 1:
    	            registerFlow();
    	            break;

    	        case 2:
    	            loginFlow();
    	            break;

    	        case 3:
    	            forgotPasswordFlow();
    	            break;

    	        case 4:
    	            System.out.println("Portal Closed");
    	            System.exit(0);

    	        default:
    	            System.out.println("Invalid choice. Try again.");
    	            ConsoleUtil.pause(sc);
    	    }
    	}

    }

    private static void registerFlow() {

        String email;
        String password;

               // 🔹 Email input with Back option
        while (true) {
            System.out.print("Enter Email OR  click 0 to MAIN Menu : ");
            email = sc.nextLine();

            if ("0".equals(email)) {
                return; // 🔙 back to main menu
            }

            if (InputValidator.isValidEmail(email)) {
                break;
            }

            System.out.println("Invalid email format. Try again.");
        }
        // Password validation
     // 🔹 Password input with Back option
       
        while (true) {
        	 System.out.print("Enter Password OR  click 0 to MAIN Menu: ");
                         
            password = sc.nextLine();
            
            if ("0".equals(password)) {
                return; // 🔙 back to main menu
            }
            if (InputValidator.isValidPassword(password)) {
                break;
            }
            System.out.println(
                "Password must contain:\n" +
                "- At least 8 characters\n" +
                "- One uppercase letter\n" +
                "- One lowercase letter\n" +
                "- One number\n" +
                "- One special character"
            );
        }

        String role;
        while (true) {
            System.out.println("\nSelect Role:");
            System.out.println("0 for Job Seeker");
            System.out.println("1 for Employer");
            System.out.print("Choice: ");

            try {
                int roleChoice = sc.nextInt();
                sc.nextLine(); // clear buffer

                if (roleChoice == 0) {
                    role = "JOB_SEEKER";
                    break;
                } else if (roleChoice == 1) {
                    role = "EMPLOYER";
                    break;
                } else {
                    System.out.println("Invalid choice. Enter 0 or 1.");
                }

            } catch (Exception e) {
                sc.nextLine(); // clear invalid input
                System.out.println("Invalid input. Please enter 0 or 1.");
            }
        }

        System.out.print("Security Question: ");
        String question = sc.nextLine().toLowerCase();

        System.out.print("Security Answer: ");
        String answer = sc.nextLine();

        User user = new User(email, password, role, question, answer);

        boolean success = authService.register(user);
        System.out.println(success ? "Registration successful" : "Registration failed");
    }


    private static void loginFlow() {

        String email;

        // 🔹 Email input with Back option
        while (true) {
            System.out.print("Enter Email OR  click 0 to MAIN Menu : ");
            email = sc.nextLine();

            if ("0".equals(email)) {
                return; // 🔙 back to main menu
            }

            if (InputValidator.isValidEmail(email)) {
                break;
            }

            System.out.println("Invalid email format. Try again.");
        }

        // 🔹 Password input with Back option
        System.out.print("Enter Password : ");
        String password = sc.nextLine();

       

        User user = authService.login(email, password);

        if (user == null) {
            System.out.println("Invalid credentials");
            return;
        }

        System.out.println("Login successful!");

        if ("JOB_SEEKER".equals(user.getRole())) {
            JobSeekerMenu.show(user.getUserId());
        } else {
            EmployerMenu.show(user.getUserId());
        }
    }


    private static void forgotPasswordFlow() {

        System.out.println("\n--- Forgot Password ---");

        String email; 
    	while (true) {
             System.out.print("Enter Email: ");
             email = sc.nextLine();

             if (InputValidator.isValidEmail(email)) {
                 break;
             }
             System.out.println("Invalid email format. Try again.");
         }


        // Fetch security question
        String securityQuestion = authService.getSecurityQuestion(email);

        if (securityQuestion == null) {
            System.out.println("No user found with this email.");
            return;
        }

        System.out.println("Security Question: " + securityQuestion);
        System.out.print("Enter Security Answer (case-sensitive): ");
        String enteredAnswer = sc.nextLine().toLowerCase();

        // CASE-SENSITIVE validation (IMPORTANT)
        boolean isValidAnswer =
            authService.validateSecurityAnswer(email, enteredAnswer);

        if (!isValidAnswer) {
            System.out.println("Security answer does not match.");
            return;
        }

        // Strong password validation loop
        String newPassword;
        while (true) {
            System.out.print("Enter New Password: ");
            newPassword = sc.nextLine();

            if (InputValidator.isValidPassword(newPassword)) {
                break;
            }

            System.out.println(
                "Password must contain:\n" +
                "- Minimum 8 characters\n" +
                "- At least 1 uppercase letter\n" +
                "- At least 1 lowercase letter\n" +
                "- At least 1 number\n" +
                "- At least 1 special character"
            );
        }

        // Update password
        authService.updatePassword(email, newPassword);

        System.out.println("Password reset successful.");

        // OPTIONAL BUT STRONG: Notification
        User user = authService.login(email, newPassword);
        if (user != null) {
            NotificationService notificationService = new NotificationService();
            notificationService.notifyUser(
                user.getUserId(),
                "Your password was reset successfully."
            );
        }
    }
    
    

}
