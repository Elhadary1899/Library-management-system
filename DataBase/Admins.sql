use Library
-- Admin


-- ##########################
-- Admins Procedures and view 
-- ##########################


-- **************************************************************************
-- 1- entering system procedures (Signin - add admins to system )
-- **************************************************************************





CREATE PROCEDURE AdminSignIn 
    @Email NVARCHAR(100), 
    @Password NVARCHAR(100) 
AS
BEGIN 
    -- Check if admin exists with correct password
    IF EXISTS (
        SELECT 1 FROM Users 
        WHERE Email = @Email 
        AND UserType = 'Admin' 
        AND Password = HASHBYTES('SHA2_256', @Password)
    )
    BEGIN
        SELECT UserID, FirstName, 'Sign-in Successful' AS message
        FROM Users 
        WHERE Email = @Email;
    END
    ELSE
    BEGIN
        select 'Invalid email or password!' as message;
    END
END;

-- ##########################

CREATE PROCEDURE AddNewAdmins 
    @FirstName NVARCHAR(50),
    @LastName NVARCHAR(50),
    @Email NVARCHAR(100),
    @Phone NVARCHAR(20),
    @Address NVARCHAR(255),
    @Password NVARCHAR(100) 
AS 
BEGIN 
    BEGIN TRY
        -- Check if Email or Phone already exists
        IF EXISTS (SELECT 1 FROM Users WHERE Email = @Email and FirstName=@FirstName)
        BEGIN
            SELECT 'Admin with this Email already exists' AS message;
            RETURN;
        END

        IF EXISTS (SELECT 1 FROM Users WHERE Phone = @Phone and FirstName=@FirstName)
        BEGIN
            SELECT 'Admin with this Phone already exists' AS message;
            RETURN;
        END

        -- Insert new admin
        INSERT INTO Users (FirstName, LastName, Email, Phone, Address, RegistrationDate, Password, UserType) 
        VALUES (@FirstName, @LastName, @Email, @Phone, @Address, GETDATE(), HASHBYTES('SHA2_256', @Password), 'Admin');

        SELECT 'Admin Added Successfully' AS message;
    END TRY
    BEGIN CATCH
        -- Catch SQL errors, including UNIQUE constraint violations
        SELECT ERROR_MESSAGE() AS message;
    END CATCH
END;

-- ##########################

-- **************************************************************************
-- 2- view admins can see (admins - memebers - available books - all books)
-- **************************************************************************




Create view Members as
select  UserID ,
    FirstName,
    LastName, 
    Email ,
    Phone, 
    Address ,
    RegistrationDate  from Users
	where UserType = 'Member';

-- ##########################

Create View Admins as
select  UserID ,
    FirstName,
    LastName, 
    Email ,
    Phone, 
    Address ,
    RegistrationDate  from Users
	where UserType = 'Admin';


-- ##########################

	
Create View AvailableBooks
as
Select Distinct B.ISBN, B.BookName, B.Price, B.BorrowPrice, C.CategoryName as 'Category', A.AuthorName, P.PublisherName From 
Book B inner join BookCopy BC 
on B.ISBN = BC.BookISBN
inner join Category C
on C.CategoryID = B.CategoryID
inner join Author A
on A.AuthorID = B.AuthorID
inner join Publisher P
on P.PublisherID = B.PublisherID
WHERE BC.Available = 1 AND BC.Sold = 0;

-- ##########################



create view AllBooks as
SELECT dbo.Book.ISBN, dbo.Book.BookName, dbo.Book.Price, dbo.Book.BorrowPrice, 
dbo.Book.PublishDate, dbo.Category.CategoryName, dbo.Publisher.PublisherName, 
dbo.Author.AuthorName
FROM     dbo.Author INNER JOIN
                  dbo.Book ON dbo.Author.AuthorID = dbo.Book.AuthorID INNER JOIN
                  dbo.Category ON dbo.Book.CategoryID = dbo.Category.CategoryID INNER JOIN
                  dbo.Publisher ON dbo.Book.PublisherID = dbo.Publisher.PublisherID 


-- ##########################


-- **************************************************************************
--3- search procedures For Books (search by Name or Author name or category )
-- **************************************************************************


-- Indexes THat helped with search 
 create index BookNameSearch on Book(BookName);
 Create index AuthorNameSearch on Author(AuthorName);

CREATE PROCEDURE SearchBookByName
    @BookName NVARCHAR(100)
AS
BEGIN
    
   IF EXISTS (SELECT 1 FROM Book WHERE BookName LIKE '%' + @BookName + '%')

   begin
    SELECT b.ISBN, b.BookName, b.Price, b.BorrowPrice, 
           b.PublishDate, c.CategoryName, p.PublisherName, a.AuthorName
    FROM Book b
    INNER JOIN Category c ON b.CategoryID = c.CategoryID
    INNER JOIN Publisher p ON b.PublisherID = p.PublisherID
    INNER JOIN Author a ON b.AuthorID = a.AuthorID
    WHERE b.BookName LIKE '%' + @BookName + '%';
	return ;
	end

	select 'Book Not Found' as message ;

END;


-- ##########################

create or ALTER PROCEDURE [dbo].[SearchBookByAuthorName]
    @AuthorName NVARCHAR(100)
AS
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM Book b
        INNER JOIN Author A ON A.AuthorID = b.AuthorID
        WHERE A.AuthorName LIKE @AuthorName + '%'
    )
	begin
    SELECT B.BookName ,B.PublishDate , CA.CategoryName FROM Book B Inner join 
	Author 
	on Author.AuthorID=B.AuthorID
	Inner join 
	Category CA 
	on CA.CategoryID=B.CategoryID
    WHERE AuthorName LIKE '%'+ @AuthorName + '%';
	end 
	else
	begin 
	select 'No Books Found For This Author ' as message
	end
END;

-- ##########################


create or ALTER PROCEDURE [dbo].[SearchBookByCategory] 
    @CategoryName NVARCHAR(100)
AS
BEGIN
    -- Check if there are books in the given category
    IF EXISTS (
        SELECT 1 
        FROM Book b
        INNER JOIN Category c ON c.CategoryID = b.CategoryID
        WHERE c.CategoryName LIKE @CategoryName + '%'
    )
    BEGIN
        -- If books exist, return the book details
        SELECT 
            b.BookName, 
            a.AuthorName, 
            b.PublishDate 
        FROM Book b
        INNER JOIN Author a ON a.AuthorID = b.AuthorID
        INNER JOIN Category c ON c.CategoryID = b.CategoryID
        WHERE c.CategoryName LIKE @CategoryName + '%';
    END
    ELSE
    BEGIN
        -- If no books exist, return a message
        select 'No books found in this category.' as message;
    END
END;


-- ##########################
                
-- **************************************************************************
-- 4- Book procedures For Admins (add book - remove book - edit book info (name-price-author name )- list over due books)
-- **************************************************************************


Create Procedure AddBook
@ISBN nvarchar(100)
,@BookName nvarchar(100)
,@AuthorName nvarchar(100)
,@PublisherName nvarchar(100)
,@Category nvarchar(100)
,@BorrowPrice decimal(10,2)
,@Price decimal(10,2)
,@PublishDate date
,@NumOfCopies int
as
Begin
IF NOT EXISTS (SELECT 1 FROM Book WHERE ISBN = @ISBN)
Begin
Declare @AuthorID int, @PublisherID int , @CategoryID int 
 select @AuthorID = AuthorID From Author where AuthorName=@AuthorName
 select @PublisherID= PublisherID from Publisher Where PublisherName=@PublisherName
 select @CategoryID=CategoryID from Category where CategoryName=@Category

 if @AuthorID is null 
 begin 
 insert into Author(AuthorName) values(@AuthorName)
 SET @AuthorID = SCOPE_IDENTITY();
 end
 
 if @PublisherID is null 
 begin 
 insert into Publisher(PublisherName) values(@PublisherName)
 SET @PublisherID = SCOPE_IDENTITY();
 end

 if @CategoryID is null 
 begin 
 insert into Category(CategoryName) values(@Category)
 SET @CategoryID = SCOPE_IDENTITY();
 end
 Insert into Book (ISBN,BookName,AuthorID,PublisherID,CategoryID,BorrowPrice,price,PublishDate)
 VALUES (@ISBN,@BookName,@AuthorID,@PublisherID,@CategoryID,@BorrowPrice,@Price,@PublishDate)
 declare @Counter int =1;
 While @Counter<=@NumOfCopies
 begin
 Insert into BookCopy(BookISBN) values (@ISBN)
 set @Counter=@Counter+1
 end
select 'Book Added Successfully' as message
end;
else
begin  
select 'Book Already Exists' as message
end

End;

-- ##########################


CREATE PROCEDURE EditBookInfo
    @BookName NVARCHAR(100),
    @ToEdit INT,
    @Input NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if the book exists
    IF EXISTS (SELECT 1 FROM Book WHERE BookName = @BookName)
    BEGIN
        -- Editing Book Name
        IF @ToEdit = 1
        BEGIN
            UPDATE Book
            SET BookName = @Input
            WHERE BookName = @BookName;
        END

        -- Editing Author Name
        ELSE IF @ToEdit = 2
        BEGIN
            DECLARE @AuthorID INT;

            -- Check if author exists
            SELECT @AuthorID = AuthorID FROM Author WHERE AuthorName = @Input;

            -- If author doesn't exist, insert it
            IF @AuthorID IS NULL
            BEGIN
                INSERT INTO Author (AuthorName) VALUES (@Input);
                SET @AuthorID = SCOPE_IDENTITY(); -- Get new author ID
            END
			
            -- Update Book with the new AuthorID
            UPDATE Book
            SET AuthorID = @AuthorID
            WHERE BookName = @BookName;
        END

        -- Editing Book Price
        ELSE IF @ToEdit = 3
        BEGIN
            UPDATE Book
            SET Price = TRY_CAST(@Input AS DECIMAL(10,2))
            WHERE BookName = @BookName;
        END

        -- Editing Borrow Price
        ELSE IF @ToEdit = 4
        BEGIN
            UPDATE Book
            SET BorrowPrice = TRY_CAST(@Input AS DECIMAL(10,2))
            WHERE BookName = @BookName;
        END

        -- Invalid Edit Option
        ELSE
        BEGIN
            select 'Invalid Edit Option!' as message;
            RETURN;
        END

        -- Success Message
        select 'Book information updated successfully!' as message;
    END
    ELSE
    BEGIN
        select 'Book Not Found!' as message;
    END
END;

-- ##########################

CREATE PROCEDURE RemoveBook 
    @BookName NVARCHAR(100)
AS 
BEGIN 
    -- Check if book exists
    IF EXISTS (SELECT 1 FROM Book WHERE BookName LIKE @BookName + '%')
    BEGIN
        -- Delete the book (BookCopy will be auto-deleted due to ON DELETE CASCADE)
        DELETE FROM Book WHERE BookName LIKE @BookName + '%';

        -- Success message
        SELECT 'Book Removed Successfully' AS Message;
    END
    ELSE
    BEGIN
        -- Book not found message
        SELECT 'Book Not Found' AS Message;
    END
END;

-- ##########################



CREATE PROCEDURE ListOverdueBooks
AS
BEGIN
    SELECT * FROM BorrowRecords WHERE ReturnDate IS NULL AND DueDate < GETDATE();
END;

-- ##########################


-- **************************************************************************
--5-  procedures admin can do on membres (remove members- search for members)
-- **************************************************************************

CREATE PROCEDURE RemoveMember 
    @UserID INT
AS
BEGIN
    -- Check if the user exists and is a member
    IF EXISTS (SELECT 1 FROM Users WHERE UserID = @UserID AND UserType = 'Member')
    BEGIN
        -- Delete the member (ensure no foreign key conflicts)
        DELETE FROM Users WHERE UserID = @UserID;
        
        -- Success message
        SELECT 'Member deleted successfully' AS Message;
    END
    ELSE 
    BEGIN
        -- User not found or is not a member
        SELECT 'Member Not Found or Cannot Be Deleted' AS Message;
    END
END;

-- ##########################

CREATE PROCEDURE SearchForMember 
    @SearchKeyword NVARCHAR(100) 
AS 
BEGIN 
    IF ISNUMERIC(@SearchKeyword) = 1 
    BEGIN
        -- Search by UserID only when the input is numeric
        SELECT * FROM Members WHERE UserID = CAST(@SearchKeyword AS INT);
    END
    ELSE
    BEGIN
        -- Search by other text fields if input is not numeric
        SELECT * FROM Members 
        WHERE 
            Email LIKE '%' + @SearchKeyword + '%' OR
            FirstName LIKE '%' + @SearchKeyword + '%' OR
            LastName LIKE '%' + @SearchKeyword + '%' OR
            Phone LIKE '%' + @SearchKeyword + '%';
    END

    -- If no results are found, return a message
    IF @@ROWCOUNT = 0
    BEGIN
        SELECT 'No member found with the given search keyword.' AS message;
    END
END;




