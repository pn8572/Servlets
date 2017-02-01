package com.servlet.mps.reports;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author prasanth.pillai
 * @version 1.0
 * Servlet implementation class ReportsUtil
 */
@WebServlet("/ReportsUtil")
public class ReportsUtil extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUtil() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print("<html><body>");
		out.print("<h3>MPS Portal<body>");
		out.print("<a href=\"" +response.encodeUrl("Utility") +"\">Check Out</a>");
		out.print("</body></html>");
		return;
	}
}
