package CollegeManagement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Main {
    static String url = "jdbc:postgresql://localhost:5432/postgres";
    static String user = "postgres";
    static String password = "Teju@123";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        AdminOperations.insertDefaults();
        AdminOperations.insertCourseDefaults();
        ProfessorOperations.insertDefaults();
        StudentOperations.insertDefaults();

        while (true) {
            System.out.println("\n===== Course Registration System =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Professor Login");
            System.out.println("3. Student Login");
            System.out.println("4. Register");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> AdminOperations.handleAdmin(sc);
                case 2 -> ProfessorOperations.handleProfessor(sc);
                case 3 -> StudentOperations.handleStudent(sc);
                case 4 -> handleRegistration(sc);
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleRegistration(Scanner sc) {
        System.out.println("\nRegister as:");
        System.out.println("1. Student");
        System.out.println("2. Professor");
        System.out.print("Enter choice: ");
        int type = Integer.parseInt(sc.nextLine());

        switch (type) {
            case 1 -> registerAsStudent(sc);
            case 2 -> registerAsProfessor(sc);
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void registerAsStudent(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter username: ");
            String uname = sc.nextLine();
            System.out.print("Enter password: ");
            String pwd = sc.nextLine();

            String sql = "INSERT INTO Student_clgPro (name, username, password) VALUES (?, ?, ?) ON CONFLICT (username) DO NOTHING";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, uname);
                ps.setString(3, pwd);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Student registered successfully.");
                } else {
                    System.out.println("Username already exists.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerAsProfessor(Scanner sc) {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter username: ");
            String uname = sc.nextLine();
            System.out.print("Enter password: ");
            String pwd = sc.nextLine();

            String sql = "INSERT INTO professor (name, username, password) VALUES (?, ?, ?) ON CONFLICT (username) DO NOTHING";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, uname);
                ps.setString(3, pwd);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Professor registered successfully.");
                } else {
                    System.out.println("Username already exists.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
