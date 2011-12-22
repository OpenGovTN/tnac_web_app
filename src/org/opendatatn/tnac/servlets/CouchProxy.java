package org.opendatatn.tnac.servlets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class CouchProxy
 */
@WebServlet(urlPatterns={"/couch/*"})
public class CouchProxy extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
     * @see HttpServlet#HttpServlet()
     */
    public CouchProxy() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String couchApi=request.getPathInfo();
		CouchJsonLoader loader=new CouchJsonLoader("api.opendatatn.org",couchApi, 80, false);
		String res=loader.load(false, null);
		if (res!=null){
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(res);
			String prettyJsonString = gson.toJson(je);
			
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
		     PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
		     out.println(prettyJsonString);
		     out.flush();
		}
		else{
			response.getWriter().println("{error:0}");
		}
	}


}
