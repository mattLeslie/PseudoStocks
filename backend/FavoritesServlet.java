package mtleslie_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/FavoritesServlet")
public class FavoritesServlet extends HttpServlet {
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
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			String error = "Invalid username.";
			pw.write(new Gson().toJson(error));
			pw.flush();
			return;
		}
		
		ArrayList<String> favoriteTickers = JDBCConnector.getFavorites(username);
		response.setStatus(HttpServletResponse.SC_OK);
		pw.write(new Gson().toJson(favoriteTickers));
		pw.flush();
		
		

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		
		Cookie[] cookies = request.getCookies();
		
		String cookie = "username";
		String username = null;
		for(int i = 0; i < cookies.length; i++) {
			if(cookie.equals(cookies[i].getName())) {
				username = cookies[i].getValue();
			}
		}
		String ticker = request.getParameter("ticker");
		String task = request.getParameter("task");
		if(username == null || username.equals("") || ticker.equals("") || ticker == null || task.equals("") || task == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Invalid parameter passed.";
			pw.write(error);
			pw.flush();
			return;
		}
		
		if(task.equals("ADD")) {
			boolean added = JDBCConnector.addFavorite(username, ticker);
			if(added) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
				String success = "Added favorite.";
				pw.write(success);
				return;
			}
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Failed to add favorite.";
				pw.write(error);
				pw.flush();
				return;
			}
		}
		else if(task.equals("REMOVE")) {
			boolean removed = JDBCConnector.removeFavorite(username, ticker);
			if(removed) {
				response.setStatus(HttpServletResponse.SC_OK);
				String success = "Removed favorite.";
				pw.write(success);
				return;
			}
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Failed to remove favorite.";
				pw.write(error);
				pw.flush();
				return;
			}
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Invalid task.";
			pw.write(error);
			pw.flush();
			return;
		}

	}
}
