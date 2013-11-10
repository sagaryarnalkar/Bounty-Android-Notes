package com.sagar.notes;

import com.sagar.localdata.NotesDB;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityAddNote extends Activity implements OnClickListener{
	
	private EditText etNote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		initViews();
		
//		initActionbar(inflater);
	}
	
	private void initViews()
	{
		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		etNote = (EditText) findViewById(R.id.etNote);
	}

	@Override
	public void onClick(View v) {
		SaveNote saveNote = new SaveNote();
		saveNote.execute(etNote.getText().toString().trim());
	}
	
	private class SaveNote extends
    AsyncTask<String, Void, Long>{

		@Override
		protected Long doInBackground(String... params) {
			
			String noteContent = params[0];
			
			NotesDB noteDb = new NotesDB(ActivityAddNote.this);
			
			NoteObj note = new NoteObj();
			note.setNoteText(noteContent);
			note.setNoteTime(System.currentTimeMillis());
			
			long result = noteDb.insertNote(note);
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Long status) {
			if(status > -1)
			{
				Toast.makeText(ActivityAddNote.this, 
			    		"Note Addition Successful !!!", 
			      Toast.LENGTH_LONG).show();
				
				setResult(RESULT_OK);
				
				finish();
			}
			else
			{
				Toast.makeText(ActivityAddNote.this, 
			    		"Note Addition NOT Successful",
			      Toast.LENGTH_LONG).show();
				
				finish();
			}
		}
	}
	
//	private void initActionbar(LayoutInflater inflater)
//	{
//		//getActionBar().setDisplayHomeAsUpEnabled(true);
//        //getActionBar().setHomeButtonEnabled(true);
//		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
//                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME|
//                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
//		
//        
//		View actionBarButtons = inflater.inflate(R.layout.edit_event_custom_actionbar,
//                new LinearLayout(this), false);
//        View cancelActionView = actionBarButtons.findViewById(R.id.action_cancel);
//        cancelActionView.setOnClickListener(this);
//        View doneActionView = actionBarButtons.findViewById(R.id.action_done);
//        doneActionView.setOnClickListener(this);
//
//        getActionBar().setCustomView(actionBarButtons);
//	}

}
