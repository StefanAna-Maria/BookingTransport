# Booking Transport

**Booking Transport** is a console-based **Ride-Sharing Java Application** that allows users to register, authenticate, and manage transport booking requests.  
It includes diffrent functionalities for clients, drivers, and admins.

---

## Overview

The application simulates a ride-sharing system with features such as:
- **Multiple user roles** — client, driver, and admin
- **User registration and authentication**
- **Placing and accepting ride requests**
- **Vehicle registration and management**
- **Client and driver feedback**
- **Payment processing and report generation**
- **User activity tracking**
- **CRUD operations managed by the admin role**
- **Design Patterns** — *Singleton* (for managing the database connection), *Builder* (for handling User class properties) and *Factory* (for managing different user roles)

---

## Available Actions 

### Startup Menu
=== Booking Transport Application ===
1. Register User
2. Authenticate User
0. Exit

### Client Meniu
1. Place a transport request
2. Send feedback to driver
3. View ride history
4. Cancel a request
5. Modify a request
0. Back

### Driver Meniu
1. Register vehicle
2. Accept a request
3. Complete a ride
4. Send feedback to client
5. View personal income
6. Delete vehicle
7. Update vehicle information
0. Back

### Admin Meniu
1. Display clients
2. Display drivers
3. Delete user
4. Update user
5. Display rides
6. Delete rides
7. Update rides
8. Display vehicles
9. Display active rides per driver
10. Display rides with pricing
11. Generate income report per driver
12. Display all reports
13. Delete report
14. Update report
15. View client ride history
16. View ride request status
17. View client feedback
18. View driver feedback
19. View user scores
0. Back

---

## Technologies Used

- **Java**
- **MySQL**
- **JDBC**
- **IntelliJ IDEA**

---

## Entity-Relationship Diagram
<img width="957" height="878" alt="BookingTransport_ERDiagram" src="https://github.com/user-attachments/assets/c140f3af-b276-450c-b1f9-263e70df72f2" />

