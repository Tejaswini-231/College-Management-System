package CollegeManagement;
import java.sql.*;
import java.util.Scanner;

public class ProfessorOperations {
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String user = "postgres";
    static String password = "Teju@123";

    public static void insertDefaults() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = """
                INSERT INTO professor (name, username, password) 
                VALUES 
                ('RamaKanth Reddy', 'rama123', 'pass123'),
                ('Kzithiz Milgani', 'KzM456', 'pass456'), 
                ('Subodh Sant', 'ss456', 'pass789'),
                ('Gaurav Sen', 'gcs456', 'pass1011')
                ON CONFLICT (username) DO NOTHING;
            """;
            con.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void handleProfessor(Scanner sc) {
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pwd = sc.nextLine();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM professor WHERE username = ? AND password = ?");
            ps.setString(1, uname);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int pid = rs.getInt("id");

                while (true) {
                    System.out.println("\nProfessor Menu:");
                    System.out.println("1. View My Courses");
                    System.out.println("2. View Students in a Course");
                    System.out.println("3. Grade Student");
                    System.out.println("4. Back");

                    int ch = Integer.parseInt(sc.nextLine());

                    switch (ch) {
                        case 1 -> {
                            ps = con.prepareStatement("SELECT * FROM Course_clgPro WHERE professor_id = ?");
                            ps.setInt(1, pid);
                            ResultSet res = ps.executeQuery();
                            System.out.println("Your Courses:");
                            while (res.next()) {
                                System.out.println("Course ID: " + res.getInt("id") + " | Name: " + res.getString("course_name"));
                            }
                        }
                        case 2 -> {
                            System.out.print("Course ID: ");
                            int cid = Integer.parseInt(sc.nextLine());

                            // Get course name
                            String courseName = "";
                            ps = con.prepareStatement("SELECT course_name FROM Course_clgPro WHERE id = ? AND professor_id = ?");
                            ps.setInt(1, cid);
                            ps.setInt(2, pid);
                            ResultSet cnameResult = ps.executeQuery();
                            if (cnameResult.next()) {
                                courseName = cnameResult.getString("course_name");
                            } else {
                                System.out.println("Invalid course ID or not assigned to you.");
                                break;
                            }

                            // Now fetch enrolled students
                            ps = con.prepareStatement("""
                                SELECT s.id, s.name 
                                FROM Enrollment_clgPro e
                                JOIN Student_clgPro s ON e.student_id = s.id
                                WHERE e.course_id = ?
                            """);
                            ps.setInt(1, cid);
                            ResultSet res = ps.executeQuery();

                            System.out.println("Students enrolled in \"" + courseName + "\":");
                            boolean found = false;
                            while (res.next()) {
                                found = true;
                                System.out.println("Student ID: " + res.getInt("id") + " | Name: " + res.getString("name"));
                            }
                            if (!found) {
                                System.out.println("No students enrolled in this course.");
                            }
                        }
                        case 3 -> {
                            System.out.print("Student ID: ");
                            int sid = Integer.parseInt(sc.nextLine());
                            System.out.print("Course ID: ");
                            int cid = Integer.parseInt(sc.nextLine());
                            System.out.print("Grade: ");
                            String grade = sc.nextLine();

                            String q = """
                                INSERT INTO grade (student_id, course_id, grade)
                                VALUES (?, ?, ?)
                                ON CONFLICT (student_id, course_id) DO UPDATE SET grade = EXCLUDED.grade
                            """;
                            ps = con.prepareStatement(q);
                            ps.setInt(1, sid);
                            ps.setInt(2, cid);
                            ps.setString(3, grade);
                            ps.executeUpdate();
                            System.out.println("Grade assigned.");
                        }
                        case 4 -> {
                            return;
                        }
                        default -> System.out.println("Invalid choice.");
                    }
                }
            } else {
                System.out.println("Invalid login.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
