package mtleslie_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/PortfolioServlet")
public class PortfolioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Cookie[] cookies = request.getCookies();
		
		String cookie = "username";
		String username = null;
		for(int i = 0; i < cookies.length; i++) {
			if(cookie.equals(cookies[i].getName())) {
				username = cookies[i].getValue();
			}
		}
		
		if(username == null || username.equals("")) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			String error = "Invalid username.";
			pw.write(new Gson().toJson(error));
			pw.flush();
			return;
		}
		
		String task = request.getParameter("task");
		
		if(task.equals("TOTALCOST")){
			String ticker = request.getParameter("ticker");
			Double totalCost = JDBCConnector.getTotalCost(username, ticker);
			pw.write(new Gson().toJson(totalCost));
			pw.flush();
			return;
		}
		else if(task.equals("PORTFOLIO")) {
			HashMap<String, Integer> results = JDBCConnector.getPortfolio(username);
			response.setStatus(HttpServletResponse.SC_OK);
			pw.write(new Gson().toJson(results));
			pw.flush();
		}
		
	}
	
	
}