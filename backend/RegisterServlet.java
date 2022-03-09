package mtleslie_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		boolean valid = JDBCConnector.check_uniqueness(email, username);
		if(!valid) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			String error = "User already exists.";
			pw.write(error);
			pw.flush();
		}
		else {
			JDBCConnector.addUser(email, username, password);
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}
}
