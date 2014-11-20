package com.ctk.notebooks.Utils;

public class Note {

	public String 	name;
	public int		notebookId;
	public int		pageNumber;
	public long		createdTimestamp;
	public long		lastModifiedTimestamp;
	public String	filepath;
	
	public Note(String name, int notebookId, int pageNumber, long createdTimestamp, long lastModifiedTimestamp, String filepath) {
		super();
		this.name = name;
		this.notebookId = notebookId;
		this.pageNumber = pageNumber;
		this.createdTimestamp = createdTimestamp;
		this.lastModifiedTimestamp = lastModifiedTimestamp;
		this.filepath = filepath;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
