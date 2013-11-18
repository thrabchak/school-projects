package com.ctk.notebooks.Utils;

public class Notebook {

	public String 	name;
	public int 		id;
	public long		createdTimestamp;
	public long		lastModifiedTimestamp;
	public int		color;
	
	public Notebook(String name, int id, long createdTimestamp, long lastModifiedTimestamp, int color) {
		this.name = name;
		this.id = id;
		this.createdTimestamp = createdTimestamp;
		this.lastModifiedTimestamp = lastModifiedTimestamp;
		this.color = color;
	}

	public void setmName(String newName) {
		name = newName;
	}

	public void setLastModifiedTimestamp(int newLastModifiedTimestamp) {
		lastModifiedTimestamp = newLastModifiedTimestamp;
	}

	public void setColor(int newColor) {
		color = newColor;
	}
}