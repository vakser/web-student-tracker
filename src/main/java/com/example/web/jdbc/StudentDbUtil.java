package com.example.web.jdbc;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDbUtil {
    private final DataSource dataSource;

    public StudentDbUtil(DataSource theDataSource) {
        dataSource = theDataSource;
    }

    public List<Student> getStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "select * from student order by last_name";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            return retrieveStudents(students, resultSet);
        } finally {
            close(connection, statement, resultSet);
        }
    }

    private void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(Student theStudent) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            String sql = "insert into student (first_name, last_name, email) values (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, theStudent.getFirstName());
            statement.setString(2, theStudent.getLastName());
            statement.setString(3, theStudent.getEmail());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, statement, null);
        }
    }

    public Student getStudent(String theStudentId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Student student;
        int studentId;
        try {
            studentId = Integer.parseInt(theStudentId);
            connection = dataSource.getConnection();
            String sql = "select * from student where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            resultSet= statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                student = new Student(studentId, firstName, lastName, email);
            } else {
                throw new Exception("Could not find student with id " + studentId);
            }
            return student;
        } finally {
            close(connection, statement, resultSet);
        }
    }

    public void updateStudent(Student student) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            String sql = "update student set first_name=?, last_name=?, email=? where id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getEmail());
            statement.setInt(4, student.getId());
            statement.execute();
        } finally {
            close(connection, statement, null);
        }
    }

    public void deleteStudent(String studentId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            int studId = Integer.parseInt(studentId);
            connection = dataSource.getConnection();
            String sql = "delete from student where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, studId);
            statement.execute();
        } finally {
            close(connection, statement, null);
        }
    }

    public List<Student> searchStudents(String theSearchName) throws Exception {
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            if (theSearchName != null && !theSearchName.trim().isEmpty()) {
                String sql = "select * from student where lower(first_name) like ? or lower(last_name) like ?";
                statement = connection.prepareStatement(sql);
                String theSearchNameLike = "%" + theSearchName.toLowerCase() + "%";
                statement.setString(1, theSearchNameLike);
                statement.setString(2, theSearchNameLike);
            } else {
                String sql = "select * from student order by last_name";
                statement = connection.prepareStatement(sql);
            }
            resultSet = statement.executeQuery();
            return retrieveStudents(students, resultSet);
        } finally {
            close(connection, statement, resultSet);
        }
    }

    private List<Student> retrieveStudents(List<Student> students, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            Student tempStudent = new Student(id, firstName, lastName, email);
            students.add(tempStudent);
        }
        return students;
    }
}
