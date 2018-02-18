package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Registration() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// doGet(request, response);

		try {
			String userSignUp = request.getParameter("passwordsignup");
			String passwordSignUp = DigestUtils.md5Hex(request.getParameter("passwordsignup"));
			String confirmPassword = DigestUtils.md5Hex(request.getParameter("passwordsignup_confirm"));

			if(passwordSignUp.equals(confirmPassword)) {
			
			String sql = "INSERT INTO user(login,password,notesID) VALUES(?,?,0)";
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webnotes", "root", "");

				PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
				ps.setString(1, userSignUp);
				ps.setString(2, passwordSignUp);
				ps.executeUpdate();
				PrintWriter out = response.getWriter();
				out.println("udalo sie zarejestrowac");
			} else {
				PrintWriter out = response.getWriter();
				out.println("podane hasla sie nie zgadzaja!");
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}

	}

}
