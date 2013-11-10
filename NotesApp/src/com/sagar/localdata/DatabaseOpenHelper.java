package com.sagar.localdata;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static DatabaseOpenHelper mInstance = null;
	
	private static final String DB_NAME = "NOTES_DB";
	
	public static final String notes_tbl_name = "sagar_notes";
	
	public static final String Content = "content";
	public static final String Time="Time";
	
	private static final String notesDBCreate = "CREATE TABLE IF NOT EXISTS "
    	    + notes_tbl_name+" ("
    	    + "_id integer primary key,"
    	    + Content+" TEXT, "+Time+" integer);";
	
	private DatabaseOpenHelper(Context ctx) throws NameNotFoundException {
		super(ctx, DB_NAME, null, ctx.getPackageManager().getPackageInfo(
				ctx.getPackageName(), 0).versionCode);
	}

	private DatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	public static DatabaseOpenHelper getInstance(Context ctx) {

		if (mInstance == null) {
			try {
				mInstance = new DatabaseOpenHelper(ctx.getApplicationContext());

			} catch (NameNotFoundException e) {
				mInstance = new DatabaseOpenHelper(ctx.getApplicationContext(),
						DB_NAME, null, 1);
			}
		}
		return mInstance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(notesDBCreate);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS "+notes_tbl_name);
		onCreate(db);
	}
}