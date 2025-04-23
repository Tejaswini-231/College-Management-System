package CollegeManagement;
import java.util.Scanner;

public class Main {
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
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> AdminOperations.handleAdmin(sc);
                case 2 -> ProfessorOperations.handleProfessor(sc);
                case 3 -> StudentOperations.handleStudent(sc);
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
