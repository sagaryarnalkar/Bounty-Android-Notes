package com.sagar.localdata;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sagar.notes.NoteObj;

public class NotesDB {
	protected SQLiteDatabase mdatabase;
    protected DatabaseOpenHelper mdbOpenHelper;
    protected Context ctx;

    public NotesDB(Context context) {
    	ctx = context;
    	mdbOpenHelper = DatabaseOpenHelper.getInstance(context);
    	open();
        }
    
    public void open() throws SQLException {
	// open database in reading/writing mode
	if (null == mdatabase || !mdatabase.isOpen())
	    mdatabase = mdbOpenHelper.getWritableDatabase();
    }

    public void close() {
	if (mdatabase != null)
	    mdatabase.close();
    }
    
    public ArrayList<NoteObj> getNotes(String where)
    {
    	Cursor mainC = getAllRows(where);
    	
    	ArrayList<NoteObj> notes = new ArrayList<NoteObj>();
    	NoteObj note = null;
    	for (boolean hasItem = mainC.moveToFirst(); hasItem; hasItem = mainC
    			.moveToNext()) {
    		note = new NoteObj();
    		note.setNoteId(mainC.getLong(mainC.getColumnIndex("_id")));
    		note.setNoteText(mainC.getString(mainC.getColumnIndex(DatabaseOpenHelper.Content)));
    		note.setNoteTime(mainC.getLong(mainC.getColumnIndex(DatabaseOpenHelper.Time)));
    		notes.add(note);
    		}
    	
    	return notes;
    }
    
    public long insertNote(NoteObj note)
    {
    	
    	ContentValues newValues = new ContentValues();
    	newValues.put(DatabaseOpenHelper.Content, note.getNoteText());
    	newValues.put(DatabaseOpenHelper.Time,note.getNoteTime());
    	return mdatabase.insert(DatabaseOpenHelper.notes_tbl_name, null, newValues);
    }
    
    public void deleteAllRows(String where) {
		if (mdatabase.isOpen())
		    mdatabase.delete(DatabaseOpenHelper.notes_tbl_name, where, null);
		else {
		    open();
		    mdatabase.delete(DatabaseOpenHelper.notes_tbl_name, where, null);
		}
	    }
    
    public Cursor getAllRows(String where) {
    	
    	return mdatabase.query(DatabaseOpenHelper.notes_tbl_name, null, where, null, null,
    			null,  DatabaseOpenHelper.Time+" desc");
        }
}
