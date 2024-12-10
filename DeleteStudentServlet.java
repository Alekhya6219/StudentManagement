package com.codegnan.studentapp.servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.codegnan.studentapp.util.Databaseutil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@WebServlet("/delete-student")
public class DeleteStudentServlet extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       HttpSession session = request.getSession();
       String role = (String) session.getAttribute("role");
       if (!"admin".equals(role)) {
           response.sendRedirect("students");
           return;
       }
       String id = request.getParameter("id");
       try (Connection connection = Databaseutil.getConnection()) {
           String sql = "DELETE FROM students WHERE id = ?";
           PreparedStatement statement = connection.prepareStatement(sql);
           statement.setInt(1, Integer.parseInt(id));
           statement.executeUpdate();
           response.sendRedirect("students");
       } catch (SQLException e) {
           throw new ServletException(e);
       }
   }
}


