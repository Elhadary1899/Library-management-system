# Library Management System - JavaFX

## Overview

This Library Management System is a JavaFX application that demonstrates the principles of Object-Oriented Programming (OOP) such as inheritance, encapsulation, and polymorphism. The system provides functionalities for both members and admins to manage books and users effectively. The project was a part of my Java development internship at CognoRise Info Tech company.

## Features

### Welcome Page
- A start button initializes the database and navigates to the sign-in or sign-up page.

### Sign-In/Sign-Up
- Users can choose to sign in or sign up as either as an admin or as a member.
- Sign-up involves entering data validated with regular expressions and checking for existing users.
- After sign-up as a member, users receive an ID and can confirm their data before signing in.

### Member Features
- View all available books
- Search for books by title or author
- Place an order for a book (with availability validation)
- Borrow a book (with availability validation)
- Return a book (with late return checks and fines)
- View borrow status and history
- Calculate and pay fines
- Proceed in days to move forward with the current date

### Admin Features
- View all available books
- Search for books by title or author
- Add, remove, and edit books
- Search, add, remove, and edit members
- View all members
- Fine members

## Technologies Used
- JavaFX for GUI
- Java for backend logic
- Regular expressions for input validation
- Try-catch blocks for exception handling

