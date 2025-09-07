import java.io.*;
import java.util.*;

// Class to represent individual Student
class Student implements Serializable {
    private String name;
    private String rollNumber;
    private String grade;

    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + " | Roll No: " + rollNumber + " | Grade: " + grade;
    }
}

// Class to manage collection of Students
class StudentManagementSystem {
    private List<Student> students;
    private static final String FILE_NAME = "students.dat";

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadFromFile();
    }

    // Add student
    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
    }

    // Remove student by roll number
    public void removeStudent(String rollNumber) {
        students.removeIf(s -> s.getRollNumber().equals(rollNumber));
        saveToFile();
    }

    // Search student by roll number
    public Student searchStudent(String rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber().equals(rollNumber)) {
                return s;
            }
        }
        return null;
    }

    // Display all students
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    // Save data to file
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load data from file
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                students = (List<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data: " + e.getMessage());
            }
        }
    }
}

// Main class with User Interface
public class StudentManagementApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManagementSystem sms = new StudentManagementSystem();

        int choice;
        do {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Edit Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Roll Number: ");
                    String roll = scanner.nextLine();
                    System.out.print("Enter Grade: ");
                    String grade = scanner.nextLine();

                    if (name.isEmpty() || roll.isEmpty() || grade.isEmpty()) {
                        System.out.println("❌ Fields cannot be empty!");
                    } else {
                        sms.addStudent(new Student(name, roll, grade));
                        System.out.println("✅ Student added successfully.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Roll Number to remove: ");
                    String rollRemove = scanner.nextLine();
                    sms.removeStudent(rollRemove);
                    System.out.println("✅ Student removed (if exists).");
                    break;

                case 3:
                    System.out.print("Enter Roll Number to search: ");
                    String rollSearch = scanner.nextLine();
                    Student found = sms.searchStudent(rollSearch);
                    if (found != null) {
                        System.out.println("✅ Student found: " + found);
                    } else {
                        System.out.println("❌ Student not found.");
                    }
                    break;

                case 4:
                    sms.displayAllStudents();
                    break;

                case 5:
                    System.out.print("Enter Roll Number to edit: ");
                    String rollEdit = scanner.nextLine();
                    Student editStudent = sms.searchStudent(rollEdit);
                    if (editStudent != null) {
                        System.out.print("Enter new Name (leave blank to keep same): ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new Grade (leave blank to keep same): ");
                        String newGrade = scanner.nextLine();

                        if (!newName.isEmpty())
                            editStudent.setName(newName);
                        if (!newGrade.isEmpty())
                            editStudent.setGrade(newGrade);

                        System.out.println("✅ Student updated successfully.");
                    } else {
                        System.out.println("❌ Student not found.");
                    }
                    break;

                case 6:
                    System.out.println("👋 Exiting Student Management System...");
                    break;

                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        } while (choice != 6);

        scanner.close();
    }
}
