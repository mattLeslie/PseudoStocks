package mtleslie_CSCI201L_Assignment4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String ticker = request.getParameter("ticker");
		String task = request.getParameter("task");
		if(task == null) {
			return;
		}
		if(task.equals("EOD")) {
			String info = this.callEOD(ticker);
			if(info.equals("error")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "could not find stock";
				pw.write(new Gson().toJson(error));
				pw.flush();
			}
			else {
				response.setStatus(HttpServletResponse.SC_OK);
				pw.write(info);
				pw.flush();
			}
		}
		else if(task.equals("DETAIL")) {
			String info = this.callINFO(ticker);
			if(info.equals("error")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "could not find stock";
				pw.write(new Gson().toJson(error));
				pw.flush();
			}
			else {
				response.setStatus(HttpServletResponse.SC_OK);
				pw.write(info);
				pw.flush();
			}
		}
		else if(task.equals("CURRENT")) {
			String info = this.callPRICE(ticker);
			if(info.equals("error")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "could not find stock";
				pw.write(new Gson().toJson(error));
				pw.flush();
			}
			else {
				response.setStatus(HttpServletResponse.SC_OK);
				pw.write(info);
				pw.flush();
			}
		}
		
	}
	
	private String callEOD(String ticker) {
		String KEY = "373383fd707b2272c0f3d03c2e32a395aeba2262";
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL("https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?form=csv&token=" + KEY);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "text/csv");
			connection.connect();
			int code = connection.getResponseCode();
			if(code != 200) {
				return "error";
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String content = in.readLine();
			return content;
		}
		catch(MalformedURLException mue) {
			System.out.println("Malformed URL");
		}
		catch(IOException ioe) {
			System.out.println("Error when accessing Tiingo API");
		}
		return "error";
	}
	
	private String callINFO(String ticker) {

		String KEY = "373383fd707b2272c0f3d03c2e32a395aeba2262";
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL("https://api.tiingo.com/tiingo/daily/" + ticker + "?token=" + KEY);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "text/csv");
			connection.connect();
			int code = connection.getResponseCode();
			
			if(code != 200) {
				return "error";
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String content = in.readLine();
			return content;
		}
		catch(MalformedURLException mue) {
			System.out.println("Malformed URL");
		}
		catch(IOException ioe) {
			System.out.println("Error when accessing Tiingo API");
		}
		return "error";
	}
	
	private String callPRICE(String ticker) {
		String KEY = "373383fd707b2272c0f3d03c2e32a395aeba2262";
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL("https://api.tiingo.com/iex?tickers="+ ticker + "&token=" + KEY);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "text/csv");
			connection.connect();
			int code = connection.getResponseCode();
			
			if(code != 200) {
				return "error";
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String content = in.readLine();
			return content;
		}
		catch(MalformedURLException mue) {
			System.out.println("Malformed URL");
		}
		catch(IOException ioe) {
			System.out.println("Error when accessing Tiingo API");
		}
		return "error";
	}
}
