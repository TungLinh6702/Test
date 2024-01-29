import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {

    private static final String INSERT_QUERY = "INSERT INTO students (name, age, gender, grade) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM students";
    private static final String SELECT_BY_GRADE_QUERY = "SELECT * FROM students WHERE grade = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM students WHERE name LIKE ?";
    private static final String SELECT_BY_GENDER_QUERY = "SELECT * FROM students WHERE gender = ?";
    private static final String UPDATE_QUERY = "UPDATE students SET name=?, age=?, gender=?, grade=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM students WHERE id=?";

    public void addStudent(Student student) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setString(3, student.getGender());
            preparedStatement.setDouble(4, student.getGrade());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                student.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> getAllStudents() {
        return getStudents(SELECT_ALL_QUERY);
    }

    public List<Student> getStudentsByGrade(double grade) {
        return getStudentsWithParameter(SELECT_BY_GRADE_QUERY, grade);
    }

    public List<Student> getStudentByName(String name) {
        return getStudentsWithParameter(SELECT_BY_NAME_QUERY, "%" + name + "%");
    }

    public List<Student> getStudentsByGender(String gender) {
        return getStudentsWithParameter(SELECT_BY_GENDER_QUERY, gender);
    }

    public Student getStudentById(int id) {
        List<Student> students = getStudentsWithParameter(SELECT_ALL_QUERY + " WHERE id=?", String.valueOf(id));
        return students.isEmpty() ? null : students.get(0);
    }

    public void updateStudent(Student student) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setString(3, student.getGender());
            preparedStatement.setDouble(4, student.getGrade());
            preparedStatement.setInt(5, student.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Student> getStudents(String query) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Student student = mapResultSetToStudent(resultSet);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    private List<Student> getStudentsWithParameter(String query, Object parameter) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (parameter instanceof String) {
                preparedStatement.setString(1, (String) parameter);
            } else if (parameter instanceof Double) {
                preparedStatement.setDouble(1, (Double) parameter);
            } else {
                preparedStatement.setObject(1, parameter);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = mapResultSetToStudent(resultSet);
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    public List<Student> getStudentsByGradeRange(double minGrade, double maxGrade) {
        String query = "SELECT * FROM students WHERE grade >= ? AND grade < ?";
        return getStudentsWithGradeRange(query, minGrade, maxGrade);
    }

    private List<Student> getStudentsWithGradeRange(String query, double minGrade, double maxGrade) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, minGrade);
            preparedStatement.setDouble(2, maxGrade);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = mapResultSetToStudent(resultSet);
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setName(resultSet.getString("name"));
        student.setAge(resultSet.getInt("age"));
        student.setGender(resultSet.getString("gender"));
        student.setGrade(resultSet.getDouble("grade"));
        return student;
    }
}
