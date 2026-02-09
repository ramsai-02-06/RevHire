package com.revature.service;

import java.util.Scanner;

import com.revature.dao.JobApplicationDAO;
import com.revature.dao.JobPostDAO;
import com.revature.dao.JobSeekerDAO;
//import com.revature.dao.JobSeekerDAO;
import com.revature.dao.ResumeDAO;

public class JobSeekerService {

    private Scanner sc = new Scanner(System.in);
    private JobSeekerDAO JobSeekerDAO = new JobSeekerDAO();
    private ResumeDAO resumeDAO = new ResumeDAO();
    private JobPostDAO jobPostDAO = new JobPostDAO();
    private JobApplicationDAO applicationDAO = new JobApplicationDAO();
    
    private NotificationService notificationService = new NotificationService();

    
    

//        private JobSeekerDAO jobSeekerDAO = new JobSeekerDAO();
//        private Scanner sc = new Scanner(System.in);

        public boolean hasProfile(int userId) {
            return JobSeekerDAO.profileExists(userId);
        }

        public void createProfile(int userId) {

            System.out.println("\n--- Create Job Seeker Profile ---");

            System.out.print("Full Name (0 to cancel): ");
            String name = sc.nextLine();
            if ("0".equals(name)) return;

            String phone;
            while (true) {
                System.out.print("Phone (10 digits, 0 to cancel): ");
                phone = sc.nextLine();

                if ("0".equals(phone)) {
                    return;
                }

                // ✅ exactly 10 digits validation
                if (phone.matches("\\d{10}")) {
                    break;
                }

                System.out.println("Invalid phone number. Enter exactly 10 digits.");
            }


            System.out.print("Location (0 to cancel): ");
            String location = sc.nextLine();
            if ("0".equals(location)) return;

            int exp;
            while (true) {
            	System.out.print("Experience : ");
                try { exp = Integer.parseInt(sc.nextLine());}
                catch (Exception e) {
                	exp = Integer.parseInt(sc.nextLine());// sc.nextLine(); // clear invalid input
                    System.out.println("Invalid input. please enter number only");
                }

                    
                    break;
                }

            JobSeekerDAO.createProfile(
                userId, name, phone, location, exp
            );

            System.out.println("Profile created successfully.");
        }

    
    
    public void createOrUpdateResume(int userId) {

        // Show existing resume if present
        if (resumeDAO.resumeExists(userId)) {
            resumeDAO.viewResume(userId);
            System.out.println("\nPress Enter to edit or 0 to go back");
            String choice = sc.nextLine();
            if ("0".equals(choice)) return;
        }

        String objective, education,  skills;
        int experience;
        // Objective
        System.out.print("Objective (0 to cancel): ");
        objective = sc.nextLine();
        if ("0".equals(objective)) return;

        // Education
        System.out.print("Education (0 to cancel): ");
        education = sc.nextLine();
        if ("0".equals(education)) return;

        // Experience
        System.out.print("Experience : ");
        try { experience = Integer.parseInt(sc.nextLine());}
        catch (Exception e) {
        	experience = Integer.parseInt(sc.nextLine());// sc.nextLine(); // clear invalid input
            System.out.println("Invalid input. please enter number only");
        }

        // Skills
        System.out.print("Skills (0 to cancel): ");
        skills = sc.nextLine();
        if ("0".equals(skills)) return;

        // Save ONLY if all fields entered
        resumeDAO.saveOrUpdateResume(
            userId, objective, education, experience, skills
        );

        notificationService.notifyUser(
                userId,
                "Your profile is now complete. You can apply for jobs."
            );

        System.out.println("Resume saved successfully.");
    }


    public boolean hasResume(int userId) {
        return resumeDAO.resumeExists(userId);
    }
    
    

    public void viewJobs() {
        jobPostDAO.viewAllOpenJobs();
    }

    public void applyJob(int userId) {
    	

    	    // 1️⃣ Show all open jobs first
    	    System.out.println("\n--- Available Jobs ---");
    	    jobPostDAO.viewAllOpenJobs();

    	    // 2️⃣ Ask for Job ID
    	    System.out.print("\nEnter Job ID to apply (0 to cancel): ");
    	    int jobId = sc.nextInt();
    	    sc.nextLine();

    	    if (jobId == 0) {
    	        System.out.println("Apply cancelled.");
    	        return;
    	    }

    	    // 3️⃣ Apply
    	    applicationDAO.applyJob(userId, jobId);
 

        
        if (!resumeDAO.resumeExists(userId)) {
            System.out.println("Please complete your resume first.");
            notificationService.notifyUser(
                userId,
                "Profile incomplete. Complete resume to apply for jobs."
            );
            return;
        }

    }

    public void viewApplications(int userId) {
        applicationDAO.viewMyApplications(userId);
    }

//    public void withdrawApplication(int userId) {
//        System.out.print("Application ID: ");
//        int appId = sc.nextInt();
//        sc.nextLine();
//        System.out.print("Reason (optional): ");
//        String reason = sc.nextLine();
//        applicationDAO.withdrawApplication(userId, appId, reason);
//    }
    
    public void withdrawApplication(int userId) {

        viewApplications(userId);

        System.out.print("Application ID (0 to cancel): ");
        int appId = sc.nextInt();
        sc.nextLine();

        if (appId == 0) return;

        System.out.print("Reason (optional): ");
        String reason = sc.nextLine();

        boolean success =
            applicationDAO.withdrawApplication(appId, userId, reason);

        if (success) {
            System.out.println("Application withdrawn successfully.");
        } else {
            System.out.println(
                "Withdrawal not allowed. Only APPLIED applications can be withdrawn."
            );
        }
    }
    
    }
