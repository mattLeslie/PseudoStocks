package mtleslie_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		
		String type = request.getParameter("type");
		
		if(type.equals("google")) {
			String email = request.getParameter("email");

			Integer id = JDBCConnector.loginGoogleUser(email);
			if(id == -1) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				String error = "Incorrect credentials.";
				pw.write(error);
				pw.flush();
			}
			else {
				Cookie userCookie = new Cookie("username", id.toString());
				userCookie.setMaxAge(60*30);
				response.addCookie(userCookie);
				response.setStatus(HttpServletResponse.SC_OK);
			}
			
		}
		else {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			Integer id = JDBCConnector.validate(username, password);
			
			if(id == -1) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				String error = "Incorrect credentials.";
				pw.write(error);
				pw.flush();
			}
			else {
				Cookie userCookie = new Cookie("username", id.toString());
				userCookie.setMaxAge(60*30);
				response.addCookie(userCookie);
				response.setStatus(HttpServletResponse.SC_OK);
			}
		}
	}
}
