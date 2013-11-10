package com.sagar.notes;

import java.io.Serializable;

public class NoteObj implements Serializable{
	
	private static final long serialVersionUID = -3171762136958288126L;
	private long noteTime;
	private String noteText;
	private long noteId;
	public long getNoteTime() {
		return noteTime;
	}
	public void setNoteTime(long noteTime) {
		this.noteTime = noteTime;
	}
	public String getNoteText() {
		return noteText;
	}
	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}
	public long getNoteId() {
		return noteId;
	}
	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}
	
}
