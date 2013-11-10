package com.sagar.notes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.WeakHashMap;

import com.sagar.localdata.NotesDB;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityHome extends Activity {

	private static final int ADD_NOTE_REQUEST_CODE = 868;
	
	private LinearLayout mLeftHolder;
	private LinearLayout mRightHolder;
	private LayoutInflater mInflater;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initViews();
		fetchNoteData();
	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_home, menu);
//		return true;
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_home, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case R.id.action_add:
	            
	        	addNote();
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
    	}
    }
	
	private void initViews()
	{
		mLeftHolder = (LinearLayout) findViewById(R.id.llLeftHolder);
		mRightHolder = (LinearLayout) findViewById(R.id.llRightHolder);
		
		
	}
	
	private void addNote()
	{
		Intent intent = new Intent(this,ActivityAddNote.class);
		startActivityForResult(intent,ADD_NOTE_REQUEST_CODE);
		
	}
	
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

		    if (requestCode == ADD_NOTE_REQUEST_CODE) {
		    	fetchNoteData();
		    }
		}
	 }
	
	private void fetchNoteData()
	{
		NotesDataProvider provider = new NotesDataProvider();
		provider.execute("");
	}
	
	private int counter = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm:ss");
	private HashMap<Long, Integer> xCoordinates = new HashMap<Long, Integer>();
	private HashMap<Long, Integer> yCoordinates = new HashMap<Long, Integer>();
	
	private void addNoteToView(final NoteObj note)
	{
		View noteView = mInflater.inflate(R.layout.item_note,null);
		TextView tv = (TextView) noteView.findViewById(R.id.tvNote);
		tv.setText(note.getNoteText());
		tv = (TextView) noteView.findViewById(R.id.tvNoteTime);
		tv.setText(sdf.format(new Date(note.getNoteTime())));
		View noteViewMain = noteView.findViewById(R.id.llItem);
		
		if(counter%3 == 0)
			noteViewMain.setBackgroundColor(getResources().getColor(R.color.blue));
		else if(counter%3 == 1)
			noteViewMain.setBackgroundColor(getResources().getColor(R.color.green));
		else
			noteViewMain.setBackgroundColor(getResources().getColor(R.color.red));
		
		if(counter % 2 == 0)
		{
			mLeftHolder.addView(noteView);
		}
		else
		{
			mRightHolder.addView(noteView);
		}
		
		final View v = noteView;
		final int row = counter;
		ViewTreeObserver vto = v.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    public void onGlobalLayout() {
		        v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        
		        int[] location = new int[2];
		        location[0] = ((Float)v.getX()).intValue();
		        location[1] = ((Float)v.getY()).intValue();

//		        v.getLocationOnScreen(location); 
		        
		        if(xCoordinates.containsKey(note.getNoteId()))
				{
		        	int xFrom = 0;
		        	int yFrom = 0;
		        	
		        	if(row % 2 == 0)
		    		{
		        		xFrom = (xCoordinates.get(note.getNoteId())-location[0])+v.getWidth();
		        		yFrom = (yCoordinates.get(note.getNoteId())-location[1]);
		    		}
		        	else
		        	{
		        		xFrom = (xCoordinates.get(note.getNoteId())-location[0])-v.getWidth();
//		        		xFrom = -xFrom;
		        		yFrom = (yCoordinates.get(note.getNoteId())-location[1]);
		        	}
		        	
		        	Log.d("X:", xFrom+" ");
		        	
		        	
		        	TranslateAnimation transAnimation= new TranslateAnimation(
		        			xFrom, 
		        			0,
		        			yFrom,
		        			0);
		        	transAnimation.setDuration(1000);
		        	v.startAnimation(transAnimation);
//		        	ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x",xCoordinates.get(note.getNoteId()),  location[0]);
//		        	ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", yCoordinates.get(note.getNoteId()),location[1]);
//		        	AnimatorSet animSetXY = new AnimatorSet();
//		        	animX.setDuration(8000);
//		        	animY.setDuration(8000);
//		        	animSetXY.playTogether(animX, animY);
//		        	animSetXY.start();
		        	
		        	xCoordinates.put(note.getNoteId(), location[0]);
					yCoordinates.put(note.getNoteId(), location[1]);
//					v.
				}
				else
				{
					xCoordinates.put(note.getNoteId(), location[0]);
					yCoordinates.put(note.getNoteId(), location[1]);
					TranslateAnimation transAnimation= new TranslateAnimation(
		        			0, 
		        			0,
		        			500,
		        			0);
		        	transAnimation.setDuration(400);
		        	v.startAnimation(transAnimation);
				}
		}
		});
		
		
		
		counter++;
	}
	
	private class NotesDataProvider extends
    AsyncTask<String, Void, ArrayList<NoteObj>>{

		@Override
		protected ArrayList<NoteObj> doInBackground(String... params) {
			
			NotesDB notesDb = new NotesDB(ActivityHome.this);
			
			ArrayList<NoteObj> notes = notesDb.getNotes(null);
			
			return notes;
		}
		
		@Override
		protected void onPostExecute(ArrayList<NoteObj> notes) {
			
			mLeftHolder.removeAllViews();
			mRightHolder.removeAllViews();
			counter = 0;
			for(NoteObj note:notes)
			{
				addNoteToView(note);
			}
		}
	}

}
