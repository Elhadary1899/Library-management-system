USE Library

--Memebers Indexes

CREATE INDEX idx_UserID ON BorrowRecords(UserID);


CREATE INDEX idx_BookISBN ON BookCopy(BookISBN);


-- ##########################
--  Members Procedures
-- ##########################



-- **************************************************************************
--1- Entering system Procedures
-- **************************************************************************

CREATE or alter PROCEDURE SignUp 
    @FirstName NVARCHAR(50),
    @LastName NVARCHAR(50),
    @Email NVARCHAR(100),
    @Phone NVARCHAR(20),
    @Address NVARCHAR(255),
    @Password NVARCHAR(100)  
AS
BEGIN
    DECLARE @NewUserID INT;

    -- Check if the user already exists
    IF EXISTS (SELECT 1 FROM Users WHERE Email = @Email AND UserType = 'Member')
    BEGIN 
        SELECT 'User Already Exists' AS Message, NULL AS UserID;
    END
    ELSE
    BEGIN
        -- Insert new user
        INSERT INTO Users (FirstName, LastName, Email, Phone, Address, Password, UserType) 
        VALUES (@FirstName, @LastName, @Email, @Phone, @Address, HASHBYTES('SHA2_256', @Password), 'Member'); 

        -- Retrieve the newly created UserID
        SET @NewUserID = SCOPE_IDENTITY();

        -- Return success message and UserID
        SELECT 'User Registered Successfully' AS Message, @NewUserID AS UserID;
    END
END;

-- ##########################

CREATE PROCEDURE MemberSignIn
    @Email NVARCHAR(100),
    @Password NVARCHAR(100)
AS
BEGIN 
    DECLARE @UserID INT;

    
    SELECT @UserID = UserID 
    FROM Users 
    WHERE Email = @Email 
      AND Password = HASHBYTES('SHA2_256', @Password) 
      AND UserType = 'Member';

    IF @UserID IS NOT NULL
    BEGIN
        SELECT 'Successful sign-in' AS Message, @UserID AS UserID; 
    END
    ELSE 
    BEGIN
       SELECT 'Invalid Email Or Password' AS Message;
    END
END;

-- ##########################



-- **************************************************************************
--2- Fine procedures related to Member
-- **************************************************************************


Create Procedure CalculateFine 
@UserID int 
as 
begin
 UPDATE BorrowRecords
    SET  LateDays=DATEDIFF(DAY, DueDate, ReturnDate),
	FineAmount = DATEDIFF(DAY, DueDate, ReturnDate) * 10
    WHERE ReturnDate IS not NULL AND DueDate < ReturnDate  
	and UserID=@UserID 
select  COALESCE(sum(FineAmount),0) as TotalFine from BorrowRecords where UserID = @UserID and ReturnDate is not null
end;

-- ##########################

CREATE PROCEDURE PayFine 
    @UserID INT
AS
BEGIN
    SET NOCOUNT ON; -- Prevent extra messages

    -- Declare variables
    DECLARE @ISBN NVARCHAR(100), @CopyNum INT, @FineAmount DECIMAL(10,2), @TotalFine DECIMAL(10,2);

    -- Initialize total fine
    SET @TotalFine = 0;

    -- **Update FineAmount Before Processing**
    UPDATE BorrowRecords
    SET LateDays = DATEDIFF(DAY, DueDate, ReturnDate), 
        FineAmount = DATEDIFF(DAY, DueDate, ReturnDate) * 10
    WHERE ReturnDate IS NOT NULL AND DueDate < ReturnDate  
      AND UserID = @UserID;

    -- **Check if there are any unpaid fines before proceeding**
    IF NOT EXISTS (
        SELECT 1 FROM BorrowRecords
        WHERE UserID = @UserID 
          AND ReturnDate IS NOT NULL
          AND FineAmount > 0
    )
    BEGIN
        SELECT 'No Fine to Pay' AS Message;
        RETURN;
    END;

    -- **Start transaction**
    BEGIN TRANSACTION;

    -- Cursor to loop through books with fines
    DECLARE FineCursor CURSOR FOR 
    SELECT BookISBN, CopyNum, FineAmount 
    FROM BorrowRecords
    WHERE UserID = @UserID 
      AND ReturnDate IS NOT NULL
      AND FineAmount > 0;  -- Only unpaid fines

    OPEN FineCursor;
    FETCH NEXT FROM FineCursor INTO @ISBN, @CopyNum, @FineAmount;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Insert individual fine payment per book
        INSERT INTO FinePayment (UserID, BookISBN, CopyNum, AmountPaid, PaymentDay) 
        VALUES (@UserID, @ISBN, @CopyNum, @FineAmount, GETDATE());

        -- Add to total fine
        SET @TotalFine = @TotalFine + @FineAmount;

        -- Reset the fine amount for the paid book
        UPDATE BorrowRecords 
        SET FineAmount = 0 
        WHERE UserID = @UserID AND BookISBN = @ISBN AND CopyNum = @CopyNum;

        -- Fetch next row
        FETCH NEXT FROM FineCursor INTO @ISBN, @CopyNum, @FineAmount;
    END;

    -- Close and deallocate cursor
    CLOSE FineCursor;
    DEALLOCATE FineCursor;

    -- Commit transaction
    COMMIT TRANSACTION;

    -- Return success message
    SELECT 'Fine Paid Successfully for All Returned Books' AS Message, @TotalFine AS TotalAmountPaid;
END;
-- ##########################


-- **************************************************************************
--3- Book Procedures Related to Member
-- **************************************************************************


-- **************************************************************************
-- a- View Records procedures
-- **************************************************************************


CREATE OR ALTER PROCEDURE BuyRecordeHistory
@UserID INT
AS  
BEGIN
    SELECT BR.UserID, BR.BookISBN, B.BookName, BR.CopyNum, BR.PurchaseDate, BR.PurchasePrice
    FROM dbo.BuyRecords BR 
	Inner JOIN dbo.Book B
        ON B.ISBN = BR.BookISBN 
	WHERE BR.UserID = @UserID
END;



CREATE OR ALTER PROCEDURE BorrowRecordeHistory
@UserID INT
AS  
BEGIN
    SELECT BR.UserID, BR.BookISBN, B.BookName, BR.CopyNum, B.BorrowPrice, BR.BorrowDate, BR.DueDate, BR.LateDays, BR.FineAmount, f.PaymentDay, f.AmountPaid 
    FROM dbo.BorrowRecords BR
    LEFT JOIN dbo.FinePayment f
        ON BR.BookISBN = f.BookISBN 
        AND BR.CopyNum = f.CopyNum 
        AND BR.UserID = f.UserID
	INNER JOIN Book B
	ON B.ISBN = BR.BookISBN
    WHERE BR.UserID = @UserID;
END;

-- ##########################

Create Procedure  CurrentBorrowRecords
@UserID int 
as  
Begin
   UPDATE BorrowRecords
    SET  LateDays=DATEDIFF(DAY, DueDate, ReturnDate),
	FineAmount = DATEDIFF(DAY, DueDate,ReturnDate) * 10
    WHERE   ReturnDate is not null and DueDate < ReturnDate  and FineAmount!=0.00
	     and UserID=@UserID
	UPDATE BorrowRecords
    SET  LateDays=DATEDIFF(DAY, DueDate, GETDATE()),
	FineAmount = DATEDIFF(DAY, DueDate,GETDATE()) * 10
    WHERE  ReturnDate is null and DueDate < ReturnDate  
	     and UserID=@UserID

select BC.UserID, B.BookName, B.ISBN, BC.CopyNum, BC.BorrowDate, BC.DueDate, FORMAT(GETDATE(), 'yyyy-MM-dd') AS 'Current Date', 
		DATEDIFF(DAY, GETDATE(), DueDate) AS DaysLeft, B.BorrowPrice * 0.5 AS 'Fine Amount'
from BorrowRecords BC, Book B 
where BC.BookISBN = B.ISBN and UserID = @UserID and ( ReturnDate is null or  (FineAmount!=0.00 and ReturnDate is not null))

End


-- ##########################


-- **************************************************************************
-- b- Transactions For Books Procedures
-- **************************************************************************


CREATE PROCEDURE BorrowBook 
    @UserID INT,
    @BookName NVARCHAR(100)  
AS
BEGIN
   -- SET NOCOUNT ON;
    DECLARE @ISBN NVARCHAR(100);
    DECLARE @CopyNum INT;
	Declare @BorrowPrice decimal (10,2);


	select TOP 1 @ISBN=c.BookISBN , @CopyNum=c.CopyNum , @BorrowPrice=b.BorrowPrice
	from Book b inner join BookCopy  c
	on b.ISBN=c.BookISBN
	where b.BookName=@BookName and c.Available = 1 
   
    -- Check if the book exists and is available
    IF @ISBN IS NOT NULL AND @CopyNum IS NOT NULL
    BEGIN
        BEGIN TRANSACTION;
        BEGIN TRY
            -- Insert the borrowing record
            INSERT INTO BorrowRecords (BookISBN, CopyNum, UserID, TotalPrice) 
            VALUES (@ISBN, @CopyNum, @UserID, @BorrowPrice);

            -- Mark the book as unavailable
            UPDATE BookCopy 
            SET Available = 0
            WHERE BookISBN = @ISBN AND CopyNum = @CopyNum AND Available = 1;  -- Ensure we only update an available copy

            SELECT 'Order to borrow the book: ' + @BookName + ' is confirmed for: ' + CAST(@BorrowPrice AS NVARCHAR(20)) + '$' + 
			CHAR(10) + 'on: ' + CAST(GETDATE() AS NVARCHAR(20)) 
			+ CHAR(10) + 'You have 7 days to return the book or you will be fined with ' 
			+ CAST(@BorrowPrice * 0.5 AS NVARCHAR(20)) + '$' AS Message;


            COMMIT TRANSACTION;
        END TRY
        BEGIN CATCH
            ROLLBACK TRANSACTION;
            SELECT ERROR_MESSAGE() AS Message;
        END CATCH
    END
    ELSE 
    BEGIN
        SELECT 'Book Not Available' AS Message;
    END
END;

-- ##########################

CREATE PROCEDURE BuyBook 
    @BookName NVARCHAR(100),
    @UserID NVARCHAR(100)
AS
BEGIN
    -- SET NOCOUNT ON;
    DECLARE @ISBN NVARCHAR(100);
    DECLARE @CopyNum INT;
    DECLARE @Price DECIMAL(10,2);

    -- Fetch available book copy and price
    SELECT TOP 1 @ISBN = c.BookISBN, @CopyNum = c.CopyNum, @Price = b.Price
    FROM Book b		
    INNER JOIN BookCopy c ON b.ISBN = c.BookISBN
    WHERE b.BookName = @BookName AND c.Available = 1;

    -- Check if the book exists and is available
    IF @ISBN IS NOT NULL AND @CopyNum IS NOT NULL
    BEGIN
        BEGIN TRANSACTION;
        BEGIN TRY
            -- Insert purchase record
            INSERT INTO BuyRecords (UserID, BookISBN, CopyNum, PurchasePrice) 
            VALUES (@UserID, @ISBN, @CopyNum, @Price);

            -- Mark the book as sold and unavailable
            UPDATE BookCopy 
            SET Sold = 1, Available = 0
            WHERE BookISBN = @ISBN AND CopyNum = @CopyNum 
            AND Available = 1;  -- Ensure we only update an available copy

            SELECT 'Order to buy the book: "' + @BookName + '" is confirmed for: ' + Cast(@Price as nchar(50)) + '$' AS Message;

            COMMIT TRANSACTION;
        END TRY
        BEGIN CATCH
            ROLLBACK TRANSACTION;
            SELECT ERROR_MESSAGE() AS Message;
        END CATCH
    END
    ELSE 
    BEGIN
        SELECT 'Book Not Available' AS Message;
    END
END;

-- ##########################



CREATE or alter PROCEDURE ReturnBook 
    @UserID INT,
    @BookName NVARCHAR(100)
AS
BEGIN
    -- Declare variables
    DECLARE @CopyNum INT, @ISBN NVARCHAR(100), @FineAmount DECIMAL(10,2);

    -- Start a transaction to maintain atomicity
    BEGIN TRANSACTION;

    -- Retrieve the BookISBN and CopyNum for the book being returned
    SELECT @CopyNum = br.CopyNum, 
           @ISBN = br.BookISBN 
    FROM BorrowRecords br
    INNER JOIN Book b ON b.ISBN = br.BookISBN
    WHERE br.UserID = @UserID 
      AND b.BookName = @BookName 
      AND br.ReturnDate IS NULL; -- Only consider active borrow records

    -- If a record is found, process the return
    IF @CopyNum IS NOT NULL AND @ISBN IS NOT NULL
    BEGIN
        -- Update BorrowRecords to set the return date, calculate LateDays and FineAmount
        UPDATE BorrowRecords 
        SET ReturnDate = GETDATE(),
            LateDays = CASE 
                          WHEN GETDATE() > DueDate THEN DATEDIFF(DAY, DueDate, GETDATE()) 
                          ELSE 0 
                       END, 
            FineAmount = CASE 
                            WHEN GETDATE() > DueDate THEN DATEDIFF(DAY, DueDate, GETDATE()) * 10 
                            ELSE 0 
                         END
			
        WHERE UserID = @UserID 
          AND BookISBN = @ISBN 
          AND CopyNum = @CopyNum 
          AND ReturnDate IS NULL;

		-- Retrieve the updated FineAmount
        SELECT @FineAmount = FineAmount 
        FROM BorrowRecords 
        WHERE UserID = @UserID 
          AND BookISBN = @ISBN 
          AND CopyNum = @CopyNum;

        -- Mark the book copy as available again
        UPDATE BookCopy
        SET Available = 1 
        WHERE CopyNum = @CopyNum 
          AND BookISBN = @ISBN;

        -- Commit transaction after successful update
        COMMIT TRANSACTION;
		
        -- Display appropriate message
        IF @FineAmount = 0 
            SELECT 'Thanks for returning the book on time, looking forward to seeing you again :)' AS Message;
        ELSE 
            SELECT 'You have returned the book after the due date, a delay fine of ' 
                    + CAST(@FineAmount AS NVARCHAR(20)) + '$ is added to your account' AS Message;

    END
    BEGIN
        -- Rollback transaction if no valid record found
        ROLLBACK TRANSACTION;
        SELECT 'No records of that book found or already returned' AS Message;
    END
END;

-- ##########################







