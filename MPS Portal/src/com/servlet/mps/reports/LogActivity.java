package com.servlet.mps.reports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class LogActivity
 */
@WebServlet("/LogActivity")
public class LogActivity extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogActivity() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("text/html");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
		String str = null;
		String name = null;
		String password = null;
		while ((str = br.readLine()) != null) 
		{
		    sb.append(str);
		}
		JSONObject jObj;
		try 
		{
			jObj = new JSONObject(sb.toString());
			name = jObj.getString("name");
			password = jObj.getString("password");
			System.out.println("the name is >>>>>"+name);
			System.out.println("the password is >>>>>"+password);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			String content = "This is the content to write into file\n";
			String header = "USER				Login Time\n";
			String separator = "==============================\n";
			
			String pattern1 = "dd-MM-yyyy";
			String dateInString1 = new SimpleDateFormat(pattern1).format(new Date());
			
			String pattern2 = "HH:mm:ss.SS";
			String dateInString2 = new SimpleDateFormat(pattern2).format(new Date());
			//String strDate = formDate.format(new Date());

			String FILENAME = "C:\\test\\";
			
			content = name+ "\t\t\t\t" + dateInString2;
			
			File dir = new File(FILENAME);
			
			if (!dir.exists())
				dir.mkdirs();
			File file = new File(dir, "logactivity"+dateInString1+".txt");
			
			if (!file.exists()) 
				file.createNewFile();
				
				fw = new FileWriter(file, true);
				bw = new BufferedWriter(fw);
			
			
			bw.write(content);
			bw.newLine();
			
			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
