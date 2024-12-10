package com.codegnan.studentapp.servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.codegnan.studentapp.util.Databaseutil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@WebServlet("/edit-student")
public class EditStudentServlet extends HttpServlet {
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       HttpSession session = request.getSession(false);
       Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
       String role = (String) session.getAttribute("role");
       if (session == null || loggedIn == null || !loggedIn) {
           response.sendRedirect("login.jsp");
           return;
       }
       if ("user".equals(role)) {
           response.sendRedirect("students");
           return;
       }
       String id = request.getParameter("id");
       try (Connection connection = Databaseutil.getConnection()) {
           String sql = "SELECT * FROM students WHERE id = ?";
           PreparedStatement statement = connection.prepareStatement(sql);
           statement.setInt(1, Integer.parseInt(id));
           ResultSet resultSet = statement.executeQuery();
           if (resultSet.next()) {
               Map<String, String> student = new HashMap<>();
               student.put("id", String.valueOf(resultSet.getInt("id")));
               student.put("name", resultSet.getString("name"));
               student.put("age", String.valueOf(resultSet.getInt("age")));
               student.put("course", resultSet.getString("course"));
               request.setAttribute("student", student);
               request.getRequestDispatcher("edit-student.jsp").forward(request, response);
           } else {
               response.sendRedirect("students");
           }
       } catch (SQLException e) {
           throw new ServletException(e);
       }
   }
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       HttpSession session = request.getSession(false);
       Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
       String role = (String) session.getAttribute("role");
       if (session == null || loggedIn == null || !loggedIn || !"admin".equals(role)) {
           response.sendRedirect("students");
           return;
       }else if(!loggedIn) {
       	response.sendRedirect("login");
       }
      
      
       String id = request.getParameter("id");
       String name = request.getParameter("name");
       String age = request.getParameter("age");
       String course = request.getParameter("course");
       try (Connection connection = Databaseutil.getConnection()) {
           String sql = "UPDATE students SET name = ?, age = ?, course = ? WHERE id = ?";
           PreparedStatement statement = connection.prepareStatement(sql);
           statement.setString(1, name);
           statement.setInt(2, Integer.parseInt(age));
           statement.setString(3, course);
           statement.setInt(4, Integer.parseInt(id));
           statement.executeUpdate();
           response.sendRedirect("students");
       } catch (SQLException e) {
           throw new ServletException(e);
       }
   }
}

