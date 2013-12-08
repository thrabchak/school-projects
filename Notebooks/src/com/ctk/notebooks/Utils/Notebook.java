package com.ctk.notebooks.Utils;

public class Notebook {

	public String 	name;
	public int 		id;
	public long		createdTimestamp;
	public long		lastModifiedTimestamp;
	public int		color;
	public int 		numPages;
	
	public Notebook(String name, int id, long createdTimestamp, long lastModifiedTimestamp, int color, int numPages) {
		this.name = name;
		this.id = id;
		this.createdTimestamp = createdTimestamp;
		this.lastModifiedTimestamp = lastModifiedTimestamp;
		this.color = color;
		this.numPages = numPages;
	}

	public void setName(String newName) {
		name = newName;
	}

	public void setLastModifiedTimestamp(int newLastModifiedTimestamp) {
		lastModifiedTimestamp = newLastModifiedTimestamp;
	}

	public void setColor(int newColor) {
		color = newColor;
	}
	
	public void setNumPages(int newNumber) {
		numPages = newNumber;
	}
	
	public void incrementPageCount() {
		numPages++;
	}
	
	public void decrementPageCount() {
		numPages--;
	}
}