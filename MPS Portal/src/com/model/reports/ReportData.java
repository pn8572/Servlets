/**
 * 
 */
package com.model.reports;

/**
 * @author prasanth.pillai
 *
 */
public class ReportData {
	
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	@Override
	public String toString() {
		return "ReportData [category=" + category + ", link=" + link + ", title=" + title + ", description="
				+ description + ", lastModifiedDate=" + lastModifiedDate + ", reportId=" + reportId + "]";
	}
	String category;
	String link;
	String title;
	String description;
	String lastModifiedDate;
	String reportId;
	
}
