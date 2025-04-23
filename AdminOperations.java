package CollegeManagement;
import java.sql.*;
import java.util.Scanner;

public class AdminOperations {
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String user = "postgres";
    static String password = "Teju@123";

    public static void insertDefaults() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String insertAdmin = "INSERT INTO admin (username, password) VALUES (?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement ps = con.prepareStatement(insertAdmin)) {
                ps.setString(1, "admin");
                ps.setString(2, "admin@123");
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertCourseDefaults() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = """
                INSERT INTO Course_clgPro (course_name, professor_id)
                VALUES 
                ('Java Programming', 1),
                ('Data Structures',  1),
                ('Operating Systems', 2),
                ('Python Programming', 2),
                ('Data Base Management System ',3),
                ('FrameWorks ',4)
                ON CONFLICT DO NOTHING;
            """;
            try (Statement st = con.createStatement()) {
                st.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void handleAdmin(Scanner sc) {
        System.out.print("Enter admin username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pwd = sc.nextLine();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String checkAdmin = "SELECT * FROM admin WHERE username = ? AND password = ?";
            try (PreparedStatement ps = con.prepareStatement(checkAdmin)) {
                ps.setString(1, uname);
                ps.setString(2, pwd);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    adminMenu(con, sc);
                } else {
                    System.out.println("Invalid credentials.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void adminMenu(Connection con, Scanner sc) throws SQLException {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Course");
            System.out.println("2. View Courses");
            System.out.println("3. Delete Course");
            System.out.println("4. View All Students");
            System.out.println("5. Delete Student");
            System.out.println("6. View All Professors");
            System.out.println("7. Delete Professor");
            System.out.println("8. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Course Name: ");
                    String cname = sc.nextLine();
                    System.out.print("Professor ID: ");
                    int pid = Integer.parseInt(sc.nextLine());
                    String insert = "INSERT INTO Course_clgPro (course_name, professor_id) VALUES (?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(insert)) {
                        ps.setString(1, cname);
                        ps.setInt(2, pid);
                        ps.executeUpdate();
                    }
                }
                case 2 -> {
                    ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Course_clgPro");
                    while (rs.next()) {
                        System.out.println(rs.getInt("id") + " | " + rs.getString("course_name"));
                    }
                }
                case 3 -> {
                    System.out.print("Enter Course ID to delete: ");
                    int cid = Integer.parseInt(sc.nextLine());
                    PreparedStatement ps = con.prepareStatement("DELETE FROM Course_clgPro WHERE id = ?");
                    ps.setInt(1, cid);
                    ps.executeUpdate();
                }
                case 4 -> {
                    ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Student_clgPro");
                    while (rs.next()) {
                        System.out.println(rs.getInt("id") + " | " + rs.getString("name"));
                    }
                }
                case 5 -> {
                    System.out.print("Enter student username: ");
                    String uname = sc.nextLine();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM Student_clgPro WHERE username = ?");
                    ps.setString(1, uname);
                    ps.executeUpdate();
                }
                case 6 -> {
                    ResultSet rs = con.createStatement().executeQuery("SELECT * FROM professor");
                    while (rs.next()) {
                        System.out.println(rs.getInt("id") + " | " + rs.getString("name"));
                    }
                }
                case 7 -> {
                    System.out.print("Enter professor username: ");
                    String uname = sc.nextLine();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM professor WHERE username = ?");
                    ps.setString(1, uname);
                    ps.executeUpdate();
                }
                case 8 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
