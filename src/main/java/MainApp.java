import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        StudentManager studentManager = new StudentManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Nhập thông tin sinh viên.");
            System.out.println("2. Hiển thị danh sách sinh viên.");
            System.out.println("3. Hiển thị danh sách theo học lực.");
            System.out.println("4. Tìm sinh viên theo tên.");
            System.out.println("5. Hiển thị thông tin giới tính.");
            System.out.println("6. Sửa thông tin sinh viên.");
            System.out.println("7. Xóa sinh viên.");
            System.out.println("0. Thoát chương trình.");

            System.out.print("Nhập lưa chọn của bạn: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Student newStudent = createStudentFromUserInput(scanner);
                    studentManager.addStudent(newStudent);
                    System.out.println("Sinh viên đã được thêm thành công!");
                    break;

                case 2:
                    displayStudentList(studentManager.getAllStudents());
                    break;

                case 3:
                    displayGradeSubMenu();
                    int gradeOption = scanner.nextInt();
                    switch (gradeOption) {
                        case 1:
                            displayStudentList(studentManager.getStudentsByGradeRange(8.0, Double.MAX_VALUE));
                            break;
                        case 2:
                            displayStudentList(studentManager.getStudentsByGradeRange(6.5, 8.0));
                            break;
                        case 3:
                            displayStudentList(studentManager.getStudentsByGradeRange(5.0, 6.5));
                            break;
                        case 4:
                            displayStudentList(studentManager.getStudentsByGradeRange(Double.MIN_VALUE, 5.0));
                            break;
                        case 5:
                            displayStudentList(studentManager.getAllStudents());
                            break;
                        case 0:
                            // Trở về menu chính
                            break;
                        default:
                            System.out.println("Lựa chọn không hợp lệ. Hãy chọn lại.");
                    }
                    break;

                case 4:
                    System.out.print("Nhập tên sinh viên cần tìm: ");
                    String searchName = scanner.next();
                    displayStudentList(studentManager.getStudentByName(searchName));
                    break;

                case 5:
                    System.out.print("Nhập giới tính cần hiển thị (Male/Female): ");
                    String gender = scanner.next();
                    displayStudentList(studentManager.getStudentsByGender(gender));
                    break;

                case 6:
                    System.out.print("Nhập ID sinh viên cần sửa: ");
                    int editId = scanner.nextInt();
                    editStudentInfo(studentManager, editId, scanner);
                    break;

                case 7:
                    System.out.print("Nhập ID sinh viên cần xóa: ");
                    int deleteId = scanner.nextInt();
                    studentManager.deleteStudent(deleteId);
                    System.out.println("Sinh viên đã được xóa thành công!");
                    break;

                case 0:
                    System.out.println("Chương trình kết thúc.");
                    System.exit(0);

                default:
                    System.out.println("Lựa chọn không hợp lệ. Hãy chọn lại.");
            }
        }
    }

    private static Student createStudentFromUserInput(Scanner scanner) {
        System.out.print("Nhập ID sinh viên (nhập 0 để tự động tạo): ");
        int inputId = scanner.nextInt();

        Integer studentId = (inputId == 0) ? null : inputId;

        System.out.print("Nhập tên sinh viên (mẫu: Nguyen_Van_A): ");
        String name = scanner.next();

        System.out.print("Nhập tuổi: ");
        int age = scanner.nextInt();

        System.out.print("Nhập giới tính (Male/Female): ");
        String gender = scanner.next();

        System.out.print("Nhập điểm (mẫu: 7,99): ");
        double grade = scanner.nextDouble();

        return new Student(name, age, gender, grade);
    }
    private static void displayGradeSubMenu() {
        System.out.println("Chọn xếp loại học lực:");
        System.out.println("1. Giỏi: >=8.0");
        System.out.println("2. Khá: >=6.5 và <8");
        System.out.println("3. Trung bình: >=5 và <6.5");
        System.out.println("4. Yếu: <5");
        System.out.println("5. Hiển thị toàn bộ");
        System.out.println("0. Trở về menu chính");
        System.out.print("Nhập lựa chọn của bạn: ");
    }
    private static void displayStudentList(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("Chưa có sinh viên. Vui lòng nhập thông tin sinh viên trước khi xem.");
        } else {
            System.out.println("Danh sách sinh viên:");
            for (Student student : students) {
                System.out.printf("ID: %d, Name: %s, Age: %d, Gender: %s, Grade: %.2f%n",
                        student.getId(), student.getName(), student.getAge(),
                        student.getGender(), student.getGrade());
            }
        }
    }

    private static void editStudentInfo(StudentManager studentManager, int studentId, Scanner scanner) {
        Student student = studentManager.getStudentById(studentId);

        if (student != null) {
            System.out.println("Thông tin sinh viên cần sửa: ");
            System.out.println("ID: " + student.getId() +
                    "; Name: " + student.getName() +
                    "; Age: " + student.getAge() +
                    "; Gender: " + student.getGender() +
                    "; Grade: " + student.getGrade());

            System.out.print("Nhập tên mới (để giữ nguyên, nhấn Enter): ");
            scanner.nextLine(); // Đọc dòng trống sau nextInt
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                student.setName(newName);
            }

            System.out.print("Nhập tuổi mới (để giữ nguyên, nhập 0): ");
            int newAge = scanner.nextInt();
            if (newAge > 0) {
                student.setAge(newAge);
            }

            System.out.print("Nhập giới tính mới (để giữ nguyên, nhấn Enter): ");
            scanner.nextLine(); // Đọc dòng trống sau nextInt
            String newGender = scanner.nextLine();
            if (!newGender.isEmpty()) {
                student.setGender(newGender);
            }

            System.out.print("Nhập điểm mới (để giữ nguyên, nhập 0): ");
            double newGrade = scanner.nextDouble();
            if (newGrade > 0) {
                student.setGrade(newGrade);
            }

            studentManager.updateStudent(student);
            System.out.println("Sinh viên đã được sửa thông tin thành công!");
        } else {
            System.out.println("Không tìm thấy sinh viên có ID là " + studentId + ". Vui lòng nhập lại.");
        }
    }
}
