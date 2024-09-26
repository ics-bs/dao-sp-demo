-- Create the SQL login for java_app_user with a secure password
CREATE LOGIN java_app_user
WITH PASSWORD = '<Your Strong Password>';

-- Switch to the target database
USE CompanyDB;

-- Create a user in the database mapped to the login
CREATE USER java_app_user FOR LOGIN java_app_user;

-- Grant EXECUTE permission on specific procedures
GRANT EXECUTE ON uspGetAllEmployeesWithDepartments 
TO java_app_user;

GRANT EXECUTE ON uspGetAllEmployees
TO java_app_user;

GRANT EXECUTE ON uspGetEmployeeByEmpNo 
TO java_app_user;

GRANT EXECUTE ON uspInsertEmployee 
TO java_app_user;

GRANT EXECUTE ON uspUpdateEmployee 
TO java_app_user;

GRANT EXECUTE ON uspDeleteEmployee 
TO java_app_user;