package demo;

import java.sql.*;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/classroom_management";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

public class CourseManagement {
    public void addCourse(String title, String instructor, String schedule, int credits) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Course (title, instructor, schedule, credits) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, instructor);
                pstmt.setString(3, schedule);
                pstmt.setInt(4, credits);
                pstmt.executeUpdate();
                System.out.println("Course added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewCourses() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Course";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Course ID: " + rs.getInt("course_id"));
                    System.out.println("Title: " + rs.getString("title"));
                    System.out.println("Instructor: " + rs.getString("instructor"));
                    System.out.println("Schedule: " + rs.getString("schedule"));
                    System.out.println("Credits: " + rs.getInt("credits"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCourse(int courseId, String title, String instructor, String schedule, int credits) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Course SET title = ?, instructor = ?, schedule = ?, credits = ? WHERE course_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, instructor);
                pstmt.setString(3, schedule);
                pstmt.setInt(4, credits);
                pstmt.setInt(5, courseId);
                pstmt.executeUpdate();
                System.out.println("Course updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCourse(int courseId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Course WHERE course_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, courseId);
                pstmt.executeUpdate();
                System.out.println("Course deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


public class StudentManagement {
    public void registerStudent(String name, String email, String phoneNumber, String address) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Student (name, email, phone_number, address) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phoneNumber);
                pstmt.setString(4, address);
                pstmt.executeUpdate();
                System.out.println("Student registered successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewStudents() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Student";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Student ID: " + rs.getInt("student_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Phone Number: " + rs.getString("phone_number"));
                    System.out.println("Address: " + rs.getString("address"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(int studentId, String name, String email, String phoneNumber, String address) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Student SET name = ?, email = ?, phone_number = ?, address = ? WHERE student_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phoneNumber);
                pstmt.setString(4, address);
                pstmt.setInt(5, studentId);
                pstmt.executeUpdate();
                System.out.println("Student updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int studentId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Student WHERE student_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, studentId);
                pstmt.executeUpdate();
                System.out.println("Student deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    public class GradeManagement {
    public void assignGrade(int courseId, int studentId, String grade) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Grade (course_id, student_id, grade) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, courseId);
                pstmt.setInt(2, studentId);
                pstmt.setString(3, grade);
                pstmt.executeUpdate();
                System.out.println("Grade assigned successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewGrades() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Grade";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Grade ID: " + rs.getInt("grade_id"));
                    System.out.println("Course ID: " + rs.getInt("course_id"));
                    System.out.println("Student ID: " + rs.getInt("student_id"));
                    System.out.println("Grade: " + rs.getString("grade"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGrade(int gradeId, int courseId, int studentId, String grade) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Grade SET course_id = ?, student_id = ?, grade = ? WHERE grade_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, courseId);
                pstmt.setInt(2, studentId);
                pstmt.setString(3, grade);
                pstmt.setInt(4, gradeId);
                pstmt.executeUpdate();
                System.out.println("Grade updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeGrade(int gradeId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Grade WHERE grade_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, gradeId);
                pstmt.executeUpdate();
                System.out.println("Grade removed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class GPAService {
    public double calculateGPA(int studentId) {
        double gpa = 0.0;
        int totalCredits = 0;
        int totalPoints = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT g.grade, c.credits FROM Grade g JOIN Course c ON g.course_id = c.course_id WHERE g.student_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, studentId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String grade = rs.getString("grade");
                    int credits = rs.getInt("credits");
                    totalCredits += credits;
                    totalPoints += gradeToPoints(grade) * credits;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (totalCredits > 0) {
            gpa = (double) totalPoints / totalCredits;
        }

        return gpa;
    }

    private int gradeToPoints(String grade) {
        switch (grade) {
            case "A":
                return 4;
            case "B":
                return 3;
            case "C":
                return 2;
            case "D":
                return 1;
            default:
                return 0;
        }
    }
}

public class ClassroomManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CourseManagement courseManagement = new CourseManagement();
        StudentManagement studentManagement = new StudentManagement();
        GradeManagement gradeManagement = new GradeManagement();
        GPAService gpaService = new GPAService();

        boolean exit = false;

        while (!exit) {
            System.out.println("Classroom Management System:");
            System.out.println("1. Course Management");
            System.out.println("2. Student Management");
            System.out.println("3. Grade Management");
            System.out.println("4. Calculate GPA");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageCourses(scanner, courseManagement);
                    break;
                case 2:
                    manageStudents(scanner, studentManagement);
                    break;
                case 3:
                    manageGrades(scanner, gradeManagement);
                    break;
                case 4:
                    calculateGPA(scanner, gpaService);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void manageCourses(Scanner scanner, CourseManagement courseManagement) {
        System.out.println("Course Management:");
        System.out.println("1. Add Course");
        System.out.println("2. View Courses");
        System.out.println("3. Update Course");
        System.out.println("4. Delete Course");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter title: ");
                String title = scanner.nextLine();
                System.out.print("Enter instructor: ");
                String instructor = scanner.nextLine();
                System.out.print("Enter schedule: ");
                String schedule = scanner.nextLine();
                System.out.print("Enter credits: ");
                int credits = scanner.nextInt();
                courseManagement.addCourse(title, instructor, schedule, credits);
                break;
            case 2:
                courseManagement.viewCourses();
                break;
            case 3:
                System.out.print("Enter course ID: ");
                int courseId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter title: ");
                title = scanner.nextLine();
                System.out.print("Enter instructor: ");
                instructor = scanner.nextLine();
                System.out.print("Enter schedule: ");
                schedule = scanner.nextLine();
                System.out.print("Enter credits: ");
                credits = scanner.nextInt();
                courseManagement.updateCourse(courseId, title, instructor, schedule, credits);
                break;
            case 4:
                System.out.print("Enter course ID: ");
                courseId = scanner.nextInt();
                courseManagement.deleteCourse(courseId);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void manageStudents(Scanner scanner, StudentManagement studentManagement) {
        System.out.println("Student Management:");
        System.out.println("1. Register Student");
        System.out.println("2. View Students");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                System.out.print("Enter email: ");
                String email = scanner.nextLine();
                System.out.print("Enter phone number: ");
                String phoneNumber = scanner.nextLine();
                System.out.print("Enter address: ");
                String address = scanner.nextLine();
                studentManagement.registerStudent(name, email, phoneNumber, address);
                break;
            case 2:
                studentManagement.viewStudents();
                break;
            case 3:
                System.out.print("Enter student ID: ");
                int studentId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter name: ");
                name = scanner.nextLine();
                System.out.print("Enter email: ");
                email = scanner.nextLine();
                System.out.print("Enter phone number: ");
                phoneNumber = scanner.nextLine();
                System.out.print("Enter address: ");
                address = scanner.nextLine();
                studentManagement.updateStudent(studentId, name, email, phoneNumber, address);
                break;
            case 4:
                System.out.print("Enter student ID: ");
                studentId = scanner.nextInt();
                studentManagement.deleteStudent(studentId);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void manageGrades(Scanner scanner, GradeManagement gradeManagement) {
        System.out.println("Grade Management:");
        System.out.println("1. Assign Grade");
        System.out.println("2. View Grades");
        System.out.println("3. Update Grade");
        System.out.println("4. Remove Grade");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter course ID: ");
                int courseId = scanner.nextInt();
                System.out.print("Enter student ID: ");
                int studentId = scanner.nextInt();
                System.out.print("Enter grade: ");
                String grade = scanner.next();
                gradeManagement.assignGrade(courseId, studentId, grade);
                break;
            case 2:
                gradeManagement.viewGrades();
                break;
            case 3:
                System.out.print("Enter grade ID: ");
                int gradeId = scanner.nextInt();
                System.out.print("Enter course ID: ");
                courseId = scanner.nextInt();
                System.out.print("Enter student ID: ");
                studentId = scanner.nextInt();
                System.out.print("Enter grade: ");
                grade = scanner.next();
                gradeManagement.updateGrade(gradeId, courseId, studentId, grade);
                break;
            case 4:
                System.out.print("Enter grade ID: ");
                gradeId = scanner.nextInt();
                gradeManagement.removeGrade(gradeId);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void calculateGPA(Scanner scanner, GPAService gpaService) {
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        double gpa = gpaService.calculateGPA(studentId);
        System.out.printf("GPA for student ID %d is %.2f%n", studentId, gpa);
    }
}

