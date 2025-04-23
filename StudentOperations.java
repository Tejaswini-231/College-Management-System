package CollegeManagement;
import java.sql.*;
import java.util.Scanner;

public class StudentOperations {
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String user = "postgres";
    static String password = "Teju@123";

    public static void insertDefaults() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = """
                INSERT INTO Student_clgPro (name, username, password) 
                VALUES 
                ('Tejaswini', 'tej123', 'abc123'),
                ('Sumiya', 'sumi123', 'abc456'),
                ('Kalpana', 'kalp123', 'abc789'),
                ('Thrisha', 'tris123', 'abc1011'),
                ('Keerthi', 'keer123', 'abc1213'),
                ('Rishitha', 'rishi123', 'abc1415')
                ON CONFLICT (username) DO NOTHING;
            """;
            con.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void handleStudent(Scanner sc) {
        System.out.print("Username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pwd = sc.nextLine();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Student_clgPro  WHERE username = ? AND password = ?");
            ps.setString(1, uname);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int sid = rs.getInt("id");
                while (true) {
                    System.out.println("\nStudent Menu:");
                    System.out.println("1. View All Courses");
                    System.out.println("2. Enroll in a Course");
                    System.out.println("3. View My Enrollments");
                    System.out.println("4. View My Grades");
                    System.out.println("5. Back");

                    int ch = Integer.parseInt(sc.nextLine());
                    if (ch == 1) {
                        ResultSet res = con.createStatement().executeQuery("SELECT * FROM Course_clgPro");
                        while (res.next()) {
                            System.out.println(res.getInt("id") + " | " + res.getString("course_name"));
                        }
                    } else if (ch == 2) {
                        System.out.print("Course ID: ");
                        int cid = Integer.parseInt(sc.nextLine());
                        ps = con.prepareStatement("INSERT INTO Enrollment_clgPro (student_id, course_id) VALUES (?, ?)");
                        ps.setInt(1, sid);
                        ps.setInt(2, cid);
                        ps.executeUpdate();
                        System.out.println("Enrolled.");
                    } else if (ch == 3) {
                        ps = con.prepareStatement("""
                            SELECT c.id, c.course_name FROM Enrollment_clgPro e
                            JOIN Course_clgPro c ON e.course_id = c.id
                            WHERE e.student_id = ?
                        """);
                        ps.setInt(1, sid);
                        ResultSet res = ps.executeQuery();
                        while (res.next()) {
                            System.out.println(res.getInt("id") + " | " + res.getString("course_name"));
                        }
                    } else if (ch == 4) {
                        ps = con.prepareStatement("""
                            SELECT c.course_name, g.grade FROM grade g
                            JOIN Course_clgPro c ON g.course_id = c.id
                            WHERE g.student_id = ?
                        """);
                        ps.setInt(1, sid);
                        ResultSet res = ps.executeQuery();
                        while (res.next()) {
                            System.out.println(res.getString("course_name") + " | " + res.getString("grade"));
                        }
                    } else break;
                }
            } else {
                System.out.println("Invalid login.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
