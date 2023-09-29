package com.example.web.jdbc;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import javax.annotation.*;

@WebServlet(name = "TestServlet", value = "/test-servlet")
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        try {
            connection = dataSource.getConnection();
            String sql = "select * from student;";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String email = resultSet.getString("email");
                out.println(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}