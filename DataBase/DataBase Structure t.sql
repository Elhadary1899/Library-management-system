-- ##########################
-- Create the Library database
-- ##########################


CREATE DATABASE Library;
GO

USE Library;
GO

-- ##########################
-- Tables creation
-- ##########################



-- Publisher table: Stores information about book publishers



CREATE TABLE Publisher (
    PublisherID INT IDENTITY(1,1) PRIMARY KEY, -- Auto-incremented primary key
    PublisherName NVARCHAR(100) NOT NULL -- Name of the publisher
);

-- Insert data into Publisher
INSERT INTO Publisher (PublisherName)
VALUES 
('Penguin Random House'), ('HarperCollins'), ('Macmillan'), ('Hachette'), ('Simon & Schuster'),
('Oxford University Press'), ('Cambridge University Press'), ('McGraw-Hill'), ('Pearson'), ('Wiley'),
('Springer'), ('Elsevier'), ('Cengage Learning'), ('Scholastic'), ('Routledge'),
('MIT Press'), ('Stanford University Press'), ('Dover Publications'), ('University of Chicago Press'), ('John Benjamins');



-- **************************************************************************




-- Author table: Stores information about book authors



CREATE TABLE Author (
    AuthorID INT IDENTITY(1,1) PRIMARY KEY, -- Auto-incremented primary key
    AuthorName NVARCHAR(100) NOT NULL -- Name of the author
);

-- Insert data into Author
INSERT INTO Author (AuthorName)
VALUES 
('J.K. Rowling'), ('Stephen King'), ('George R.R. Martin'), ('J.R.R. Tolkien'), ('Agatha Christie'),
('Isaac Asimov'), ('Dan Brown'), ('Margaret Atwood'), ('Haruki Murakami'), ('Jane Austen'),
('Ernest Hemingway'), ('Mark Twain'), ('Charles Dickens'), ('F. Scott Fitzgerald'), ('Leo Tolstoy'),
('Fyodor Dostoevsky'), ('Gabriel García Márquez'), ('James Joyce'), ('Virginia Woolf'), ('Oscar Wilde');


-- **************************************************************************




-- Category table: Stores book categories



CREATE TABLE Category (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY, -- Auto-incremented primary key
    CategoryName NVARCHAR(100) NOT NULL -- Name of the category
);



-- Insert data into Category
INSERT INTO Category (CategoryName)
VALUES 
('Fiction'), ('Non-Fiction'), ('Science Fiction'), ('Fantasy'), ('Mystery'),
('Biography'), ('History'), ('Self-Help'), ('Education'), ('Psychology'),
('Business'), ('Technology'), ('Science'), ('Religion'), ('Health'),
('Philosophy'), ('Politics'), ('Poetry'), ('Art'), ('Travel');


-- **************************************************************************



-- Book table: Stores information about books



CREATE TABLE Book (
    ISBN NVARCHAR(100) PRIMARY KEY, -- Unique identifier for the book
    BookName NVARCHAR(100) NOT NULL, -- Name of the book
    Price DECIMAL(10,2) NOT NULL, -- Price of the book
    BorrowPrice DECIMAL(10,2) NOT NULL, -- Borrowing price of the book
    PublisherID INT NOT NULL REFERENCES Publisher(PublisherID), -- Foreign key to Publisher
    AuthorID INT NOT NULL REFERENCES Author(AuthorID), -- Foreign key to Author
    CategoryID INT NOT NULL REFERENCES Category(CategoryID), -- Foreign key to Category
    PublishDate DATE NOT NULL -- Date the book was published
);



-- Insert data into Book
INSERT INTO Book (ISBN, BookName, Price, BorrowPrice, PublisherID, AuthorID, CategoryID, PublishDate)
VALUES 
('978-0439139595', 'Harry Potter and the Goblet of Fire', 29.99, 4.99, 1, 1, 4, '2000-07-08'),
('978-0450411434', 'The Shining', 24.50, 3.99, 2, 2, 5, '1977-01-28'),
('978-0553103540', 'A Game of Thrones', 35.00, 5.50, 3, 3, 4, '1996-08-06'),
('978-0261102385', 'The Lord of the Rings', 40.00, 6.50, 4, 4, 4, '1954-07-29'),
('978-0007527502', 'And Then There Were None', 15.99, 2.99, 5, 5, 5, '1939-11-06'),
('978-0553293357', 'Foundation', 18.99, 3.50, 6, 6, 3, '1951-06-01'),
('978-0385504201', 'The Da Vinci Code', 22.99, 4.25, 7, 7, 5, '2003-03-18'),
('978-0385490818', 'The Handmaid''s Tale', 21.50, 3.99, 8, 8, 1, '1985-09-01'),
('978-0099448822', 'Norwegian Wood', 17.99, 3.00, 9, 9, 1, '1987-09-04'),
('978-0141439518', 'Pride and Prejudice', 14.50, 2.75, 10, 10, 1, '1813-01-28');


-- **************************************************************************




-- BookCopy table: Stores individual copies of books




CREATE TABLE BookCopy (
    BookISBN NVARCHAR(100) NOT NULL REFERENCES Book(ISBN) ON DELETE CASCADE, -- Foreign key to Book
    CopyNum INT IDENTITY(1,1) NOT NULL, -- Unique identifier for each copy
    Available BIT DEFAULT 1, -- Indicates if the copy is available (1 = available, 0 = not available)
    Sold BIT DEFAULT 0, -- Indicates if the copy is sold (1 = sold, 0 = not sold)
    PRIMARY KEY (BookISBN, CopyNum) -- Composite primary key
);


-- Insert data into BookCopy
INSERT INTO BookCopy (BookISBN, Available, Sold)
VALUES 
('978-0439139595', 1, 0), ('978-0450411434', 1, 0), ('978-0553103540', 1, 0), ('978-0261102385', 1, 0),
('978-0007527502', 1, 0), ('978-0553293357', 1, 0), ('978-0385504201', 1, 0), ('978-0385490818', 1, 0),
('978-0099448822', 1, 0), ('978-0141439518', 1, 0);

-- **************************************************************************



-- Users table: Stores information about library users



CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1), -- Auto-incremented primary key
    FirstName NVARCHAR(50) NOT NULL, -- User's first name
    LastName NVARCHAR(50), -- User's last name
    Email NVARCHAR(100) UNIQUE NOT NULL, -- User's email (unique)
    Phone NVARCHAR(20) UNIQUE NOT NULL, -- User's phone number (unique)
    Address NVARCHAR(255), -- User's address
    UserType NVARCHAR(20) CHECK (UserType IN ('Admin', 'Member')) NOT NULL, -- User type (Admin, Member, or Guest)
    RegistrationDate DATETIME DEFAULT GETDATE(), -- Date the user registered
    Password VARBINARY(64) NOT NULL -- Hashed password
);


-- Insert data into Users
INSERT INTO Users (FirstName, LastName, Email, Phone, Address, UserType, Password)
VALUES 
('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main St', 'Member', 0x5F4DCC3B5AA765D61D8327DEB882CF99),
('Jane', 'Smith', 'jane.smith@example.com', '0987654321', '456 Elm St', 'Admin', 0x5F4DCC3B5AA765D61D8327DEB882CF99);




-- **************************************************************************




-- BuyRecords table: Stores records of book purchases




CREATE TABLE BuyRecords (
    BookISBN NVARCHAR(100) NOT NULL, -- Foreign key to Book
    CopyNum INT NOT NULL, -- Foreign key to BookCopy
    UserID INT NOT NULL REFERENCES Users(UserID) ON DELETE CASCADE, -- Foreign key to Users
    PurchaseDate DATE DEFAULT GETDATE(), -- Date of purchase
    PurchasePrice DECIMAL(10,2) NOT NULL, -- Price at which the book was purchased
    PRIMARY KEY (BookISBN, CopyNum, UserID), -- Composite primary key
    FOREIGN KEY (BookISBN, CopyNum) REFERENCES BookCopy(BookISBN, CopyNum) ON DELETE CASCADE
);


-- Insert data into BuyRecords


INSERT INTO BuyRecords (BookISBN, CopyNum, UserID, PurchasePrice)
VALUES 
('978-0439139595', 1, 1, 29.99), ('978-0450411434', 2, 2, 24.50);


-- **************************************************************************



-- BorrowRecords table: Stores records of book borrowings



CREATE TABLE BorrowRecords (
    BookISBN NVARCHAR(100) NOT NULL, -- Foreign key to Book
    CopyNum INT NOT NULL, -- Foreign key to BookCopy
    UserID INT NOT NULL REFERENCES Users(UserID) ON DELETE CASCADE, -- Foreign key to Users
    BorrowDate DATE NOT NULL DEFAULT GETDATE(), -- Date the book was borrowed
    DueDate DATE NOT NULL DEFAULT DATEADD(DAY, 30, GETDATE()), -- Due date for returning the book
    ReturnDate DATE, -- Date the book was returned
    TotalPrice DECIMAL(10,2), -- Total price for borrowing
    LateDays INT DEFAULT 0, -- Number of days the book is overdue
    FineAmount DECIMAL(10,2) DEFAULT 0.0, -- Fine amount for overdue books
    PRIMARY KEY (BookISBN, CopyNum, UserID), -- Composite primary key
    FOREIGN KEY (BookISBN, CopyNum) REFERENCES BookCopy(BookISBN, CopyNum) ON DELETE CASCADE
);

-- Insert data into BorrowRecords
INSERT INTO BorrowRecords (BookISBN, CopyNum, UserID, BorrowDate, DueDate, TotalPrice)
VALUES 
('978-0553103540', 3, 1, '2024-01-10', '2024-02-10', 5.50),
('978-0261102385', 4, 2, '2024-02-01', '2024-03-01', 6.50);


-- **************************************************************************




-- FinePayment table: Stores records of fine payments



CREATE TABLE FinePayment (
    UserID INT NOT NULL REFERENCES Users(UserID) on delete cascade, -- Foreign key to Users
    BookISBN NVARCHAR(100) NOT NULL, -- Foreign key to Book
    CopyNum INT NOT NULL, -- Foreign key to BookCopy
    PaymentDay DATE NOT NULL, -- Date the fine was paid
    AmountPaid DECIMAL(10,2) NOT NULL, -- Amount paid
    FOREIGN KEY (BookISBN, CopyNum) REFERENCES BookCopy(BookISBN, CopyNum) -- Composite foreign key
);


INSERT INTO FinePayment (UserID, BookISBN, CopyNum, PaymentDay, AmountPaid)
VALUES 
(1, '978-0553103540', 3, '2024-02-15', 2.00),
(2, '978-0261102385', 4, '2024-03-05', 3.00);


-- **************************************************************************
