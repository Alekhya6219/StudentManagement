package com.codegnan.studentapp.servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.codegnan.studentapp.util.Databaseutil;
@jakarta.servlet.annotation.WebServlet("/add-student")
public class AddStudentServlet extends jakarta.servlet.http.HttpServlet {
	protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
			throws jakarta.servlet.ServletException, IOException {
		jakarta.servlet.http.HttpSession session = request.getSession(false);
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
		String role = (String) session.getAttribute("role");
		if (session == null || loggedIn == null || !loggedIn) {
			response.sendRedirect("login.jsp");
			return;
		}
		else if ("user".equals(role)) {
			response.sendRedirect("students");
			return;
		} else {
			request.getRequestDispatcher("add-student.jsp").forward(request, response);
		}
	}
	protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
			throws jakarta.servlet.ServletException, IOException {
		jakarta.servlet.http.HttpSession session = request.getSession();
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
		String role = (String) session.getAttribute("role");
		if (session == null || loggedIn == null || !loggedIn) {
			response.sendRedirect("login.jsp");
			return;
		}
		if (!"admin".equals(role)) {
			response.sendRedirect("students");
			return;
		}
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String course = request.getParameter("course");
		try (Connection connection = Databaseutil.getConnection()) {
			String sql = "INSERT INTO students (name, age, course) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setInt(2, Integer.parseInt(age));
			statement.setString(3, course);
			statement.executeUpdate();
			response.sendRedirect("students");
		} catch (SQLException e) {
			throw new jakarta.servlet.ServletException(e);
		}
	}
}


