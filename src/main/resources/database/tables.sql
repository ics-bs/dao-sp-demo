CREATE TABLE Course
(
    CourseID 	INT IDENTITY(1,1), -- Surrogate key
    CourseCode VARCHAR(5) NOT NULL, -- Natural key with NOT NULL
    CourseName VARCHAR(100),
    Credits INT,
    CONSTRAINT PK_Course_CourseID PRIMARY KEY(CourseID),
    CONSTRAINT UQ_Course_CourseCode UNIQUE(CourseCode) -- Unique constraint for natural key
);

CREATE TABLE Student
(
    StudentID INT IDENTITY(1,1), -- Surrogate key
    StudentNo VARCHAR(5) NOT NULL, -- Natural key with NOT NULL
    StudentName VARCHAR(100),
    StudentAddress VARCHAR(100),
    CONSTRAINT PK_Student_StudentID PRIMARY KEY(StudentID),
    CONSTRAINT UQ_Student_StudentNo UNIQUE(StudentNo) -- Unique constraint for natural key
);

CREATE TABLE HasStudied
(
    StudentID INT, 
    CourseID INT,
    Grade INT,
    CONSTRAINT PK_HasStudied_StudentID_CourseID PRIMARY KEY(StudentID, CourseID),
    CONSTRAINT FK_HasStudied_Student_StudentID FOREIGN KEY(StudentID) 
    REFERENCES Student(StudentID),
    CONSTRAINT FK_HasStudied_Course_CourseID FOREIGN KEY(CourseID) 
    REFERENCES Course(CourseID)
);
