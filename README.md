# RevHire
RevHire -P1 
┌──────────────────────────────────────────┐
│          Presentation Layer               │
│ (Console Menus & User Interaction)        │
│                                          │
│  - MainMenu                               │
│  - JobSeekerMenu                          │
│  - EmployerMenu                           │
│  - NotificationMenu                      │
└──────────────────────────────────────────┘
                    ↓
┌──────────────────────────────────────────┐
│            Service Layer                  │
│ (Business Logic & Validation)             │
│                                          │
│  - AuthService                            │
│  - JobSeekerService                      │
│  - EmployerService                       │
│  - ResumeService                         │
│  - JobPostService                        │
│  - NotificationService                  │
└──────────────────────────────────────────┘
                    ↓
┌──────────────────────────────────────────┐
│              DAO Layer                    │
│ (Database Access via JDBC)                │
│                                          │
│  - UserDAO                                │
│  - JobSeekerDAO                          │
│  - EmployerDAO                           │
│  - ResumeDAO                             │
│  - JobPostDAO                            │
│  - JobApplicationDAO                    │
│  - NotificationDAO                      │
└──────────────────────────────────────────┘
                    ↓
┌──────────────────────────────────────────┐
│            Database Layer                 │
│              MySQL                        │
│                                          │
│  Tables:                                 │
│  - users                                 │
│  - job_seeker                            │
│  - resume                                │
│  - employer                              │
│  - job_post                              │
│  - job_application                       │
│  - notification                          │
└──────────────────────────────────────────┘
