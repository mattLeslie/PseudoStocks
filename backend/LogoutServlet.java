package mtleslie_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");

		Cookie logoutCookie = new Cookie("username", "");
		logoutCookie.setMaxAge(0);
		response.addCookie(logoutCookie);
		response.setStatus(HttpServletResponse.SC_OK);
		pw.write("Logged out.");
	}
}
