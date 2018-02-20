package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);

		try {
			String username = request.getParameter("username");
			String password = DigestUtils.md5Hex(request.getParameter("password"));
			//String password = request.getParameter("password");
			String dbUsername = null;
			String dbPassword = null;

			String sql = "SELECT * FROM user WHERE login = ? AND password = ?";
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webnotes", "root", "");

			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			PrintWriter out = response.getWriter();

			while (rs.next()) {
				dbUsername = rs.getString(2);
				dbPassword = rs.getString("password");
			}

			if (username.equals(dbUsername) && password.equals(dbPassword)) {
				//String site = new String("https://www.wp.pl/");
				//response.setStatus(response.SC_MOVED_TEMPORARILY);
				//response.setHeader("Location", site);
				// out.println("udalo sie zalogowac");
				HttpSession session = request.getSession();
				session.setAttribute("user", username);
				session.setMaxInactiveInterval(30*60);
				Cookie userName = new Cookie("user", username);
				userName.setMaxAge(30*60);
				response.addCookie(userName);
				response.sendRedirect("panel.jsp");
			} else {
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
				out.println("<font color=red>Either user name or password is wrong.</font>");
				rd.include(request, response);
				
				//response.sendRedirect("login.jsp");
				//RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				out.print("incorrect login or password");
				//rd.include(request, response);
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
