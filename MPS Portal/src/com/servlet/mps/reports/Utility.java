package com.servlet.mps.reports;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.model.reports.ReportData;

/**
 * @author prasanth.pillai
 * @version 1.0
 * 
 * Servlet implementation class Report Utility
 *  *    
 *     downloads(root directory)
 *     	 |
 *       |
 *     	 |--ad_hoc_reports
 *     	 |			|
 *     	 |			|--ad_hoc_Jan2017(most recent)
 *     	 |			|--archive (folder)
 *     	 |				  |
 *     	 |				  |--ad_hoc_Dec2016(previous reports)
 *     	 |				  |--ad_hoc_Nov2016(previous reports)
 *       |
 *       |
 *     	 |--dna_result_dashboard
 *     	 |			|
 *     	 |		|--dna_result_Jan2017(most recent)
 *     	 |			|--archive (folder)
 *     	 |				  |
 *     	 |				  |--dna_result_Dec2016(previous reports)
 *       |					  |--dna_result_Nov2016(previous reports)
 *       |
 *       etc    		
 */
@WebServlet("/Utility")
public class Utility extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(Utility.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Utility() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		PrintWriter out = response.getWriter();
		out.print("[");
			generateReportDetails(out, response);
		out.print("]");
	}
	
	/**
	 * @throws URISyntaxException 
	 * 
	 */
	private void generateReportDetails(PrintWriter out, HttpServletResponse response) throws IOException
	{
		File[] listOfFiles = null;
		File folder = null;
		String lastCategoryFolderName="weekly_case_reports";//default
		Properties properties = new Properties();
		try 
		{
			//logger.info("Servlet Path is ::"+getServletContext().getContextPath());
			properties.load(getServletContext().getResourceAsStream("/WEB-INF/classes/properties/config.properties"));
			folder = new File(properties.getProperty("local_report_location"));
			lastCategoryFolderName = properties.getProperty("last_category_folder_name");
			logger.info("MPS Reports local location is :"+folder.getAbsolutePath());
			listOfFiles = folder.listFiles();
			logger.info("No of files/directories in this location are :"+listOfFiles.length);
		
		} 
		catch (IOException e1) 
		{
			logger.warn("Directory does not exist. or could not load the property file >>>"+e1.getMessage());
			folder = new File("../../../../var/www/html/downloads");
			listOfFiles = folder.listFiles();
			logger.info("Local MPS Reports recovery location is :"+folder.getAbsolutePath());
		}
		
		if (listOfFiles == null || listOfFiles.length == 0) 
		{
			logger.info("No MPS Reports in the local location :"+folder.getAbsolutePath());
			return;
		}
		/*
		 * When the last report category is reached , the application must know that, so it could stop
		 * adding characters to JSON file.
		 */
		boolean isLastReportCategory = false;
		int reportID = 1;
		boolean isArchiveFolder= true;
		
	    for (int i = 0; i < listOfFiles.length; i++) 
	    {
	    	/*
	    	 * As per the folder structure, the discard the files in the root.
	    	 * i.e consider only the report category folders under the root and disard other files
	    	 * such as reports.categories.properties
	    	 */
	    	
	    	if (i+1 == listOfFiles.length)
    		{
    			isLastReportCategory = true;
    		}
	    	if (listOfFiles[i].isDirectory()) 
	    	{
	    		//this is to check if the last category folder has been reached.
	    		if (listOfFiles[i].getName().contains(lastCategoryFolderName)) {
	    			isLastReportCategory = true;
	    		}
	    		logger.debug("Directory is :"+listOfFiles[i]);
	    		
	    		if (listOfFiles[i].getName().contains("archive"))
					isArchiveFolder = true;
				else
					isArchiveFolder = false;
	    		reportID = setReportData(reportID, out, listOfFiles[i], listOfFiles[i] , response, isLastReportCategory, isArchiveFolder);
	    	}
	    }
	    
	    logger.info("The reports details are stored to JSON format and stored in the response object...");
	}
	/**
	 * 
	 * @param src
	 * @param response
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	
	public int setReportData(int reportID, PrintWriter out,File srcRoot, File src, HttpServletResponse response, boolean isLastReportCategory, boolean isArchiveFolder) throws IOException	
	{
		ReportData report = new ReportData();
		report.setCategory(srcRoot.getName());
		//list all the directory contents
		File[] listOfFiles = src.listFiles();
		int count = 1; //to know the loop count
		boolean setComma = true;
		String fileLink = null;
		int tempLength = listOfFiles.length;
		for (File srcFile : listOfFiles) 
		{
			logger.debug(" >>File name is :"+srcFile.getName()+" and count is "+count+" and length is "+listOfFiles.length+" and tempLength is "+tempLength);
			
			if ( (count == tempLength) && isLastReportCategory && srcFile.isFile() && !isArchiveFolder)
			{
				logger.debug("Last value, don't set comma");
				
				setComma =  false;
			}
			if (srcFile.isFile()) 
		    {
				   //construct the src and dest file structure
				   report.setReportId(String.valueOf(reportID));
				   reportID++;
				   report.setTitle(srcFile.getName());
				   fileLink = srcFile.getPath();
				   fileLink = fileLink.replace("\\", "/");
				   
				   //remove the ../../../../var/www/html/downloads/weekly_case_reports/archive/Report28.xlsx
				   // to downloads/weekly_case_reports/archive/Report28.xlsx , so that the NODE server can download the reports without any issues.
				   int index = fileLink.indexOf("downloads");
				   if (index > 0)
					   fileLink = fileLink.substring(index, fileLink.length());
				   report.setLink(fileLink);
				   //logger.debug(">>>>>>>>File link is :"+fileLink);
				   
				   Date d = new Date(srcFile.lastModified());
				   report.setLastModifiedDate(d.toString());
				   String json = new Gson().toJson(report);
			       response.setContentType("application/json");
			       response.getWriter().write(json);
			       if (setComma)
			    	   out.print(",");
		    }
			else if (srcFile.isDirectory()) 
			{
				if (srcFile.getName().contains("archive"))
					isArchiveFolder = true;
				else
					isArchiveFolder = false;
				
				reportID = setReportData(reportID, out, src, srcFile, response, isLastReportCategory,isArchiveFolder );
				isArchiveFolder = false;
			}
			count++;
	    }
		return reportID;
	}
}
