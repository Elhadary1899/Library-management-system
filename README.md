# 📚 Library Management System

## 📝 Description
The Library Management System is a JavaFX-based desktop application designed to efficiently manage library transactions. It provides distinct functionalities for members and admins, allowing users to search, 
borrow, return books, and manage members. The system follows Object-Oriented Programming (OOP) principles, ensuring clean and maintainable code. <br>
This system provides two distinct user roles:
- **Members** can search for books, borrow, return, and even purchase books. They can also track their borrowing history, check due dates, and manage fines.
- **Admins** have extended control, including managing books, overseeing members, handling overdue returns, and adding new administrators.

For data persistence and security, the system integrates Microsoft SQL Server, ensuring efficient database management for book records, user accounts, transactions, and authentication.
To enhance reliability, the system includes robust input validation using regular expressions and exception handling, preventing invalid inputs and unexpected errors, and ensuring smooth user experience.
<br>

## ✏️ Problem Description
Library management has traditionally been a manual process, leading to inefficiencies such as:
- Difficulty in tracking borrowed books, often resulting in lost or overdue books.
- Manual record-keeping errors, leading to misplaced data and inconsistencies.
- No centralized system for members to search for books or track their borrowing status.
- Inefficient fine management, making it difficult to calculate and collect overdue fines.
- Limited administrative control, making book and member management tedious.

This project was originally developed as a final project for the "Introduction to Databases" course, where we had to design and implement a simple library system. However, we aimed to go beyond the basic requirements by building a fully-functional system with:
- A modern GUI for seamless user interaction
- Role-based access control for members and admins
- Efficient borrowing, returning, and purchasing features
- Automated fine calculation & tracking
- A secure and scalable database with MS SQL Server

By addressing these challenges, our Library Management System transforms the traditional library experience into a fast, accurate, and user-friendly process.🚀

## ✨ Features
- User-friendly GUI built with JavaFX
- Secure Login & Sign-Up with data validation
- Separate functionalities for both members and admins
- Robust handling for both buying and borrowing transactions
- Monitor borrowed books and check if the due date has passed
- Automatic fine calculation for late returns

## 🔎 Common Functionalities between admins and members
- Search & Filter books by category, availability, and author for better search results

## 👤 Member Functionalities
- Borrow available books
- Buy available books
- Return Borrowed Books
- Checking for his borrow status (recently borrowed books & due dates)
- Check borrow history (previously borrowed books, borrow date, due date, return date, and any fines)
- Calculate & Pay fines (if applicable)

## 🔐 Admin Functionalities
- Add & Remove Books from the system
- Edit existing books in the system
- Search for a member in the system
- Remove a member from the system
- view all registered members
- List all overdue borrowed books
- Add new admins to the system

## 📌 Project Phases
#### 1️⃣ DataBase Design
The first challenge was to analyze the business requirements and design an efficient database structure. We created an Entity-Relationship Diagram (ERD) that accurately represents the relationships between different entities in the system.
<br>
**Final ERD Design:**
![ERD](https://github.com/user-attachments/assets/2807f31a-1810-41a2-8317-5539944dee6a)

#### 2️⃣ Mapping Entities into Tables
Once the ERD was finalized,  we needed to map entities into relational tables, ensuring proper normalization to enhance efficiency, integrity, and performance.
<br>
**Final Database Schema:**
![image](https://github.com/user-attachments/assets/ed8f7659-3b23-4992-904b-a7bbb22dbf81)

#### 3️⃣ Database Implementation & Population
At this stage, Maram implemented the database using MS SQL Server, ensuring a structured and optimized setup. The implementation included:
- Table creation with appropriate constraints (Primary Keys, Foreign Keys, Other Constraints)
- Stored Procedures for key operations
- Views & Indexes for optimized querying
- Data Population with meaningful test data for real-world simulation

#### 4️⃣ Application Development
With the database in place, the next step was to build the Java application following Object-Oriented Programming (OOP) principles to ensure:
- Clean, scalable, and maintainable code
- Robust input validation using regular expressions
- Exception handling to prevent invalid inputs & errors
- A visually appealing UI built with JavaFX, designed with a vintage-inspired, eye-soothing theme

#### 5️⃣ JDBC
Finally, we integrated the Java application with MS SQL Server using the JDBC driver, enabling seamless interaction between the Java application and database.<br> 
This phase ensured:
- Efficient transaction handling for borrowing, returning, and purchasing books
- Real-time data retrieval and updates
- Secure & optimized database communication

## 🛠️ Technologies Used
- Java (Core functionality & Classes)
- JavaFX (GUI)
- CSS (Styling the JavaFX interface)
- MS SQL Server (Database for data persistence)
- JDBC (Java Database Connectivity for database interactions)
- Regular Expressions (Input validation)
- Exception Handling (Ensuring smooth execution)

## 🎯 Conclusion
The Library Management System was designed to simplify and automate the process of managing books, members, and transactions in a digital library. By implementing a well-structured database, a user-friendly interface, and efficient backend operations, we successfully developed a fully-functional and scalable library management solution.


