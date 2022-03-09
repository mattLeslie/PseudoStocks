package mtleslie_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/TradeServlet")
public class TradeServlet extends HttpServlet {
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
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Invalid user.";
			pw.write(new Gson().toJson(error));
			pw.flush();
			return;
		}
		
		Double cash = JDBCConnector.getCash(username);
		pw.write(new Gson().toJson(cash));
		pw.flush();

		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		
		String ticker = request.getParameter("ticker");
		String task = request.getParameter("task");
		if(task == null || task.equals("")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "No task specified.";
			pw.write(error);
			pw.flush();
			return;
		}
		String quantity_str = request.getParameter("quantity");
		String price_str = request.getParameter("price");
		Integer quantity = null;
		Double price = null;
		try {
			price = Double.parseDouble(price_str);
			quantity = Integer.parseInt(quantity_str);
		}
		catch(NumberFormatException nfe) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "No quantity or price specified.";
			pw.write(error);
			pw.flush();
			return;
		}
		
		Cookie[] cookies = request.getCookies();
		
		String cookie = "username";
		String username = null;
		for(int i = 0; i < cookies.length; i++) {
			if(cookie.equals(cookies[i].getName())) {
				username = cookies[i].getValue();
			}
		}
		if(username == null || username.equals("")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Invalid user.";
			pw.write(error);
			pw.flush();
			return;
		}
		
		if(task.equals("BUY")) {
			if(quantity <= 0) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Can't buy a negative quantity.";
				pw.write(error);
				pw.flush();
				return;
			}
			else {
				boolean canBuy = JDBCConnector.buyShares(username, ticker, quantity, price);
				if(!canBuy) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					String error = "Not enough money to buy these shares.";
					pw.write(error);
					pw.flush();
					return;
				}
				else {
					response.setStatus(HttpServletResponse.SC_OK);
					String success = "Successfully purchased " + quantity + " shares of " + ticker + " for $" + (price*quantity) +".";
					pw.write(success);
					pw.flush();
					return;
				}
			}
		}
		else if(task.equals("SELL")) {
			if(quantity <= 0) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "Can't sell a negative quantity.";
				pw.write(error);
				pw.flush();
				return;
			}
			else {
				boolean canSell = JDBCConnector.sellShares(username, ticker, quantity, price);
				if(!canSell) {
					
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					String error = "Not enough money to buy these shares.";
					pw.write(error);
					pw.flush();
					return;
				}
				else {
					response.setStatus(HttpServletResponse.SC_OK);
					String success = "Successfully sold " + quantity + " shares of " + ticker + " for $" + (price*quantity) +".";
					pw.write(success);
					pw.flush();
					return;
				}
			}
			
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Invalid task specification.";
			pw.write(error);
			pw.flush();
			return;
		}
	}
}
