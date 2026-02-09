package com.revature.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import com.revature.dao.EmployerDAO;
import com.revature.dao.JobApplicationDAO;
import com.revature.dao.JobPostDAO;
import com.revature.util.DBConnectionUtil;

public class EmployerService {

	private Scanner sc = new Scanner(System.in);
	private EmployerDAO employerDAO = new EmployerDAO();
	private JobPostDAO jobPostDAO = new JobPostDAO();
	private JobApplicationDAO applicationDAO = new JobApplicationDAO();

	private NotificationService notificationService = new NotificationService();

	public void completeProfile(int userId) {

		if (employerDAO.employerExists(userId)) {
			System.out.println("Employer profile already exists");
			return;
		}

		System.out.print("Company Name: ");
		String companyName = sc.nextLine();

		System.out.print("Industry: ");
		String industry = sc.nextLine();

		System.out.print("Description: ");
		String description = sc.nextLine();

		System.out.print("Location: ");
		String location = sc.nextLine();

		System.out.print("Your Designation: ");
		String designation = sc.nextLine();

		employerDAO.createEmployerProfile(userId, companyName, industry, description, location, designation);
	}

	public boolean isProfileCompleted(int userId) {
		return employerDAO.isProfileCompleted(userId);
	}

	public void postJob(int userId) {

		System.out.print("Job Title: ");
		String title = sc.nextLine();

		System.out.print("Description: ");
		String description = sc.nextLine();

		System.out.print("Skills: ");
		String skills = sc.nextLine();

		int exp;
		while (true) {
			System.out.print("Experience Required (years): ");
			try {
				exp = sc.nextInt();
				sc.nextLine();
				break;
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Invalid input. Enter a number.");
			}
		}

		System.out.print("Education: ");
		String education = sc.nextLine();

		System.out.print("Location: ");
		String location = sc.nextLine();

		double salary;
		while (true) {
			System.out.print("Salary: ");
			try {
				salary = sc.nextDouble();
				sc.nextLine();
				break;
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Invalid input. Enter a valid salary.");
			}
		}

		// ✅ JOB TYPE SELECTION (NUMERIC)
		String jobType;
		while (true) {
			System.out.println("\nSelect Job Type:");
			System.out.println("0. FULL_TIME");
			System.out.println("1. PART_TIME");
			System.out.println("2. INTERNSHIP");
			System.out.println("3. CONTRACT");
			System.out.print("Choice: ");

			try {
				int typeChoice = sc.nextInt();
				sc.nextLine();

				switch (typeChoice) {
				case 0:
					jobType = "FULL_TIME";
					break;
				case 1:
					jobType = "PART_TIME";
					break;
				case 2:
					jobType = "INTERNSHIP";
					break;
				case 3:
					jobType = "CONTRACT";
					break;
				default:
					System.out.println("Invalid choice. Enter 0–3.");
					continue;
				}
				break;

			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Invalid input. Enter a number (0–3).");
			}
		}

		// 1️ Create Job
		jobPostDAO.createJob(userId, title, description, skills, exp, education, location, salary, jobType);

		//System.out.println("Job posted successfully");

		// 2️ Notify matching job seekers
		notifyMatchingJobSeekers(skills, title);
	}

	private void notifyMatchingJobSeekers(String skills, String jobTitle) {

		String sql = "SELECT u.user_id " + "FROM users u " + "JOIN job_seeker js ON u.user_id = js.user_id "
				+ "JOIN resume r ON js.seeker_id = r.seeker_id " + "WHERE r.skills LIKE ?";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, "%" + skills + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				notificationService.notifyUser(rs.getInt("user_id"),
						"New job posted matching your skills: " + jobTitle);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Invalid input.");
		}
	}

	public void viewMyJobs(int userId) {
		jobPostDAO.viewJobsByEmployer(userId);
	}

	public void viewApplications(int userId) {
		applicationDAO.viewApplicationsForEmployer(userId);
	}
	
	public boolean hasApplications(int userId) {
	    return employerDAO.hasApplications(userId);
	}


	
		public void updateApplicationStatus(int userId) {

		    // 1️⃣ Show applications first
		    //employerDAO.viewApplications(userId);

		    applicationDAO.viewApplicationsForEmployer(userId);
		    System.out.print("\nEnter Application ID (0 to cancel): ");
		    int appId;

		    try {
		        appId = sc.nextInt();
		        sc.nextLine();
		    } catch (Exception e) {
		        sc.nextLine();
		        System.out.println("Invalid input.");
		        return;
		    }

		    if (appId == 0) {
		        return;
		    }

		    // 2️⃣ STATUS SELECTION (NUMERIC)
		    String status;
		    while (true) {
		        System.out.println("\nSelect Status:");
		        System.out.println("0. SHORTLISTED");
		        System.out.println("1. REJECTED");
		        System.out.print("Choice: ");

		        try {
		            int choice = sc.nextInt();
		            sc.nextLine();

		            if (choice == 0) {
		                status = "SHORTLISTED";
		            } else if (choice == 1) {
		                status = "REJECTED";
		            } else {
		                System.out.println("Invalid choice.");
		                continue;
		            }
		            break;

		        } catch (Exception e) {
		            sc.nextLine();
		            System.out.println("Enter a number.");
		        }
		    }

		    

		    // 3️⃣ Update DB
//		    int seekerUserId =
//		        employerDAO.updateApplicationStatus(appId, status, comment, userId);
//
//		    // 4️⃣ Notify ONLY IF VALID USER
//		    if (seekerUserId > 0) {
//		        notificationService.notifyUser(
//		            seekerUserId,
//		            "Your application has been " + status
//		        );
//		    }
//
//		    System.out.println("Application updated successfully.");
//		}

		int seekerUserId = getSeekerUserId(appId);

		notificationService.notifyUser(seekerUserId, "Your application (ID: " + appId + ") has been " + status);
		System.out.println("Application updated successfully.");
	}

	private int getSeekerUserId(int applicationId) {

		String sql = "SELECT u.user_id " + "FROM users u " + "JOIN job_seeker js ON u.user_id = js.user_id "
				+ "JOIN job_application a ON js.seeker_id = a.seeker_id " + "WHERE a.application_id=?";

		try (Connection con = DBConnectionUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, applicationId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt(1);

		} catch (Exception e) {
			//e.printStackTrace();
			//sc.nextLine(); // clear invalid input
            System.out.println("Invalid User ID");
		}
		return 0;
	}

}
