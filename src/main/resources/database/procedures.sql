-- =============================================
-- Author:        [Björn Svensson]
-- Create date:   [2024-10-27]
-- Description:   Returns all Employee columns except EmployeeID (surrogate key).
-- =============================================
CREATE OR ALTER PROCEDURE uspGetAllEmployees
AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

    -- Turn off the message count for performance
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        SELECT 
            EmpNo, 
            EmpName, 
            EmpSalary
        FROM Employee;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        ;THROW;

    END CATCH
END;

GO

-- =============================================
-- Author:        [Björn Svensson]
-- Create date:   [2024-10-27]
-- Description:   Returns the Employee details for the given EmpNo, excluding EmployeeID (surrogate key).
-- =============================================
CREATE OR ALTER PROCEDURE uspGetEmployeeByEmpNo
    @EmpNo VARCHAR(10)
AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

    -- Turn off the message count for performance
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        SELECT 
            EmpNo, 
            EmpName, 
            EmpSalary
        FROM Employee
        WHERE EmpNo = @EmpNo;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        ;THROW;

    END CATCH
END;

GO

-- =============================================
-- Author:        [Björn Svensson]
-- Create date:   [2024-10-27]
-- Description:   Inserts a new Employee into the Employee table, excluding EmployeeID (surrogate key).
-- Parameters:    
--   @EmpNo     - The unique employee number for the new employee.
--   @EmpName   - The name of the new employee.
--   @EmpSalary - The salary of the new employee.
-- =============================================
CREATE OR ALTER PROCEDURE uspInsertEmployee
    @EmpNo     VARCHAR(10),
    @EmpName   VARCHAR(50),
    @EmpSalary DECIMAL(19,2)
AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

    -- Turn off the message count for performance
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -- Insert the new employee
        INSERT INTO Employee (EmpNo, EmpName, EmpSalary)
        VALUES (@EmpNo, @EmpName, @EmpSalary);

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        ;THROW;

    END CATCH
END;

GO

-- =============================================
-- Author:        [Björn Svensson]
-- Create date:   [2024-10-27]
-- Description:   Updates Employee details for the given EmpNo, excluding EmployeeID (surrogate key).
-- Parameters:    
--   @EmpNo     - The employee number (business key) of the employee to update.
--   @EmpName   - The new name for the employee.
--   @EmpSalary - The new salary for the employee.
-- =============================================
CREATE OR ALTER PROCEDURE uspUpdateEmployee
    @EmpNo     VARCHAR(10),
    @EmpName   VARCHAR(50),
    @EmpSalary DECIMAL(19,2)
AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        UPDATE Employee
        SET EmpName = @EmpName, 
            EmpSalary = @EmpSalary
        WHERE EmpNo = @EmpNo;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        ;THROW;

    END CATCH
END;

GO 

-- =============================================
-- Author:        [Björn Svensson]
-- Create date:   [2024-10-27]
-- Description:   Deletes an Employee based on the given EmpNo.
-- Parameters:    
--   @EmpNo  - The employee number (business key) of the employee to delete.
-- =============================================
CREATE OR ALTER PROCEDURE uspDeleteEmployee
    @EmpNo VARCHAR(10)
AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

    -- Turn off the message count for performance
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -- Delete the employee where EmpNo matches
        DELETE 
		FROM Employee
        WHERE EmpNo = @EmpNo;

        -- Commit the transaction
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH

        -- Rollback the transaction in case of error
        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        -- Re-throw the error
        ;THROW;

    END CATCH
END;

GO

-- =============================================
-- Author:        [Björn Svensson]
-- Create date:   [2024-10-27]
-- Description:   Retrieves all employees and their respective department names.
-- =============================================
CREATE OR ALTER PROCEDURE uspGetAllEmployeesWithDepartments
AS
BEGIN
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        SELECT 
            Employee.EmpNo, 
            Employee.EmpName, 
            Employee.EmpSalary, 
            Department.DeptName,
            Department.DeptBudget
        FROM Employee
        JOIN Work ON Employee.EmployeeID = Work.EmployeeID
        JOIN Department ON Work.DepartmentID = Department.DepartmentID;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
        BEGIN
            ROLLBACK TRANSACTION;
        END

        ;THROW;

    END CATCH
END;